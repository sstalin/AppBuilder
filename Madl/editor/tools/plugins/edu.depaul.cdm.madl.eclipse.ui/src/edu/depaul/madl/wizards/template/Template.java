package edu.depaul.madl.wizards.template;

public class Template implements Comparable<Template> {

	private String displayName;
	private int order;
	private String description;
	
	public Template(String displayName, int order, String description) {
		this.displayName = displayName;
		this.order = order;
		this.description = description;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Template [displayName=" + displayName + ", order=" + order
				+ ", description=" + description + "]";
	}
	
	@Override
	public int compareTo(Template displayName) {
		if (getOrder() < displayName.getOrder()) {
			return -1;
		} else if (getOrder() > displayName.getOrder()) {
			return 1;
		} else {
			return 0;
		}
	}
}
