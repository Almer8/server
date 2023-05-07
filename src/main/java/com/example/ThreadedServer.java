package com.example;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import message.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ThreadedServer implements Runnable {
    int port;
    static public List<Client> clients = new ArrayList<>();
    static public ObservableList<Message> messages = FXCollections.observableArrayList();



    public ThreadedServer(int port) {
        this.port = port;
    }

    public void run(){
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

