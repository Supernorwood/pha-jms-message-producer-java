package com.pha.health.jms.producer;

import com.pha.health.jms.worker.JMSQueueWorker;
import java.io.Serializable;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.spring.ActiveMQConnectionFactory;

public class JMSQueueProducer extends JMSQueueWorker {

    /**
     * Initialize a JMS Queue Producer given a target JMS Queue name
     *
     * @param targetServer Server hosting the JMS queue application
     * @param port port the JMS Queue application is listening on
     * @param targetJMSQueue target JMS Queue name
     */
    public JMSQueueProducer(final String targetServer, final int port, final String targetJMSQueue) {
        super(targetServer, port, targetJMSQueue);
    }

    /**
     * Send a message to a target Queue
     *
     * @param message messages to send to the target Queue
     * @throws javax.jms.JMSException if errors sending message to queue
     */
    public void sendMessage(final Object message) throws JMSException {

        Connection connection = null;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue inboxQueue = session.createQueue(this.targetJMSQueue);
        Queue outboxQueue = null;

        if (targetJMSQueue != null) {
            outboxQueue = (Queue) session.createQueue(targetJMSQueue);
        }

        MessageProducer producer = session.createProducer(inboxQueue);

        Integer countOfMessagesSent = 1;

        Message messageToSendToQueue = session.createObjectMessage((Serializable) message);

        if (outboxQueue != null) {
            messageToSendToQueue.setJMSReplyTo((Destination) outboxQueue);
        }

        producer.send(messageToSendToQueue);
        countOfMessagesSent++;

        producer.close();
        session.close();
        connection.close();

    }

}
