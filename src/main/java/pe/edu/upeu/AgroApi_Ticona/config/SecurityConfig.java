package pe.edu.upeu.AgroApi_Ticona.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;

import pe.edu.upeu.AgroApi_Ticona.security.JwtAuthenticationFilter;
import pe.edu.upeu.AgroApi_Ticona.security.JwtService;
import pe.edu.upeu.AgroApi_Ticona.security.RestAccessDeniedHandler;
import pe.edu.upeu.AgroApi_Ticona.security.RestAuthenticationEntryPoint;

import java.util.Arrays;
import java.util.List;

import static pe.edu.upeu.AgroApi_Ticona.enums.Permiso.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] RUTAS_PUBLICAS = {
            "/api/v1/auth/login",
            "/api/health",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/error"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            CorsConfigurationSource corsConfigurationSource,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) throws Exception {

        JwtAuthenticationFilter jwtFilter =
                new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource))

                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Rutas publicas
                        .requestMatchers(RUTAS_PUBLICAS).permitAll()

                        .requestMatchers(
                                HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // Perfil del usuario autenticado
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/usuarios/me")
                        .authenticated()

                        // Consultar labores de un lote
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/lotes/*/labores")
                        .hasAuthority(LABOR_LEER.name())

                        // Cerrar campaña
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/lotes/*/cerrar-campania")
                        .hasAuthority(CAMPANIA_CERRAR.name())

                        // Consultar lotes
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/lotes",
                                "/api/v1/lotes/**")
                        .hasAuthority(LOTE_LEER.name())

                        // Administrar lotes: solo ADMIN
                        .requestMatchers(
                                "/api/v1/lotes",
                                "/api/v1/lotes/**")
                        .hasRole("ADMIN")

                        // Registrar labores
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/labores")
                        .hasAuthority(LABOR_CREAR.name())

                        // Consultar labores
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/labores",
                                "/api/v1/labores/**")
                        .hasAuthority(LABOR_LEER.name())

                        // Actualizar labores
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/labores/**")
                        .hasAuthority(LABOR_EDITAR.name())

                        // Eliminar labores
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/labores/**")
                        .hasAuthority(LABOR_ELIMINAR.name())

                        // Reportes: solo ADMIN
                        .requestMatchers(
                                "/api/v1/reportes",
                                "/api/v1/reportes/**")
                        .hasRole("ADMIN")

                        // Administracion de usuarios
                        .requestMatchers(
                                "/api/v1/usuarios",
                                "/api/v1/usuarios/**",
                                "/api/v1/empleados",
                                "/api/v1/empleados/**",
                                "/api/v1/roles",
                                "/api/v1/roles/**")
                        .hasAuthority(USUARIO_GESTIONAR.name())

                        // Cualquier otra ruta requiere autenticacion
                        .anyRequest().authenticated()
                )

                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint(
                                authenticationEntryPoint)
                        .accessDeniedHandler(
                                accessDeniedHandler))

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /*
     * Configuracion CORS para clientes web.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:"
                    + "http://localhost:5173,"
                    + "http://127.0.0.1:5173,"
                    + "http://localhost:4200,"
                    + "http://localhost:3000}")
            String origenesPermitidos) {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                Arrays.stream(origenesPermitidos.split(","))
                        .map(String::trim)
                        .toList()
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Origin"
                )
        );

        configuration.setExposedHeaders(
                List.of("Location")
        );

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/api/**", configuration);

        return source;
    }

    /*
     * BCrypt para cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * AuthenticationManager utiliza UsuarioDetailsService
     * y PasswordEncoder.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
