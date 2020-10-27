package com.Dracos.blackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class skeletonServer extends Thread {

    private Socket server;
    private DataInputStream in;
    private DataOutputStream out;

    // shared memory
    // make a deck
    static Deck deck = new Deck();
    // server/ dealer player
    static Player dealer = new Player();
    // Total number of Players
    static int TotalNumberOfPlayers = 0;
    static int maxPlayersAllowed = 5;
    static boolean enterPressed = false;
    static int numberOfPlayersOut = 0;

    boolean allPlayerDataSent = false;
    static ArrayList<Player> plist = new ArrayList<Player>();
    static ArrayList<Player> endlist = new ArrayList<Player>();

    public skeletonServer(Socket theSocket) throws IOException {
        server = theSocket;
    }

    public void run() {
        String line = "start";
        int score = 0;
        int index = 0;
        int playerID = 0;
        try {
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
            // Make a new player for each thread
            Player p = new Player();
            p.addCard(deck.takeCard());
            p.addCard(deck.takeCard());
            plist.add(p);
            out.writeUTF(p.stringCard(index++));
            out.writeUTF(p.stringCard(index++));
            out.writeUTF(Integer.toString(p.getNumAces()));
            out.writeUTF(Integer.toString(TotalNumberOfPlayers - 1));
            playerID = TotalNumberOfPlayers - 1;
            out.writeUTF(Integer.toString(maxPlayersAllowed));
            // send data of all players 
            while (enterPressed == false) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.err.print(ie);
                }
            }
            //print all lists
            out.writeUTF(dealer.stringCards());
            for (int i = 0; i < plist.size(); i++) {
                out.writeUTF(plist.get(i).stringCards());
            }
            // holding value
            out.writeUTF(Integer.toString(p.getTotalValue()));
            score = p.getTotalValue();
            while (score <= 21) {
                if (in.available() > 0) {
                    if (in.readUTF().equals("y")) {
                        p.addCard(deck.takeCard());
                        out.writeUTF(p.stringCard(index++));
                        out.writeUTF(Integer.toString(p.getNumAces()));
                        out.writeUTF(Integer.toString(p.getTotalValue()));
                        score = p.getTotalValue();
                    } else {
                        break;
                    }
                }

            }
            endlist.add(p);
            numberOfPlayersOut++;
            while (numberOfPlayersOut != TotalNumberOfPlayers) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    System.err.print(ie);
                }
            }

            for (int i = 0; i < endlist.size(); i++) {
                out.writeUTF(endlist.get(i).stringCards());
                out.writeUTF(Integer.toString(endlist.get(i).getTotalValue()));
            }
            System.out.print("ID " + (playerID + 1) + " has " + p.getTotalValue());

            /* Echo back whatever the client writes until the client exits. */
            /*while (!line.equals("exit")) {
                if (in.available() > 0) {
                	// display first two cards
                	// display no of aces
                	// ask client to either take another card or not
                	// display final results
                    line = in.readUTF();
                    out.writeUTF(line);
                }
            }*/
            out.close();
            in.close();
            server.close();

        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");

        }

    }

    public static void main(String[] args) throws IOException {

        int port = 0;
        ServerSocket mySocket = new ServerSocket(port);
        Scanner keyboard = new Scanner(System.in);
        boolean once = false;
        // initialize the deck
        deck.initFullDeck();
        // shuffle deck
        deck.shuffle();
        deck.shuffle();
        maxPlayersAllowed = Integer.parseInt("2");
        // Max player allowed can be upto 5
        if (maxPlayersAllowed > 5) {
            maxPlayersAllowed = 5;
        }
        // Need a way to close the server without just killing it.
        while (true) {
            if (maxPlayersAllowed > TotalNumberOfPlayers) {
                // wait for arg[0] times clients
                System.out.println("Waiting for client on port "
                        + mySocket.getLocalPort() + "...");
                Socket server = mySocket.accept(); //blocking
                try {
                    TotalNumberOfPlayers++;
                    Thread t = new skeletonServer(server);
                    System.out.println("Just connected to " + server.getRemoteSocketAddress());
                    t.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (enterPressed == false) {
                    // Start Table
                    dealer.addCard(deck.takeCard());
                    dealer.addCard(deck.takeCard());
                    System.out.print("\r\nDealer first card ");
                    dealer.printCard(0);
                    System.out.print("\r\nDealer second card ");
                    dealer.printCard(1);
                    System.out.print("\r\nHit enter to play dealer ...\r\n");
                    String enterkey = keyboard.nextLine();
                    enterPressed = true;
                } else {
                    while (dealer.getTotalValue() <= 16) {
                        Card c = deck.takeCard();
                        dealer.addCard(c);
                        System.out.print("Dealer next card " + c.toString() + "\r\n");
                    }
                    if (once == false) {
                        endlist.add(dealer);
                        once = true;
                        System.out.print("Dealer has " + dealer.getTotalValue() + "\r\n");
                    }
                }
            }
        }
    }
}
