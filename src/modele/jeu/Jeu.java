package modele.jeu;

import modele.plateau.*;
import modele.item.ItemChaine;

public class Jeu extends Thread{
    private Plateau plateau;

    public Jeu() {
        plateau = new Plateau();

        MachineGeneratrice gen = new MachineGeneratrice(4);
        MachineAffichage<ItemChaine> aff = new MachineAffichage<ItemChaine>(4);
        MachinePoubelle<ItemChaine> poub = new MachinePoubelle<ItemChaine>(4);

        aff.addInput(0, gen, 0);
        poub.addInput(0, aff, 0);

        plateau.setMachine(1, 1, gen);
        plateau.setMachine(2, 1, aff);
        plateau.setMachine(3, 1, poub);

        start();

    }

    public Plateau getPlateau() {
        return plateau;
    }


    /** Appelée par le contrôleur lors d'un clic sur la case (x, y) de la grille */
    public void press(int x, int y) {
        System.out.println("clic case : " + x + ", " + y);
    }

    /** Appelée par le contrôleur lorsque la souris, bouton enfoncé, entre dans la case (x, y) */
    public void slide(int x, int y) {
        System.out.println("glisser case : " + x + ", " + y);
    }

    /** Appelée par le contrôleur lors d'un clic sur un bouton du panneau de machines */
    public void choisirMachine(Class<? extends Machine> classeMachine) {
        System.out.println("machine choisie : " + classeMachine.getSimpleName());
    }


    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
