package edu.ncssm.iwp.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

import edu.ncssm.iwp.objects.DObject_AbstractWindow;
import edu.ncssm.iwp.util.IWPLog;

/**
 * Abstract class that contains the gui objects for the visual
 * window properties editor. Not sure why it's abstract, as all
 * this could just live in a single class.
 * 
 * This calls GWindow.myRepaintAll() after the update is applied. I 
 * am not sure how the 'OK' actions get fired.
 * 
 * Old code that shoudl probably be refactored one day. 
 * 
 * @author brockman
 *
 */

public abstract class GWindow_Animator_setWindowPropertiesAbstract extends JPanel
implements ActionListener
{
	
	JTextField minX;
	JTextField maxX;
	JTextField gridX;
	JTextField minY;
	JTextField maxY;
	
	JTextField gridY;
	JButton ok;
	JButton cancel;
	JButton apply;
	GWindow_Animator parent;
	
	JPanel holder;
	
	public GWindow_Animator_setWindowPropertiesAbstract ( GWindow_Animator parent ) {
		// save the parent, so we know where to write the time info back to.
		this.parent = parent;
		init( parent );
	}
	
	private void init( GWindow_Animator parent )
	{	
		// BEware: there was a bug here where the window window wasn't
		// beinng updated, but the graph window was. Brockman 2006-Oct-01
		//subject= parent.getProblem().getGraphWindow();
		
		holder=new JPanel();
		holder.setLayout(new GridLayout(3,4,5,5));
		minX=new JTextField("",4);
		maxX=new JTextField("",4);
		gridX=new JTextField("",4);
		
		minY=new JTextField("",4);
		maxY=new JTextField("",4);
		gridY=new JTextField("",4);

		// 2007-Sep-02 brockman: This class is NOT able to do Graph window resizing.
		this.resetValuesFromProblem( parent.getProblem().getWindow() );

		finishInit(parent.getProblem().getWindow());
	}
	
	
	//This function adds 
	protected abstract void finishInit(DObject_AbstractWindow a);
	
	public void actionPerformed(ActionEvent e) {
		
		// BUG here: it doesn't update the visual window.
		// make sure our pointer to the problem is up to date.
		//subject= parent.getProblem().getGraphWindow();
		DObject_AbstractWindow subject= parent.getProblem().getWindow();
		
		IWPLog.debug(this, "actionPerformed: "+e.getActionCommand());
		if(e.getActionCommand().equals("Cancel")) {
			setVisible(false);
			//parent.killModifier();
			//IWPLog.debug(this,"[DObject_GraphWindo_Modifier] cancelled.");
		}else if(e.getActionCommand().equals("OK")) {
			try{
				
				IWPLog.debug(this, "updating window: X[" + subject.getMinX() + ":" + subject.getMaxX() + "] Y[" +
						subject.getMinY() + ":" + subject.getMaxY() +"]" );
				
				//IWPLog.debug(this,"[DObject_GraphWindo_Modifier] Okay'ing.");
				subject.setMinX(Double.parseDouble(minX.getText()));
				subject.setMaxX(Double.parseDouble(maxX.getText()));
				subject.setGridX(Double.parseDouble(gridX.getText()));
				subject.setMinY(Double.parseDouble(minY.getText()));
				subject.setMaxY(Double.parseDouble(maxY.getText()));
				subject.setGridY(Double.parseDouble(gridY.getText()));
				
				
				
				IWPLog.info(this, "updatED window: X[" + subject.getMinX() + ":" + subject.getMaxX() + "] Y[" +
						subject.getMinY() + ":" + subject.getMaxY() +"]" );
				
				
				//setVisible(false);
				//IWPLog.debug(this,"[DObject_GraphWindo_Modifier] okay'ed.");
				//parent.killModifier();
			}
			catch (Exception a) {
				JOptionPane.showMessageDialog(null,"Setting paramaters failed");
			}//finally {parent.killModifier();}
		}else if(e.getActionCommand().equals("Apply")) {
			try{
				
				IWPLog.debug(this, "updating window: X[" + subject.getMinX() + ":" + subject.getMaxX() + "] Y[" +
						subject.getMinY() + ":" + subject.getMaxY() +"]" );
				
				IWPLog.debug(this,"[DObject_GraphWindo_Modifier] apply'ing.");
				subject.setMinX(Double.parseDouble(minX.getText()));
				subject.setMaxX(Double.parseDouble(maxX.getText()));
				subject.setGridX(Double.parseDouble(gridX.getText()));
				subject.setMinY(Double.parseDouble(minY.getText()));
				subject.setMaxY(Double.parseDouble(maxY.getText()));
				
				subject.setGridY(Double.parseDouble(gridY.getText()));
				
				
				
				IWPLog.debug(this, "updatED window: X[" + subject.getMinX() + ":" + subject.getMaxX() + "] Y[" +
						subject.getMinY() + ":" + subject.getMaxY() +"]" );
				
				
			}
			catch (Exception a) {
				JOptionPane.showMessageDialog(null,"Setting paramaters failed");
			}//finally {parent.killModifier();}
		}
		
		// all actions should ask the parent to repaint themselves.
		parent.myRepaintAll();
		
	}
	
	
    /**
     * 2007-Sep-02 brockman. We need to set the values of the window when the problem is loaded.
     * This is called from GWindowAnimator.loadProblem.
     * @param problem
     */
    
    public void resetValuesFromProblem ( DObject_AbstractWindow window )
    {
    	maxX.setText( ""+window.getMaxX() );
    	minX.setText( ""+window.getMinX() );
    	maxY.setText( ""+window.getMaxY() );
    	minY.setText( ""+window.getMinY() );
    	gridX.setText( ""+window.getGridX() );
    	gridY.setText( ""+window.getGridY() );
    	this.invalidate();
    }

    
}
