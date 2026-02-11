// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.command.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.db.Database;

public class Commandes {
	private int id;
	private static Map<Integer, Commandes> commandes;
	private ArrayList<String> arguments;

	static {
		Commandes.commandes = new TreeMap<Integer, Commandes>();
	}

	public Commandes(final int id, final String commande, final String args, final String description) {
		this.arguments = new ArrayList<String>();
		if (commande == null) {
			return;
		}
		this.id = id;
		this.arguments.add(0, commande);
		this.arguments.add(1, (args == null) ? "" : args);
		this.arguments.add(2, (description == null) ? "" : description);
		Commandes.commandes.put(id, this);
	}

	public int getId() {
		return this.id;
	}

	public ArrayList<String> getArguments() {
		return this.arguments;
	}

	public static void reload() {
		vider();
		Database.getStatique().getCommandeData().load();
	}

	public static void vider() {
		Commandes.commandes.clear();
	}

	public static ArrayList<Commandes> getAllCommandes() {
		final ArrayList<Commandes> list = new ArrayList<Commandes>();
		for (final Map.Entry<Integer, Commandes> entry : Commandes.commandes.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}

	public static int size() {
		return Commandes.commandes.size();
	}

	public static Commandes getCommandeById(final int id) {
		if (Commandes.commandes.containsKey(id)) {
			return Commandes.commandes.get(id);
		}
		return null;
	}

	public static Commandes getCommandeByName(final String name) {
		for (final Map.Entry<Integer, Commandes> entry : Commandes.commandes.entrySet()) {
			if (entry.getValue().getArguments().get(0).equalsIgnoreCase(name)) {
				return entry.getValue();
			}
		}
		return null;
	}
}
