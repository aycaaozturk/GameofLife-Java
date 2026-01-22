package jpp.gol.logic;

import jpp.gol.model.CellState;
import jpp.gol.model.World;
import jpp.gol.rules.Rules;

public class StandardGameLogic implements GameLogic {
//    public StandardGameLogic(World world, Rules rules)
//    Initialisiert eine neue Instanz mit einer Welt und den zu verwendenden Regeln.
//    Hat einer der beiden Parameter world und rules den Wert null, werfen Sie eine NullPointerException.
//    public void step()
//    Berechnet die nächste Iteration des Spielfeldes. Dazu werden die im Konstruktor gesetzten Regeln verwendet.

    World world;
    Rules rules;

    public StandardGameLogic(World world, Rules rules) {
        if(world==null || rules==null){
            throw new NullPointerException("parameters: null");
        }
        this.world=world;
        this.rules=rules;

    }

    @Override
    public void step() {
//        for(int y=0; y< world.getHeight(); y++){
//            for(int x=0; x< world.getWidth(); x++){
//                changeState(x,y);
//            }
//        }
        World newWorldToUpdate = new World(world.getWidth(), world.getHeight());
        for(int i =0; i<world.getHeight(); i++){
            for(int e =0; e< world.getWidth(); e++){    //genislik x
                int nachbar = world.countNeighbors(e, i);
                CellState currentState = world.get(e,i);
                CellState newState = rules.nextState(nachbar, currentState);
               newWorldToUpdate.set(e,i, newState);
            }
        }
       this.world=newWorldToUpdate;

    }

    @Override
    public void setWorld(World world) {
        this.world=world;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void changeState(int x, int y) {
//        int livingNeighbors = this.world.countNeighbors(x,y);
//        CellState current = this.world.get(x,y);
//        CellState newState =rules.nextState(livingNeighbors,current);
//        this.world.set(x,y, newState);

        CellState currentState = this.world.get(x,y).invert();
        this.world.set(x,y,currentState);

    }
}
