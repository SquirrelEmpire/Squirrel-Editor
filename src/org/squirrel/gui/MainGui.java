package org.squirrel.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Timer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JLabel;

import java.awt.event.ActionListener;

import javax.swing.JList;

public class MainGui extends JFrame implements ActionListener{

	private static final long serialVersionUID = -8532498602203490794L;
	private JTextArea area = new JTextArea();
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private String title = "Squirrel's Editor ";
	private JLabel RowCollum;
	private int rowNum = 0;
	private int colNum = 0;
	private Color textColor;
	private int[] rgbColor = {139, 5, 239};
	private JList<Object> list;
	private Timer programTimer;
	
	private boolean changed = false;
	
	public MainGui() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, BadLocationException {
		getContentPane().getLayout();
		area.setWrapStyleWord(true);
		area.setBackground(new Color(59,59,59));
		textColor = new Color(rgbColor[0], rgbColor[1], rgbColor[2]);
		area.setForeground(textColor);
		programTimer = new Timer(10, this);
		programTimer.start();
		
		//Set le font
		area.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		//How to be MLG ------------^
		area.setCaretColor(Color.WHITE);
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		getContentPane().add(scroll,BorderLayout.CENTER);
		
		//Menu
		JMenuBar JMB = new JMenuBar();
		JMB.setBorderPainted(false);
		setJMenuBar(JMB);
		
		//File menu
		JMenu file = new JMenu("File");
		JMB.add(file);
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		file.add(Open);
		file.add(Save);
		file.add(SaveAs);
		file.add(OpenCredits);
		file.add(Quit);

		file.addSeparator();
		
		//Options
		JMenu options = new JMenu("Options");
		JMB.add(options);
		
		//Red value
		Red = new JTextField();
		options.add(Red);
		Red.setColumns(10);
		
		//Green Value
		Green = new JTextField();
		options.add(Green);
		Green.setColumns(10);
		
		//Blue value
		Blue = new JTextField();
		options.add(Blue);
		Blue.setColumns(10);
		
		//Info for dummies :D
		JLabel lblNewLabel = new JLabel("Text Color(rgb)");
		options.add(lblNewLabel);
		
		//Apply the new Color
		JButton SaveButton = new JButton("Save");
		SaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				rgbColor[0] = Integer.parseInt(Red.getText());
				rgbColor[1] = Integer.parseInt(Green.getText());
				rgbColor[2] = Integer.parseInt(Blue.getText());
				textColor = new Color(rgbColor[0], rgbColor[1], rgbColor[2]);
				area.setForeground(textColor);
			}
		});
		
		JSeparator separator = new JSeparator();
		options.add(separator);
		
		//Save options button
		options.add(SaveButton);
		
		//Font menu
		JMenu fonts = new JMenu("Fonts");
		JMB.add(fonts);
		
		//Add all the fonts this system can use
		String[] allFonts = getFonts();
		
		//init the list
		list = new JList<Object>(allFonts);
		fonts.add(list);
		list.setVisibleRowCount(10);
		
		//So u can scroll trough all the 9999999 fonts 
		JScrollPane scrollPane = new JScrollPane(list);
		fonts.add(scrollPane);
		
		//Apply the font to the text
		JButton fontsEquip = new JButton("Apply");
		fontsEquip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newFont = (String) list.getSelectedValue();
				area.setFont(new Font(newFont, Font.PLAIN, 18));
			}
		});
		
		
		fonts.add(fontsEquip);
		
		//Row n' stuff
		RowCollum = new JLabel("");
		JMB.add(RowCollum);
    	RowCollum.setText("Line: "+ 1 +" Column: "+ 1);
		
		
		Save.setEnabled(false);
		SaveAs.setEnabled(false);
		
		setPreferredSize(new Dimension(1280, 800));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		area.addKeyListener(k1);
		setTitle(title+"- "+currentFile);
		
		WindowListener exitListener = new WindowAdapter() {

		    @Override
		    public void windowClosing(WindowEvent e) {
		    	saveOld();
		    }
		};
		addWindowListener(exitListener);

	}
	private KeyListener k1 = new KeyAdapter() {

		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if(key != KeyEvent.VK_SHIFT){
				changed = true;
				setTitle("• "+title+"- "+currentFile);
				Save.setEnabled(true);
				SaveAs.setEnabled(true);
	
			}

		}
	};
	
	
	@SuppressWarnings("serial")
	Action Open = new AbstractAction("Open") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			if(dialog.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
				currentFile = dialog.getSelectedFile().getAbsolutePath();
				setTitle(title+"- "+currentFile);
			}
			SaveAs.setEnabled(true);
		}
	};
	
	@SuppressWarnings("serial")
	Action Save = new AbstractAction("Save") {
		public void actionPerformed(ActionEvent e) {
			if(!currentFile.equals("Untitled"))
				saveFile(currentFile);
			else
				saveFileAs();
		}
	};
	
	@SuppressWarnings("serial")
	Action SaveAs = new AbstractAction("Save as...") {
		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	@SuppressWarnings("serial")
	Action Quit = new AbstractAction("Quit") {
		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	
	@SuppressWarnings("serial")
	Action OpenCredits = new AbstractAction("About") {
		public void actionPerformed(ActionEvent e) {
			showCredits();
		}
	};
	private JTextField Red;
	private JTextField Green;
	private JTextField Blue;
	
	private void showCredits(){
		JOptionPane.showMessageDialog(this,"Made by DemSquirrel - https://github.com/DemSquirrel");
	}
	
	private void saveFileAs() {
		if(dialog.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
			saveFile(dialog.getSelectedFile().getAbsolutePath());
	}
	
	private void saveOld() {
		if(changed && currentFile != "Untitled") {
			if(JOptionPane.showConfirmDialog(this, "Would you like to save "+ currentFile +" ?","Save",JOptionPane.YES_NO_OPTION)== JOptionPane.YES_OPTION)
				saveFile(currentFile);
		}
	}
	
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r,null);
			r.close();
			currentFile = fileName;
			changed = false;
			setTitle(title+"- "+currentFile);
		}
		catch(IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Sorry the open this file "+fileName);
		}
	}
	
	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			area.write(w);
			w.close();
			currentFile = fileName;
			setTitle(title+"- "+currentFile);
			changed = false;
			Save.setEnabled(false);
		}
		catch(IOException e) {
		}
	}
	
	private String[] getFonts(){
		String fontsNames[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		return fontsNames;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		int caretPos = area.getCaretPosition();
		rowNum = (caretPos == 0) ? 1 : 1;
		int Xoffset = -1;
		
		try {
			Xoffset = Utilities.getRowStart(area, caretPos);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		colNum = caretPos - Xoffset + 1;
		for (int offset = caretPos; offset > 0;) {
		    try {
				offset = Utilities.getRowStart(area, offset)- 1;
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		    rowNum++;
		    RowCollum.setText("Line: "+rowNum+" Column: "+colNum);
		}
	}
}