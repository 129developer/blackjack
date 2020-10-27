package com.Dracos.blackjack;

 // This class is created for every card and stores the information for each of them
public class Card {
	// Variables to store rank, suit and value of card
	public String rank = "", suit = "";
	public int value = 0;
	
	// Constructor - initialise values
	Card(String r, String s, int v) {
		this.rank = r;
		this.suit = s;
		this.value = v;
	}
	
	// print a card value
	public void print() { // Debug - print out info on card
		System.out.printf("%s%s", this.rank, this.suit);
	}
	public String toString() {
		return this.rank + this.suit;
		
	}

}
