package com.example;

import message.Message;
import message.MessageType;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Source;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ServerThread extends Thread {
    Socket clientSocket;
    BufferedReader inputStream;
    PrintWriter outputStream;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;

    public ServerThread(Socket socket) throws IOException {
        this.clientSocket = socket;

        try {
            this.objOutput = new ObjectOutputStream(socket.getOutputStream());
            objOutput.flush();
            this.objInput = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }


    }

    @Override
    public void run(){



                while (!clientSocket.isClosed()) {

                    try {
                        Message request = (Message) objInput.readObject();

                        if (request != null) {

                            if (request.getMessageType().equals(MessageType.SERVER)) {
                                if (request.getMessageText().equals("GET_USERS")) {
                                    try {
                                        List<String> users = new ArrayList<>();
                                        for (Client client : ThreadedServer.clients) {
                                            users.add(client.getName());
                                        }
                                        objOutput.writeObject(new Message(MessageType.SERVER,"GET_USERS"));
                                        objOutput.flush();
                                        objOutput.writeObject(users);
                                        objOutput.flush();
                                        System.out.println("Userlist sended to client");

                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        }
                    } catch (EOFException  | SocketException e) {
                        ThreadedServer.clients.removeIf(client -> client.getSocket().equals(clientSocket));
                        try {
                            clientSocket.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("Socket successfully closed");

                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }


    }
}




