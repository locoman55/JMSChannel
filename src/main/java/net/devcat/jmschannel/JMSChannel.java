package net.devcat.jmschannel;

import java.io.Serializable;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.BytesMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.devcat.jmschannel.JMSChannelMessage;
import net.devcat.jmschannel.exceptions.JMSChannelException;


public abstract class JMSChannel implements JMSChannelMessage {
    protected ChannelType type = ChannelType.UNKNOWN;
    protected String name;
    protected Connection connection;
    protected Session session;
    protected Destination destination;
	
    /**
     * Class constructor that creates a new JMSChannel object.
     * 
     * @param type        ChannelType (Topic or Queue)
     * @param name        Topic or Queue name
     * @param connection  Connection object (for JMS communication)
     * @throws JMSChannelException
     */
    public JMSChannel(ChannelType type, String name, Connection connection) 
                throws JMSChannelException {
        if (type == null) {
            throw new JMSChannelException("JMSChannel:Type cannot be NULL\n");
        }
        if (ChannelType.UNKNOWN.equals(type)) {
            throw new JMSChannelException("JMSChannel: Invalid channel type.");
        }

        this.type = type;
        if (name == null) {
            throw new JMSChannelException("JMSChannel:Name cannot be NULL\n");
        }
        this.name = name;
        if (connection == null) {
            throw new JMSChannelException(
                "JMSChannel:Connection cannot be NULL\n");
        }
        this.connection = connection;
        try {
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            if (ChannelType.TOPIC.equals(type)) {
                destination = session.createTopic(name);
            } else if (ChannelType.QUEUE.equals(type)) {
                destination = session.createQueue(name);
            }
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
    }

    public ChannelType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Connection getConnection() {
        return connection;
    }

    public Session getSession() {
        return session;
    }

    @Override
    public TextMessage getTextMessage(String text) throws JMSChannelException {
        TextMessage message = null;
        try {
            message = session.createTextMessage();
            message.setText(text);
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
        return message;
    }

    public ObjectMessage getObjectMessage(Object obj) 
                throws JMSChannelException {
        ObjectMessage message = null;
        try {
            message = session.createObjectMessage();
            message.setObject((Serializable) obj);
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
        return message;
    }

    public BytesMessage getBytesMessage(byte[] bytes)
                throws JMSChannelException {
        BytesMessage message = null;
        try {
            message = session.createBytesMessage();
            message.writeBytes(bytes);
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
        return message;
    }
}
