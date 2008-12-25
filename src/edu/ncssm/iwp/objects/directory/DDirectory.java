package edu.ncssm.iwp.objects.directory;

import edu.ncssm.iwp.problemdb.DEntity;
import java.util.Collection;
import java.util.ArrayList;

/**
 * A directory is an ordered collection of categories that then in turn,
 * hold problem links. This is the model object for the packaged problems
 * viewer.
 * 
 * 2006-Aug-14 brockman
 * 
 * @author brockman
 *
 */

public class DDirectory extends DEntity
{

	public String title;
	public Collection categories = new ArrayList(20);
	
	public DDirectory() {}
	
	public DDirectory( String title )
	{
		super();
		this.title = title;
	}

	public Collection getCategories() {
		return categories;
	}

	public void addCategory ( DCategory category) {
		try { 
			this.categories.add(category);
		} catch ( Exception e ) { 
			System.err.println("CAUGHT E: " + e );
			e.printStackTrace();
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}

