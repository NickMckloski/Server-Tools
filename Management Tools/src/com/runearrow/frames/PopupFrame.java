package com.runearrow.frames;

import com.runearrow.panels.ShopEditorPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * PopupFrame class, used to create a jframe popup to edit our shops
 * 
 * @author Nick Mckloski
 */
public class PopupFrame extends JFrame {

	private static final long serialVersionUID = 5391499390326664118L;
	private static JFrame popupJFrame;
	private int itemSlot;
	private int shopIdLoaded;
	JTextField itemField;
	JTextField amountField;

	/**
	 * Class constructor for PopupFrame
	 * 
	 * @param itemSlot - item slot we are editing
	 * @param shopIdLoaded - shop id that we are in
	 */
	public PopupFrame(int itemSlot, int shopIdLoaded) {
		super();
		
		popupJFrame = this;
		this.itemSlot = itemSlot;
		this.shopIdLoaded = shopIdLoaded;

		constructPopupJFrame(250, 150);
		constructJPanel();
		populateTextFields();
		finalizePopupJFrame();
		
	}

	/**
	 * Builds the jframe for our popup
	 * 
	 * @param width - x size
	 * @param height - y size
	 */
	private void constructPopupJFrame(int width, int height) {

		//defines our frame
		popupJFrame = new JFrame();
		popupJFrame.setSize(width, height);
		popupJFrame.setTitle("Shop Entry");
		popupJFrame.setLocationRelativeTo(null);
		popupJFrame.setDefaultCloseOperation(2);
	}

	/**
	 * Builds the jpanel to go in our popup jframe
	 */
	private void constructJPanel() {
		
		//declare components
		JPanel mainPanel = new JPanel();
		JLabel itemFieldDescription = new JLabel();
		itemField = new JTextField();

		JLabel amountFieldDescription = new JLabel();
		amountField = new JTextField();
		JButton enter = new JButton();
		
		//manipulate components
		itemFieldDescription.setText("Item Id: ");
		itemField.setPreferredSize(new Dimension(100, 25));

		amountFieldDescription.setText("Item Amount: ");
		amountField.setPreferredSize(new Dimension(100, 25));

		enter.setText("Enter");
		//add action listener to enter button so we make changes when pressed
		enter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				enterSlotChange(itemSlot, shopIdLoaded);
			}
		});
		
		//add components to their parents
		mainPanel.add(itemFieldDescription);
		mainPanel.add(itemField);
		mainPanel.add(amountFieldDescription);
		mainPanel.add(amountField);
		mainPanel.add(enter);
		popupJFrame.add(mainPanel);
	}

	/**
	 * Finalizes our changes and updates the hashmaps
	 * 
	 * @param slot - item slot we edited
	 * @param shopId - shop id we are in
	 */
	protected void enterSlotChange(int slot, int shopId) {

		//load data from shops hashmap into our temporary arrays
		Object[] shopData = new Object[3];
		int[][] itemsArrayLoaded = new int[41][2];
		for (HashMap.Entry<Object[], int[][]> entry : ShopEditorPanel.shopsMap.get(Integer.valueOf(shopId)).entrySet()) {
			shopData = (Object[]) entry.getKey();
			itemsArrayLoaded = (int[][]) entry.getValue();
		}
		
		//edit temporary arrays
		itemsArrayLoaded[slot][0] = Integer.parseInt(itemField.getText());
		itemsArrayLoaded[slot][1] = Integer.parseInt(amountField.getText());

		HashMap<Object[], int[][]> shopValues = new HashMap<Object[], int[][]>();
		
		//put edited data back into the shops hashmap
		shopValues.put(shopData, itemsArrayLoaded);
		ShopEditorPanel.shopsMap.put(Integer.valueOf(shopId), shopValues);

		//redraw the grid of items and close the popup jframe
		ShopEditorPanel.createItemsGrid(shopId);
		popupJFrame.dispose();
	}

	/**
	 * Fills the text fields with data to be edited
	 */
	public void populateTextFields() {
		
		//create temporary array
		int[][] itemsArrayLoaded = new int[41][2];

		//load data from shops hashmap
		for (HashMap.Entry<Object[], int[][]> entry : ShopEditorPanel.shopsMap.get(Integer.valueOf(this.shopIdLoaded)).entrySet()) {
			itemsArrayLoaded = (int[][]) entry.getValue();
		}

		//set text fields to the data we loaded
		itemField.setText(Integer.toString(itemsArrayLoaded[itemSlot][0]));
		amountField.setText(Integer.toString(itemsArrayLoaded[itemSlot][1]));
	}

	/**
	 * Finalizes the jframe by setting it visible
	 */
	private void finalizePopupJFrame() {
		popupJFrame.setVisible(true);
	}
}