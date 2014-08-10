package net.devcat.jmschannel;

import javax.jms.*;

import net.devcat.jmschannel.exceptions.JMSChannelException;

public class JMSInChannel extends JMSChannel implements Runnable {
    private MessageConsumer consumer;
    private MsgQueue msgQueue;
    private int timeout = 1000;
    private boolean running;
    private int messagesReceived;

    public JMSInChannel(ChannelType type, String name, Connection connection,
            MsgQueue msgQueue) throws JMSChannelException {
        super(type, name, connection);
        try {
            if (ChannelType.TOPIC.equals(type)) {
	        consumer = session.createConsumer((Topic)destination);
            } else if (ChannelType.QUEUE.equals(type)) {
	        consumer = session.createConsumer((Queue)destination);
            }
        } catch (JMSException e) {
            throw new JMSChannelException(e.getMessage());
        }
        this.msgQueue = msgQueue;
    }

    public MsgQueue getMsgQueue() {
        return msgQueue;
    }

    public int getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(int messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public synchronized void start() {
        running = true;
    }
	
    public synchronized void stop() {
        running = false;
    }

    public synchronized boolean isRunning() {
        return running;
    }

    public void run() throws JMSChannelException {
		
        // Enable the InputChannel
        start();

        // Start processing messages
        while (isRunning()) {
            Message message;
            try {
                message = consumer.receive(timeout);
                if (message != null) {
                    onMessage(message);
                    setMessagesReceived(getMessagesReceived() + 1);
                }
            } catch (JMSException e) {
                throw new JMSChannelException(e.getMessage());
            }
        }
    }
	
    // Handle a JMS message (put it on the queue)
    public void onMessage(Message message) {
        msgQueue.putMsg(message);
    }
}
