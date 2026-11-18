import java.util.ArrayList;
import java.util.List;

/**
 * Appelle, à chaque tour de simulation, la méthode executer() de
 * chaque machine enregistrée, dans l'ordre d'enregistrement.
 */
public class Ordonnanceur {

    private static final int PAUSE_MS = 500;

    private final List<Machine<?, ?, ?, ?>> machines = new ArrayList<Machine<?, ?, ?, ?>>();

    public void ajouter(Machine<?, ?, ?, ?> machine) {
        machines.add(machine);
    }

    public void tour() {
        for (Machine<?, ?, ?, ?> machine : machines) {
            machine.executer();
        }
    }

    public void simuler(int nbTours) {
        for (int i = 0; i < nbTours; i++) {
            tour();
            try {
                Thread.sleep(PAUSE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        MachineConcatenatrice c = new MachineConcatenatrice(1);
    }
}
