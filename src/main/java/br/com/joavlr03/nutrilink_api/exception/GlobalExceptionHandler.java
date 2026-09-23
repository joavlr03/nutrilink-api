package br.com.joavlr03.nutrilink_api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - campos do DTO que falharam no @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.putIfAbsent(erro.getField(), erro.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos na requisição", request, campos);
    }

    // 400 - JSON malformado, data em formato errado, valor de enum inexistente no corpo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonInvalido(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST,
                "Corpo da requisição inválido. Verifique o formato do JSON, datas (yyyy-MM-ddTHH:mm:ss) e valores de enum.",
                request, null);
    }

    // 400 - UUID ou enum inválido na URL (ex.: /coleta/abc, /coleta/status/QUALQUER)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParametroInvalido(MethodArgumentTypeMismatchException ex,
                                                                HttpServletRequest request) {
        String mensagem = "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue();
        return build(HttpStatus.BAD_REQUEST, mensagem, request, null);
    }

    // 400 - regras de negócio sobre os dados enviados (CPF duplicado, menor de idade, CEP fora do corredor...)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleArgumentoInvalido(IllegalArgumentException ex,
                                                                HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    // 404 - recurso não encontrado
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNaoEncontrado(EntityNotFoundException ex,
                                                            HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    // 409 - operação incompatível com o estado atual (doadora não aprovada, ticket fechado, corredor desabilitado...)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleEstadoInvalido(IllegalStateException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    // 409 - violação de chave única/estrangeira (ex.: excluir doadora que possui triagens ou coletas)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegridade(DataIntegrityViolationException ex,
                                                          HttpServletRequest request) {
        return build(HttpStatus.CONFLICT,
                "Operação viola a integridade dos dados. O registro pode estar vinculado a outros registros ou já existir.",
                request, null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String mensagem,
                                                HttpServletRequest request, Map<String, String> campos) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                request.getRequestURI(),
                campos);
        return ResponseEntity.status(status).body(body);
    }
}
