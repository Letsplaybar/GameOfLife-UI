package de.letsplaybar.gameoflife.utils;

public class GameRunable extends Parallism implements Runnable {
    private int start;
    private int end;
    private Game game;
    private int round;

    public GameRunable(Game game, int number, int amount, int length, int round){
     this.game = game;
     this.round = round;
     start = getStart(number,Statics.THREADS.toInt(),length,2);
     end = getEnd(number,Statics.THREADS.toInt(),length,2);
    }



    @Override
    public void run() {
        Feld vor = game.getRounds().get(round-1);
        byte[][] game = vor.getGame();
        Feld roun = this.game.getRounds().get(round);
        for(int x = start; x <=end; x++){
            for(int y = 1; y < game[x].length-1;y++){
                int rule = vor.isFeldChangeing(x,y);
                if((game[x][y] == 0 &&rule == 3) || (game[x][y] == 1 && rule != 2 && rule != 3))
                    roun.changeFeld(x,y);
            }
        }
    }
}
