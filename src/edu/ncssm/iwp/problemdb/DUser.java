package edu.ncssm.iwp.problemdb;

public class DUser extends DEntity
{
	public String username;
	public String password;
	public String email;

	public String realName;

	public boolean adminFlag = false;

	public DUser ( ) { }

	public DUser ( String username,
				   String password,
				   String email )
	{
		this.username = username;
		this.password = password;
		this.email = email;
	}


	public boolean getAdminFlag ( ) { return adminFlag; }
	public void setAdminFlag ( boolean adminFlag )
	{
		this.adminFlag = adminFlag;
	}


	public String getEmail ( ) { return email; }
	public void setEmail ( String email ) { this.email = email; }

	public String getUsername ( ) { return username; }
	public void setUsername ( String username ) { this.username = username; }

	public String getPassword (  ) { return password; }
	public void setPassword ( String password ) { this.password = password; }

	public boolean passwordMatches ( String newPassword )
	{
		if ( this.password.equals ( newPassword ) ) {
			return true;
		} else { 
			return false;
		}
	}


	public String getRealName ( ) { return realName; }
	public void setRealName ( String realName )
	{
		this.realName = realName;
	}

}



