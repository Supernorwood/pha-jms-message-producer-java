package org.example.producer;

import java.util.Date;
import javax.jms.JMSException;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) throws JMSException {
        System.out.println("I am the JMS Producer !");

        System.out.println("trying t0o write to the queue");

        JMSQueueProducer jmsQueueProducer = new JMSQueueProducer("localhost", 61616, "PHA_FORM_A");
        jmsQueueProducer.sendMessage("wwhat up tho!!");

        JSONObject asdf = new JSONObject();
        asdf.put("hello", "what up tho");
        asdf.put("timestamp", new Date());
        asdf.put("balance", 1_000_000);

        jmsQueueProducer.sendMessage(asdf.toString(3));
        /*
        
PHA Form A Challange

Part 1:
	- Create an endpoint (POST) to receive sample PHA Form A
	- Unmarshall and parse the JSON doc
	- Extract demographic data (Name, Address, Phone, Email, DOB, DODID, Gender, Service, Rank)
	- Design a new JSON doc called PhaPersonInfo.  Have seperate subsections for personal data, address data,
	  contact data, service data.
	- Return new JSON doc as response (HTTP 200).
	- Add some basic validation. Come up w/ meaningful validation messages for a few required fields:
		- input message itself, required First and Last Names, required DOB, DODID, Email
	- Use standard libs (Spring Boot, Jackson)
	
Part 2:
	- Modify Part 1. Instead of returning new JSON doc in a response, just return HTTP 200 if message validates and is
	  successfully parsed and transformed.
	- [done] Download and install Active MQ.
	- Add a message producer to send new JSON doc to a queue in AMQ.
        -  [done] Call the queue PHA_FORM_A.
        
        
	- Write a message consumer (seperate project and WAR) to retrieve the message from the queue, and
	  write it out to a local file.
         */
    }
}
