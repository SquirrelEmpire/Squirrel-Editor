package org.squirrel;


import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

import org.squirrel.gui.MainGui;

public class Main {
	public static MainGui frame;
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, BadLocationException{
		frame = new MainGui();
		frame.setVisible(true);
	}
}
