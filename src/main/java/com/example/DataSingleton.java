package com.example;

public class DataSingleton {

    private static final DataSingleton instance = new DataSingleton();

    public ThreadedServer threadedServer;

    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public void setServer(ThreadedServer threadedServer){
        this.threadedServer = threadedServer;
    }

    public ThreadedServer getServer(){
        return instance.threadedServer;
    }


}
