
package pe.edu.upeu.AgroApi_Ticona.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pe.edu.upeu.AgroApi_Ticona.entity.Rol;
import pe.edu.upeu.AgroApi_Ticona.enums.Permiso;

import java.util.*;

public final class PermisosPorRol {

    private static final Map<String, Set<Permiso>> PERMISOS = Map.of(

            "ADMIN", Collections.unmodifiableSet(
                    EnumSet.allOf(Permiso.class)
            ),

            "TECNICO", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.LOTE_LEER,
                    Permiso.LABOR_LEER,
                    Permiso.LABOR_CREAR,
                    Permiso.LABOR_EDITAR
            )),

            "JEFE_CAMPO", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.LOTE_LEER,
                    Permiso.LABOR_LEER,
                    Permiso.LABOR_CREAR,
                    Permiso.LABOR_EDITAR,
                    Permiso.LABOR_ELIMINAR,
                    Permiso.CAMPANIA_CERRAR
            ))
    );

    private PermisosPorRol() {
    }

    public static Set<Permiso> de(String nombreRol) {
        return PERMISOS.getOrDefault(nombreRol, Set.of());
    }

    /* Union de los permisos de todos los roles, ordenada alfabeticamente. */
    public static List<String> permisos(Collection<Rol> roles) {
        Set<String> nombres = new TreeSet<>();
        roles.forEach(rol -> de(rol.getNombre())
                .forEach(permiso -> nombres.add(permiso.name())));
        return new ArrayList<>(nombres);
    }

    /*
     * Authorities para Spring Security: un "ROLE_<rol>" por cada rol
     * mas un authority por cada permiso.
     */
    public static List<GrantedAuthority> authorities(Collection<Rol> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        roles.stream()
                .map(Rol::getNombre)
                .sorted()
                .forEach(nombre -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_" + nombre)));

        permisos(roles).forEach(permiso ->
                authorities.add(new SimpleGrantedAuthority(permiso)));

        return authorities;
    }
}
