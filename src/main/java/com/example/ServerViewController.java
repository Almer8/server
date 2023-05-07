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

    }

    public void updateConversations() {


            if (!conversations.isEmpty()) {
                conversations.clear();
            }
        ArrayList<String> clients = new ArrayList<>();
        for (Client client : ThreadedServer.clients) {
            clients.add(client.getName());
        }
        Generator.combination(clients)
                .simple(2)
                .stream()
                .forEach(x -> conversations.add(x));
            if (!conversationsList.getItems().isEmpty()) {
                conversationsList.getItems().clear();
            }
        if (conversations != null) {
            for (List<String> conversation : conversations) {
                String conversationString = conversation.get(0) + " <---> " + conversation.get(1);
                conversationsList.getItems().add(conversationString);
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