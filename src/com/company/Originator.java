package com.company;

import javax.swing.JTextArea;

public class Originator {
	
	private JTextArea article;
	
	public void set( JTextArea newArticle){
		article = newArticle;
	}
	
	public Memento storeInMemento(){
		return new Memento(article);
	}
	public JTextArea restoreFromMemento(Memento memento){
		article = memento.getSavedText();
		return article;
	}

}
