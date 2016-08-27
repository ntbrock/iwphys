package edu.ncssm.iwp.objects.grapher;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

import edu.ncssm.iwp.exceptions.InvalidEquationException;
import edu.ncssm.iwp.exceptions.InvalidObjectNameX;
import edu.ncssm.iwp.exceptions.UnknownTickException;
import edu.ncssm.iwp.exceptions.UnknownVariableException;
import edu.ncssm.iwp.graphicsengine.GColor;
import edu.ncssm.iwp.graphicsengine.IWPDrawer;
import edu.ncssm.iwp.objects.DObjectUtility;
import edu.ncssm.iwp.plugin.IWPAnimated;
import edu.ncssm.iwp.plugin.IWPCalculated;
import edu.ncssm.iwp.plugin.IWPDesigned;
import edu.ncssm.iwp.plugin.IWPDrawable;
import edu.ncssm.iwp.plugin.IWPObject;
import edu.ncssm.iwp.plugin.IWPObjectXmlCreator;
import edu.ncssm.iwp.plugin.IWPObjectXmlHandler;
import edu.ncssm.iwp.plugin.IWPXmlable;
import edu.ncssm.iwp.problemdb.DProblem;
import edu.ncssm.iwp.problemdb.DProblemState;
import edu.ncssm.iwp.ui.GAccessor_designer;

public class DObject_Grapher
	implements IWPObject, IWPAnimated, IWPDrawable, IWPCalculated, IWPDesigned, IWPXmlable
{
	String name = "New_Grapher";
	
	//Equation
	String equation = "Sin(x+y)";
	
	//Graphing Parameters
	double BoxX = 0;
	double BoxY = 0;
	double BoxH = 20;
	double BoxW = 20;
	int Res  = 100;
	int Stroke = 1;
	boolean transformCoords = true;
	boolean showBounding	= true;
	
	GColor fontColor = new GColor();
	NumberFormat nf1 = NumberFormat.getInstance();
	
	public String getName() { return name; }

	public GColor getGColor() { return fontColor; }

	
	//----------------

	public GColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(GColor fontColor) {
		this.fontColor = fontColor;
	}


	//Equation
	public String getEquation() 		{	return equation;					}
	public void setEquation(String text){	this.equation = text;				}
	
	//Height
	public double getBoxH()				{	return BoxH;						}
	public void setBoxH(Double value)	{	this.BoxH = value.doubleValue();	}
	
	//Width
	public double getBoxW()				{	return BoxW;						}
	public void setBoxW(Double value)	{	this.BoxW = value.doubleValue();	}
	
	//Start X
	public double getBoxX()				{	return BoxX;						}
	public void setBoxX(Double value)	{	this.BoxX = value.doubleValue();	}
	
	//Start Y
	public double getBoxY()				{	return BoxY;						}
	public void setBoxY(Double value)	{	this.BoxY = value.doubleValue();	}
	
	//Resolution
	public int getRes()					{	return Res;							}
	public void setRes(Integer value)	{	this.Res = value.intValue();		}
	
	//Stroke Size
	public int getStroke()				{	return Stroke;						}
	public void setStroke(Integer value){	this.Stroke = value.intValue();		}
	
	//Show Bounding Box
	public boolean getShowBounding()	{	return showBounding;				}
	public void setShowBounding(boolean value){this.showBounding = value;		}
	
	//Transform Coordinates
	public boolean getTransformCoords()	{	return transformCoords;				}
	public void setTransformCoords(boolean value){this.transformCoords = value;	}
	
	

	
	public void setName(String name)
		throws InvalidObjectNameX
	{
		DObjectUtility.ensureName(name);
		this.name = name;
	}
	
	
	// Interface: Xmlable
	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_Grapher_XMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_Grapher_XMLHandler();
	}

	// Interface: Desinable
	public GAccessor_designer getDesigner( )
	{
		return new DObject_Grapher_designer(this);
	}

	public String getIconFilename()
	{
		return "icon_DObject_Grapher.gif";
	}

	// Interface: Calculated
	public Collection getProvidedSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList();
		/*out.add(getName());
		out.addAll(xpath.getProvidedSymbols());
		out.addAll(ypath.getProvidedSymbols());
		out.addAll(value.getProvidedSymbols());*/
		return out;
	}

	public Collection getRequiredSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList();
		/*out.addAll(xpath.getRequiredSymbols());
		out.addAll(ypath.getRequiredSymbols());
		out.addAll(value.getRequiredSymbols());*/
		return out;
	}

	
	// Animated
	public void tick(DProblemState state)
		throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
			
		
	}

	public void zero(DProblem problem, DProblemState state)
		throws UnknownVariableException, InvalidEquationException, UnknownTickException
	{
		tick(state);
	}

	
	// Drawable
	public void iwpDraw(IWPDrawer drawer, DProblemState state)
		throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
		
		MathExpression evaluator = new MathExpression(equation);
		
		double stepsize = BoxW / Res;		
		
		double bx=0,by=0;
		
		if(transformCoords) {
			bx = BoxX;
			by = BoxY;
		}
		for(double x=(BoxX - BoxW/2)-bx; x<BoxX+BoxW/2-bx; x+=stepsize) {
			double y1 = evaluator.value(x,state.vars().getCurrentTime());
			double y2 = evaluator.value(x+stepsize,state.vars().getCurrentTime());
			drawer.drawLine(x+bx, y1+by, x+stepsize+bx, y2+by , Stroke);
		}
		
		if(showBounding) {
			drawer.drawRect(BoxX-BoxW/2, BoxY+BoxH/2, BoxW, BoxH);
		}
		
		
		//Here's the Current Time!
		//state.vars().getCurrentTime();
		
	}
}
