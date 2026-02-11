// 
// Decompiled by Procyon v0.5.30
// 

package org.aestia.other;

public class Animation {
	private int id;
	private int animationId;
	private String animationName;
	private int animationArea;
	private int animationAction;
	private int animationSize;

	public Animation(final int id, final int animId, final String name, final int area, final int action,
			final int size) {
		this.id = id;
		this.animationId = animId;
		this.animationName = name;
		this.animationArea = area;
		this.animationAction = action;
		this.animationSize = size;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.animationName;
	}

	public int getArea() {
		return this.animationArea;
	}

	public int getAction() {
		return this.animationAction;
	}

	public int getSize() {
		return this.animationSize;
	}

	public int getanimationId() {
		return this.animationId;
	}

	public static String PrepareToGA(final Animation animation) {
		final StringBuilder Packet = new StringBuilder();
		Packet.append(animation.getanimationId()).append(",").append(animation.getArea()).append(",")
				.append(animation.getAction()).append(",").append(animation.getSize());
		return Packet.toString();
	}
}
