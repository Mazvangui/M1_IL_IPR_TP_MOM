package fr.istic.date.route;


import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EnvoyerDate {

	private static final String EXCHANGE_NAME = "date_route";
	
	private static String getDate() {
	    return (new Date()).toString();
	}
	
	private static String getDateGMT() {
	    return (new Date()).toGMTString();
	}

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setUri("amqp://admin:admin@localhost:8088/ipr");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            
            while(true) {
            	String date = getDate();
            	String dateGMT = getDateGMT();

            	channel.basicPublish(EXCHANGE_NAME, "locale", null, date.getBytes());
            	channel.basicPublish(EXCHANGE_NAME, "gmt", null, dateGMT.getBytes());
            	
                System.out.println(" [x] Sent date :'" + date + "'");
                System.out.println(" [x] Sent dateGMT :'" + dateGMT + "'");
                
                Thread.sleep(1000);
            }
        }
    }
}
