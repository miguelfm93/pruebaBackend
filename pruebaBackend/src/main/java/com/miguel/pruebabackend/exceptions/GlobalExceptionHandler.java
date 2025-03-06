package com.miguel.pruebabackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase GlobalExceptionHandler
 *
 * Maneja las excepciones globales de la aplicación, proporcionando respuestas
 * estructuradas y códigos de estado HTTP apropiados.
 *
 * @author Miguel Fernandez
 * @version 1.0
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando el carrito no es encontrado.
     */
    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Object> handleCartNotFound(CartNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), "Carrito no encontrado", HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando los productos enviados no son válidos.
     */
    @ExceptionHandler(InvalidProductException.class)
    public ResponseEntity<Object> handleInvalidProduct(InvalidProductException ex) {
        return buildErrorResponse(ex.getMessage(), "Error en los productos", HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de parámetros inválidos, como un ID negativo o nulo.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInvalidArguments(IllegalArgumentException ex) {
        return buildErrorResponse(ex.getMessage(), "Parámetro inválido", HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones genéricas.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildErrorResponse(ex.getMessage(), "Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Construye una respuesta estructurada en JSON para los errores.
     */
    private ResponseEntity<Object> buildErrorResponse(String message, String errorType, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorType);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, status);
    }
}
