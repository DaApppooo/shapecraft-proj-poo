package modele.jeu;

import modele.plateau.Machine;
import modele.plateau.Plateau;

public class Jeu extends Thread{
    private Plateau plateau;




    public Jeu() {
        plateau = new Plateau();

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
