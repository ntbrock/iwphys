package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.objects.DObject_Description;
import edu.ncssm.iwp.objects.DObject_DescriptionXMLHandler;
import edu.ncssm.iwp.objects.DObject_GraphWindow;
import edu.ncssm.iwp.objects.DObject_GraphWindowXMLHandler;
import edu.ncssm.iwp.objects.DObject_Input;
import edu.ncssm.iwp.objects.DObject_InputXMLHandler;
import edu.ncssm.iwp.objects.DObject_Output;
import edu.ncssm.iwp.objects.DObject_OutputXMLHandler;
import edu.ncssm.iwp.objects.DObject_Solid;
import edu.ncssm.iwp.objects.DObject_SolidXMLHandler;
import edu.ncssm.iwp.objects.DObject_Time;
import edu.ncssm.iwp.objects.DObject_TimeXMLHandler;
import edu.ncssm.iwp.objects.DObject_Window;
import edu.ncssm.iwp.objects.DObject_WindowXMLHandler;
import edu.ncssm.iwp.plugin.*;

import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import edu.ncssm.iwp.util.*;
import java.lang.reflect.*;

/**
 * This is the XML Handler that processes everything inside the 'objects' tag.
 * Control is passed down from the DProblemXMLHandler.
 * We are augmenting this in iwp 3 to handle the generic <object class="edu.ncssm..">
 * 
 *  
 * @author brockman
 *
 */
public class DProblemObjectsXMLHandler extends IWPDefaultXmlHandler
{

	IWPDefaultXmlHandler parent;
	SAXParser parser;
	ArrayList objects;


	DObject_WindowXMLHandler xmlWindow = new DObject_WindowXMLHandler();
    DObject_GraphWindowXMLHandler xmlGraphWindow = new DObject_GraphWindowXMLHandler();
	DObject_DescriptionXMLHandler xmlDescription =
		new DObject_DescriptionXMLHandler();
	DObject_TimeXMLHandler xmlTime = new DObject_TimeXMLHandler();

 	DObject_SolidXMLHandler xmlSolid = new DObject_SolidXMLHandler();
	 DObject_InputXMLHandler xmlInput= new DObject_InputXMLHandler();
	DObject_OutputXMLHandler xmlOutput = new DObject_OutputXMLHandler();

	/*--------------------------------------------------------------------*/

	public void collectObjects ( SAXParser parser, IWPDefaultXmlHandler parent,
							 ArrayList objects )
		throws SAXException
	{
		this.objects = objects;
		this.parent = parent;
		this.parser = parser;
		parser.getXMLReader().setContentHandler ( this );
	}


	/*--------------------------------------------------------------------*/

	public void startElement ( String namespaceURI, String localName,
			   String qName, Attributes attr )
		throws SAXException
	{
		resetContents();

		if ( qName.equals("window") ) {

			DObject_Window object = new DObject_Window();
			objects.add ( object );
			xmlWindow.collectObject ( parser, this, object );

		}else if (qName.equals("GraphWindow")) {
		    DObject_GraphWindow object = new DObject_GraphWindow();
		    objects.add(object);
		    xmlGraphWindow.collectObject(parser,this,object);

		} else if ( qName.equals("description") ) { 

			DObject_Description object = new DObject_Description();
			objects.add ( object );
			xmlDescription.collectObject ( parser, this, object );

 		} else if ( qName.equals("time") ) { 

			DObject_Time object = new DObject_Time();
			objects.add ( object );
			xmlTime.collectObject ( parser, this, object );

		} else if ( qName.equals("solid") ) { 

			DObject_Solid object = new DObject_Solid();
			objects.add ( object );
			xmlSolid.collectObject ( parser, this, object );

		} else if ( qName.equals("input") ) { 

			DObject_Input object = new DObject_Input();
			objects.add ( object );
			xmlInput.collectObject ( parser, this, object );

		} else if ( qName.equals("output") ) { 

			DObject_Output object = new DObject_Output();
			objects.add ( object );
			xmlOutput.collectObject ( parser, this, object );

		} else if ( qName.equals("object") ) {
			
			// IWP 3
			// New, generic object handler.
			
			
			String className = attr.getValue("class");
			
			// instantiate an instance of the class,
			// and get the XMLHanlder from it, 
			// and then delegate control to it.
			
			
			try { 
				Class objectClass = this.getClass().getClassLoader().loadClass(className);
			
				// BUGBUG: Check to make sure that the class has the newXmlCreator.				
				// Require a null constructor.
				Constructor construct = objectClass.getConstructor(new Class[0]);
			
				// new isntance, no arguemnts.
				Object objectInstance = construct.newInstance(new Object[0]);
				
				if ( objectInstance instanceof IWPXmlable ) {
					
					IWPObjectXmlHandler xmlHandler = 
						((IWPXmlable)objectInstance).newXmlObjectHandler();
					
					// Handing control to my sub instance.
					objects.add(objectInstance);
					xmlHandler.collectObject(parser, this, (IWPXmlable)objectInstance);
					
					
				} else { 
					IWPLog.error("Dynamic Class was not instance of DObjectXmlWorker. Check code!");
				}
				
				
			} catch ( Exception x ) { 
				IWPLog.x("Dynamic Class Construction Exception", x );
				throw new SAXException(x);
			}
			
			
			
			
		} else { 
			IWPLog.error(this,"[XMLHandler] Unknown tag: " + qName );
		}

	}


	public void endElement(String namespaceURI, String localName,
			String qName) throws SAXException
	{

		if ( qName.equals("objects") ) {
			parser.getXMLReader().setContentHandler ( parent );
		}

	}

	/*--------------------------------------------------------------------*/

}

