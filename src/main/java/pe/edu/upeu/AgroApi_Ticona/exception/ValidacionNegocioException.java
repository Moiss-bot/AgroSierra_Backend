package pe.edu.upeu.AgroApi_Ticona.exception;

import java.util.Map;

public class ValidacionNegocioException extends RuntimeException {

    private final Map<String, String> validationErrors;

    public ValidacionNegocioException(
            String mensaje,
            Map<String, String> validationErrors) {

        super(mensaje);
        this.validationErrors = validationErrors;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}
