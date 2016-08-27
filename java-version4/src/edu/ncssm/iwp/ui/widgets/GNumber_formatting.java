//first class written by paul. welcome to the project buddy.
package edu.ncssm.iwp.ui.widgets;
import java.text.*;

public class GNumber_formatting
{
    protected static DecimalFormat format;

    static {
    	format = new DecimalFormat();
    	format.setMaximumFractionDigits(14);
    }
    
    public GNumber_formatting() {
    	
    }
    
    public static String format(double in)
    {
	if(in<.001 && in>-.001 && in!=0.0)
	    {
		format.applyPattern("0.0##E0");
		String formatted = format.format(in)+"";
		return formatted;
	    }
	else if (in>1000.0 || in<-1000.0) {
	    format.applyPattern("0.0##E0");
	    String formatted = format.format(in)+"";
	    return formatted;
	}
	else
	    {
		format.applyPattern("0.0###");
		String formatted = format.format(in)+"";
		return formatted;
	    }
    }
}
