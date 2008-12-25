/*
  DAuthor
  The author information

  Author: Taylor Brockman
  Date: 6/23/01
*/

package edu.ncssm.iwp.problemdb;


public class DAuthor extends DEntity
{
	public String username = null;
	public String name = null;
	public String organization = null;
	public String email = null;

	/*--------------------------------------------------------------------*/

	public DAuthor ( ) {}

	public DAuthor ( String username,
					 String name,
					 String organization,
					 String email )
	{
		this.username = username;
	}

	/*--------------------------------------------------------------------*/

	public String getName ( ) { return name; }
	public void setName ( String name ) 
	{
		this.name = name;
	}

	public String getUsername ( ) { return username; }
	public void setUsername ( String username ) 
	{
		this.username = username;
	}

	public String getOrganization ( ) { return organization; }
	public void setOrganization ( String organization ) 
	{
		this.organization = organization;
	}

	public String getEmail ( ) { return email; }
	public void setEmail ( String email ) 
	{
		this.email = email;
	}

	/*-----------------------------------------------------------------------*/
}








