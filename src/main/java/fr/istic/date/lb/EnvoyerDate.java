package fr.istic.date.lb;


import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EnvoyerDate {

	private static final String EXCHANGE_NAME = "date";
	
	private static String getDate() {
	    return (new Date()).toString();
	}

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        //factory.setHost("localhost");
        factory.setUri("amqp://admin:admin@localhost:8088/ipr");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            
            while(true) {
            	String date = getDate();

                channel.basicPublish(EXCHANGE_NAME, "", null, date.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + date + "'");
                
                Thread.sleep(1000);
            }
        }
    }
}
