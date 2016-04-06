package org.squirrel;


import java.io.IOException;

import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

import org.squirrel.gui.MainGui;

public class Main {
	public static MainGui frame;
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, BadLocationException, NumberFormatException, IOException{
		frame = new MainGui();
		frame.setVisible(true);
	}
}
