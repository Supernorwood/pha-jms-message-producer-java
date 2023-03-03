package org.example.producer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.slf4j.LoggerFactory;

public class JMSQueueProducer extends JMSQueueProducerConsumer {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JMSQueueProducer.class);

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
     * @param outboxQueueName Queue for Replies to the message(s)
     * @param message messages to send to the target Queue
     * @throws javax.jms.JMSException if errors sending message to queue
     */
    private void sendMessage(final String outboxQueueName, final Object message) throws JMSException {

        List<Object> messageList = new ArrayList();

        messageList.add(message);

        this.sendMessage(outboxQueueName, messageList);

        messageList.clear();
        messageList = null;
    }

    /**
     * Send a message to a target Queue
     *
     * @param message messages to send to the target Queue
     * @throws javax.jms.JMSException if errors sending message to queue
     */
    public void sendMessage(final Object message) throws JMSException {
        sendMessage(null, message);
    }

    /**
     * Send a collection of messages to a target Queue
     *
     * @param messageList List of messages to send to the target Queue
     * @throws javax.jms.JMSException if errors sending message(s) to queue
     */
    public void sendMessage(final Collection<? extends Object> messageList) throws JMSException {
        sendMessage(null, messageList);
    }

    /**
     * Send a collection of messages to a target Queue
     *
     * @param outboxQueueName Queue for Replies to the message(s)
     * @param messageList List of messages to send to the target Queue
     * @throws javax.jms.JMSException if errors sending message(s) to queue
     */
    private void sendMessage(final String outboxQueueName, final Collection<? extends Object> messageList) throws JMSException {
        if (logger.isDebugEnabled()) {
            logger.debug("enter :: sendMessage()");
            logger.debug("param - inbox queue: {}", this.targetJMSQueue);
            logger.debug("param - outbox queue: {}", outboxQueueName);
            logger.debug("param - messages:");
            for (final Object message : messageList) {
                logger.debug(" * {}", message);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("logging into JMS server with connection string: {}", this.connectionString);
        }

        Connection connection = null;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connection = connectionFactory.createConnection();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue inboxQueue = session.createQueue(this.targetJMSQueue);
        Queue outboxQueue = null;

        if (logger.isDebugEnabled()) {
            logger.debug("connected");
        }

        if (outboxQueueName != null) {
            outboxQueue = (Queue) session.createQueue(outboxQueueName);
        }

        MessageProducer producer = session.createProducer(inboxQueue);

        if (logger.isDebugEnabled()) {
            logger.debug("sending all messages to target queue");
        }

        Integer countOfMessagesSent = 1;

        for (final Object message : messageList) {

            if (logger.isDebugEnabled()) {
                logger.debug("sending message [ {} / {} ]", countOfMessagesSent, messageList.size());
            }

            if (logger.isDebugEnabled()) {
                logger.debug("creating Message from: {}", message);
            }

            Message messageToSendToQueue = session.createObjectMessage((Serializable) message);

            if (outboxQueue != null) {
                messageToSendToQueue.setJMSReplyTo((Destination) outboxQueue);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Sending message to Queue [{}]: {}", outboxQueue, messageToSendToQueue);
            }

            producer.send(messageToSendToQueue);
            countOfMessagesSent++;

            if (logger.isDebugEnabled()) {
                logger.debug("Message sent");
            }

            messageToSendToQueue = null;

        }

        producer.close();
        producer = null;

        outboxQueue = null;
        inboxQueue = null;

        session.close();
        session = null;

        connection.close();
        connection = null;
        connectionFactory = null;

        if (logger.isDebugEnabled()) {
            logger.debug("exit :: sendMessage()");
        }
    }

}
