package jpp.gol.rules;

import javafx.scene.control.Cell;
import jpp.gol.model.CellState;

public class StandardRules implements Rules {
//    Die Klasse StandardRules implementiert die Schnittstelle Rules mit den Standardregeln von Conway's Game of Life:
//
//   + Eine tote Zelle mit genau drei lebenden Nachbarn wird in der Folgegeneration neu geboren.
//   + Lebende Zellen mit weniger als zwei lebenden Nachbarn sterben in der Folgegeneration an Einsamkeit.
//    +Eine lebende Zelle mit zwei oder drei lebenden Nachbarn bleibt in der Folgegeneration lebend.
//    Lebende Zellen mit mehr als drei lebenden Nachbarn sterben in der Folgegeneration an Überbevölkerung.
//    public CellState nextState(int numberOfNeighbors, CellState currentValue)
//    Implementieren Sie die Methode mit den oben definierten Regeln.
//    Sollte numberOfNeighbors unerwartete Werte annehmen, werfen Sie eine IllegalNumberOfNeighborsException
//    mit aussagekräftiger Nachricht.

    //numberofLivingNeighbors

    public StandardRules(){

    }

    @Override
    public CellState nextState(int numberOfNeighbors, CellState currentValue) {
        if(numberOfNeighbors<0 || numberOfNeighbors>8){
            throw new IllegalNumberOfNeighborsException("number of neighbors: invalid");
        }


        CellState a= CellState.DEAD;

            if (numberOfNeighbors == 3 && currentValue == CellState.DEAD) {
                a= CellState.ALIVE;
            } else if (currentValue == CellState.ALIVE && numberOfNeighbors < 2) {
                a= CellState.DEAD;
            } else if (currentValue == CellState.ALIVE && (numberOfNeighbors == 2 || numberOfNeighbors == 3)) {
                a= CellState.ALIVE;
            } else if (currentValue == CellState.ALIVE && numberOfNeighbors > 3) {
                a= CellState.DEAD;
            }


        return a;
    }
}
