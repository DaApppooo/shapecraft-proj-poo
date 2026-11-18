package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

/**
 * Prototype de tapis : ne fait rien, présente simplement en sortie
 * l'ItemShape reçu à la construction (pour visualiser son rendu).
 */
public class MachineProtoTapis extends Machine<Item, Item, ItemShape, Item> {

    public MachineProtoTapis(ItemShape item) {
        super(1, 0, 1);
        sortie1(item);
    }

    @Override
    protected void traiter(Item entree1, Item entree2) {
    }
}
