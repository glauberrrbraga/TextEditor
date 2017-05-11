package com.company;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;

/*
* TODO: Handle multiple files
* TODO: saveFileAs does not put filename in fileList
* */

public class TextEditor extends JFrame implements ActionListener {

	private JTextArea area = new JTextArea(20, 120);
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "New Document";
	JToolBar tool;

	private boolean changed = false;

	ArrayList<String> fileList = new ArrayList<>();
	CareTaker caretaker = new CareTaker();
	Originator originator;
	Estados estado;
	JButton undo;

	public TextEditor() {
		area.setFont(new Font("Monospaced", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scroll, BorderLayout.CENTER);

		JMenuBar JMB = new JMenuBar();
		setJMenuBar(JMB);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMB.add(file);
		JMB.add(edit);

		file.add(New);
		file.add(Open);
		file.add(Save);
		file.add(Quit);
		file.add(SaveAs);
		file.addSeparator();

		for (int i = 0; i < 4; i++)
			file.getItem(i).setIcon(null);

		edit.add(Cut);
		edit.add(Copy);
		edit.add(Paste);
		edit.add(Undo);

		edit.getItem(0).setText("Cut out");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		edit.getItem(3).setText("Undo");

		tool = new JToolBar();
		add(tool, BorderLayout.NORTH);

		JButton newB = tool.add(New), openB = tool.add(Open), saveB = tool.add(Save);
		newB.setToolTipText("New");
		newB.setText(null);
		openB.setToolTipText("Open");
		openB.setText(null);
		saveB.setToolTipText("Save");
		saveB.setText(null);
		tool.addSeparator();

		JButton cut = tool.add(Cut), cop = tool.add(Copy), pas = tool.add(Paste);

		undo = new JButton(Undo);
		tool.add(undo);

		cut.setText(null);
		cut.setIcon(new ImageIcon("images/icons/cut.gif"));
		cut.setToolTipText("Cut");
		cop.setText(null);
		cop.setIcon(new ImageIcon("images/icons/copy.gif"));
		cop.setToolTipText("Copy");
		pas.setText(null);
		pas.setIcon(new ImageIcon("images/icons/paste.gif"));
		pas.setToolTipText("Paste");
		undo.setText("Undo");
		undo.setToolTipText("Undo");
		undo.setIcon(new ImageIcon("images/icons/undo.png"));

		tool.addSeparator();

		Save.setEnabled(false);
		undo.setEnabled(false);
		SaveAs.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(currentFile);
		setVisible(true);

		originator = new Originator();
		originator.set(area);
		caretaker.addMemento(originator.storeInMemento());
		
		estado = new Vazio();


		area.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {

				// colocar aqui os if's dos strategy!!!!!!!!!!!!!!!!!
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
					Undo();
				}
				if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
					saveFileAs();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				if (!e.isControlDown()) {
					originator = new Originator();
					originator.set(area);
					caretaker.addMemento(originator.storeInMemento());
					estado = new NaoVazio();
				}
				estado.estadoDesfazer(undo);
			}

		});

	}

	private KeyListener k1 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			Save.setEnabled(true);
			SaveAs.setEnabled(true);

		}
	};

	// What every button does
	Action Open = new AbstractAction("Open", new ImageIcon("images/icons/open.gif")) {

		public void actionPerformed(ActionEvent e) {
			saveOld();
			if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			SaveAs.setEnabled(true);
		}
	};

	Action Save = new AbstractAction("Save", new ImageIcon("images/icons/save.gif")) {
		public void actionPerformed(ActionEvent e) {
			if (!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}

	};

	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};

	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};

	// TODO complete New
	Action New = new AbstractAction("New", new ImageIcon("images/icons/new.gif")) {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			newFile();
		}
	};

	Action Undo = new AbstractAction("Undo") {
		public void actionPerformed(ActionEvent actionEvent) {
			Undo();
		}

	};

	// TODO: complete this
	// Action toolFile = new AbstractAction(currentFile, new
	// ImageIcon("images/icons/file.gif")) {
	// @Override
	// public void actionPerformed(ActionEvent actionEvent) {
	// // TODO: Open this file if not already open
	// if (currentFile != fileList.get(0)) {
	// saveOld();
	// currentFile = fileList.get(0);
	// }
	// System.out.println(currentFile);
	// area.setText(null);
	// readInFile(currentFile);
	// }
	// };

	ActionMap m = area.getActionMap();
	Action Cut = m.get(DefaultEditorKit.cutAction);
	Action Copy = m.get(DefaultEditorKit.copyAction);
	Action Paste = m.get(DefaultEditorKit.pasteAction);

	// TODO: complete this
	private void newFile() {
		if (!saveOld())
			return;
		currentFile = "Untitled2";

		Action FileThumbnail = new AbstractAction(currentFile, new ImageIcon("images/icons/file.gif")) {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (changed)
					saveOld();

				currentFile = "Untitled2";
				area.setText(null);
				if (fileList.size() > 1) {
					System.out.println("Fetching contents..");
					readInFile(currentFile);
				}
				System.out.println(currentFile);

			}
		};
		tool.add(FileThumbnail);

	}

	private void saveFileAs() {
		if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}

	private boolean saveOld() {
		if (changed) {
			if (JOptionPane.showConfirmDialog(this, "Would you like to save " + currentFile + " ?", "Save",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				saveFile(currentFile);
				fileList.add(currentFile);
				return true;
			}
		}
		return false;
	}

	private void readInFile(String fileName) {
		try {
			// System.out.println("reading: " + fileName);

			FileReader r = new FileReader(fileName);
			area.read(r, null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		} catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this, "Editor can't find the file called " + fileName);
		}
	}

	private void Undo() {
		try {
			if (caretaker.getSize() > 0) {

				area.setText(originator.restoreFromMemento(caretaker.getMemento(caretaker.getSize() - 1)).getText());

				caretaker.savedArticles.remove(caretaker.getSize() - 1);

			}
			if (caretaker.getSize() == 0) {
				
				estado = new Vazio();

			}
			if (caretaker.getSize() > 0){
				estado = new NaoVazio();
			}
			
			estado.estadoDesfazer(undo);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			estado = new Vazio();
			estado.estadoDesfazer(undo);
		}

	}

	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);

			w.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
			Save.setEnabled(false);
		} catch (IOException e) {
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
