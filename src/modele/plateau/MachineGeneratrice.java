package modele.plateau;

import modele.item.Item;
import modele.item.ItemChaine;

public class MachineGeneratrice extends Machine<Item, Item, ItemChaine, Item> {

    public MachineGeneratrice(int tailleBuffer) {
        super(tailleBuffer, 0, 1);
    }

    @Override
    protected void traiter(Item entree1, Item entree2) {
        // TODO : reprendre le code développé en partie 1
    }
}
