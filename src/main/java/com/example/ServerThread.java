
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
        try {
            this.clientSocket = client.getSocket();
            this.objOutput = client.getObjOutput();
            objOutput.flush();
            this.objInput = client.getObjInput();
            String name = null;
            while (name == null){
                try {
                    name = (String) objInput.readObject();
                    System.out.println(name);
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            client.setName(name);

        } catch (Exception e){
            System.out.println(e);
        }


    }

    @Override
    public void run(){
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
                                System.out.println(users);
                                objOutput.flush();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }
                    if(request.getMessageType().equals(MessageType.MESSAGE)){
                        ThreadedServer.messages.add(request);
                        for (Client user:
                                ThreadedServer.clients) {
                            if (user.getName().equals(request.getReceiver())){
                                user.getObjOutput().writeObject(request);
                                user.getObjOutput().flush();
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