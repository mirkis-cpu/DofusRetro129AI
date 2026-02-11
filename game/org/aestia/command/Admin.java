// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.command;

import org.aestia.client.Player;
import org.aestia.command.server.CDefault;
import org.aestia.kernel.Config;

public class Admin {
	public static Command get(final Player player) {
		Config.getInstance().NAME.toUpperCase().hashCode();
		return new CDefault(player);
	}
}
