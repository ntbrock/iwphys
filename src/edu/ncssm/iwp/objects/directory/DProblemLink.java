package edu.ncssm.iwp.objects.directory;

import edu.ncssm.iwp.problemdb.DEntity;

public class DProblemLink extends DEntity
{
	public String filename;
	public String summary;
	
	public DProblemLink() {}
	
	public DProblemLink(String filename, String summary) {
		super();
		this.filename = filename;
		this.summary = summary;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
	
	public String toString()
	{
		// show only the filename after the last /
		
		String filenameToPrint = this.filename;
		
		int lastSlash = filenameToPrint.lastIndexOf('/');
		if ( lastSlash < 0 ) { 
			lastSlash = filenameToPrint.lastIndexOf('\\');
		}
		
		if ( lastSlash > 0 && filenameToPrint.length() > lastSlash ) { 
			filenameToPrint = filenameToPrint.substring(lastSlash+1);
		}

		return filenameToPrint + ": " + this.summary;
	}
}
