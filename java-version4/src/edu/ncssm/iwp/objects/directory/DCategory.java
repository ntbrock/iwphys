package edu.ncssm.iwp.objects.directory;

import edu.ncssm.iwp.problemdb.DEntity;

import java.util.ArrayList;
import java.util.Collection;

public class DCategory extends DEntity
{
	private static final long serialVersionUID = 1L;
	public String name = "Default Name";
	public Collection problemLinks = new ArrayList(20);
	public boolean expandedByDefault = false;
	
	public DCategory() {}
	
	public DCategory(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Collection getProblemLinks() {
		return problemLinks;
	}

	public void addProblemLink( DProblemLink problemLink ) {
		this.problemLinks.add(problemLink);
	}

	public boolean isExpandedByDefault() {
		return expandedByDefault;
	}

	public void setExpandedByDefault(boolean expandedByDefault) {
		this.expandedByDefault = expandedByDefault;
	}
	
	public String toString()
	{
		return getName();
	}

}
