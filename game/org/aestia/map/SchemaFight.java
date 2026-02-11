// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.map;

public class SchemaFight {
	private short id;
	private String placesStr;

	public SchemaFight(final String placeStr) {
		this.placesStr = placeStr;
	}

	public SchemaFight(final short i, final String placesStr) {
		this.id = i;
		this.placesStr = placesStr;
	}

	public void setPlacesStr(final String placesStr) {
		this.placesStr = placesStr;
	}

	public String getPlacesStr() {
		return this.placesStr;
	}

	public short getId() {
		return this.id;
	}

	public void setId(final short id) {
		this.id = id;
	}
}
