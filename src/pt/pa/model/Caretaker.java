package pt.pa.model;

/**
 * Memento pattern - Caretaker
 * <p>
 * Docente Orientador: Luís Damas
 * <p>
 * Grupo: Diogo Letras - Nº 202002529 - Turma: 2ºL_EI-SW-06
 * Miguel Vicente - Nº 202000563 - Turma: 2ºL_EI-SW-06
 * Pedro Cunha - Nº 202000757 - Turma: 2ºL_EI-SW-02
 * Jorge Mimoso - Nº 202000695 - Turma: 2ºL_EI-SW-03
 */
public class Caretaker {

    private Memento memento;

    private Originator originator;

    public Caretaker(Originator originator) {
        this.originator = originator;
    }

    /**
     * Saves the state, creating a snapshot.
     */
    public void saveState() {
        this.memento = originator.createMemento();
    }

    /**
     * Restores the state to a specific snapshot.
     *
     * @throws NoMementoException
     */
    public void restoreState() throws NoMementoException {
        originator.setMemento(this.memento);
    }


}
