package edu.depaul.madl.wizards.template;

public class TemplateDisplayName implements Comparable<TemplateDisplayName> {

	private String displayName;
	private int order;
	
	public TemplateDisplayName(String displayName, int order) {
		this.displayName = displayName;
		this.order = order;
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

	@Override
	public String toString() {
		return "TemplateDisplayName [displayName=" + displayName + ", order=" + order
				+ "]";
	}
	
	@Override
	public int compareTo(TemplateDisplayName displayName) {
		if (getOrder() < displayName.getOrder()) {
			return -1;
		} else if (getOrder() > displayName.getOrder()) {
			return 1;
		} else {
			return 0;
		}
	}
}
