// For IWP
// 06/17/01 brockman

package edu.ncssm.iwp.toolkit.xml;

import java.util.*;

import edu.ncssm.iwp.util.*;


public class XMLNode
{

	public ArrayList subNodes = new ArrayList();
	public Hashtable attributes = new Hashtable();
	public boolean realContent;
	public String contents;
	public String tag;
	public int depth;


	public XMLNode ( String contents ) 
	{
		this.depth = 0;
		parseContents(contents);
	}
	
	public XMLNode ( String tag, String contents, int depth )
	{
		IWPLog.debug(this,"[XMLNode] -------------- depth: " + depth + " ---------");
		IWPLog.debug(this,"[XMLNode] ---- tag      : " + tag );
		IWPLog.debug(this,"[XMLNode] ---- contents : " + contents );

		this.depth = depth;
		this.tag = tag;
		parseContents ( contents );
	}

	

	private void parseContents ( String contents )
	{
		/* ok.... look for openings and closings of tags */

		
		/* are there any tags in this branch? */
		// int nTagIndex = contents.indexOf ( '<' );


		//attributes = getAttributes ( tag, contents );


		ArrayList subTags = getSubTags ( contents );

		if ( subTags.size() > 0 ) {
			System.out.println("[XMLNode] Found a container tag" );
			/* we've got guys inside */
			realContent = false;

			for ( int i=0; i < subTags.size(); i++ ) { 
				XMLTag tag = (XMLTag) subTags.get(i);
				subNodes.add ( new XMLNode ( tag.getTag(), tag.getInsideContent(), depth+1 ));
			}


		} else {
			System.out.println("[XMLNode] Found a data tag" );
			/* it's a data tag! */
			realContent = true;
			//contents = contents; // had no effect. 2006-Jan-12 brock
		}
	}






	public ArrayList getSubTags ( String contents )
	{
		int tagDepth = 0;
		int holderCount = 0;
		Hashtable subTags = new Hashtable ();
		ArrayList tags = new ArrayList();
		int startPos = -1;
		int endPos;

		/* parse contents,,, and pull out tags (starting, stopping) */

		int n = contents.length();

		for ( int i=0; i < n; i++ ) {
			if ( contents.charAt(i) == '<' ) {
				holderCount++;
				//IWPLog.info(this,"("+i+":<) HolderCount: " + holderCount );

				if ( holderCount == 1 ) { startPos = i;	}

			} else if ( contents.charAt(i) == '>' ) {
				holderCount--;
				////IWPLog.debug(this,"("+i+":>) HolderCount: " + holderCount );

				if ( holderCount == 0 ) {
					endPos = i;

					String subTag = contents.substring ( startPos + 1, endPos );
					if ( isSubTagOpen ( subTag ) ) {

						subTags.put ( new Integer (tagDepth),
									  new XMLTag ( startPos, -1 ) );
						tagDepth++;
					}
					else if ( isSubTagClose ( subTag ) ) {
						tagDepth--;

						XMLTag tag = (XMLTag) subTags.get ( new Integer(tagDepth) );
						if ( tag == null ) { 
							IWPLog.info(this,"PARSE ERROR!");
						} else { 

							/* finish the tag */
							tag.setStop ( endPos + 1);
							tag.setContent ( contents.substring ( tag.getStart(),
																  tag.getStop() ));
							tag.print();
							tags.add ( tag );
						}

					}

//					IWPLog.debug(this,"[XMLNode] SubTag("+startPos+","+endPos+"): " + subTag + "   depth: " + tagDepth );

				}
			}

			
		}


		return tags;

	}


	private boolean isSubTagOpen ( String tag )
	{
		if ( isSubTagClose ( tag ) ) { return false; }
		/* check for slash at end */
		int n = tag.length() - 1;
		if ( tag.charAt(n) == '/' ) { return false; }
		else { return true; }
	} 


	private boolean isSubTagClose ( String tag ) 
	{
		/* check for a slash @ beginning */
		if ( tag.charAt(0) == '/' ) { return true; } 
		else { return false; }
	}






	public void print ( )
	{

		if ( depth == 0 ) { 
//			IWPLog.debug(this,"[XMLNode] TOP NODE" );
		} else { 
//			IWPLog.debug(this,"[XMLNode] ("+depth+") START TAG: " + tag );
		}

		if ( realContent ) { 
//			IWPLog.debug(this,"[XMLNode] Contents: " + contents );
		} else { 
//			IWPLog.debug(this,"[XMLNode] Container" );
		}


		for ( int i=0; i < subNodes.size(); i++ ) { 
			XMLNode subNode = (XMLNode)subNodes.get(i);
			subNode.print();
		}


		if ( depth == 0 ) { 
//			IWPLog.debug(this,"[XMLNode] END" );
		} else { 
//			IWPLog.debug(this,"[XMLNode] ("+depth+") STOP TAG: " + tag );
		}

	}


}




class XMLTag
{

	int start;
	int stop;
	String content;

	public XMLTag ( int start, int stop )
	{
		this.start = start;
		this.stop = stop;
	}

	public int getStart ( ) { return start; }
	public void setStart ( int start ) { 
		this.start = start;
	}

	public int getStop ( ) { return stop; }
	public void setStop ( int stop ) { 
		this.stop = stop;
	}

	public String getContent () { return content; }
	public void setContent ( String content ) {
		this.content = content;
	}

	public String getInsideContent ()
	{
		int startInside = content.indexOf ('>') + 1;
		int stopInside = content.lastIndexOf ( '<' );

		return content.substring ( startInside, stopInside );
	}


	public String getTag ( )
	{
		int nTagIndex = content.indexOf ('<');
		if ( nTagIndex < 0 ) { return null; }

		String tag_1 = content.substring ( nTagIndex );
		int nSpaceIndex = tag_1.indexOf (' ');
		if ( nSpaceIndex < 0 ) {
			nSpaceIndex = tag_1.indexOf ('/');
			if ( nSpaceIndex < 0 ) { 
				nSpaceIndex = tag_1.indexOf ('>');
			}
		}

		if ( nSpaceIndex < 0 ) { return null; }


		return tag_1.substring ( 0, nSpaceIndex );
	}




	public void print ()
	{
		System.out.println("[XMLTag] "+start+":"+stop+" "+content );
	}
}
