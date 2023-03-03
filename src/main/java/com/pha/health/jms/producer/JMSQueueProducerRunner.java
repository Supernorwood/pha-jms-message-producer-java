package com.pha.health.jms.producer;

import java.util.Date;
import javax.jms.JMSException;
import org.json.JSONObject;

public class JMSQueueProducerRunner {

    public static void main(String[] args) throws JMSException {

        System.out.println("I am the JMS Producer !");

        JMSQueueProducer jmsQueueProducer = new JMSQueueProducer("localhost", 61616, "PHA_FORM_A");

        System.out.println("Creating sample data to write to Queue");

        JSONObject jsonObjectStub = new JSONObject();

        jsonObjectStub.put("welcome-messagee", "hello there!");

        jsonObjectStub.put("timestamp", new Date());

        jsonObjectStub.put("id", 42);

        System.out.println("sample data: ");
        System.out.println(jsonObjectStub);

        jmsQueueProducer.sendMessage(jsonObjectStub.toString(3));

    }
}
