package com.runearrow;


import com.runearrow.shopeditor.ShopEditorPanel;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

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

		MainFrame.constructMainJFrame(700, 450);
		MainFrame.constructJPanelTabs();
		MainFrame.constructTabbedPane();
		MainFrame.finalizeMainFrame();
		
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
		mainJFrame.add(tabbedPane);
		
	}

	/**
	 * Builds each individual panel
	 */
	public static void constructJPanelTabs() {

		MainFrame.constructShopEditorPanel();
		
	}

	/**
	 * Builds ShopEditorPanel
	 */
	private static void constructShopEditorPanel() {

		shopEditorJPanel = new ShopEditorPanel();
		
	}

}