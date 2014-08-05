package net.devcat.jmschannel;

import javax.jms.*;

import net.devcat.jmschannel.exceptions.JMSChannelException;

public interface JMSChannelMessage {

    TextMessage getTextMessage(String text) throws JMSChannelException;

    ObjectMessage getObjectMessage(Object obj) throws JMSChannelException;
}
