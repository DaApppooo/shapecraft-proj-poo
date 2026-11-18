package modele.plateau;

import modele.item.Item;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Une machine universelle, paramétrée par les types d'Item qu'elle
 * attend sur ses deux entrées (E1, E2) et produit sur ses deux sorties
 * (S1, S2) au plus. Une machine ayant moins de deux entrées ou de deux
 * sorties laisse simplement Item comme paramètre pour l'entrée ou la
 * sortie inutilisée.
 *
 * Le nombre d'entrées et le nombre de sorties sont fixés à la
 * construction. Chaque entrée dispose d'un buffer (une file de taille
 * tailleBuffer) ; chaque sortie dispose d'un emplacement de taille 1 (un
 * Item, ou rien si vide).
 *
 * executer() réalise un pas de simulation en trois temps :
 *  1. on tire, pour chaque entrée connectée, l'item prêt chez la machine
 *     amont vers son propre buffer d'entrée, s'il y a de la place ;
 *  2. on vérifie que toutes les sorties utilisées sont libres et que
 *     tous les buffers d'entrée utilisés contiennent au moins un item ;
 *  3. si c'est le cas, on consomme un item par entrée, on appelle
 *     traiter(), et on dépose le résultat dans les buffers de sortie.
 *
 * Toute machine concrète (génératrice, concaténatrice, poubelle, ...)
 * ne définit que traiter(E1, E2) : void, en déposant son résultat via
 * sortie1()/sortie2() ; executer() est entièrement générique et commun
 * à toutes les machines.
 */
public abstract class Machine<E1 extends Item, E2 extends Item, S1 extends Item, S2 extends Item> {

    private final int tailleBuffer;
    private final int nbEntrees;
    private final int nbSorties;

    private Machine<?, ?, ?, ?> amont1;
    private int idBufferSortieAmont1;
    private Machine<?, ?, ?, ?> amont2;
    private int idBufferSortieAmont2;

    private final Queue<E1> bufferEntree1 = new LinkedList<>();
    private final Queue<E2> bufferEntree2 = new LinkedList<>();

    private S1 bufferSortie1;
    private S2 bufferSortie2;

    public Machine(int tailleBuffer, int nbEntrees, int nbSorties) {
        this.tailleBuffer = tailleBuffer;
        this.nbEntrees = nbEntrees;
        this.nbSorties = nbSorties;
    }

    /** Connecte le buffer d'entrée idBufferEntree (0 ou 1) de this au buffer de sortie idBufferSortieAmont (0 ou 1) de machineAmont. */
    public void addInput(int idBufferEntree, Machine<?, ?, ?, ?> machineAmont, int idBufferSortieAmont) {
        if (idBufferEntree == 0) {
            amont1 = machineAmont;
            idBufferSortieAmont1 = idBufferSortieAmont;
        } else {
            amont2 = machineAmont;
            idBufferSortieAmont2 = idBufferSortieAmont;
        }
    }

    /** Dépose directement item dans le buffer d'entrée 1, sans passer par une machine amont (amorçage). */
    public void initialiserEntree1(E1 item) {
        bufferEntree1.add(item);
    }

    /** Retire et renvoie l'item présent à l'emplacement de sortie idBufferSortie (0 ou 1), ou null s'il est vide. */
    private Item retirerSortie(int idBufferSortie) {
        if (idBufferSortie == 0) {
            Item item = bufferSortie1;
            bufferSortie1 = null;
            return item;
        } else {
            Item item = bufferSortie2;
            bufferSortie2 = null;
            return item;
        }
    }

    /** Dépose item dans le buffer de sortie 1 ; à appeler depuis traiter(). */
    protected void sortie1(S1 item) {
        bufferSortie1 = item;
    }

    /** Dépose item dans le buffer de sortie 2 ; à appeler depuis traiter(). */
    protected void sortie2(S2 item) {
        bufferSortie2 = item;
    }

    /** Consulte le buffer de sortie 1 sans le vider (pour la vue, qui doit pouvoir le lire à chaque rafraîchissement). */
    public S1 getSortie1() {
        return bufferSortie1;
    }

    protected abstract void traiter(E1 entree1, E2 entree2);

    public static <A, B> boolean canCast(B obj)
    {
        try
        {
            A a = (A)obj;
            return true;
        }
        catch (ClassCastException e)
        {
            return false;
        }
    }
    private int[] idBufferSorties()
    {
        return new int[]{idBufferSortieAmont1, idBufferSortieAmont2};
    }
    private Machine[] amonts()
    {
        return new Machine[]{amont1, amont2};
    }
    public void executer() {
        for (int i = 0; i < nbEntrees; i++)
        {
            Item inp = amonts()[i].retirerSortie(idBufferSorties()[i]);
            if (bufferEntree1.size() < tailleBuffer && Machine.<E1, Item>canCast(inp))
            {
                bufferEntree1.add((E1)inp);
            }
            else if (bufferEntree2.size() < tailleBuffer && Machine.<E1, Item>canCast(inp))
            {
                bufferEntree2.add((E2)inp);
            }
            else
            {
                throw new RuntimeException("Type mismatch when treating machine inputs.");
            }
        }
        if (nbSorties > 0 && bufferSortie1 != null)
            return;
        if (nbSorties > 1 && bufferSortie2 != null)
            return;
        E1 e1 = bufferEntree1.poll();
        E2 e2 = bufferEntree2.poll();
        traiter(e1, e2);
    }
}
