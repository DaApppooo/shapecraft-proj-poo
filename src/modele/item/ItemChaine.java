package modele.item;

public class ItemChaine extends Item {

    private final String valeur;

    public ItemChaine(String valeur) {
        this.valeur = valeur;
    }

    @Override
    public String toString() {
        return valeur;
    }
}
