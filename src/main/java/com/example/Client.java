package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private String name;
    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

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

    public ObjectInputStream getObjInput() {
        return objInput;
    }

    public ObjectOutputStream getObjOutput() {
        return objOutput;
    }
    public Client(Socket socket, ObjectOutputStream objOutput, ObjectInputStream objInput) throws IOException {
        this.socket = socket;
        this.objOutput = objOutput;
        this.objOutput.flush();
        this.objInput = objInput;
    }
}
