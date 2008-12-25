// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.objects;

import edu.ncssm.iwp.plugin.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The designer for editing description text.
 * @author brockman
 *
 */


public class DObject_Description_designer extends DObject_designer
{

    DObject_Description object; /* For the novelty */


    public DObject_Description_designer ( DObject_Description iobject )
    {
        object = iobject;

        buildGui();
        setVisible(true);
    }


    JPanel editor = new JPanel();

    JEditorPane editorPane = new JEditorPane();

    JTextArea textArea = new JTextArea();

    public void buildGui()
    {

        //editorPane.setEditable(true);
        textArea.setEditable(true);

        String txt = object.getText();
        if (txt == null) { txt = new String(); }

        //editorPane.setText(txt);
        textArea.setText(txt);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
                
        //JScrollPane editorGlove = new JScrollPane ( editorPane );
        JScrollPane editorGlove = new JScrollPane ( textArea );
        editorGlove.setPreferredSize ( new Dimension ( WIDTH, 400 ));
        editorGlove.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        editorGlove.setBorder(new LineBorder(Color.BLACK));

        
        JPanel spacerPanel = new JPanel();
        spacerPanel.setBorder(new EmptyBorder(10,10,10,10));
        spacerPanel.setLayout(new BorderLayout() );
        spacerPanel.add(BorderLayout.CENTER, editorGlove);

        buildEasyGui ( "Description", Color.BLACK, Color.YELLOW, spacerPanel );

    }

    //add a bunch of event handlers to change the object when they trigger

    /*--------------------------------------------------------------------*/
    /* Accessors */
    /*--------------------------------------------------------------------*/

    public String getName ( ) { return object.getName ( ); }


    //object (g/s)
    public DObject_Description getObject() {
        return object;
    }


    public void setObject(DObject_Description iObject) { object = iObject; }

    public void write()
    {
        object.setText(editorPane.getText());
    }

    public IWPObject buildObjectFromDesigner() {
        DObject_Description goingBack = new DObject_Description();
        //goingBack.setText(editorPane.getText());
        goingBack.setText(textArea.getText());
        return goingBack;
    }

}


