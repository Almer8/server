package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
    protected Socket socket;

    public ServerThread(Socket clientSocket){
        this.socket = clientSocket;
    }

    public void run(){
        InputStream input = null;
        BufferedReader reader = null;
        DataOutputStream output = null;
        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            output = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            return;

        }
        String line;
        while (true) {
            try {
                line = reader.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                    output.writeBytes(line + "\n\r");
                    output.flush();
                }
            } catch (Exception e) {
                System.out.println(e);
                return;
            }


    
        }
    }
}

