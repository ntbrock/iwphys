package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.toolkit.xml.*;


public class DUserXMLCreator extends XMLCreator
{
	DUser user = null;

	public DUserXMLCreator ( DUser user )
	{
		this.user = user;
	} 

	public XMLElement getElement ( ) 
	{
		XMLElement elem = new XMLElement ( "user" );
			 
		elem.addElement ( new XMLElement ( "username", user.getUsername() ));
		elem.addElement ( new XMLElement ( "password", user.getPassword() ));
		elem.addElement ( new XMLElement ( "realName", user.getRealName() ));
		elem.addElement ( new XMLElement ( "email", user.getEmail() ));

		String adminFlagString = "false";
		if ( user.getAdminFlag() ) { adminFlagString = "true"; }

		elem.addElement ( new XMLElement ( "adminFlag", adminFlagString ));

		return elem;
	}


}

