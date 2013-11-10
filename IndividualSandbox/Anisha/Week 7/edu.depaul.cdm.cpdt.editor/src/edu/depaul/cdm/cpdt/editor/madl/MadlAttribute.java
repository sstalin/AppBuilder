package edu.depaul.cdm.cpdt.editor.madl;

public class MadlAttribute {

	private String name;
	private String value;

	public MadlAttribute(String name)
	{
		super();
		this.name = name;
	}

	public MadlAttribute(String name, String value)
	{
		super();
		this.name = name;
		this.value = value;
	}

	public String getName()
	{
		return name;
	}

	public String getValue()
	{
		return value;
	}

}
