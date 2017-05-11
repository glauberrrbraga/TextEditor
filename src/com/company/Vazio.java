package com.company;
import javax.swing.JButton;

public class Vazio implements Estados {

	public boolean estadoDesfazer(JButton undoButton){
		undoButton.setEnabled(false);
		return true;
	}

}
