package jpp.gol.model;

import java.util.Arrays;
import java.util.Objects;

public class World {
    int width;   //sütun sayisi
    int height;  //satir sayisi
    CellState[][] stateOfField;
//    [a][b] ifadesinde:
//                           a: Satır indeksini temsil eder. Bu, dizinin hangi satırına erişeceğinizi belirtir.
//                           b: Sütun indeksini temsil eder. Bu, seçilen satırdaki hangi sütuna erişeceğinizi belirtir.

    //      [height][width]
    // stateOfField.length : satir sayisi ->       .               stateOfField[i].length : sütun sayisi     | | |
    //                                    ->       .                                                         | | |
    //                                    ->       .
    //

    public World() {
        this.width=10;
        this.height=10;
        stateOfField= new CellState[10][10];  //soldaki: satir indexi
        //sagdaki: sütun indexi
        for(int i =0; i<stateOfField.length;i++){
            for(int e=0; e<stateOfField[i].length; e++){
                stateOfField[i][e]=CellState.DEAD;
            }
        }


    }

    public World(int width, int height) {
        if(width<=0 || height<=0){
            throw new IllegalWorldSizeException("");
        }

        this.width=width;
        this.height=height;

        stateOfField= new CellState[height][width];  //soldaki: satir indexi
        //sagdaki: sütun indexi
        for(int i =0; i<stateOfField.length;i++){
            for(int e=0; e<stateOfField[i].length; e++){
                stateOfField[i][e]=CellState.DEAD;
            }
        }

    }

    public World(World toCopy) {
        this.width=toCopy.getWidth();
        this.height=toCopy.getHeight();
        this.stateOfField=new CellState[height][width];
        for(int i =0; i<stateOfField.length;i++){
            for(int e=0; e<stateOfField[i].length; e++){
                this.stateOfField[i][e]=toCopy.stateOfField[i][e];
            }
        }



    }

    public int countNeighbors(int x, int y) {
        if(x<0 || x>=width || y<0 || y>=height){
            throw new IllegalCoordinateException("coord. invalid (nachbar) ");
        }
        int livingNachbar =0;
        for (int xx = -1; xx <= 1; xx++) {
            for (int yy = -1; yy <= 1; yy++) {
                if (xx == 0 && yy == 0) {
                    continue;
                }
                int neighborX = (x + xx + getWidth()) % getWidth();
                int neighborY = (y + yy + getHeight()) % getHeight();
                if (neighborX < 0 ) {
                    neighborX += width;
                }
                if (neighborY < 0 ) {
                    neighborY += height;
                }
                neighborX = neighborX  % width;
                neighborY = neighborY  % height;


                if (get(neighborX, neighborY) == CellState.ALIVE) {
                    livingNachbar++;
                }
            }
        }

        return livingNachbar;



    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void set(int x, int y, CellState value) {
//        if(x<0 || x>=width || y<0 || y>=height){
//            throw new IllegalCoordinateException("coord. invalid (set)");
//        }
        if(x<0 ){
            throw new IllegalCoordinateException("coord. invalid (set) x<0");
        }
        if( x>=width ){
            throw new IllegalCoordinateException("coord. invalid (set) x>=width");
        }
        if( y<0 ){
            throw new IllegalCoordinateException("coord. invalid (set) y<0");
        }
        if( y>=height){
            throw new IllegalCoordinateException("coord. invalid (set) y>=height");
        }

        stateOfField[y][x]=value;

    }

    public CellState get(int x, int y) {
//        if(x<0 || x>=width || y<0 || y>=height){
//            throw new IllegalCoordinateException("coord. invalid (get) ");
//        }
        if(x<0 ){
            throw new IllegalCoordinateException("coord. invalid (get)1 ");
        }
        if( x>=width ){
            throw new IllegalCoordinateException("coord. invalid (get) 2");
        }
        if( y<0 ){
            throw new IllegalCoordinateException("coord. invalid (get) 3");
        }
        if( y>=height){
            throw new IllegalCoordinateException("coord. invalid (get) 4");
        }

        return stateOfField[y][x];

    }

    @Override
    public String toString() {
        StringBuilder ourWorld = new StringBuilder();

        for(int i=0; i<stateOfField.length; i++){
            for(int e=0; e<stateOfField[i].length; e++) {
                String dead = "0";
                String alive = "1";
                if (stateOfField[i][e] == CellState.DEAD) {
                    ourWorld.append(dead);
                } else {
                    ourWorld.append(alive);
                }

            }
            if (i < stateOfField.length - 1) {
                ourWorld.append("\n");
            }


        }
        return ourWorld.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(o==null){
            return false;
        }

        World inputWorld = (World) o;
        if(height!= ((World) o).height  || width!= ((World) o).width){
            return false;
        }
        for(int i =0; i< stateOfField.length; i++){
            for(int e=0; e<stateOfField[i].length; e++){
                if(stateOfField[i][e]!=((World) o).stateOfField[i][e]){
                    return false;
                }
            }
        }
        return true;
    }
    //
//    @Override
//    public int hashCode() {
//
//    }
    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.deepHashCode(stateOfField);
        return result;
    }


    @Override
    public World clone() {
        World w1 = new World(this);
        return w1;
    }

    public static void main(String[] args) {
        World w1 = new World();
        World w2 =new World(w1);
        World w3 = w1.clone();
        int w1hash = w1.hashCode();
        int w2hash = w2.hashCode();
        int w3hash = w3.hashCode();
        System.out.println("w1:" + w1hash+" w2: " + w2hash+" w3: " + w3hash);
    }


}