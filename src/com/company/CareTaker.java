package com.company;

import java.util.ArrayList;

//Keep all mementos saved
//From here, we can save or remove one state
public class CareTaker {

	ArrayList<Memento> savedArticles;

	public CareTaker() {
		// constructor
		savedArticles = new ArrayList<Memento>();
	}

	public void addMemento(Memento m) {
		savedArticles.add(m);
		// Add memento to the ArrayList
	}

	public int getSize() {

		return savedArticles.size();

	}

	public Memento getMemento(int index) {
		return savedArticles.get(index);
	}

}