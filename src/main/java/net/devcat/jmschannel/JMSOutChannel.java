package net.devcat.jmschannel;

import javax.jms.*;

import net.devcat.jmschannel.exceptions.JMSChannelException;

public class JMSOutChannel extends JMSChannel {
    private MessageProducer producer;
    private int messagesSent;

    public JMSOutChannel(ChannelType type, String name, Connection connection)
                throws JMSChannelException {
        super(type, name, connection);
        try {
            if (ChannelType.TOPIC.equals(type)) {
                producer = session.createProducer((Topic)destination);
            } else if (ChannelType.QUEUE.equals(type)) {
                producer = session.createProducer((Queue)destination);
            }
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
    }
	
    public int getMessagesSent() {
		return messagesSent;
	}

	public void setMessagesSent(int messagesSent) {
		this.messagesSent = messagesSent;
	}

	public void sendMsg(Message message) throws JMSChannelException {
        try {
            producer.send(message);
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
        setMessagesSent(getMessagesSent() + 1);
    }
}
