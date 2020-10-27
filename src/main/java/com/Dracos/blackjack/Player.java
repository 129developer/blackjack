package com.Dracos.blackjack;

import java.util.*; 

public class Player {
	public ArrayList<Card> cards = new ArrayList<Card>();
	// add card
	public void addCard(Card cardToAdd)
	{
		this.cards.add(new Card(cardToAdd.rank, cardToAdd.suit, cardToAdd.value));
	}
	// check aces
	public int getNumAces() {
		int numAces = 0;
		for (int i = 0; i < this.cards.size(); i++)
			if (this.cards.get(i).rank == "A")
				numAces++;
		return numAces;
	}
	// get total
	public int getTotalValue() {
		int totalValue = 0;
		for (int i = 0; i < this.cards.size(); i++)
			totalValue += this.cards.get(i).value;
		return totalValue;
	}
	// print all cards of player
	public void print() {
		for (int i = 0; i < this.cards.size(); i++) {
			this.cards.get(i).print();
			System.out.printf(",");
		}
	}
	// return string of all cards
	public String stringCards() {
		String retVal = "";
		for (int i = 0; i < this.cards.size(); i++) {
			retVal += this.cards.get(i).toString();
			retVal += ",";
		}
		return retVal;
	}
	//print a single card
	public void printCard(int cardIndex)
	{
		this.cards.get(cardIndex).print();
	}
	//return string type of a card

	public String stringCard(int cardIndex)
	{
		return this.cards.get(cardIndex).toString();
	}
}
