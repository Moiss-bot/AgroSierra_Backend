package pe.edu.upeu.AgroApi_Ticona.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteRequestDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.LoteResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.dto.PaginaResponseDTO;
import pe.edu.upeu.AgroApi_Ticona.service.service.LoteService;

@RestController
@RequestMapping("/api/v1/lotes")
public class LoteController {

    private final LoteService loteService;

    public LoteController(LoteService loteService) {
        this.loteService = loteService;
    }

    /*
     * Registrar un nuevo lote.
     */
    @PostMapping
    public ResponseEntity<LoteResponseDTO> crear(
            @Valid @RequestBody LoteRequestDTO request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loteService.crear(request));
    }

    /*
     * Listar lotes con paginacion y filtros opcionales.
     */
    @GetMapping
    public ResponseEntity<PaginaResponseDTO<LoteResponseDTO>> listar(

            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanio,
            @RequestParam(required = false) String valle,
            @RequestParam(required = false) Boolean campaniaCerrada) {

        return ResponseEntity.ok(
                loteService.listar(
                        pagina,
                        tamanio,
                        valle,
                        campaniaCerrada
                )
        );
    }

    /*
     * Obtener un lote por su identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                loteService.obtenerPorId(id)
        );
    }

    /*
     * Actualizar los datos de un lote.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteRequestDTO request) {

        return ResponseEntity.ok(
                loteService.actualizar(id, request)
        );
    }

    /*
     * Eliminar un lote.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        loteService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * Cerrar la campania de un lote.
     * Requiere al menos una labor de tipo COSECHA.
     */
    @PostMapping("/{id}/cerrar-campania")
    public ResponseEntity<LoteResponseDTO> cerrarCampania(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                loteService.cerrarCampania(id)
        );
    }
}
