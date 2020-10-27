package com.Dracos.blackjack;

import java.util.*;

public class Deck {

    public ArrayList<Card> cards = new ArrayList<Card>();

    // initialize a full deck 
    // this is part of shared memory
    public void initFullDeck() {
        this.cards.clear();
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        int[] rankValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
        String[] suits = {"C", "D", "H", "S"};

        for (int i = 0; i < ranks.length; i++) {
            for (String suit : suits) {
                this.cards.add(new Card(ranks[i], suit, rankValues[i]));
            }
        }
    }
    // Removes card from top of card group's ArrayList and returns it

    public Card takeCard() {
        if (this.cards.size() < 1) {
            System.out.println("Error: no more cards!");
            System.exit(0);
        }
        Card tempCard = this.cards.get(this.cards.size() - 1);
        this.cards.remove(this.cards.size() - 1);
        return tempCard;
    }

    // Shuffle Deck
    public void shuffle() {
        // Seed for random class
        long seed = System.nanoTime();
        // This implementation traverses the list backwards, from the last element up to the second, repeatedly swapping a randomly selected element into the "current position"
        Collections.shuffle(this.cards, new Random(seed));
    }

}
