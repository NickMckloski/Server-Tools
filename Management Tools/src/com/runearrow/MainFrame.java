package com.runearrow;


import com.runearrow.shopeditor.ShopEditorPanel;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import npcdefdumper.NPCDefDumperPanel;

/**
 * MainFrame class, the wrapping jframe for the whole application
 * 
 * @author Nick Mckloski
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = 8588817393339828091L;
	private static JFrame mainJFrame;
	private static JTabbedPane tabbedPane;
	private static ShopEditorPanel shopEditorJPanel;
	private static NPCDefDumperPanel npcDumperJPanel;

	/**
	 * Constructor for MainFrame class
	 */
	public MainFrame() {
		super();
		mainJFrame = this;
	}

	/**
	 * main void of MainFrame class
	 * 
	 * @param parameters
	 */
	public static void main(String[] parameters) {

		constructMainJFrame(700, 450);
		constructJPanelTabs();
		constructTabbedPane();
		finalizeMainFrame();
		
	}

	/**
	 * Builds the jframe for MainFrame
	 * 
	 * @param width - x size
	 * @param height - y size
	 */
	public static void constructMainJFrame(int width, int height) {

		mainJFrame = new MainFrame();
		mainJFrame.setSize(width, height);
		mainJFrame.setTitle("Server Management Tools");
		mainJFrame.setLocationRelativeTo(null);
		mainJFrame.setDefaultCloseOperation(3);
		
	}

	/**
	 * Finish building jframe by setting visiable
	 */
	public static void finalizeMainFrame() {

		mainJFrame.setVisible(true);
		
	}

	/**
	 * Builds the tabbed pane 
	 */
	private static void constructTabbedPane() {

		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Shop Editor", (Component) shopEditorJPanel);
		tabbedPane.addTab("Shop Editor", (Component) npcDumperJPanel);
		mainJFrame.add(tabbedPane);
		
	}

	/**
	 * Builds each individual panel
	 */
	public static void constructJPanelTabs() {

		constructShopEditorPanel();
		constructNPCDumperPanel();
		
	}

	/**
	 * Builds ShopEditorPanel
	 */
	private static void constructShopEditorPanel() {

		shopEditorJPanel = new ShopEditorPanel();
		
	}
	/**
	 * Builds NPCDefDumperPanel
	 */
	private static void constructNPCDumperPanel() {

		npcDumperJPanel = new NPCDefDumperPanel();
		
	}

}