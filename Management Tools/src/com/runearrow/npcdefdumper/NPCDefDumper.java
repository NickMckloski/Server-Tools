package com.runearrow.npcdefdumper;

import java.io.IOException;

import com.runearrow.utilities.FileOperations;
import com.runearrow.utilities.Stream;


/**
 * Class that unpacks the definitions - copied from a 317 client.
 */
public class NPCDefDumper {

	public static NPCDefDumper forID(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].typeId == (long) i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		NPCDefDumper entityDef = cache[anInt56] = new NPCDefDumper();
		stream.currentOffset = streamIndices[i];
		entityDef.typeId = i;
		entityDef.readValues(stream);
		
		return entityDef;
	}
	public static void run() {
		System.out.println("Dumping NPC Definitions...");
		stream = new Stream(FileOperations.ReadFile("./data/npc.dat"));
		Stream stream2 = new Stream(FileOperations.ReadFile("./data/npc.idx"));
		int totalNPCs = stream2.readUnsignedWord();
		
		NPCDefDumperPanel.npcData = new Object[totalNPCs][5];
		System.out.println(totalNPCs+" npcs loaded.");
		
		streamIndices = new int[totalNPCs];
		int i = 2;
		for (int j = 0; j < totalNPCs; j++) {
			streamIndices[j] = i;
			i += stream2.readUnsignedWord();
		}
		cache = new NPCDefDumper[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new NPCDefDumper();
		for (int index = 0; index < totalNPCs; index++) {
			NPCDefDumper ed = forID(index);
			if (ed == null)
				continue;
			if (ed.name == null)
				continue;
			
		}
	}
	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = stream.readUnsignedByte();
				anIntArray94 = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					anIntArray94[j1] = stream.readUnsignedWord();

			} else if (i == 2) {
				name = stream.readString();
			} else if (i == 3) {
				description = stream.readBytes();
			} else if (i == 12)
				aByte68 = stream.readSignedByte();
			else if (i == 13) {
				standAnim = stream.readUnsignedWord();
			} else if (i == 14)
				walkAnim = stream.readUnsignedWord();
			else if (i == 17) {
				walkAnim = stream.readUnsignedWord();
				anInt58 = stream.readUnsignedWord();
				anInt83 = stream.readUnsignedWord();
				anInt55 = stream.readUnsignedWord();
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = stream.readString();
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = stream.readUnsignedByte();
				anIntArray76 = new int[k];
				anIntArray70 = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					anIntArray76[k1] = stream.readUnsignedWord();
					anIntArray70[k1] = stream.readUnsignedWord();
				}

			} else if (i == 60) {
				int l = stream.readUnsignedByte();
				anIntArray73 = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					anIntArray73[l1] = stream.readUnsignedWord();

			} else if (i == 90)
				stream.readUnsignedWord();
			else if (i == 91)
				stream.readUnsignedWord();
			else if (i == 92)
				stream.readUnsignedWord();
			else if (i == 93)
				aBoolean87 = false;
			else if (i == 95) {
				combatLevel = stream.readUnsignedWord();
				
				int indexId = (int) typeId;
				
				NPCDefDumperPanel.npcData[indexId][0] = name;
				NPCDefDumperPanel.npcData[indexId][1] = combatLevel;
				try {
					NPCDefDumperPanel.npcData[indexId][2] = WikiScraper.dumpHealthFrom2007RSWiki(name, combatLevel); //health
				} catch (IOException e) {
					NPCDefDumperPanel.npcData[indexId][2] = 0;
				}
				NPCDefDumperPanel.npcData[indexId][3] = 16; //respawn timer
				NPCDefDumperPanel.npcData[indexId][4] = aByte68; //size
					
				System.out.println("Id : "+typeId+", Name: "+NPCDefDumperPanel.npcData[indexId][0]+", Combat: "+NPCDefDumperPanel.npcData[indexId][1]+","
							+ " Health: "+NPCDefDumperPanel.npcData[indexId][2]+", Respawn: "+NPCDefDumperPanel.npcData[indexId][3]+" ,"
									+ " Size: "+NPCDefDumperPanel.npcData[indexId][4]);
			} else if (i == 97)
				anInt91 = stream.readUnsignedWord();
			else if (i == 98)
				anInt86 = stream.readUnsignedWord();
			else if (i == 99)
				aBoolean93 = true;
			else if (i == 100)
				anInt85 = stream.readSignedByte();
			else if (i == 101)
				anInt92 = stream.readSignedByte() * 5;
			else if (i == 102)
				anInt75 = stream.readUnsignedWord();
			else if (i == 103)
				anInt79 = stream.readUnsignedWord();
			else if (i == 106) {
				anInt57 = stream.readUnsignedWord();
				if (anInt57 == 65535)
					anInt57 = -1;
				anInt59 = stream.readUnsignedWord();
				if (anInt59 == 65535)
					anInt59 = -1;
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedWord();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (i == 107) {
				aBoolean84 = false;
			}
			
		} while (true);
	}
	public NPCDefDumper() {
		anInt55 = -1;
		anInt57 = -1;
		anInt58 = -1;
		anInt59 = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		aByte68 = 1;
		anInt75 = -1;
		standAnim = -1;
		typeId = -1L;
		anInt79 = 32;
		anInt83 = -1;
		aBoolean84 = true;
		anInt86 = 128;
		aBoolean87 = true;
		anInt91 = 128;
		aBoolean93 = false;
	}

	public int anInt55;
	public static int anInt56;
	public int anInt57;
	public int anInt58;
	public int anInt59;
	public static Stream stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte aByte68;
	public int[] anIntArray70;
	public static int[] streamIndices;
	public int[] anIntArray73;
	public int anInt75;
	public int[] anIntArray76;
	public int standAnim;
	public long typeId;
	public int anInt79;
	public static NPCDefDumper[] cache;
	//public static Client clientInstance;
	public int anInt83;
	public boolean aBoolean84;
	public int anInt85;
	public int anInt86;
	public boolean aBoolean87;
	public int childrenIDs[];
	public byte description[];
	public int anInt91;
	public int anInt92;
	public boolean aBoolean93;
	public int[] anIntArray94;
}
