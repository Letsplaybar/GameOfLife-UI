package de.letsplaybar.gameoflife.utils;

import de.letsplaybar.gameoflife.GameOfLife;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;

public class InitRunnable extends Parallism implements Runnable {

    private int length;
    private int width;
    private ValueCallback valueCallback;
    private Game currentGame;
    private int start;
    private int end;

    public InitRunnable(int length, int width, ValueCallback valueCallback, Game currentGame, int number){
        this.length = length;
        this.width = width;
        this.valueCallback = valueCallback;
        this.currentGame = currentGame;
        start = getStart(number,Statics.THREADS.toInt(),length,2);
        end = getEnd(number,Statics.THREADS.toInt(),length,2);
    }


    @Override
    public void run() {

        Random random = new Random(System.nanoTime());
        for(int x = start-1; x <end; x++)
            for(int y = 0; y < width; y++) {
                if (random.nextLong() % 2 == 1) {
                    currentGame.getRounds().get(0).changeFeld(x + 1, y + 1);
                    int finalX = x;
                    int finalY = y;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            valueCallback.handle(finalX, finalY);
                        }
                    });
                }
            }
    }
}
