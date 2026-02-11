// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.common;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.aestia.map.Case;
import org.aestia.map.Map;

public class CryptManager {
	public static String CryptPassword(final String Key, final String Password) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		String crypted = "#1";
		for (int i = 0; i < Password.length(); ++i) {
			final char PPass = Password.charAt(i);
			final char PKey = Key.charAt(i);
			final int APass = PPass / '\u0010';
			final int AKey = PPass % '\u0010';
			final int ANB1 = (APass + PKey) % hash.length;
			final int ANB2 = (AKey + PKey) % hash.length;
			crypted = String.valueOf(crypted) + hash[ANB1];
			crypted = String.valueOf(crypted) + hash[ANB2];
		}
		return crypted;
	}

	public static String decryptpass(final String pass, final String key) {
		String l7 = "";
		final String Chaine = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
		for (int l8 = 0; l8 <= pass.length() - 1; l8 += 2) {
			final int l9 = key.charAt(l8 / 2);
			int l10 = Chaine.indexOf(pass.charAt(l8));
			final int l11 = 64 + l10 - l9;
			final int l12 = l8 + 1;
			l10 = Chaine.indexOf(pass.charAt(l12));
			int l13 = 64 + l10 - l9;
			if (l13 < 0) {
				l13 += 64;
			}
			l7 = String.valueOf(l7) + (char) (16 * l11 + l13);
		}
		return l7;
	}

	public static String CryptIP(final String IP) {
		final String[] Splitted = IP.split("\\.");
		String Encrypted = "";
		int Count = 0;
		for (int i = 0; i < 50; ++i) {
			for (int o = 0; o < 50; ++o) {
				if (((i & 0xF) << 4 | (o & 0xF)) == Integer.parseInt(Splitted[Count])) {
					final Character A = (char) (i + 48);
					final Character B = (char) (o + 48);
					Encrypted = String.valueOf(Encrypted) + A.toString() + B.toString();
					i = 0;
					o = 0;
					if (++Count == 4) {
						return Encrypted;
					}
				}
			}
		}
		return "DD";
	}

	public static String CryptPort(final int config_game_port) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		int P = config_game_port;
		String nbr64 = "";
		for (int a = 2; a >= 0; --a) {
			nbr64 = String.valueOf(nbr64) + hash[(int) (P / Math.pow(64.0, a))];
			P %= (int) Math.pow(64.0, a);
		}
		return nbr64;
	}

	public static String cellID_To_Code(final int cellID) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		final int char1 = cellID / 64;
		final int char2 = cellID % 64;
		return String.valueOf(hash[char1]) + hash[char2];
	}

	public static int cellCode_To_ID(final String cellCode) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		final char char1 = cellCode.charAt(0);
		final char char2 = cellCode.charAt(1);
		int code1 = 0;
		int code2 = 0;
		for (int a = 0; a < hash.length; ++a) {
			if (hash[a] == char1) {
				code1 = a * 64;
			}
			if (hash[a] == char2) {
				code2 = a;
			}
		}
		return code1 + code2;
	}

	public static int getIntByHashedValue(final char c) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		for (int a = 0; a < hash.length; ++a) {
			if (hash[a] == c) {
				return a;
			}
		}
		return -1;
	}

	public static char getHashedValueByInt(final int c) {
		final char[] hash = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
				's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
				'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '-', '_' };
		return hash[c];
	}

	public static ArrayList<Case> parseStartCell(final Map map, final int num) {
		ArrayList<Case> list = null;
		String infos = null;
		if (!map.getPlaces().equalsIgnoreCase("-1")) {
			infos = map.getPlaces().split("\\|")[num];
			int a = 0;
			list = new ArrayList<Case>();
			while (a < infos.length()) {
				list.add(map.getCase(
						(getIntByHashedValue(infos.charAt(a)) << 6) + getIntByHashedValue(infos.charAt(a + 1))));
				a += 2;
			}
		}
		return list;
	}

	public static java.util.Map<Integer, Case> decompileMapData(final Map map, final String dData) {
		final java.util.Map<Integer, Case> cells = new TreeMap<Integer, Case>();
		for (int f = 0; f < dData.length(); f += 10) {
			final String CellData = dData.substring(f, f + 10);
			final List<Byte> cellInfos = new ArrayList<Byte>();
			for (int i = 0; i < CellData.length(); ++i) {
				cellInfos.add((byte) getIntByHashedValue(CellData.charAt(i)));
			}
			final int walkable = (cellInfos.get(2) & 0x38) >> 3;
			final boolean los = (cellInfos.get(0) & 0x1) != 0x0;
			final int layerObject2 = ((cellInfos.get(0) & 0x2) << 12) + ((cellInfos.get(7) & 0x1) << 12)
					+ (cellInfos.get(8) << 6) + cellInfos.get(9);
			final boolean layerObject2Interactive = (cellInfos.get(7) & 0x2) >> 1 != 0;
			final int object = layerObject2Interactive ? layerObject2 : -1;
			cells.put(f / 10, new Case(map, f / 10, walkable != 0 && !CellData.equalsIgnoreCase("bhGaeaaaaa")
					&& !CellData.equalsIgnoreCase("Hhaaeaaaaa"), los, object));
		}
		return cells;
	}
}
