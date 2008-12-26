/*
  DProblem
  The object that reprensents problems.
  This guy is VERY important.

  Author: Taylor Brockman
  Date: 6/10/00
*/


package edu.ncssm.iwp.problemdb;

import edu.ncssm.iwp.exceptions.CircularDependencyException;
import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.objects.*;
import edu.ncssm.iwp.plugin.IWPCalculated;
import edu.ncssm.iwp.plugin.IWPObject;

import java.util.*;

import edu.ncssm.iwp.util.*;

public class DProblem extends DEntity
{

    public String username;
    public String problemName;
    public String filename = null; // a good default filename.
    public int accessMode;

    public Vector objectsInDrawOrder;
    public Vector objectsInTickOrder;
    

    public DProblemVariables variables;


    /* the 'essential' objects */
    DAuthor author;
    DObject_Time time;
    DObject_Window window;
    DObject_Description description;
    DObject_GraphWindow graphWindow;

    /*--------------------------------------------------------------------*/

    public DProblem ( String username, String filename )
    {
        this.username = username;
        this.filename = filename;
        create ( );
    }


    public DProblem ( )
    {
        create ( );
    }

    void create ( ) {

        objectsInDrawOrder = new Vector(15);
        
        author = new DAuthor();

        /* Set the problem defaults right here */

        /* add the time object */
        time = new DObject_Time ();

        /* add the window object */
        window = new DObject_Window();
        graphWindow = new DObject_GraphWindow();

        /* add the description Object */
        description = new DObject_Description();

    }

    /*--------------------------------------------------------------------*/

    public String getUsername ( ) { return author.getUsername( ); }
    public void setUsername ( String username )
    {
        author.setUsername ( username );
    }

    /** 
     * Used by the save dialog.
     * @return
     */
    public boolean hasValidFilename() {
    	if ( filename == null ) { 
    		return false;
    	} else { 
    		return true;
    	}
    }
    public String getFilename ( ) { return filename; }
    public void setFilename ( String filename )
    {
        this.filename = filename;
    }


    public int getAccessMode ( ) { return accessMode; }
    public void setAccessMode ( int accessMode )
    {
        this.accessMode = accessMode;
    }



    public DAuthor getAuthor ( ) { return author; }
    public void setAuthor ( DAuthor author ) {
        this.author = author;
    }


    public DObject_Time getTimeObject ( ) { return time; }
    public DObject_Time getTime ( ) { return time; }
    public void setTime ( DObject_Time time )
    {
        this.time = time;
    }

    public DObject_Description getDescriptionObject ( ) { return description; }
    public DObject_Description getDescription ( ) { return description; }
    public void setDescription ( DObject_Description description )
    {
        this.description = description;
    }

    public DObject_Window getWindowObject ( ) { return window; }
    public DObject_Window getWindow ( ) { return window; }
    public void setWindow ( DObject_Window window )
    {
        this.window = window;
    }


    public DObject_GraphWindow getGraphWindowObject ( ) { return graphWindow; }
    public DObject_GraphWindow getGraphWindow ( ) { return graphWindow; }
    public void setGraphWindow ( DObject_GraphWindow window )
    {
        this.graphWindow = window;
    }

    public DProblemMeta getMeta ( )
    {
        return new DProblemMeta ( username, filename );
    }


    

    public void addObject ( IWPObject iObject )
    {

        if ( iObject instanceof DObject_Description ) {
            setDescription ( (DObject_Description) iObject );
        } else if ( iObject instanceof DObject_Time ) {
            setTime ( (DObject_Time) iObject );
        } else if ( iObject instanceof DObject_Window ) {
            setWindow ( (DObject_Window) iObject );
        } else if ( iObject instanceof DObject_GraphWindow) {
        	setGraphWindow ( (DObject_GraphWindow) iObject);
        } else {
            objectsInDrawOrder.add ( iObject );
        }
    }

    public void addObjects ( Collection objects )
    {
        Iterator i = objects.iterator();

        
        while ( i.hasNext() ) {
        	IWPObject obj = (IWPObject) i.next();
            addObject ( obj );
        }

    }

    /*--------------------------------------------------------------------*/

    public Collection getInputObjects ( )
    {
        Collection out = (Collection) new ArrayList();

        Iterator i = ((Collection)objectsInDrawOrder).iterator();
        while ( i.hasNext() ) {

            Object o = (Object) i.next();
            if ( o instanceof DObject_Input ) {
                out.add ( o );
            }
        }

        return out;
    }


    public Collection getOutputObjects ( )
    {
        Collection out = (Collection) new ArrayList();

        Iterator i = ((Collection)objectsInDrawOrder).iterator();
        while ( i.hasNext() ) {

        	Object o = (Object) i.next();
            if ( o instanceof DObject_Output ) {
               out.add ( o );
            }
        }

        return out;
    }


    public Collection getSolidObjects ( )
    {
        Collection out = (Collection) new ArrayList();

        Iterator i = ((Collection)objectsInDrawOrder).iterator();
        IWPLog.info(this,"[DProblem]");
        while ( i.hasNext() ) {
        	Object o = (Object) i.next();
            if ( o instanceof DObject_Solid ) {
                out.add ( o );
            }
        }

        return out;
    }

    // Problems do not zero or tick any more,
    // That is all handled by the DProblemState now.
  
    /*----------------------------------------------------------------*/
    /* Object operation interface */

    /**
     * Returns the problem objects ordered by how they should draw. This is 
     * the order in the file + the designer left window.
     */
    public Collection getObjectsForDrawing ( )
    {

        Collection fullList = (Collection) new ArrayList();

        fullList.add ( time );
        fullList.add ( graphWindow );
        fullList.add ( window );
        fullList.add ( description );
        fullList.addAll ( objectsInDrawOrder );
	
        return fullList;
    }

    /**
     * This returns the objects ordered by how they should tick. This is the 
     * recalculation of order that has been added as of IWP3
     * @author brockman
     * @return
     */
    
    public Collection getObjectsForTicking ( )
    {

        Collection fullList = (Collection) new ArrayList();

        fullList.add ( time );
        fullList.add ( graphWindow );
        fullList.add ( window );
        fullList.add ( description );

        // Circular Dependency can cause a null pointer to be thrown here.
        if ( objectsInTickOrder != null ) { 
        	fullList.addAll ( objectsInTickOrder );
        }

        return fullList;
    }

    
    
    
    public void removeObject ( IWPObject iObject )
    {
        objectsInDrawOrder.remove( iObject );
        objectsInTickOrder.remove( iObject );
        
    }

    /**
     * The required objects in each problem.
     * 
     * TODO: Move this to the IWPObject interface.
     * 
     * @param object
     * @return
     */
    private boolean isObjectEssential (IWPObject object )
    {
    	return object instanceof DObject_Time || 
    			object instanceof DObject_Window ||
    			object instanceof DObject_GraphWindow ||
    			object instanceof DObject_Description;
    }
    
    
    
    public boolean moveObjectUp ( IWPObject iObject )
    {
        /* can't move the big guys around */
        if ( isObjectEssential ( iObject ) ) { return false; }


        /* find the item in the Vector ... */
        int nIndex = objectsInDrawOrder.indexOf ( (Object) iObject );

        if ( nIndex <= 0 ) {
            IWPLog.info(this,"[DProblem.moveObjectUp] It's at the top already!");
            return false;
        } else {
            objectsInDrawOrder.insertElementAt ( objectsInDrawOrder.remove ( nIndex ), nIndex - 1 );
            return true;
        }
    }


    public boolean moveObjectDown ( IWPObject iObject )
    {
    	/* can't move the big guys around */
        if ( isObjectEssential ( iObject ) ) { return false; }

        int nIndex = objectsInDrawOrder.indexOf ( (Object) iObject );

        System.err.println ("[DProblem.moveObjectDown] nIndex = "+nIndex+"  Size = "+objectsInDrawOrder.size());

        if ( nIndex >= objectsInDrawOrder.size() - 1  ) {
            IWPLog.info(this,"[DProblem.moveObjectDown] It's at the bottom already!");
            return false;
        } else {
        	objectsInDrawOrder.insertElementAt ( objectsInDrawOrder.remove ( nIndex ), nIndex + 1 );
            return true;
        }

    }



    /*--------------------------------------------------------------------------*/

    public String toString ( )
    {
        StringBuffer sb = new StringBuffer ( );

        sb.append("---------------------------------------------------------------" +"\n");
        sb.append("Problem");
        sb.append("Username: "+ username +"\n" );
        sb.append("Filename: "+ filename +"\n");
        sb.append("---------------------------------------------------------------" +"\n");
        sb.append("[objects] count = "+ objectsInDrawOrder.size() );

        for ( int i=0; i < objectsInDrawOrder.size(); i++ ) {
            Object oCurrentObj = (Object) objectsInDrawOrder.get( i );
            sb.append( oCurrentObj.toString() + "\n" );
        }
        sb.append("---------------------------------------------------------------" +"\n");

        return sb.toString();
    }

    public void print ( )
    {
        System.err.println ( toString ( ) );
    }

    /*-----------------------------------------------------------------------*/
    public DProblem_designer getDesigner ( )
    {
        return new DProblem_designer ( this );
    }

    /*-----------------------------------------------------------------------*/

    

    //---------------------------------------------------------------
    /**
     * IWP 3 - on initial zero of the problem, re-order the obejcts in the
     * problem based on their variable dependency.
     * 2007-Jan-29 brockman
     */
    
    public synchronized void reorderProblemObjectsBySymbolicDependency ( )
    	throws UnknownVariableException, CircularDependencyException, InvalidEquationException
    {
    	// thiS methodlooks at the IWPCalculated interface
    	// and re-orders the objects based on their dependency.
    	
    	// ALSO TODO: make the Mvariables by default just include the current frame of data.
    	// because now I don't need to look back any further than NOW. cool.
    	IWPLog.debug(this, "Starting to order " + objectsInDrawOrder.size() + " objects");
    	
    	Collection objectsToAnalyze = this.getObjectsForDrawing(); // This adds the full lsit of objects. Internally,
    	// they split up for efficiency.
    	
    	Collection objectsForTheEnd = new ArrayList();
    	Collection objectsForTheMiddle = new ArrayList();
    	Collection orderingCandidates = new ArrayList();
    	
    	Hashtable requiresCache = new Hashtable();
    	Hashtable providesCache = new Hashtable();

    	
    	// This is my attempt at an algorithim to solve the requires provides problem.
    	// I am going to find the objects that are candidates, and iterate over that list,
    	// looking for objects that have had their requirements satisfied, and if they are 
    	// satisfied, pull them out of the candidate array, and move them to the end of the
    	// objectsForMiddle.
    	
    	for ( Iterator i = objectsToAnalyze.iterator(); i.hasNext(); ) {
    		Object o = (Object) i.next();
    		if ( ! ( o instanceof IWPCalculated ) ) {
    			//IWPLog.error(this, "ERROR Object Class: " + o.getClass().getName() + " is not a type of IWPCalculated. Should push to end?");
    			objectsForTheEnd.add(o);
    			continue;
    		}    		
    		
    		IWPCalculated objectCalc = (IWPCalculated) o;
    		
    		// Go ahead and cache the provides - even if it doesnt' depend on anything.
    		Collection providedSymbols = objectCalc.getProvidedSymbols();
    		if ( providedSymbols != null ) {
    			providesCache.put(objectCalc,providedSymbols);
    		} else {
    			providesCache.put(objectCalc, new ArrayList(0) ); // cache an empty array to avoid nulls below/
    		}
	
    		// if it has a null or empty requires, go ahead and put it into the objectsForMiddle,
    		// otherwise cache the results
    		Collection requiredSymbols = objectCalc.getRequiredSymbols();
    		//_safePrint("ProvidedSymbols by " + ((DObject)objectCalc).name, providedSymbols);
    		//_safePrint("RequiredSymbols for " + ((DObject)objectCalc).name, requiredSymbols);
    		
    		if ( requiredSymbols == null ||
    			  requiredSymbols.size() == 0 ) { 
    			objectsForTheMiddle.add(objectCalc);
    			requiresCache.put(objectCalc, new ArrayList(0) ); // cache an empty array to avoid nulls below.
    		} else {
    			orderingCandidates.add(objectCalc);
    			requiresCache.put(objectCalc, requiredSymbols );
    		}
    		   
    	}
    	
    	// Here is where I pull the guys out one by one if their dependenci9es have been 
    	// satisfied. INtensive, but we only do once at the beginning of a problem.
    	
    	int lastCandidateCount = -1;
    	int loopCount = 0;
    	
    	while ( orderingCandidates.size() > 0 ) {

    		Collection nextOrderingCandidates = new ArrayList(orderingCandidates.size());
    		Collection missingVariables = new ArrayList(15);
    		
    		IWPLog.debug(this, "orderingCandidates.size: " + orderingCandidates.size() + "  Loop Count: " + loopCount++  );
    		
    		for ( Iterator i = orderingCandidates.iterator(); i.hasNext() ; ) { 
        		
    			IWPCalculated calcObject = (IWPCalculated) i.next();
    			
    			
    			// have all of my dependencies been satisfied by what's areadly in objectsForMIddle?
    			boolean allRequirementsSatisfied = true;
    			Collection thisObjectRequires = ((Collection)requiresCache.get(calcObject));

    			for ( Iterator j = thisObjectRequires.iterator(); j.hasNext() && allRequirementsSatisfied; ){
    				boolean requirementSatisfied = false;
    				String requiresSymbol = (String) j.next();
    				
    				for ( Iterator k = objectsForTheMiddle.iterator(); k.hasNext() && ! requirementSatisfied; ) { 
    					IWPCalculated alreadyPushedObject = (IWPCalculated) k.next();
    					
    					// does this guy provide the symbol already?
    					for ( Iterator l = ((Collection)providesCache.get(alreadyPushedObject)).iterator();
    						l.hasNext() && ! requirementSatisfied; ){
    						String providedSymbol = (String) l.next();
    						
    						if ( requiresSymbol.equals(providedSymbol) ) { 
    							requirementSatisfied = true;
    						}
    					}
    					
    				}
    				
    				// 2007-Jun-04 brockman - Self-dependency smarts.
    				// The object can also reference itself. The implemetnation of this lives down in the calculator.
    				if ( ! requirementSatisfied ) { 
    					for ( Iterator k = calcObject.getProvidedSymbols().iterator(); k.hasNext() && ! requirementSatisfied; ) {
    						String providedSymbol = (String) k.next();
    						if ( requiresSymbol.equals(providedSymbol) ) { 
    							IWPLog.info(this,"Object self-provided it's own referenced variable: " + requiresSymbol);
    							requirementSatisfied = true;
    						}
    					}
    				}
    				
    				// Ok, we've looked back , and if we found one, then requirementSatisfied became true,.
    				// and we can move the object to the 'good' array here.
    				if ( ! requirementSatisfied ) {
    					allRequirementsSatisfied = false;
    					
    					// Also keep track of the variable + what it's missing so we can report 
    					IWPObject object = (IWPObject) calcObject;
    					missingVariables.add(object.getName()+ " requires '" + requiresSymbol + "'");
    				}
    				
    			}

				
				if ( allRequirementsSatisfied ) { 
					objectsForTheMiddle.add(calcObject);
				} else {
					nextOrderingCandidates.add(calcObject);
				}

    			
    		} // outside of teh ordering candidates iterator.
   		
    		orderingCandidates = nextOrderingCandidates;
    		
    		// If we make it through an interation of the loop and don't remove any, then we might have
    		// a circular.
    		if ( lastCandidateCount == orderingCandidates.size() ) { 
    			// This could also be an unknown variable exception. I report back the missing names.
    			StringBuffer missingText = new StringBuffer("\n");
    			for ( Iterator m = missingVariables.iterator(); m.hasNext(); ) { 
    				missingText.append(m.next().toString());
    				if ( m.hasNext() ) { missingText.append("\n"); }; // yeah, it's cheating - but \n works.
    			}
    			throw new CircularDependencyException("Missing Variable or Circular Dependecy Detected: " + missingText );
    		} else {
    			lastCandidateCount = orderingCandidates.size();
    		}
    	}
    	
    	
    	// Reset the internal object vectorset.
    	
    	Collection orderedObjects = new Vector(objectsToAnalyze.size() );
    	orderedObjects.addAll(objectsForTheMiddle);
    	orderedObjects.addAll(objectsForTheEnd);    	
    	this.fullOrderedObjectReset(orderedObjects);
    	
    	IWPLog.debug(this, "Done ordering " + objectsInDrawOrder.size() + " objects");    	
    }


    /**
     * IWP3 - Allowing the reordering of the objects on problem load.
     * 
     * @param newObjects
     */
    private void fullOrderedObjectReset ( Collection newObjects )
    {
    	// This is the big reset for tick order.
    	this.objectsInTickOrder = new Vector();
    	for ( Iterator i = newObjects.iterator(); i.hasNext(); ) { 

    		IWPObject object = (IWPObject) i.next();
			//System.err.println("[DProblem] ticking objecT: " + object.getName() ) ;
    		
    		// Instead of adding via addObject, add via objectsInTickOrder.
    		this.objectsInTickOrder.add(object);
    	}
    }


    /*
    private void _safePrint(String str, Collection col)
    {
    	StringBuffer sb = new StringBuffer( str + ": " );
    	if ( col != null ) { 
    		for (Iterator i = col.iterator(); i.hasNext(); ) { 
    			sb.append(i.next()+", "); 
    		}
    	}
    	IWPLog.info(this, sb.toString());
    }
    */
}








