package com.company;

import javax.swing.JTextArea;

public class Memento {
	private JTextArea article;

	public Memento(JTextArea received) {
		JTextArea aux = new JTextArea();
		aux.setText(received.getText());
		article = aux;
	}

	public JTextArea getSavedText() {
		return article;
	}

}