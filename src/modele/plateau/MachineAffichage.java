package modele.plateau;

import modele.item.Item;

public class MachineAffichage<T extends Item> extends Machine<T, Item, T, Item> {

    public MachineAffichage(int tailleBuffer) {
        super(tailleBuffer, 1, 1);
    }

    @Override
    protected void traiter(T entree1, Item entree2) {
        // TODO : reprendre le code développé en partie 1
    }
}
