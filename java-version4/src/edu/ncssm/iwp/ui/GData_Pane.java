package edu.ncssm.iwp.ui;

import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GWindow_Animator;
import edu.ncssm.iwp.objects.DObject_Window;

import javax.swing.JTabbedPane;


public class GData_Pane extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	protected GWindow_Animator parent;
	protected DObject_Window window;

    public GData_Pane(GWindow_Animator a) {
	parent=a;
    }

    public void tick(DProblemState state)
    	throws Exception
    {
    	((IWPAnimated)getSelectedComponent()).tick(state);
    }
    public void reset( )
    {
	window = parent.getProblem().getWindowObject();
    }
}
