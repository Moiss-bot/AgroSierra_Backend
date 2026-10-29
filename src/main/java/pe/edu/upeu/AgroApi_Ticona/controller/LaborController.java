package pe.edu.upeu.AgroApi_Ticona.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LaborResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.service.service.LaborService;

@RestController
@RequestMapping("/api/v1")
public class LaborController {

    private final LaborService laborService;

    public LaborController(LaborService laborService) {
        this.laborService = laborService;
    }

    /*
     * Registrar una labor agricola.
     */
    @PostMapping("/labores")
    public ResponseEntity<LaborResponseDTO> crear(
            @Valid @RequestBody LaborRequestDTO request,
            Authentication authentication) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(laborService.crear(
                        request,
                        authentication.getName()
                ));
    }

    /*
     * Listar las labores de un lote.
     * Ordenadas de la mas reciente a la mas antigua.
     */
    @GetMapping("/lotes/{loteId}/labores")
    public ResponseEntity<PaginaResponseDTO<LaborResponseDTO>> listarPorLote(

            @PathVariable Long loteId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio) {

        return ResponseEntity.ok(
                laborService.listarPorLote(
                        loteId,
                        pagina,
                        tamanio
                )
        );
    }

    /*
     * Consultar una labor por su identificador.
     */
    @GetMapping("/labores/{id}")
    public ResponseEntity<LaborResponseDTO> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                laborService.obtenerPorId(id)
        );
    }

    /*
     * Actualizar una labor.
     * TECNICO: solo sus propias labores dentro de 24 horas.
     * JEFE_CAMPO y ADMIN: pueden actualizar cualquier labor.
     */
    @PutMapping("/labores/{id}")
    public ResponseEntity<LaborResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LaborRequestDTO request,
            Authentication authentication) {

        return ResponseEntity.ok(
                laborService.actualizar(
                        id,
                        request,
                        authentication.getName()
                )
        );
    }

    /*
     * Eliminar una labor.
     * Permitido para JEFE_CAMPO y ADMIN.
     */
    @DeleteMapping("/labores/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id,
            Authentication authentication) {

        laborService.eliminar(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
