package modele.plateau;

import modele.item.Item;

public class MachinePoubelle<T extends Item> extends Machine<T, Item, Item, Item> {

    public MachinePoubelle(int tailleBuffer) {
        super(tailleBuffer, 1, 0);
    }

    @Override
    protected void traiter(T entree1, Item entree2) {
        System.out.println("Destruction de " + entree1.toString());
        sortie1(entree1);
        sortie2(entree2);
    }
}
