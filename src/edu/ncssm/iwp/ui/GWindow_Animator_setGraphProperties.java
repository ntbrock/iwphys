package edu.ncssm.iwp.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import edu.ncssm.iwp.ui.GWindow_Animator_Graph;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.util.IWPLog;
import java.text.NumberFormat;

/**
 * This is the widget that sits at the bottom of the graph window, allowing you to adjust
 * the scale of the graph.
 * 
 * Hitting apply sets the values into the grapher itself.
 * 
 * @author brockman
 *
 */

public class GWindow_Animator_setGraphProperties extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	JTextField minX = new JTextField("",5);
    JTextField maxX = new JTextField("",5);
    JTextField minY = new JTextField("",5);
    JTextField maxY = new JTextField("",5);
    JTextField gridX = new JTextField("",5);
    JTextField gridY = new JTextField("",5);
    JButton apply = new JButton("Apply");
    
    GWindow_Animator_Graph parent;

    JPanel holder;

    
    public GWindow_Animator_setGraphProperties ( GWindow_Animator_Graph parent )
    {
    	this.parent = parent;
    	
    	_constructGui();
    
    }
    
					
    private void _constructGui()
    {
    	holder=new JPanel();
    	holder.setLayout(new GridLayout(4,5));    

    	holder.add(new JLabel("Graphing:"));
    	holder.add(new JLabel(" "));		
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel("Time max: "));
    	holder.add(maxX);
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel("PVA max: "));
    	holder.add(maxY);
    	holder.add(new JLabel("Time min: "));
    	holder.add(minX);
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel("PVA min: "));
    	holder.add(minY);
    	holder.add(new JLabel("Time grid: "));
    	holder.add(gridX);
    	holder.add(new JLabel(" "));
    	holder.add(new JLabel("PVA grid: "));
    	holder.add(gridY);
	
    	JPanel buttonPanel = new JPanel();
    	
    	buttonPanel.add(apply);
	
    	setLayout(new BorderLayout());
    	add(BorderLayout.NORTH,holder);
    	add(BorderLayout.SOUTH,buttonPanel);
	
    	apply.addActionListener(this);
    }
        
    public void actionPerformed(ActionEvent e) {

    	IWPLog.debug("actionPerformed: " +e.getActionCommand());
    	
    	if ( e.getSource() == apply ) {

    		parent.getGGraph().updateGraphWindowObjectAndTick ( 
    				Double.parseDouble(minX.getText()),
    				Double.parseDouble(maxX.getText()),
    				Double.parseDouble(minY.getText()),
    				Double.parseDouble(maxY.getText()),
    				Double.parseDouble(gridX.getText()),
    				Double.parseDouble(gridY.getText()) );

    		// Reduces user control, but I kinda like this. brockman 2006-Aug-28
    		//subject.setGridX((subject.getMaxX() - subject.getMinX())/10);
    		//subject.setGridY((subject.getMaxY() - subject.getMinY())/10);
    		
    		// gridX.setText(""+subject.getGridX());
    		// gridY.setText(""+subject.getGridY());
        }
    }
    
    
    public void zero ( DProblem problem, DProblemState state )
    {
    	// load the graph window basics from the 
    	NumberFormat format = NumberFormat.getNumberInstance();
    	
    	minX.setText(format.format(problem.getGraphWindowObject().getMinX()));
    	minY.setText(format.format(problem.getGraphWindowObject().getMinY()));
    	maxX.setText(format.format(problem.getGraphWindowObject().getMaxX()));
    	maxY.setText(format.format(problem.getGraphWindowObject().getMaxY()));
    	gridX.setText(format.format(problem.getGraphWindowObject().getGridX()));
    	gridY.setText(format.format(problem.getGraphWindowObject().getGridY()));
    }
    
}
