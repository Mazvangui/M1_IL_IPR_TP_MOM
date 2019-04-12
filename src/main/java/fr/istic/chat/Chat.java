package fr.istic.chat;


import java.util.Date;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Chat {

	private static final String EXCHANGE_NAME = "chat";
	
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
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            
        	Scanner scanner = new Scanner(System.in);
        	System.out.println("veuillez entrer votre nom");
        	String name = scanner.nextLine();
        	System.out.println("veuillez entrer le nom de la salle pour chatter");
        	String room = scanner.nextLine();

        	System.out.println("Bienvenue à la salle "+room);
        	
            //affichage
            ThreadView view = new ThreadView(room);
            view.start();
            
            while(true) {
            	//envoie message
            	String msg = room + "#" + name + ">" + scanner.nextLine();
            	
            	channel.basicPublish(EXCHANGE_NAME, "chat."+room, null, msg.getBytes());
            }
        }
    }
}
