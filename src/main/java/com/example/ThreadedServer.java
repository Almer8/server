package com.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadedServer {
    int port;

    public ThreadedServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true){

                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected");

                    try {
                        new ServerThread(socket);
                    } catch (IOException e){
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

