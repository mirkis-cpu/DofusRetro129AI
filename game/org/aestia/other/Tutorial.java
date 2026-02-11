// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

import java.util.ArrayList;

import org.aestia.kernel.Console;
import org.aestia.kernel.Main;

public class Tutorial {
	private int id;
	private ArrayList<Action> reward;
	private Action start;
	private Action end;

	public Tutorial(final int id, final String reward, final String start, String end) {
		this.reward = new ArrayList<Action>(4);
		this.id = id;
		try {
			String[] split3;
			for (int length = (split3 = reward.split("\\$")).length, i = 0; i < length; ++i) {
				final String str = split3[i];
				if (str.isEmpty()) {
					this.reward.add(null);
				} else {
					final String[] split = str.split("@");
					if (split.length >= 2) {
						this.reward.add(new Action(Integer.parseInt(split[0]), split[1], "", null));
					} else {
						this.reward.add(new Action(Integer.parseInt(split[0]), "", "", null));
					}
				}
			}
			if (start.isEmpty()) {
				this.start = null;
			} else {
				final String[] split2 = start.split("\\@");
				if (split2.length >= 2) {
					this.start = new Action(Integer.parseInt(split2[0]), split2[1], "", null);
				} else {
					this.start = new Action(Integer.parseInt(split2[0]), "", "", null);
				}
			}
			if (end.isEmpty()) {
				end = null;
			} else {
				final String[] split2 = end.split("\\@");
				if (split2.length >= 2) {
					this.end = new Action(Integer.parseInt(split2[0]), split2[1], "", null);
				} else {
					this.end = new Action(Integer.parseInt(split2[0]), "", "", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Console.println("Erreur lors du chargement du tutoriel " + id, Console.Color.ERROR);
			Main.stop();
		}
	}

	public int getId() {
		return this.id;
	}

	public Action getStart() {
		return this.start;
	}

	public Action getEnd() {
		return this.end;
	}

	public ArrayList<Action> getReward() {
		return this.reward;
	}
}
