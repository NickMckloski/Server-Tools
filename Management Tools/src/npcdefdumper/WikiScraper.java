package npcdefdumper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;



public class WikiScraper {
	
	public static int dumpHealthFrom2007RSWiki(String name, int combatLevel) throws IOException {
		try {
			URL url = new URL("http://2007.runescape.wikia.com/wiki/" + name.replace(" ", "_"));
			String line;
			
			URLConnection urlConnection = url.openConnection();
			urlConnection.setReadTimeout(10000);
			BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			while ((line = stream.readLine()) != null) {
				
				if (line.startsWith("<th><a href=\"/wiki/Hit_points\" title=\"Hit points\" class=\"mw-redirect\">Hit points</a>")) {
					String healthString = stream.readLine().replace("</th><td>", "");
					
					String nextLine = stream.readLine();
					if(nextLine.startsWith("<ul><li>")) {
						
						String firstListItem = nextLine.replace("<ul><li>", "");
						
						stream.mark(1000);
						int listItems;
						for(listItems = 0; stream.readLine().startsWith("</li><li>"); listItems++);
						
						stream.reset();
						String[] listStringArray = new String[listItems];
						listStringArray[0] = firstListItem;
						
						for(int i = 0; i < listItems;i++) {
							listStringArray[i] = stream.readLine().replace("</li><li>", "");

							if(getLevelFromString(listStringArray[i]) == combatLevel) {
								String healthRetrievedFromList = listStringArray[i].split(" ")[1];
								return Integer.parseInt(healthRetrievedFromList);
							}
							
						}
						
						
					} else {
						return Integer.parseInt(healthString);
					}
				}
				
			}
			stream.close();
		} catch(Exception e) {
			return 0;
		}
		return 0;
	}
	
	public static int getLevelFromString(String string) {
		
		int getStartIndex = string.indexOf("level");
		String level = string.substring(getStartIndex).replace(")", "");
		String preparedLevelString = level.replace("level ", "");
		return Integer.parseInt(preparedLevelString);
		
	}
}
