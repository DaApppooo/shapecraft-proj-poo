public class ItemChaine extends Item {

    private final String valeur;

    public ItemChaine(String valeur) {
        this.valeur = valeur;
    }

    public ItemChaine concatener(ItemChaine autre) {
        return new ItemChaine(valeur + autre.valeur);
    }

    public ItemChaine inverser() {
        ItemChaine c = new ItemChaine(valeur);

    }

    @Override
    public String toString() {
        return valeur;
    }
}
