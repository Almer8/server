package com.example;

import java.net.Socket;

public class Client {
    private Socket socket;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }
    public Client(Socket socket,String name){
        this.name = name;
        this.socket = socket;
    }
}
