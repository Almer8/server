package message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Message {

    private String sender;
    private String receiver;
    private String messageText;
    private MessageType messageType;

    public String getSender() {
        return sender;
    }
    @XmlElement
    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }
    @XmlElement
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }
    @XmlElement
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public MessageType getMessageType() {
        return messageType;
    }
    @XmlElement
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
