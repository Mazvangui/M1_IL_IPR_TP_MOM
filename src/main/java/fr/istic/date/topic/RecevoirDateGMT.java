package fr.istic.date.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RecevoirDateGMT {

		private static final String EXCHANGE_NAME = "date_topic";

	    public static void main(String[] argv) throws Exception {
	        ConnectionFactory factory = new ConnectionFactory();

	        factory.setUri("amqp://admin:admin@localhost:8088/ipr");
	        Connection connection = factory.newConnection();
	        Channel channel = connection.createChannel();

	        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	        
	        String queueName = "file_date";
	        channel.queueDeclare(queueName, false, false, false, null);
	        
	        channel.queueBind(queueName, EXCHANGE_NAME, "date.gmt");

	        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	            String message = new String(delivery.getBody(), "UTF-8");
	            System.out.println(" [x] Received '" + message + "'");
	        };
	        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
	    }
}
