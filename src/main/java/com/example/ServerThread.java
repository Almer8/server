package com.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ServerThread extends Thread {
     Socket clientSocket;
    BufferedReader inputStream;
    PrintWriter outputStream;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    public  ServerThread(Socket socket) throws IOException{
        this.clientSocket = socket;

        try {
            this.objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.flush();
            this.objInput = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e){
            System.out.println(e);
        }


    }

    @Override
    public void run(){

    while (true) {
    String request = null;
        try {

            while (true) {
                try {

                    String temprequest = (String) objInput.readObject();
                    if (!Objects.equals(temprequest, request)) {
                    request = temprequest;
                    break;
                    }
                }
                catch (Exception e) {

                }

            }
            if (request.equals("GET_USERS")) {
                List<String> users = new ArrayList<>();
                for (Client client : ThreadedServer.clients) {
                    users.add(client.getName());
                }

                objOutput.writeObject(users);
                objOutput.flush();
                System.out.println("Userlist sended to client");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
}




