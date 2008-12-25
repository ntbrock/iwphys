
package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.toolkit.xml.*;


public class DAuthorXMLCreator
{
	DAuthor author;

	public DAuthorXMLCreator ( DAuthor author )
	{
		this.author = author;
	}

	XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ("author");

		XMLElement username = new XMLElement ("username");
		username.setText ( author.getUsername() );
		elem.addElement ( username );

		XMLElement name = new XMLElement ("name");
		name.setText ( author.getName() );
		elem.addElement ( name );

		XMLElement organization = new XMLElement ("organization");
		organization.setText ( author.getOrganization() );
		elem.addElement ( organization );

		XMLElement email = new XMLElement ("email");
		email.setText ( author.getEmail() );
		elem.addElement ( email );

		return elem;
	}

	/*-----------------------------------------------------------------*/



	/*-----------------------------------------------------------------*/

}


