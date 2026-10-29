
package pe.edu.upeu.AgroApi_Ticona.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import pe.edu.upeu.AgroApi_Ticona.exception.ValidacionNegocioException;

import java.util.Map;
import java.util.Set;

public final class PaginacionUtil {

    public static final int TAMANIO_MAXIMO = 100;

    private PaginacionUtil() {
    }

    /*
     * Construir paginacion con ordenamiento.
     */
    public static Pageable construir(
            int pagina,
            int tamanio,
            String ordenarPor,
            String direccion,
            Set<String> camposOrdenables,
            String ordenPorDefecto) {

        if (pagina < 0) {
            throw new ValidacionNegocioException(
                    "La página no puede ser negativa",
                    Map.of(
                            "pagina",
                            "Debe ser mayor o igual a cero"
                    )
            );
        }

        if (tamanio < 1 || tamanio > TAMANIO_MAXIMO) {
            throw new ValidacionNegocioException(
                    "El tamaño de página debe estar entre 1 y "
                            + TAMANIO_MAXIMO,
                    Map.of(
                            "tamanio",
                            "Debe estar entre 1 y " + TAMANIO_MAXIMO
                    )
            );
        }

        return PageRequest.of(
                pagina,
                tamanio,
                construirSort(
                        ordenarPor,
                        direccion,
                        camposOrdenables,
                        ordenPorDefecto
                )
        );
    }

    /*
     * Construir ordenamiento.
     */
    public static Sort construirSort(
            String ordenarPor,
            String direccion,
            Set<String> camposOrdenables,
            String ordenPorDefecto) {

        String campo =
                (ordenarPor == null || ordenarPor.isBlank())
                        ? ordenPorDefecto
                        : ordenarPor.trim();

        if (!camposOrdenables.contains(campo)) {
            throw new ValidacionNegocioException(
                    "El campo de ordenamiento '"
                            + campo
                            + "' no está permitido",
                    Map.of(
                            "ordenarPor",
                            "Campos válidos: " + camposOrdenables
                    )
            );
        }

        String sentido =
                (direccion == null || direccion.isBlank())
                        ? "desc"
                        : direccion.trim();

        if (!sentido.equalsIgnoreCase("asc")
                && !sentido.equalsIgnoreCase("desc")) {

            throw new ValidacionNegocioException(
                    "La dirección de ordenamiento '"
                            + sentido
                            + "' no está permitida",
                    Map.of(
                            "direccion",
                            "Valores válidos: asc, desc"
                    )
            );
        }

        return sentido.equalsIgnoreCase("asc")
                ? Sort.by(campo).ascending()
                : Sort.by(campo).descending();
    }
}
