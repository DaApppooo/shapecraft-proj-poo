public class MachineConcatenatrice extends Machine<ItemChaine, ItemChaine, ItemChaine, Item> {

    public MachineConcatenatrice(int tailleBuffer) {
        super(tailleBuffer, 2, 1);
    }

    @Override
    protected void traiter(ItemChaine entree1, ItemChaine entree2) {
        sortie1(entree1.concatener(entree2));
    }
}
