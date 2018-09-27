package de.letsplaybar.gameoflife.utils;

public class GameRunable implements Runnable {
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

    private int getStart(int rank, int size, int lines, int ghost){
        int start = (rank*((int)lines/size))+ghost/2;
        if(rank != 0)
            if(rank < lines%size)
                start += rank;
            else
                start +=lines%size;
        return start;
    }

    private int getEnd(int rank, int size, int lines, int ghost){
        int start = getStart(rank,size,lines, ghost);
        int end = start + lines/size;
        if(rank >= lines% size){
            end = end-1;
        }
        return end;
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
