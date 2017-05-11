package com.company;

import javax.swing.JButton;

public class NaoVazio implements Estados {

	public boolean estadoDesfazer(JButton undoButton){
		undoButton.setEnabled(true);
		return true;
	}

}
