package jpp.gol.model;

public enum CellState {
//    Es gibt zwei Zustände für Felder im Game of Life: tot und lebendig.
//    Die Enumeration CellState soll den Zustand eines Feldes unseres Spiels darstellen. Sie enthält die Werte DEAD und ALIVE.
//
//    public static CellState fromBoolean(boolean b)
//    Wandelt einen Booleschen Wert in einen CellState um.
//    Geben Sie DEAD zurück, wenn b false ist und ALIVE, wenn b true ist.


    //    public CellState invert()
//    Gibt den jeweils anderen CellState zurück. Geben Sie DEAD beim Aufruf von CellState.ALIVE.invert() zurück und ALIVE bei CellState.DEAD.invert().
//
//
    DEAD,ALIVE;

    public static CellState fromBoolean(boolean b) {
       if(b==false){
           return DEAD;
       }
       else {
           return ALIVE;   //true ise
       }

    }

    public CellState invert() {  //tersini veriyor
        if(this==DEAD){
            return ALIVE;
        }
        else{
            return DEAD;
        }
    }
}
