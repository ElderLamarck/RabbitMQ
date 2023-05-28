package com.example;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;

import com.example.consmessenger.ConsApplication;
import com.example.prodmessenger.ProdApplication;

public class ChatMenu {
    public static void main(String[] args){
        Scanner ler = new Scanner(System.in);
        while(true){
            System.out.println("Select the type of user: ");
            System.out.println("1 - Producer");
            System.out.println("2 - Consumer");

            String choice = ler.nextLine();
    
            if(choice.equals("1")){
                SpringApplication.run(ProdApplication.class, args);
                ler.close();
                break;
            }
            else if(choice.equals("2")){
                SpringApplication.run(ConsApplication.class, args);
                ler.close();
                break;
            }
            else {
                System.out.println("Invalid option\n");
            }    
        } 
    }
}
