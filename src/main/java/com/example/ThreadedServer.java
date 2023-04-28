package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
            Socket socket;
                try {
                    socket = serverSocket.accept();


                    try {

                        System.out.println("Client accepted");
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        clients.add(new Client(socket,bufferedReader.readLine()));
                        ServerThread client = new ServerThread(socket);
                        client.start();
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

