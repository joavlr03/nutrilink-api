package br.com.joavlr03.nutrilink_api.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato padrão de erro devolvido pela API.
 * "campos" só vem preenchido em erros de validação (400).
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos) {
}
