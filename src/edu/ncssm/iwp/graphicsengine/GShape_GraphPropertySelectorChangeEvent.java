package edu.ncssm.iwp.graphicsengine;

/**
 * 2007-Oct-20 This event carries the signal up to the graph frame window that
 * the input option has changed.
 * @author Cory
 *
 */

public class GShape_GraphPropertySelectorChangeEvent
{
	GShape_GraphPropertySelector source;

	public GShape_GraphPropertySelector getSource() {
		return source;
	}

	public void setSource(GShape_GraphPropertySelector source) {
		this.source = source;
	}

	public GShape_GraphPropertySelectorChangeEvent(
			GShape_GraphPropertySelector source) {
		super();
		this.source = source;
	}
	
}
