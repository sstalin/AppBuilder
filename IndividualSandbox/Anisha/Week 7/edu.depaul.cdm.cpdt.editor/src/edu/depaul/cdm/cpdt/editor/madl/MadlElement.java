package edu.depaul.cdm.cpdt.editor.madl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.Position;


public class MadlElement {

	private List elementChildren = new ArrayList();
	private List attributeChildren = new ArrayList();

	private String name;
	private MadlElement parent;
	private Position position;

	public MadlElement(String name)
	{
		super();
		this.name = name;
	}

	public List getChildrenDTDElements()
	{
		return elementChildren;
	}

	public MadlElement addChildElement(MadlElement element)
	{
		elementChildren.add(element);
		element.setParent(this);
		return this;
	}

	public void setParent(MadlElement element)
	{
		this.parent = element;
	}

	public MadlElement getParent()
	{
		return parent;
	}

	public MadlElement addChildAttribute(MadlAttribute attribute)
	{
		attributeChildren.add(attribute);
		return this;
	}

	public String getName()
	{
		return name;
	}
	
	public String getAttributeValue(String localName)
	{
		for (Iterator iter = attributeChildren.iterator(); iter.hasNext();)
		{
			MadlAttribute attribute = (MadlAttribute) iter.next();
			if (attribute.getName().equals(localName)) return attribute.getValue();
		}
		return null;
	}

	public void clear()
	{
		elementChildren.clear();
		attributeChildren.clear();
	}

	public void setPosition(Position position)
	{
		this.position = position;
	}

	public Position getPosition()
	{
		return position;
	}
}
