package com.pha.health.jms.producer;

import org.json.JSONObject;

import javax.jms.JMSException;
import java.util.Date;

public class JMSQueueProducerRunner {

    public static void main(String[] args) throws JMSException {

        System.out.println("I am the JMS Producer !");

        //Create JSM Consumer and connect to the JMS server
        JMSQueueProducer jmsQueueProducer = new JMSQueueProducer("localhost", 61616, "PHA_FORM_A");

        System.out.println("Creating sample data to write to Queue");

        // Generate fake data to put onto the ququ
        JSONObject jsonObjectStub = new JSONObject();
        jsonObjectStub.put("welcome-message", "hello there!");
        jsonObjectStub.put("timestamp", new Date());
        jsonObjectStub.put("id", 42);

        System.out.println("sample data: ");
        System.out.println(jsonObjectStub);

        // Send the content to the JMS queue
        jmsQueueProducer.sendMessage(jsonObjectStub.toString(3));

    }
}
