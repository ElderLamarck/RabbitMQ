package com.example.prodmessenger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");  
    LocalDateTime now = LocalDateTime.now(); 

    private final RabbitTemplate rabbitTemplate;
    private final ConfigurableApplicationContext context;

    public Runner(RabbitTemplate rabbitTemplate,
                  ConfigurableApplicationContext context) {
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
    	Scanner ler = new Scanner(System.in);
        String name, msg, routingKey;

        System.out.println("Write your name:");
        name = ler.nextLine();

        //routing
        while(true){
            System.out.println("\nSelect a topic of interest:");
            System.out.println("1 - Entertainment");
            System.out.println("2 - News");
            routingKey = ler.nextLine();

            if(routingKey.equals("1")){
                while(true){
                    System.out.println("\nSelect a subtopic:");
                    System.out.println("1 - Sports");
                    System.out.println("2 - Games");
                    routingKey = ler.nextLine();

                    if(routingKey.equals("1")){
                        routingKey = "entertainment.sports";
                        break;
                    }
                    else if(routingKey.equals("2")){
                        routingKey = "entertainment.games";
                        break;
                    }
                    System.out.println("Invalid option.");
                }
                break;
            } 
            else if(routingKey.equals("2")) {
                while(true){
                    System.out.println("\nEscolha um Subtopico:");
                    System.out.println("1 - Local News");
                    System.out.println("2 - Global News");
                    routingKey = ler.nextLine();

                    if(routingKey.equals("1")){
                        routingKey = "news.local";
                        break;
                    }
                    else if(routingKey.equals("2")){
                        routingKey = "news.global";
                        break;
                    }
                    System.out.println("Invalid option.");
                }
                break;
            }

            else{
                System.out.println("Invalid option.");
            }
        }
        msg = "'" + name + "' has entered the "+ routingKey +" chat\n";
        rabbitTemplate.convertAndSend(ProdApplication.topicExchangeName, routingKey, msg);
    	while(true) {
    		System.out.println("\nWrite your message:");
   		    msg = ler.nextLine(); 
   		    
   		    if(msg.contains("sair"))
   		    	break;
                
   		    msg ="Received From: " + routingKey + ";\n" + name +": "+ msg + " [" + dtf.format(now) + "];\n";
    		rabbitTemplate.convertAndSend(ProdApplication.topicExchangeName, routingKey, msg);

    	}
        context.close();
        ler.close();
    }
    
}