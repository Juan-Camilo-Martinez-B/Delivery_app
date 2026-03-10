package com.delivery.delivery_app.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String mensaje;
    private int status;
    private String error;
    private Map<String, String> errores;

    public ErrorResponse() {}

    public ErrorResponse(LocalDateTime timestamp, String mensaje, int status, String error) {
        this.timestamp = timestamp;
        this.mensaje = mensaje;
        this.status = status;
        this.error = error;
    }

    public ErrorResponse(LocalDateTime timestamp, String mensaje, int status, String error, Map<String, String> errores) {
        this.timestamp = timestamp;
        this.mensaje = mensaje;
        this.status = status;
        this.error = error;
        this.errores = errores;
    }

    // Getters y Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public Map<String, String> getErrores() { return errores; }
    public void setErrores(Map<String, String> errores) { this.errores = errores; }
}
