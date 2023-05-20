package com.example;


import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import message.Message;
import org.paukov.combinatorics3.Generator;

public class ServerViewController implements Initializable {
    @FXML
    private TextFlow messageViewArea;
    @FXML
    private ListView<String> conversationsList;
    @FXML
    private Button refreshButton;
    private static List<List<String>> conversations;
    static int selectedIndex = -1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        conversations = new ArrayList<>();
        ThreadedServer.messages.addListener(new ListChangeListener<Message>() {
            @Override
            public void onChanged(Change<? extends Message> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        List<Message> msgList = (List<Message>) change.getAddedSubList();
                        for (Message message : msgList) {
                            updateMessage(message);
                        }
                    }
                }
            }
        });
        ThreadedServer.clients.addListener(new ListChangeListener<Client>() {
            @Override
            public void onChanged(Change<? extends Client> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        List<Client> clientList = (List<Client>) change.getAddedSubList();

                        for (Client client : clientList){
                            updateConversations(client);
                        }
                    }
                }
            }
        });

    }

    public void updateConversations(Client newclient) {


        for (Client client : ThreadedServer.clients) {
            if (!client.getName().equals(newclient.getName())) {
                if (!conversations.contains(List.of(client.getName(), newclient.getName()))
                    && !conversations.contains(List.of(newclient.getName(), client.getName()))) {
                    Platform.runLater(()->{
                    conversations.add(List.of(client.getName(), newclient.getName()));
                    conversationsList.getItems().add(client.getName() + " <---> " + newclient.getName());
                    });
                }
            }
        }

    }

    public void refreshMessages(MouseEvent mouseEvent) throws IOException {
        if (conversationsList.getSelectionModel().getSelectedIndex() != selectedIndex) {
            selectedIndex = conversationsList.getSelectionModel().getSelectedIndex();
            String selectedItem = conversationsList.getSelectionModel().getSelectedItem();
            if(messageViewArea != null) {
                messageViewArea.getChildren().clear();
            }
            for (Message message : ThreadedServer.messages) {

                if (message.getSender().equals(conversations.get(selectedIndex).get(0)) && message.getReceiver().equals(conversations.get(selectedIndex).get(1))
                        || message.getSender().equals(conversations.get(selectedIndex).get(1)) && message.getReceiver().equals(conversations.get(selectedIndex).get(0))) {
                    Text sender = new Text(message.getSender() + ": ");
                    Text text = new Text(message.getMessageText() + "\n");
                    sender.setStyle("-fx-font-weight: bold");
                    Platform.runLater(() -> {
                        messageViewArea.getChildren().add(sender);
                        messageViewArea.getChildren().add(text);
                    });
                }
            }

        }

    }


    public void updateMessage(Message message) {
        if(selectedIndex>=0) {
            if (message.getSender().equals(conversations.get(selectedIndex).get(0)) && message.getReceiver().equals(conversations.get(selectedIndex).get(1))
                    || message.getSender().equals(conversations.get(selectedIndex).get(1)) && message.getReceiver().equals(conversations.get(selectedIndex).get(0))) {
                Text sender = new Text(message.getSender() + ": ");
                Text text = new Text(message.getMessageText() + "\n");
                sender.setStyle("-fx-font-weight: bold");
                Platform.runLater(() -> {
                    messageViewArea.getChildren().add(sender);
                    messageViewArea.getChildren().add(text);
                });
            }
        }
    }
}