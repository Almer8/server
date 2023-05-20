package com.example;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import message.Message;
import message.MessageType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class ThreadedServer implements Runnable {
    int port;
    static public ServerSocket serverSocket;
    static public ObservableList<Client> clients = FXCollections.observableArrayList();
    static public ObservableList<Message> messages = FXCollections.observableArrayList();



    public ThreadedServer(int port) throws IOException {
        this.port = port;
        ThreadedServer.serverSocket = new ServerSocket(port);
    }

    public void run(){
        while (!serverSocket.isClosed()){
        Socket socket = null;
        Client newclient = null;
        ServerThread clientThread = null;
        BufferedReader bufferedReader = null;


            try {
                socket = serverSocket.accept();


                try {

                    System.out.println("Client accepted");
                    ObjectOutputStream objOutput = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objInput = new ObjectInputStream(socket.getInputStream());

                    newclient = new Client(socket,objOutput,objInput);
                    clientThread = new ServerThread(newclient);
                    clientThread.start();
                    System.out.println("Client connected");
                    int count = 0;
                    System.out.println(newclient.getName());
                    for (Client userclient: ThreadedServer.clients) {
                        if(userclient.getName().equals(newclient.getName())){
                            count++;
                        }
                    }
                    if (count > 0){
                        Message message = new Message(MessageType.SERVER,"DUPLICATE_USER");
                        try {
                            System.out.println(message.getMessageType().toString() + " " + message.getMessageText());
                            objOutput.writeObject(message);
                            objOutput.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else {
                        List<String> users = new ArrayList<>();
                        for (Client client : clients) {
                            users.add(client.getName());
                        }
                        objOutput.writeObject(new Message(MessageType.SERVER, "GET_USERS"));
                        objOutput.flush();
                        objOutput.writeObject(users);
                        objOutput.flush();

                        clients.add(newclient);

                        for (Client client : clients) {
                            if(!client.equals(newclient)) {
                                ObjectOutputStream outputStream = client.getObjOutput();
                                try {
                                    outputStream.writeObject(new Message(MessageType.SERVER, "GET_USERS"));
                                    outputStream.flush();
                                    List<String> newuserlist = new ArrayList<>();
                                    newuserlist.add(newclient.getName());
                                    outputStream.writeObject(newuserlist);
                                    outputStream.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                } catch (Exception e){
                    System.out.println(e);
                }
            }
            catch (EOFException | SocketException e){
                System.out.println("Server socket successfully closed");
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    public static void closeConnection(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

