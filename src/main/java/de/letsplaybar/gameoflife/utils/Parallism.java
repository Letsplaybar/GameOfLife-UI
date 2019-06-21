package de.letsplaybar.gameoflife.utils;

public class Parallism {

    protected int getStart(int rank, int size, int lines, int ghost){
        int start = (rank*(lines /size))+ghost/2;
        if(rank != 0)
            if(rank < lines%size)
                start += rank;
            else
                start +=lines%size;
        return start;
    }

    protected int getEnd(int rank, int size, int lines, int ghost){
        int start = getStart(rank,size,lines, ghost);
        int end = start + lines/size;
        if(rank >= lines% size){
            end = end-1;
        }
        return end;
    }
}
