package jpp.gol.logic;

import jpp.gol.model.World;

import java.util.ArrayList;
import java.util.List;

public class ObservableGameLogicDecorator implements GameLogic {

    public GameLogic delegate;
    public List<WorldChangedListener> listeners;


    public ObservableGameLogicDecorator(GameLogic delegate) {
        if(delegate==null){
            throw new NullPointerException("delegate is null");
        }
        this.delegate=delegate;
        listeners= new ArrayList<>();
    }

    public void addWorldChangedListener(WorldChangedListener listener) {
           if( !listeners.contains(listener)){
               listeners.add(listener);
           }

    }

    public void removeWorldChangedListener(WorldChangedListener listener) {
        listeners.remove(listener);
    }
    public void notifyListeners(){
        for(WorldChangedListener l : listeners){
            l.onChange(delegate.getWorld());
        }
    }
    @Override
    public void step() {  //is a change
       delegate.step();
       notifyListeners();
    }

    @Override
    public void setWorld(World world) {
       delegate.setWorld(world);
    }

    @Override
    public World getWorld() {
        return delegate.getWorld();
    }

    @Override
    public void changeState(int x, int y) {     //is a change
       delegate.changeState(x,y);
       notifyListeners();
    }
}
