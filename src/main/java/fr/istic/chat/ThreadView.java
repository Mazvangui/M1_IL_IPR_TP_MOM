package fr.istic.chat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ThreadView extends Thread{
	
	private static final String EXCHANGE_NAME = "chat";
	private String room;
	
	private ThreadView() {
		
	}
	
	public ThreadView(String room) {
		this.room = room;
	}
	public void run(){
		ConnectionFactory factory = new ConnectionFactory();
        try {
			factory.setUri("amqp://admin:admin@localhost:8088/ipr");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	        String queueName = channel.queueDeclare().getQueue();
	        
	        channel.queueBind(queueName, EXCHANGE_NAME, "chat."+room);

	        while(true) {
		        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		            String message = new String(delivery.getBody(), "UTF-8");
		            System.out.println(message);
		        };
		        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	        }
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException | URISyntaxException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
