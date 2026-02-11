// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.command.server;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.aestia.db.Database;

public class Groupes {
	private int id;
	private String nom;
	private boolean isPlayer;
	private ArrayList<Commandes> commandes;
	private static Map<Integer, Groupes> groupes;
	private boolean headAdmin;

	static {
		Groupes.groupes = new TreeMap<Integer, Groupes>();
	}

	public Groupes(final int id, final String nom, final boolean isPlayer, final String commande) {
		this.commandes = new ArrayList<Commandes>();
		this.headAdmin = false;
		this.id = id;
		this.nom = nom;
		this.isPlayer = isPlayer;
		if (commande.equalsIgnoreCase("all")) {
			this.headAdmin = true;
			this.commandes = Commandes.getAllCommandes();
		} else if (commande.contains(",")) {
			String[] split;
			for (int length = (split = commande.split(",")).length, i = 0; i < length; ++i) {
				final String str = split[i];
				this.commandes.add(Commandes.getCommandeById(Integer.parseInt(str)));
			}
		} else {
			this.commandes.add(Commandes.getCommandeById(Integer.parseInt(commande)));
		}
		Groupes.groupes.put(id, this);
	}

	public boolean isPlayer() {
		return this.isPlayer;
	}

	public boolean isHeadAdmin() {
		return this.headAdmin;
	}

	public ArrayList<Commandes> getCommandes() {
		return this.commandes;
	}

	public String getNom() {
		return this.nom;
	}

	public int getId() {
		return this.id;
	}

	public static void reload() {
		vider();
		Database.getStatique().getGroupeData().load();
	}

	public static void vider() {
		Groupes.groupes.clear();
	}

	public static int size() {
		return Groupes.groupes.size();
	}

	public boolean haveCommande(final String command) {
		for (final Commandes c : this.commandes) {
			if (c.getArguments().get(0).equalsIgnoreCase(command)) {
				return true;
			}
		}
		return false;
	}

	public static Groupes getGroupeById(final int id) {
		if (Groupes.groupes.containsKey(id)) {
			return Groupes.groupes.get(id);
		}
		return null;
	}
}
