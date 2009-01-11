package edu.ncssm.iwp.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.ncssm.iwp.objects.DObject_AbstractWindow;
import edu.ncssm.iwp.ui.GWindow_Animator;


/**
 * This class is used in the GWindow Animator in the bottom tabs. It holds the grid windows.
 * Brockman fxied a bug in here on 2007-Sep-02 so that the values for the inputs are re-loaded
 * every time a new problem is viewed in the Animator.
 * 
 * @author brockman
 *
 */

public class GWindow_Animator_setWindowProperties
    extends GWindow_Animator_setWindowPropertiesAbstract
{
	private static final long serialVersionUID = 1L;
	
    public GWindow_Animator_setWindowProperties( GWindow_Animator parent )
    {
    	super( parent );
    }
							
    protected void finishInit(DObject_AbstractWindow parent) 
    {

    	
    	String xunit = parent.getUnitX();
    	String yunit = parent.getUnitY();
    	
    	holder.add(new JLabel(" X Max ("+xunit+"): "));
    	holder.add(maxX);
    	holder.add(new JLabel(" Y Max ("+yunit+"): "));
    	holder.add(maxY);
    	holder.add(new JLabel(" X Min ("+xunit+"): "));
    	holder.add(minX);
    	holder.add(new JLabel(" Y Min ("+yunit+"): "));
    	holder.add(minY);
    	holder.add(new JLabel(" X Grid ("+xunit+"): "));
    	holder.add(gridX);
    	holder.add(new JLabel(" Y Grid ("+yunit+"): "));
    	holder.add(gridY);
		
    	
    	JPanel buttonPanel = new JPanel();
    	JButton apply = new JButton("Apply");
    	buttonPanel.add(apply);
    	apply.addActionListener(this);
    	
    	setLayout(new BorderLayout());
    	add(BorderLayout.NORTH,holder);
    	add(BorderLayout.CENTER,buttonPanel);
    	
    }
   
}
