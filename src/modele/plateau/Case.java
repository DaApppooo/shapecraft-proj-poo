/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.Item;

public class Case {


    protected Plateau plateau;
    protected Machine<?, ?, ?, ?> machine;
    protected Item gisement; // certaines cases sont des gisements, pour placer des mines


    public void setMachine(Machine<?, ?, ?, ?> m) {
        machine = m;
    }

    public Machine<?, ?, ?, ?> getMachine() {
        return machine;
    }

    public Case(Plateau _plateau) {

        plateau = _plateau;
    }

}