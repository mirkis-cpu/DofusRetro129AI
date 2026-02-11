// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.client.other;

import java.util.HashMap;
import java.util.Map;

public class Restriction {
	public static Map<Integer, Restriction> restrictions;
	public long timeDeblo;
	public Map<String, Long> aggros;

	static {
		Restriction.restrictions = new HashMap<Integer, Restriction>();
	}

	public Restriction() {
		this.timeDeblo = -1L;
		this.aggros = new HashMap<String, Long>();
	}

	public static Restriction get(final int id) {
		if (Restriction.restrictions.get(id) != null) {
			return Restriction.restrictions.get(id);
		}
		return new Restriction();
	}
}
