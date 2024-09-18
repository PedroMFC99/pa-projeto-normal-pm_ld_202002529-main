package pt.pa.observerpattern;

/**
 * Observable Interface.
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public interface Observable {
    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObservers(Object arg);
}
