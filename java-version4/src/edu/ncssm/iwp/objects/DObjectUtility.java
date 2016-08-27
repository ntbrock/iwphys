package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.math.MEquation;

/**
 * A utility class to help prevent logic duplication in the various DObject implemenations.
 * 
 * @author brockman
 *
 */

public class DObjectUtility
{

	/**
	 * Throw an exception if the name is bad. Used by DObject_Solid, Input, Output, and should be
	 * re-used by each of the other implemenations.
	 * 
	 * 2007-Jun-03 brockman
	 *
	 * @param name
	 * @throws InvalidObjectNameX
	 */
	
	public static void ensureName(String name )
		throws InvalidObjectNameX
	{	
		// Check and make sure the name exists
		if ( name == null || name.length() <= 0 ) { 
			throw new InvalidObjectNameX("Object name cannot be blank.");
		}

		// Check the name against the functions and variables
		if ( MEquation.isSymbolReservedFunctionOrConstant(name) ) { 
			throw new InvalidObjectNameX("Name: " + name + " is a reserved function or constant.");
		}
		
		// Check for a character that is not a-z A-Z or 0-9
		char[] chars = name.toCharArray();
		for ( int i = 0; i < chars.length; i++ ) { 
			char c = chars[i];
			if ( c < 'a' || c > 'z' ) { 
				if ( c < 'A' || c > 'Z' ) { 
					if ( c < '0' || c > '9' ) { 
						if ( c != '_' ) {
							throw new InvalidObjectNameX("Name: " + name + " contains invalid character: '" + c + "'. Use a-z 0-9 and underscore.");
						}
					}
				}
			}
		}
	}

}
