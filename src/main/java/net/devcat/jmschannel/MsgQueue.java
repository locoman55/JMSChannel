package net.devcat.jmschannel;

import javax.jms.*;

import java.util.LinkedList;                        
import java.util.Queue;                             

public class MsgQueue {
    private Queue<Message> msgQueue = new LinkedList<Message>();
    private boolean running = true;

    public synchronized boolean putMsg(Message m) {
        boolean result = msgQueue.offer(m);
        notify();
        return result;
    }

    public synchronized void start() {
        running = true;
    }

    public synchronized void stop() {
        running = false;
        notify();
    }

    public synchronized Message getMsg(int Timeout) {
        while (msgQueue.isEmpty()) {
            try {
                wait(Timeout);
                if (!running) {
                    return null;
                }
            } catch (InterruptedException ex) {
                return null;
            }
        }
        return msgQueue.poll();
    }

    public synchronized int msgCount() {
        return msgQueue.size();
    }
}
