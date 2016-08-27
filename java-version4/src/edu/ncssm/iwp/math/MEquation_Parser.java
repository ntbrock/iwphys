// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
//
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.math;

import edu.ncssm.iwp.exceptions.*;
import edu.ncssm.iwp.util.*;

//the happy interface class to MathBranch

import java.util.*;

/**
 * This was one of the first pieces of IWP - an equation parser, written in 1999.
 * 
 * In IWP3, I am making this a bit smarter in that it can report up the
 * variables that it depends on, whether they be constants or not.
 * 
 * @author brockman
 *
 */


public class MEquation_Parser
{
	public static final String ERROR_EQUATION_IS_NULL = "Equation is null";
	public static final String ERROR_EQUATION_IS_EMPTY = "Equation is empty";
	public static final String ERROR_EQUATION_HAS_MIS_MATCHED_PARENTHESIS = "Equation has mis-matched parenthesis";

	public static final boolean UNRECKD_VARIABLES_CREATE_ERRORLOG = false;
	
	
    private MEquation_Parser_Branch toplevel;

    public MEquation_Parser ( String eqn )
        throws InvalidEquationException
    {
        checkBasicValidity(eqn);
        eqn = prepEquation(eqn);
        toplevel = new MEquation_Parser_Branch(eqn);
    }


    //should I put some sort of nifty variable interface in here?
    //add 'em to a hash or a vector?

    /* Attention: The Objects in the hash double be Doubles */

    public double calculate( MVariables iVariableHash )
    	throws InvalidEquationException, UnknownVariableException
    {
        return toplevel.calculate( iVariableHash );
    }

    


    private int countChars(int ch, String in)
    {

        int count = 0;
        int index;

        index = in.indexOf(ch);

        if ( index > -1 ) {
            while ( index > -1 ) {
                count++;
                index = in.indexOf(ch, index+1);
            }
            return count;
        } else {
            return 0;
        }
    }



    /**
     * Check the validity of an equation. Is it null, >0 length + have
     * matching parenthesis?
     *
     * @param eqn The text of the equation
     */

    private void checkBasicValidity ( String eqn )
        throws InvalidEquationException
    {

        //does it exist?
        if ( eqn == null  ) {
            throw new InvalidEquationException(ERROR_EQUATION_IS_NULL);
        }

        if ( eqn.length() <= 0 ) {
            throw new InvalidEquationException(ERROR_EQUATION_IS_EMPTY);
        }

        //balanced () ?
        if ( countChars('(',eqn) != countChars(')',eqn) ) {
            throw new InvalidEquationException(ERROR_EQUATION_HAS_MIS_MATCHED_PARENTHESIS);
        }
        
    }


    private String removeSpaces ( String eqn )
    {

        int lc, c;
        String newEqn = new String();
        // take out all the spaces //

        // IWPLog.debug("MEqation_Parser:removeSpaces] Old String: "+eqn );

        lc = 0;
        while (( c = eqn.indexOf ( ' ', lc )) >= 0 ) {

            //IWPLog.debug("MEqation_Parser:removeSpaces] concat ("+lc+","+c+"): "+newEqn );

            newEqn = newEqn.concat ( eqn.substring ( lc, c ));
            lc = c + 1;

        }

        newEqn = newEqn.concat ( eqn.substring (lc) );

        //IWPLog.debug("MEqation_Parser:removeSpaces] New String: "+newEqn );

        return newEqn;
    }




    public String prepEquation(String eqn)
    {

        String newEqn = removeSpaces ( eqn );

        //what about )( => )*(  ??
        //this workds... just punch in a *
        int divider = newEqn.indexOf(")(");
        while (divider != -1) {
            divider++;
            newEqn = newEqn.substring(0,divider)+"*"+newEqn.substring(divider,newEqn.length());
            divider = newEqn.indexOf(")(",divider);
        }
        
        
        /*
        // now properly handled in MConstants.
        divider = newEqn.indexOf("PI.value");
        while(divider!=-1) {
            newEqn=newEqn.substring(0,divider)+Math.PI+newEqn.substring(divider+8,newEqn.length());
            divider=newEqn.indexOf("PI.value",divider);
            IWPLog.info(this,newEqn);
        }
        */

        // brian sweeney's idea
        return replaceMinusWithPlusMinus ( newEqn );
    }


    // 2005-Mar-06 brockman
    public static String replaceMinusWithPlusMinus ( String eqn )
    {
        // 1-1 =>  1+-1

        StringBuffer out = new StringBuffer(eqn.length() + 10 );

        char prevChar = 0;

        // iterate over eqn
        for ( int i = 0; i < eqn.length(); i++ ) {

            char thisChar = eqn.charAt(i);

            if ( thisChar != '-' ) {
                out.append ( thisChar );

            } else if ( thisChar == '-' ) {
                // find all situations where char = -

                if ( prevChar == '+' ||  // bad cases where things would start w/ +
                     prevChar == '(' ||
                     i == 0 ) {
                    out.append ( "-" );
                } else {
                    // if prevChar != '+'
                    // output +- instead of just -
                    out.append ( "+-" );
                }
            }

            prevChar = thisChar;
        }

        return out.toString();
    }


    /**
     * IWP3 - Now I can report back up what the variabels for this equation are.
     * brockman 2007-Jan-29
     * Here, symbols are non-number, non-operator nodes in the equation.
     * 
     */
    
    public Collection listRequiredVariables()
    {
    	ArrayList out = new ArrayList(100);
    	toplevel.listSymbols( out );
    	return out;
    }

    
}










class MEquation_Parser_Branch
{

	public double nowValue;
    public double stableValue;


    public boolean nodeIsFunction = false;
    public boolean nodeIsVariable = false;

    public String value;
    public MEquation_Parser_Branch left;
    public MEquation_Parser_Branch right;



    int[] operators = {'+','-','*','/','^','%'};
    String[] str_operators = {"+","-","*","/","^","%"};



    public MEquation_Parser_Branch(String eqn)
    	throws InvalidEquationException
    {
        boolean[] valid = getValidArray(eqn);

        //rip out the outer parenthesis
        while ( !isValidArray(valid) ) {

            //parenthesis on outside (second... order DOES matteR)
            while ( // NOte: bug here where functions have no arguments.
                   eqn.substring(0,1).equals("(") &&
                   eqn.substring(eqn.length()-1,eqn.length()).equals(")") ) {
                eqn = eqn.substring(1,eqn.length()-1);
            }

            valid = getValidArray(eqn);
        }


        int splitter =-1;  // position of divide.
        int type=-1; // operator on divide

        
        ///ok... this sequence handles all of the basic operators

        //this has been made public.. kept in reverse order of
        //operations. please excuse my dear aunt sally.

        for (int c=0; c < operators.length; c++)
        {
            if (type < 0) {
                splitter = findFirstGood(operators[c],eqn,valid);
                if (splitter != -1) {
                    type = operators[c];
                }
            }
        }


        if (type < 0) {

            //omk.. the string IS messed up!


            //ok.. there's not any standard operators in our
            //string!

            //are variables, numerics, and functions globbed?



            //BOOKMARK
            //this is the stuff I need to be workingo .


            //i had to move it in here...


            //build a numerics truth array

            //now generate a flip array.(for slew)

            boolean[] flips = generateFlipTruth(eqn);


/*
            for (int count=0; count<flips.length;count++) {
                IWPLog.debug("FlipTruth: "+count+": "+flips[count]);
            }
*/


            //display this to get it right




            //ok.. I need to somehow merge flips + valid...
            // so that flips dosen't investigate inside parenthesis


            boolean[] combo = combineSlewedArrays(valid,flips);



            //was using flips here
            eqn = subForFlips(combo,eqn,'*');


            //re-check the string for '*'

            valid = getValidArray(eqn);


            splitter = findFirstGood('*',eqn,valid);
            if (splitter != -1) {
                type = '*';
            }

        }






        //ahhhh HAH!! there may be no operator!! check splitter


        //ok.. what do I do w/ functions and text/number combo
        //variable names????


        String leftstr = "";
        String rightstr = "";



        if ( type < 0 ) {
            leftstr = "";
            rightstr = "";
            value = eqn;
        } else {

            leftstr = eqn.substring(0,splitter);
            rightstr = eqn.substring(splitter+1,eqn.length());
            value = eqn.substring(splitter, splitter+1);
        }




        /*
        IWPLog.debug.println("Equation   = " + eqn);
        IWPLog.debug.println("Left Half  = " + leftstr);
        IWPLog.debug.println("Right Half = " + rightstr);
        IWPLog.debug.println("Op/Value   = " + value);
        IWPLog.debug.println("");
        */


        /* analyze the left */
        if ( ! leftstr.equals("") ) {
            left = new MEquation_Parser_Branch(leftstr);
        } else {
            left = null;
        }


        /* Analyze the Right */
        if ( ! rightstr.equals("") ) {
            right = new MEquation_Parser_Branch(rightstr);
        } else {
            right = null;
        }




        /* try and parse this into a value! */
        try {

            stableValue = Double.valueOf( value ).doubleValue();

        } catch (NumberFormatException e) {
            /* IWPLog.debug("Number format exception - more than likely a variable / operation: "+ value ); */
            stableValue = 0;

            /* is this right? -- at this point... it's either a function
               or a variable */
            nodeIsVariable = true;
        }

    }





    public boolean[] combineSlewedArrays(
                                         boolean[] straight,
                                         boolean[] slewed) {

        boolean[] returnarray = new boolean[slewed.length];

        //now.. worked this out on paper... it's going to kinda work like an AND, but
        //the straight one is going to effect the slewed out one.


        for (int count=0; count<slewed.length; count++) {


            if (straight[count] && !slewed[count] ) {
                returnarray[count] = false;
            } else if (straight[count+1] && !slewed[count] ) {
                returnarray[count] = false;
            } else {
                returnarray[count] = true;
            }


//          IWPLog.debug("combineSlewedArrays: "+count+": "+returnarray[count]);
        }


        return returnarray;
    }



    //this function modifies and returns the passed string
    //by looking at the boolean array, and inserting a
    // character (ch) wherever the array is positive
    // (the array is a record of the flips between the
    // chars from numeric to alaphebet)

    public String subForFlips(boolean[] flips, String str, int ch)
    {
        //need to work backwards thrut he string as not to
        //mess up the numbers


        for (int count=flips.length-1;count > -1;count--) {
            //50% guess.. did I make it 1 or 0 for a flip?

            if ( !flips[count] ) {
                str = str.substring(0,count+1) + (char)ch + str.substring(count+1,str.length());
            }
        }

        return str;
    }



    //return an array the describes were in string str
    //the chars from from numerics to alpha or from
    //alpha to numerics.

    public boolean[] generateFlipTruth(String str)
    {

//      IWPLog.debug("Generating Flip Truth");

        String comparator;
        boolean[] switcharray = new boolean[str.length()];
        boolean[] returnarray = new boolean[str.length()-1];
        boolean flag = false;
        boolean lastFlag = false;
        //sweeneyb's note -- true=number

        //moved ( ) to later part... they should have * added ALL The time.
        String[] numerics = {"0","1","2","3","4","5","6","7","8","9"};

        //gi thru and find all of the numbers,

        for (int count=0; count<str.length(); count++) {

            lastFlag = flag;

            //gotta reset the flag, silly!
            flag = false;

            comparator = str.substring(count,count+1);

            for (int subcount =0; subcount<numerics.length;subcount++)
            {
                if ( comparator.equals(numerics[subcount]) ) {
                    flag = true;
                }
            }

            /* the period is a special case...
               if it comes after characters, it's NOT a #,
               otherwise, it IS a number */
            /* 06/24/01 - mod works */
            /*if ( comparator.equals(".") && lastFlag == true ) {
                flag = true;
            }
            */
            /*
             *sweeneyb 7/14/03 -- I changed this logic a bit so that it was smarter.
             *Now, you shouldn't need a number in front of decimals <1. An operator,
             *character, ( or ) should  tip off this EQ that following stuff is a number.
             */
            if(comparator.equals(".")) {
                if(lastFlag==true) {
                flag=true;
                }
                else {
                try{
                    String backOne=str.substring(count-1,count);
                    for(int subcount=0;subcount<str_operators.length;subcount++) {
                    if(backOne.equals(str_operators[subcount])) {
                        flag=true;
                    }
                    }
                    if(backOne.equals("(")||backOne.equals(")"))
                    flag=true;
                }
                catch(IndexOutOfBoundsException e) {flag=true;} //starts w/ "."
                catch(Exception e) {
                    IWPLog.info(this,"[MEquation_Parser] ~line 490: Error in new \".\" handling");
                }
                }
            }

            switcharray[count] = flag;

        }




        //IWPLog.debug("Returnarray.length = "+returnarray.length);





        if (returnarray.length == 1 )
        {

            returnarray[0] = true;

        } else {

            for (int count=0; count < returnarray.length; count++) {

//              IWPLog.debug("this: "+switcharray[count]+ "    that: "+switcharray[count+1]);


                if (switcharray[count] == switcharray[count+1]) {
                    returnarray[count] = true;
                } else {
                    returnarray[count] = false;
                }




                //yeah.. this should take care of our little ( ) problem...
                //we want parenthesis to switch ALL the time

                if ( str.substring(count+1,count+2).equals("(") ) {
                    returnarray[count] = false;
                }

                if ( str.substring(count,count+1).equals(")") ) {
                    returnarray[count] =false;
                }




            }
        }

        return returnarray;

    }




    
    public double calculate( MVariables iVars )
    	throws InvalidEquationException, UnknownVariableException
    {

        if ( left == null && right == null) {

            //Is This a variable?
            if ( nodeIsVariable ) {

            	// is it a constant?
            	if ( constantMatch(value) ) {
            		nowValue = constant(value);
            	} else {
            		
            		try {
            			nowValue = iVars.get ( value );
            			
            		} catch ( NullPointerException e ) {

            			// 2006-Aug-23 brockman. This could be a more global configuration.
            			if ( MEquation_Parser.UNRECKD_VARIABLES_CREATE_ERRORLOG ) {
            				// This kinda works ok.
            				IWPLog.error( this, "Unknown Variable: " + value );
            			}
            			throw new UnknownVariableException( value );
            			                	
            		} catch ( ClassCastException e ) {
            			throw new InvalidEquationException("MEquation_Parser: Variable Hashtable objects need to be a Number class! (is: " + value.getClass().getName() + ")");
            		}
            	}

            	
            } else {
                /* BUGBUG: This Should be done once on creation */
                nowValue = stableValue;
            }
            return nowValue;

        } else if (value.equals("+") ) {

            // THis was necessary w/ the addition of the +- rewriting
            // we did as a group on 2005-March-06
            if ( left == null ) {
                return right.calculate ( iVars );
            } else {
                return left.calculate( iVars ) + right.calculate( iVars );
            }

        } else if (value.equals("-") ) {
            if (left == null) {
                return -1*right.calculate( iVars );
            } else {
                return left.calculate( iVars ) - right.calculate( iVars );
            }
        } else if (value.equals("*") ) {

            if (left==null && right != null) {

            	//IWPLog.debug("mlpxnull - right = "+right.value);

            } else if (left!=null && right == null) {

            	//IWPLog.debug("mlpxnull - left = "+left.value);


            }

            else if ( functionMatch(left.value) ) {

//              IWPLog.debug("Multiply: it's a FUNCTION!");
                return function(left.value, right.calculate( iVars ));


            //KEYWORD: leftside functions
            } else if ( functionMatch(right.value) ) {
            	throw new InvalidEquationException("IWP_Equation_Parser:694 LeftSide Functions Had been DISABLED. Please report this bug!");
            } else {
                return left.calculate( iVars) * right.calculate( iVars);

            }

        } else if (value.equals("/") ) {
            return left.calculate( iVars ) / right.calculate ( iVars );
        } else if (value.equals("^") ) {
            return Math.pow ( left.calculate( iVars ) , right.calculate( iVars ) );
        
        } else if (value.equals("%") ) {
            return left.calculate( iVars ) % right.calculate( iVars );

        } else {

        }

        return 0;

    }


    // IWP3 - I moved the functions out to MFunctions
        
    public boolean functionMatch(String in)
    {
    	return MFunctions.isFunctionSymbol(in);
    }

    public double function(String function, double value)
    	throws UnknownVariableException
    {
    	return MFunctions.calc(function, value);
    }

    
    public boolean constantMatch(String in)
    {
    	return MConstants.isConstantSymbol(in);
    }

    public double constant(String in)
    	throws UnknownVariableException
    {
    	return MConstants.get(in);
    }
    
    
    
      
    /**
     * IWP3 - able to list out the pieces of the equation that are variables.
     * 
     * When I find a symbol, I add it to out.
     * 
     * @param iVars
     * @return
     */

    public void listSymbols(Collection out)
    {

        if ( left == null && right == null) {

            //Is This a variable?
            if ( nodeIsVariable ) {
            	// if the value matches a function, then don't add.
            	
            	if ( ! MFunctions.isFunctionSymbol(this.value) &&
            			! MConstants.isConstantSymbol(this.value) &&
            			! MInjectedSymbols.isPresentVariableSymbol(this.value) ) {
            		out.add(this.value);
            	}
            	
            } else {
            	// it's a constant.
            }
        }
        if ( left != null ) { left.listSymbols(out); }        
        if ( right != null ) { right.listSymbols(out); }
    }

   

    private boolean isValidArray(boolean[] array)
    {
        for(int c=0; c<array.length;c++) {
            if (array[c]) {return true; }
        }

        return false;
    }


    private boolean[] getValidArray (String eqn)
    {
        boolean[] valid = new boolean[eqn.length()];

        int opencount = 0;
        int closecount = 0;

        //make an array of booleans that tells which caracters
        // are good for analysis.
        // I worked it out.. and if you go thru and count, ( and )
        // when the #'s are equal, then the equation is good.
        //that's what I do here.

        for (int c=0; c < eqn.length(); c++) {

            //found a (, raise the count

            if ( eqn.substring(c,c+1).equals("(") ) {
                opencount++;
            }

            //are the counts equal? modify our memory array
            //appropriately.

            if (opencount == closecount) {
                valid[c] = true;
            } else {
                valid[c] = false;
            }

            //ok,, I've moved this after the validation to
            //make sure the final ) is counted false.

            if ( eqn.substring(c,c+1).equals(")") ) {
                closecount++;
            }

            //IWPLog.debug(this,c+": "+valid[c]+ "  "+eqn.substring(c,c+1));
        }

        return valid;
    }



    //return the posision of ch, being in the valid entries in
    // valid[]
    private int findFirstGood(int ch, String str, boolean[] valid)
    {
        int current = str.indexOf(ch);

        //keep going through uintil you find a vailid one.
        while ( (current>0) && (!valid[current])) {
            current = str.indexOf(ch,current+1);
        }

        if (current >0 ) {
            String prev = str.substring(current-1,current);

            //if position behind it is valid.. then search for operator seccession

            //search through operators and find if character before is one.
            for (int c=0; c<str_operators.length; c++) {

                if (prev.equals(str_operators[c])) {
                    return current-1;
                }
            }
        }

        return current;
    }


}
