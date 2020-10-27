package com.Dracos.blackjack;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class skeletonClient {

    public static void main(String[] args) {
        System.out.println("Main started\r\n");
        String serverName = "localhost";
        String key = "y";
        int port = Integer.parseInt("4031");
        Scanner keyboard = new Scanner(System.in);
        String typing = "firstmessage";
        int score = 0;
        int PlayerId = 0;
        int TotalPlayers = 0;
        try {
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            java.io.OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            //out.writeUTF("Hello from " + client.getLocalSocketAddress());
            java.io.InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            //System.out.println("Server says " + in.readUTF());
            System.out.println("First card " + in.readUTF());
            System.out.println("Second card " + in.readUTF());
            System.out.print("You are holding " + in.readUTF() + " Aces.\r\n");
            PlayerId = Integer.parseInt(in.readUTF());
            TotalPlayers = Integer.parseInt(in.readUTF());
            while (in.available() <= 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.err.print(ie);
                }
            }
            System.out.print("The Table\r\nDealer: " + in.readUTF() + "\r\n");
            for (int i = 0; i < TotalPlayers; i++) {
                System.out.print("Player " + (i + 1) + ": " + in.readUTF());
                if (i == PlayerId) {
                    System.out.print(" <--- You");
                }
                System.out.print("\r\n");
            }
            System.out.print(".... It is not your turn yet....\r\n");
            System.out.print("Now it is your turn\r\n");
            System.out.print("You are holding " + in.readUTF() + "\r\n");

            while ((key.equals("y")) & (score < 21)) {
                System.out.print("Take another card y/n? ");
                key = keyboard.nextLine();
                out.writeUTF(key);
                if ((key.equals("y")) & (score < 21)) {
                    System.out.print("\r\nNext card " + in.readUTF() + "\r\n");
                    System.out.print("You are holding " + in.readUTF() + " aces\r\n");
                    score = Integer.parseInt(in.readUTF());
                    System.out.print("You are holding " + score + "\r\n");
                } else {
                    break;
                }
            }
            for (int i = 0; i <= TotalPlayers; i++) {
                System.out.print("ID " + i + " has " + in.readUTF() + " = " + in.readUTF());
                if (i == (PlayerId + 1)) {
                    System.out.print(" <--- You");
                } else if (i == 0) {
                    System.out.print(" <--- Dealer");
                }
                System.out.print("\r\n");
            }
            /*while(!typing.equals("exit")){
             typing = keyboard.nextLine();
             out.writeUTF(typing);
             try{
                Thread.sleep(1000);
             }catch(InterruptedException ie){
                 System.err.print(ie);
             }
             if(in.available() > 0){
                 System.out.println("Server replies " + in.readUTF());
             }
         }*/
            out.writeUTF("exit");
            out.close();
            in.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Exception catch");
            e.printStackTrace();

        }
    }
}
