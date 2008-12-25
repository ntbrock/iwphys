package edu.ncssm.iwp.ui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;


import edu.ncssm.iwp.ui.GWindow_Animator_Graph;
import edu.ncssm.iwp.objects.DObject_GraphWindow;
import edu.ncssm.iwp.util.IWPLog;

public class GWindow_Animator_Graph_setProperties extends JFrame
    implements ActionListener{

    JTextField minX;
    JTextField maxX;
    JTextField minY;
    JTextField maxY;
    JTextField gridX;
    JTextField gridY;
    JButton ok;
    JButton cancel;
    JButton apply;
    DObject_GraphWindow subject;
    GWindow_Animator_Graph parent;

    public GWindow_Animator_Graph_setProperties ( DObject_GraphWindow in, GWindow_Animator_Graph iparent ) {
    subject=in;
    parent=iparent;

    JPanel holder=new JPanel();
    holder.setLayout(new GridLayout(4,4));
    minX=new JTextField(""+subject.getMinX(),8);
    maxX=new JTextField(""+subject.getMaxX(),8);

    minY=new JTextField(""+subject.getMinY(),8);
    maxY=new JTextField(""+subject.getMaxY(),8);

    gridX=new JTextField(""+subject.getGridX(),8);
    gridY=new JTextField(""+subject.getGridY(),8);

    ok=new JButton("OK");
    ok.addActionListener(this);
    cancel=new JButton("Cancel");
    cancel.addActionListener(this);
    apply=new JButton("Apply");
    apply.addActionListener(this);

    holder.add(new JLabel("min time: "));
    holder.add(minX);
    holder.add(new JLabel("max time: "));
    holder.add(maxX);
    holder.add(new JLabel("Y Min: "));
    holder.add(minY);
    holder.add(new JLabel("Y Max: "));
    holder.add(maxY);
    holder.add(new JLabel("grid time: "));
    holder.add(gridX);
    holder.add(new JLabel("Y Grid: "));
    holder.add(gridY);
    holder.add(ok);
    holder.add(apply);
    holder.add(cancel);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(BorderLayout.NORTH,holder);
    setSize(300,200);
    setResizable(false);
    setVisible(true);
    requestFocus();

    }
    public void actionPerformed(ActionEvent e) {
    
    if(e.getActionCommand().equals("Cancel")) {
        setVisible(false);
    
    
    }else if(e.getActionCommand().equals("OK")) {
        try{
    
        subject.setMinX(Double.parseDouble(minX.getText()));
        subject.setMaxX(Double.parseDouble(maxX.getText()));
        subject.setMinY(Double.parseDouble(minY.getText()));
        subject.setMaxY(Double.parseDouble(maxY.getText()));
        subject.setGridX(Double.parseDouble(gridX.getText()));
        subject.setGridY(Double.parseDouble(gridY.getText()));
        parent.tick(parent.getAnimator().getProblemState());
        setVisible(false);
       
       
        }
        catch (Exception a) {
        JOptionPane.showMessageDialog(null,"Setting paramaters failed");
        }
    }else if(e.getActionCommand().equals("Apply")) {
        try{
       
        subject.setMinX(Double.parseDouble(minX.getText()));
        subject.setMaxX(Double.parseDouble(maxX.getText()));
        subject.setMinY(Double.parseDouble(minY.getText()));
        subject.setMaxY(Double.parseDouble(maxY.getText()));
        subject.setGridX(Double.parseDouble(gridX.getText()));
        subject.setGridY(Double.parseDouble(gridY.getText()));
        parent.tick(parent.getAnimator().getProblemState());
        IWPLog.info("[DObject_GraphWindo_Modifier] apply'ed.");
        }
        catch (Exception a) {
        JOptionPane.showMessageDialog(null,"Setting paramaters failed");
        }//finally {parent.killModifier();}
    }
    }
}
