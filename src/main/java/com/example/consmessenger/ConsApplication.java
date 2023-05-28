package com.example.consmessenger;

import java.util.Scanner;

import java.lang.Math;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsApplication{
    static final Double radname = Math.random();
    static final String topicExchangeName = "topic-exchange";
    static final String queueName = String.valueOf(radname);
   
    @Bean
    Queue queue() {
    	return new Queue(queueName, false, false, true);
    }
    @Bean
    TopicExchange exchange() {
    	return new TopicExchange(topicExchangeName,false, true);
    }

    @Bean
    Binding binding(String[] args) {
		String routingKey = routing();
    	return BindingBuilder.bind(queue()).to(exchange()).with(routingKey);
    }
    

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
											 MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	private String routing(){
		Scanner ler = new Scanner(System.in);
		String routingKey;
		while(true){
            System.out.println("Select a topic of interest:");
            System.out.println("1 - Entertainment");
            System.out.println("2 - News");
            System.out.println("0 - Console log");
            routingKey = ler.nextLine();

            if(routingKey.equals("1")){
                while(true){
                    System.out.println("Select a subtopic:");
                    System.out.println("1 - Sports");
                    System.out.println("2 - Games");
					System.out.println("3 - all of the above");
                    routingKey = ler.nextLine();

                    if(routingKey.equals("1")){
                        routingKey = "entertainment.sports";
                        break;
                    }
                    else if(routingKey.equals("2")){
                        routingKey = "entertainment.games";
                        break;
                    }
					else if(routingKey.equals("3")){
						routingKey = "entertainment.*";
                        break;
					}
                    System.out.println("Invalid option.");
                }
                break;
            } 
            else if(routingKey.equals("2")) {
                while(true){
                    System.out.println("Escolha um Subtopico:");
                    System.out.println("1 - Local News");
                    System.out.println("2 - Global News");
					System.out.println("3 - all of the above");

                    routingKey = ler.nextLine();

                    if(routingKey.equals("1")){
                        routingKey = "news.local";
                        break;
                    }
                    else if(routingKey.equals("2")){
                        routingKey = "news.global";
                        break;
                    }
					else if(routingKey.equals("3")){
						routingKey = "news.*";
                        break;
					}

                    System.out.println("Invalid option.");
                }
                break;
            }
            else if(routingKey.equals("0")){
                routingKey = "#";
                break;
            }
            else{
                System.out.println("Invalid option.");
            }
        }
		ler.close();
		return routingKey;
	}

	public static void main(String[] args) {
		SpringApplication.run(ConsApplication.class, args);
	}
}