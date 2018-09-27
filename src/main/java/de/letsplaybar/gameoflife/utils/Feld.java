package de.letsplaybar.gameoflife.utils;

import lombok.Getter;

public class Feld  {

    private @Getter byte [][] game;

    public Feld(int length, int width){
        game = new byte[length+2][width+2];
    }

    protected Feld(byte[][] game){
        this.game = game;
    }

    public void changeFeld(int x, int y){
        game[x][y] = game[x][y]==0? (byte)1 : 0;
    }

    public int isFeldChangeing(int x, int y){
        return ( game[x-1][y-1]
                +game[x][y-1]
                +game[x+1][y-1]
                +game[x-1][y]
                +game[x+1][y]
                +game[x-1][y+1]
                +game[x][y+1]
                +game[x+1][y+1]);
    }

    public void closeGen(){
        for(int i = 0; i < game.length; i++){
            game[i][0]=game[i][game[i].length-2];
            game[i][game[i].length-1] = game[i][1];
        }
        for(int i = 0; i < game[1].length; i++){
            game[0][i]=game[game.length-2][i];
            game[game.length-1][i] = game[1][i];
        }

    }

    @Override
    protected Object clone() {
        byte[][] clone = new byte[game.length][game[0].length];
        for(int x = 0; x< clone.length; x++)
            for (int y =0; y<clone[x].length;y++){
                clone[x][y] = game[x][y];
            }
        return new Feld(clone);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Feld)
            return game.equals(((Feld) obj).game);
        return false;
    }

    public boolean isDead(){
        for(int x = 1; x < game.length-1;x++){
            for (int y = 1; y < game[x].length-1; y++){
                if(game[x][y] == 1)
                    return false;
            }
        }
        return true;
    }
}
