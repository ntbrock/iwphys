/*
  TEMPLATE FILE

  This is a template flie that is a gui harness for DObject testing
  Replace Time w/ the specific object naming type
*/

package edu.ncssm.iwp.objects;

import javax.swing.*;
import edu.ncssm.iwp.util.IWPLog;
import java.awt.event.*;

public class TEST_DObject_Time_designer extends JPanel implements ActionListener
{

    DObject_Time oObject;
    DObject_designer oDesigner;
    JButton oGetButton;

    public TEST_DObject_Time_designer ( )
    {

        oObject = new DObject_Time (  );
        oDesigner = (DObject_designer) oObject.getDesigner (  );

        oGetButton = new JButton ( "DObject_Time_designer.get ( )" );
        oGetButton.addActionListener ( this );

        //setLayout ( new BorderLayout ( ) );
        //add ( BorderLayout.CENTER, oDesigner );
        //add ( BorderLayout.SOUTH, oGetButton );
        add ( oDesigner );
    }


    /* INTERFACE: ActionListener */
    public void actionPerformed ( ActionEvent e )
    {
        IWPLog.out ( "[TEST_DObject_Time_designer] get button pressed" );

        try {
            DObject_Time oGetObject = (DObject_Time) oDesigner.buildObjectFromDesigner ( );
            IWPLog.out ( "[TEST_DObject_Time_designer] return: "+ oGetObject );
        } catch ( Exception x ) {
            IWPLog.x (this, "Generic Exception", x );
        }


    }

}







