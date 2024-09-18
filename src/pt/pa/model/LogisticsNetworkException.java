package pt.pa.model;

import java.io.IOException;

/**
 * Exception Handler for LogisticsNetwork class.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class LogisticsNetworkException extends RuntimeException {

    public LogisticsNetworkException() {
    }

    public LogisticsNetworkException(String message) {
        super(message);
    }
}
