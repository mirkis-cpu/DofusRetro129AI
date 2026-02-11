package org.aestia.db.statique.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.aestia.db.statique.AbstractDAO;
import org.aestia.game.world.World;
import org.aestia.map.InteractiveObject;

public class Interactive_objects_dataData extends AbstractDAO<InteractiveObject.InteractiveObjectTemplate> {
	public Interactive_objects_dataData(Connection dataSource) {
		super(dataSource);
	}

	@Override
	public void load(Object obj) {
	}

	@Override
	public boolean update(InteractiveObject.InteractiveObjectTemplate obj) {
		return false;
	}

	public void load() {
		ResultSet RS = null;
		try {
			try {
				RS = this.getData("SELECT * from interactive_objects_data");

				while (RS.next()) {
					World.addIOTemplate(
							new InteractiveObject.InteractiveObjectTemplate(RS.getInt("id"), RS.getInt("respawn"),
									RS.getInt("duration"), RS.getInt("unknow"), RS.getInt("walkable") == 1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} finally {
			this.close(RS);
		}
	}
}
