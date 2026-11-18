package vuecontroleur;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;


import modele.item.Item;
import modele.item.ItemChaine;
import modele.item.ItemShape;
import modele.jeu.Jeu;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 82; // nombre de pixel par case
    private static final int pxBouton = 64; // taille des icônes des boutons
    // icones affichées dans la grille
    private Image icoRouge;
    private Image icoTapisDroite;
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoRotater;
    private Image icoGeneratrice;

    private JComponent grilleIP;

    private boolean mousePressed = false; // permet de mémoriser l'état de la souris, à vous de voir si cela peut vous être utile

    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


    private void chargerLesIcones() {

        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();

        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoRotater = new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoGeneratrice = new ImageIcon("./data/sprites/buildings/item_producer.png").getImage();

    }



    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase + pxBouton + 16, sizeX * pxCase); // grille + panneau de boutons à droite
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille


        tabIP = new ImagePanel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();

                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mousePressed = false;
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            jeu.slide(xx, yy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;
                        jeu.press(xx, yy);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;

                    }
                });


                grilleIP.add(iP);
            }
        }
        add(grilleIP, BorderLayout.CENTER);

        // panneau de droite : un bouton par machine disponible
        JPanel panneauMachines = new JPanel(new GridLayout(3, 1));
        panneauMachines.add(creerBouton(icoGeneratrice, null, MachineGeneratrice.class));
        panneauMachines.add(creerBouton(null, "abc", MachineAffichage.class));
        panneauMachines.add(creerBouton(icoPoubelle, null, MachinePoubelle.class));

        JPanel panneauDroite = new JPanel(new BorderLayout());
        panneauDroite.add(panneauMachines, BorderLayout.NORTH);
        add(panneauDroite, BorderLayout.EAST);
    }

    /**
     * Crée un bouton (icône ou texte) qui transmet au modèle la classe de machine choisie
     */
    private JButton creerBouton(Image ico, String texte, final Class<? extends Machine> classeMachine) {
        JButton bouton = new JButton(texte);
        if (ico != null) {
            bouton.setIcon(new ImageIcon(ico.getScaledInstance(pxBouton, pxBouton, Image.SCALE_SMOOTH)));
        }
        bouton.setPreferredSize(new Dimension(pxBouton + 16, pxBouton + 16));
        // écouteur de clics sur le bouton
        bouton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.choisirMachine(classeMachine);
            }
        });
        return bouton;
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabIP)
     */
    private void mettreAJourAffichage() {


        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                tabIP[x][y].setBackground((Image) null);

                tabIP[x][y].setFront(null);

                tabIP[x][y].setShape(null);

                tabIP[x][y].setTexte(null);

                Case c = plateau.getCases()[x][y];

                Machine<?, ?, ?, ?> m = c.getMachine();

                if (m instanceof MachineAffichage) {
                    Item resultat = ((MachineAffichage<?>) m).getSortie1();
                    if (resultat != null) {
                        tabIP[x][y].setTexte(resultat.toString());
                    } else {
                        tabIP[x][y].setTexte("");
                    }
                } else if (m instanceof MachineGeneratrice) {
                    tabIP[x][y].setBackground(icoGeneratrice);
                } else if (m instanceof MachinePoubelle) {
                    tabIP[x][y].setBackground(icoPoubelle);
                } else if (m instanceof MachineProtoTapis) {
                    tabIP[x][y].setBackground(icoTapisDroite);
                    tabIP[x][y].setShape(((MachineProtoTapis) m).getSortie1());
                }


            }
        }
        grilleIP.repaint();


    }

    @Override
    public void update(Observable o, Object arg) {

        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 

    }
}
