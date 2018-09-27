package de.letsplaybar.gameoflife.utils;

public enum Statics {

    THREADS(System.getProperty("NUMBER_OF_PROCESSORS")!= null? System.getProperty("NUMBER_OF_PROCESSORS")
            :System.getenv("NUMBER_OF_PROCESSORS"))
    ;


    private String value;
    Statics(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public int toInt(){
        return Integer.parseInt(value);
    }
}
