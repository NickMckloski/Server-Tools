package com.runearrow.panels;

import com.runearrow.frames.PopupFrame;
import com.runearrow.mysql.MysqlConnector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * ShopEditorPanel class, used to create wrapping panel for the shop editor tab
 * 
 * @author Nick Mckloski
 */
public class ShopEditorPanel extends JPanel {

	private static final long serialVersionUID = 1173853913987844641L;
	static JPanel shopEditorJPanel;
	public static HashMap<Integer, HashMap<Object[], int[][]>> shopsMap = new HashMap<Integer, HashMap<Object[], int[][]>>();

	int shopAmount = 0;
	static JPanel gridOfItems;
	static JTextField shopNameField;
	static JTextField typeField;
	static JTextField currencyField;

	/**
	 * Constructor for ShopEditorPanel class
	 */
	public ShopEditorPanel() {
		super();
		shopEditorJPanel = this;

		//load data from msqyl and fill our maps with it
		populateMapsFromMysql();

		//declare components
		JLabel shopNameFieldDescription = new JLabel();
		shopNameField = new JTextField();

		JLabel typeFieldDescription = new JLabel();
		typeField = new JTextField();

		JLabel currencyFieldDescription = new JLabel();
		currencyField = new JTextField();

		JComboBox<Integer> shopSelector = new JComboBox<Integer>();
		JButton saveButton = new JButton();
		JButton newButton = new JButton();

		//manipulate components
		shopNameFieldDescription.setText("Shop name:");
		shopNameField.setPreferredSize(new Dimension(200, 25));

		typeFieldDescription.setText("Shop type:");
		typeField.setPreferredSize(new Dimension(100, 25));

		currencyFieldDescription.setText("Currency type:");
		currencyField.setPreferredSize(new Dimension(100, 25));

		for (int i = 0; i < shopAmount;i++) {
			shopSelector.addItem(Integer.valueOf(i + 1));
		}
		newButton.setText("New Shop");
		//add action listener to new button
		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				createNewShop();
				shopSelector.addItem(Integer.valueOf(shopAmount));
				shopSelector.setSelectedItem(Integer.valueOf(shopAmount));
				
			}

		});
		saveButton.setText("Save");
		//add action listener to save button
		saveButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				saveChangesToMysql(((Integer) shopSelector.getSelectedItem()).intValue());
			}

		});
		//add action listener to shop selector combo box
		shopSelector.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				createItemsGrid(((Integer) shopSelector.getSelectedItem()).intValue());
			}
		});
		
		//add components to their parents
		shopEditorJPanel.add(shopNameFieldDescription);
		shopEditorJPanel.add(shopNameField);
		shopEditorJPanel.add(typeFieldDescription);
		shopEditorJPanel.add(typeField);
		shopEditorJPanel.add(currencyFieldDescription);
		shopEditorJPanel.add(currencyField);
		shopEditorJPanel.add(shopSelector);
		shopEditorJPanel.add(saveButton);
		shopEditorJPanel.add(newButton);

		//draw the first shop loaded by default
		createItemsGrid(1);
	}

	/**
	 * Creates a new shop in our shops hashmap
	 */
	private void createNewShop() {

		//increment our total shop amount
		shopAmount += 1;
		int newShopId = shopAmount;
		
		//create a new object array of data to put into hashmap
		Object[] shopData = new Object[3];
		//put defualt options into the object array
		shopData[0] = "New Shop";
		shopData[1] = "SPECIALIST";
		shopData[2] = Integer.valueOf(995);
		
		//create a blank items array to put into hashmap
		int[][] itemsArray = new int[41][2];

		//put all our new shop data into the shops hashmap
		HashMap<Object[], int[][]> shopValues = new HashMap<Object[], int[][]>();
		shopValues.put(shopData, itemsArray);
		shopsMap.put(Integer.valueOf(newShopId), shopValues);

		//draw our new shop on the grid
		createItemsGrid(newShopId);
	}

	/**
	 * Saves changes to our mysql database
	 * 
	 * @param selectedShop - shop id to update/insert into database
	 */
	private void saveChangesToMysql(int selectedShop) {

		//local array of items
		int[][] itemsArrayLoaded = new int[41][2];
		
		//grab data from hashmaps and load into the local array
		for (HashMap.Entry<Object[], int[][]> entry : shopsMap.get(Integer.valueOf(selectedShop)).entrySet()) {
			itemsArrayLoaded = (int[][]) entry.getValue();
		}

		//string builders for our items and amounts
		StringBuilder itemStringBuilder = new StringBuilder();
		StringBuilder amountStringBuilder = new StringBuilder();

		//loop through our array and seperate each item with a comma
		String prefix = "";
		for (int i = 0; i < itemsArrayLoaded.length; ++i) {
			if (itemsArrayLoaded[i][0] == 0)
				break;
			itemStringBuilder.append(prefix);
			amountStringBuilder.append(prefix);
			itemStringBuilder.append(itemsArrayLoaded[i][0]);
			amountStringBuilder.append(itemsArrayLoaded[i][1]);
			prefix = ",";
		}

		try {
			//create a mysql statement
			Statement s = MysqlConnector.getConnection().createStatement();
			//trys to insert a new shop, if it already exists it will instead ignore this and continue
			s.executeUpdate("INSERT IGNORE INTO shops (id,name,type,currency,itemid,amount) VALUES ('" + selectedShop + "',"
					+ " '" + shopNameField.getText() + "'" + ", '" + typeField.getText() + "',"
							+ " '" + Integer.parseInt(currencyField.getText()) + "'" + ", '" + itemStringBuilder.toString() + "',"
									+ " '" + amountStringBuilder.toString() + "')");

			//if we ignored the last statement(meaning this is a new shop) then create a new shop entry
			s.executeUpdate("UPDATE shops SET name = '" + shopNameField.getText() + "', type = '" + typeField.getText() + "'" + ","
					+ " currency = '" + Integer.parseInt(currencyField.getText()) + "', itemid = '" + itemStringBuilder.toString() + "'" + ","
							+ " amount = '" + amountStringBuilder.toString() + "' WHERE id = '" + selectedShop + "'");

			JOptionPane.showMessageDialog(shopEditorJPanel, "SQL Executed.");
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(shopEditorJPanel, "SQL Failed.", "Error", 0);
			e.printStackTrace();
		}
	}

	/**
	 * Grabs data from the mysql database and populates our hashmaps
	 */
	private void populateMapsFromMysql() {

		try {
			//create our statement and query the database
			Statement s = MysqlConnector.getConnection().createStatement();
			s.executeQuery("SELECT * FROM shops");
			ResultSet results = s.getResultSet();
			
			//loop through the results from the query
			while (results.next()) {
				
				//increment our total shop count
				shopAmount ++;

				//grab data from the sql result
				int id = Integer.valueOf(results.getInt("id")).intValue();
				String shopName = results.getString("name");
				String type = results.getString("type");
				int currency = results.getInt("currency");
				String[] itemsIdsFromMysql = results.getString("itemid").split(",");
				String[] amountsForEachItem = results.getString("amount").split(",");

				//create an array of items and populate it with data we got above
				int[][] itemsArray = new int[41][2];
				for (int i = 0; i < itemsArray.length; ++i) {
					itemsArray[i][0] = ((i < itemsIdsFromMysql.length) ? Integer.parseInt(itemsIdsFromMysql[i]) : 0);
					itemsArray[i][1] = ((i < amountsForEachItem.length) ? Integer.parseInt(amountsForEachItem[i]) : 0);
				}

				//create a hashmap that contains our shopdata, and items array
				HashMap<Object[], int[][]> shopValues = new HashMap<Object[], int[][]>();
				Object[] shopData = new Object[3];
				shopData[0] = shopName;
				shopData[1] = type;
				shopData[2] = Integer.valueOf(currency);
				
				//put all the hashmap we created into the shops hashmap by id
				shopValues.put(shopData, itemsArray);
				shopsMap.put(Integer.valueOf(id), shopValues);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the grid of items
	 * 
	 * @param shopIdToLoad - shop id to render in grind
	 */
	public static void createItemsGrid(int shopIdToLoad) {

		//if the grid exists, remove it so we can redraw it
		if (gridOfItems != null)
			shopEditorJPanel.remove(gridOfItems);
		gridOfItems = new JPanel();

		//create local arrays for our data and fill it with data from shops hashmap
		Object[] shopData = new Object[3];
		int[][] itemsArrayLoaded = new int[41][2];
		for (HashMap.Entry<Object[], int[][]> entry : shopsMap.get(Integer.valueOf(shopIdToLoad)).entrySet()) {
			shopData = (Object[]) entry.getKey();
			itemsArrayLoaded = (int[][]) entry.getValue();
		}

		//set the text fields to the data we got
		shopNameField.setText((String) shopData[0]);
		typeField.setText((String) shopData[1]);
		currencyField.setText(Integer.toString(((Integer) shopData[2]).intValue()));

		//set the grid up
		int row = 5;
		int col = 8;
		int itemSlot = 0;
		gridOfItems.setLayout(new GridLayout(row, col));
		gridOfItems.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		//loop through grid and create a unique panel item for each
		for (int i = 0; i < row * col;i++) {
			
			int itemId = itemsArrayLoaded[i][0];
			int itemAmount = itemsArrayLoaded[i][1];
			
			//declare components
			JPanel itemPanel = new JPanel();
			JLabel itemLabel = new JLabel();
			JLabel itemAmountLabel = new JLabel();

			//manipulate components
			itemLabel.setIcon(new ImageIcon("./img/" + itemId + ".png"));
			itemAmountLabel.setForeground(Color.YELLOW);
			itemAmountLabel.setText(Integer.toString(itemAmount));

			itemPanel.setEnabled(true);
			itemPanel.setPreferredSize(new Dimension(60, 60));
			itemPanel.setBackground(new Color(57, 50, 40));
			itemPanel.setBorder(BorderFactory.createLineBorder(new Color(34, 28, 22)));
			itemPanel.addMouseListener(new PanelListener());

			//pass data onto the name of the panel so we can tell the panels appart
			itemPanel.setName(itemSlot + "," + shopIdToLoad);
			itemSlot++;

			//if theres no item, dont add the sprite and amount
			if (itemId != 0) {
				itemPanel.add(itemLabel);
				itemPanel.add(itemAmountLabel);
			}
			
			//add components
			gridOfItems.add(itemPanel);
		}

		shopEditorJPanel.add(gridOfItems);

		//called to redraw the panel
		shopEditorJPanel.revalidate();
	}

	/**
	 * Inner class that handles the click events on each item panel
	 * 
	 * @author Nick Mckloski
	 */
	public static class PanelListener extends MouseAdapter {

		public void mouseClicked(MouseEvent mouseEvent) {
			
			//get the name of the item panel
			JPanel clickedItemPanel = (JPanel) mouseEvent.getSource();
			
			//seperate the data into a string array
			String[] dataFromPanel = clickedItemPanel.getName().split(",");

			//create a popupjframe and pass it the data from the name of the panel
			PopupFrame popup = new PopupFrame(Integer.parseInt(dataFromPanel[0]), Integer.parseInt(dataFromPanel[1]));
		}
	}
}