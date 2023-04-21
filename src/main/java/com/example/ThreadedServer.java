package com.example;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadedServer {
     int port;

    public ThreadedServer(int port){
        this.port = port;
    }
    public void run() throws Exception{
        try{
        ServerSocket serverSocket = new ServerSocket(6666);
        Socket socket = serverSocket.accept();
        }
        catch(SocketTimeoutException e){
            System.out.println(e);
        }

            
            //new ServerThread(socket).start();
        }
    
}

