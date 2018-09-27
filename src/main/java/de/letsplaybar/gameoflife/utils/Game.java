package de.letsplaybar.gameoflife.utils;

import lombok.Getter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Game {

    private @Getter int length;
    private @Getter int width;
    private int[] worklines;
    private @Getter HashMap<Integer,Feld> rounds;
    private ExecutorService pool;

    public Game(int length, int width){
        this.length = length;
        this.width = width;
        rounds = new HashMap();
        rounds.put(0,new Feld(length,width));
        worklines = new int[Statics.THREADS.toInt()];
        for(int i = 0; i< worklines.length; i++) {
            worklines[i] = length / worklines.length;
            if(length%worklines.length>=i)
                worklines[i]++;
        }
        pool = Executors.newFixedThreadPool(Statics.THREADS.toInt());
    }

    public void run() throws CloneNotSupportedException, InterruptedException {
        int generation = rounds.size();
        rounds.put(generation,(Feld)rounds.get(generation-1).clone());
        int i=0;
        for(int amount : worklines)
            pool.execute(new GameRunable(this,i++,amount,length,generation));
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        pool = Executors.newFixedThreadPool(Statics.THREADS.toInt());
        rounds.get(generation).closeGen();
    }

    public File save(File name) throws IOException {
        File game = name;
        JSONObject object = new JSONObject();
        object.put("length",length);
        object.put("width", width);
        object.put("rounds", new JSONObject());
        for(Integer feld : rounds.keySet())
            ((JSONObject)object.get("rounds")).put(String.valueOf(feld),rounds.get(feld).getGame());
        FileWriter writer = new FileWriter(game);
        writer.write(object.toString());
        writer.close();
        return game;
    }

    public static Game load(File name) throws IOException {
        File file = name;
        if(!file.exists())
            throw new NullPointerException("File not exists");
        FileReader reader = new FileReader(file);
        BufferedReader buf = new BufferedReader(reader);
        JSONObject object = new JSONObject(buf.readLine());
        buf.close();
        reader.close();
        Game game =new Game(object.getInt("length"),object.getInt("width"));
        JSONObject array = object.getJSONObject("rounds");
        Map<String, Object> ron = array.toMap();
        for(String key : ron.keySet()){
            byte[][] load = new byte[game.getLength()+2][game.getWidth()+2];
            for(int x =0; x< game.length+2 ; x++)
                for(int y = 0;y<game.width+2;y++)
                    load[x][y] = (byte) array.getJSONArray(key).getJSONArray(x).getInt(y);
            game.rounds.put(Integer.parseInt(key), new Feld(load));
        }
        return game;
    }

}
