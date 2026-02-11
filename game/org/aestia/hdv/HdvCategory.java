// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.hdv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HdvCategory {
	private int categoryId;
	private Map<Integer, HdvTemplate> templates;

	public HdvCategory(final int categoryId) {
		this.templates = new HashMap<Integer, HdvTemplate>();
		this.setCategoryId(categoryId);
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(final int categoryId) {
		this.categoryId = categoryId;
	}

	public Map<Integer, HdvTemplate> getTemplates() {
		return this.templates;
	}

	public HdvTemplate getTemplate(final int templateId) {
		return this.getTemplates().get(templateId);
	}

	public void addTemplate(final int templateId, final HdvEntry toAdd) {
		this.getTemplates().put(templateId, new HdvTemplate(templateId, toAdd));
	}

	public void delTemplate(final int templateId) {
		this.getTemplates().remove(templateId);
	}

	public void addEntry(final HdvEntry toAdd) {
		final int templateId = toAdd.getObject().getTemplate().getId();
		if (this.getTemplates().get(templateId) == null) {
			this.addTemplate(templateId, toAdd);
		} else {
			this.getTemplates().get(templateId).addEntry(toAdd);
		}
	}

	public boolean delEntry(final HdvEntry toDel) {
		boolean toReturn = false;
		this.getTemplates().get(toDel.getObject().getTemplate().getId()).delEntry(toDel);
		if (toReturn = this.getTemplates().get(toDel.getObject().getTemplate().getId()).isEmpty()) {
			this.delTemplate(toDel.getObject().getTemplate().getId());
		}
		return toReturn;
	}

	public ArrayList<HdvEntry> getAllEntry() {
		final ArrayList<HdvEntry> toReturn = new ArrayList<HdvEntry>();
		for (final HdvTemplate template : this.getTemplates().values()) {
			toReturn.addAll(template.getAllEntry());
		}
		return toReturn;
	}

	public String parseTemplate() {
		boolean isFirst = true;
		String strTemplate = "";
		for (final int templateId : this.getTemplates().keySet()) {
			if (!isFirst) {
				strTemplate = String.valueOf(strTemplate) + ";";
			}
			strTemplate = String.valueOf(strTemplate) + templateId;
			isFirst = false;
		}
		return strTemplate;
	}
}
