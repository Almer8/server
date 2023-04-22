package com.example;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
     Socket socket;
     BufferedReader inputStream;
     PrintWriter outputStream;

    public  ServerThread(Socket socket) throws IOException{
        this.socket = socket;
        try {
            this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputStream = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        }
        catch (IOException e){
            System.out.println(e);
        }


    }
    @Override
    public void run(){
        while (true){
            System.out.println("200");
        }
    }




    
}



