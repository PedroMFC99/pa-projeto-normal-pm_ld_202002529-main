package pt.pa.model;

/**
 * Memento Exception Handler.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class NoMementoException extends RuntimeException {
    public NoMementoException() {
        super("There is no Memento");
    }

    public NoMementoException(String s) {
        super(s);
    }
}
