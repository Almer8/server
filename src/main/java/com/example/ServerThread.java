
package com.example;

import message.Message;
import message.MessageType;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class ServerThread extends Thread {
    Socket clientSocket;
    private ObjectInputStream objInput;
    private ObjectOutputStream objOutput;


    public ServerThread(Client client) throws IOException {
        System.out.println("I got to constructor");
        try {
            this.clientSocket = client.getSocket();
            this.objOutput = client.getObjOutput();
            objOutput.flush();
            this.objInput = client.getObjInput();
        } catch (Exception e){
            System.out.println(e);
        }


    }

    @Override
    public void run(){
        String name = null;
        int count = 0;
        while (name == null){
            try {
                name = (String) objInput.readObject();
                System.out.println(name);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        for (Client user:
                ThreadedServer.clients) {
            if(user.getSocket().equals(clientSocket)){
                user.setName(name);
            }
        }
        for (Client client: ThreadedServer.clients) {
            if(client.getName().equals(name)){
                count++;
            }
        }
        if (count > 1){
            Message message = new Message(MessageType.SERVER,"DUPLICATE_USER");
            try {
                objOutput.writeObject(message);
                objOutput.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        while (!clientSocket.isClosed()) {

            try {
                Message request = (Message) objInput.readObject();
                System.out.println("I got message from " + request.getSender() + " to " + request.getReceiver() + " with text " + request.getMessageText());

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
                                System.out.println("Userlist sent to client");

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    if(request.getMessageType().equals(MessageType.MESSAGE)){
                        System.out.println("Trying send to user");
                        for (Client user:
                                ThreadedServer.clients) {
                            if (user.getName().equals(request.getReceiver())){
                                System.out.println("Find client");
                                user.getObjOutput().writeObject(request);
                                user.getObjOutput().flush();
                                System.out.println("Sent?");
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