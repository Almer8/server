package message;


import java.io.Serializable;

public class Message implements Serializable {

    private String sender;
    private String receiver;
    private String messageText;
    private final MessageType messageType;


    public Message(String sender, String receiver, String messageText, MessageType messageType) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.messageType = messageType;
    }

    public Message(String sender, MessageType messageType) {
        this.sender = sender;
        this.messageType = messageType;
    }
    public Message(MessageType messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
