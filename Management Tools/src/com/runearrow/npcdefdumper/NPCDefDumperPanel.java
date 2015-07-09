package com.runearrow.npcdefdumper;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

import com.runearrow.utilities.MysqlConnector;


/**
 * @author Nick Mckloski
 * 
 * Class for the npc def dumper panel
 */
public class NPCDefDumperPanel extends JPanel {
	
	private static final long serialVersionUID = 7454459929171705712L;

	static JPanel npcDumperJPanel;

	SwingWorker worker;
	
	public static Object[][] npcData;
	
	
	/**
	 * Class constructor
	 */
	public NPCDefDumperPanel() {
		super();
		
		npcDumperJPanel = this;
		
		JTextArea txtConsole = new JTextArea(15, 80);
		JScrollPane scrollPane = new JScrollPane(txtConsole);
		JButton dumpNPCDefinitions = new JButton();
		JButton saveToMysql = new JButton();
		
		PrintStream out = new PrintStream(new TextAreaOutputStream( txtConsole ));
		System.setOut(out);
		
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		txtConsole.setFont(font);		
		DefaultCaret caret = (DefaultCaret)txtConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
        scrollPane.setViewportView(txtConsole);
		
		dumpNPCDefinitions.setText("Dump NPC Definitions.");
		dumpNPCDefinitions.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				if (worker != null) {
					worker.cancel(true);
				}
				worker = new SwingWorker() {

					@Override
					protected Integer doInBackground() {
						try {
							txtConsole.setText("");
							NPCDefDumper.run();
						} catch (Exception ex) {
						}
						return 0;
					}
				};
				worker.execute();

			}

		});
		saveToMysql.setText("Execute to Mysql.");
		saveToMysql.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(null, "Continueing will permanatly erase current npc definitions."
						+ " \n Are you sure you want to continue?", "Are you sure?", dialogButton);
				
				txtConsole.setText("");
				
				if(dialogResult == 0) {
					executeMysqlFromData();
				}
				
				
			}

		});

		npcDumperJPanel.add(scrollPane);
		npcDumperJPanel.add(dumpNPCDefinitions);
		npcDumperJPanel.add(saveToMysql);
		
	}


	private void executeMysqlFromData() {
		
		System.out.println("Executing to database...");
		if (worker != null) {
			worker.cancel(true);
		}
		worker = new SwingWorker() {

			@Override
			protected Integer doInBackground() {

				try {
					Statement s = MysqlConnector.getConnection().createStatement();
					s.executeUpdate("TRUNCATE TABLE npcdefinitions");
					System.out.println("Truncating table.");
					
					if (npcData == null) {
						System.out.println("No definitions have been dumped.");
						return null;
					}

					System.out.println("looping through "+npcData.length+" npcs.");
					for (int i = 0; i < npcData.length; i++) {

						String name = npcData[i][0] == null ? "null" : (String) npcData[i][0];
						if(name.length() > 100)
							name = "null";
						String combat = npcData[i][1] == null ? "0" : Integer.toString((int) npcData[i][1]);
						String health = npcData[i][2] == null ? "0" : Integer.toString((int) npcData[i][2]);
						String respawn = npcData[i][3] == null ? "16" : Integer.toString((int) npcData[i][3]);
						String size = npcData[i][4] == null ? "1" : Integer.toString((byte) npcData[i][4]);

						String string = "INSERT INTO npcdefinitions (id,name,combat,health,respawn,size) VALUES ('" + i + "','" + name.replace("'", "") + "',"
								+ " " + "'" + combat + "','" + health + "','" + respawn + "','" + size + "')";

						s.executeUpdate(string);

						System.out.println(i+" Executed: " + string);
						System.out.println("next name to save: " + npcData[i+1][0]);

					}
				} catch (Exception ex) {
				}
				return 0;
			}
		};
		worker.execute();
		
	}
	
}
