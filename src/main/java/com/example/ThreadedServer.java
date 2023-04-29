package com.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ThreadedServer {
    int port;
    static public List<Client> clients = new ArrayList<>();

    public ThreadedServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true){
            Socket socket = null;
            Client client = null;
            ServerThread clientThread = null;
            BufferedReader bufferedReader = null;


                try {
                    socket = serverSocket.accept();


                    try {

                        System.out.println("Client accepted");
                        ObjectOutputStream objOutput = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream());

                        client = new Client(socket,objOutput,objInput);
                        clients.add(client);
                        clientThread = new ServerThread(client);
                        clientThread.start();
                        System.out.println("Client connected");
                    } catch (Exception e){
                        System.out.println(e);
                    }
                }
                catch (IOException e ){
                    System.out.println(e);
                }
            }
        }
        catch (IOException e){
            System.out.println(e);
        }

    }
}

