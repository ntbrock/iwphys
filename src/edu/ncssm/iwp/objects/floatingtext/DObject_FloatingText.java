package edu.ncssm.iwp.objects.floatingtext;

import java.util.*;
import java.text.NumberFormat;
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
import edu.ncssm.iwp.math.*;

public class DObject_FloatingText
	implements IWPObject, IWPAnimated, IWPDrawable, IWPCalculated, IWPDesigned, IWPXmlable
{
	String name = "New_FloatingText";
	String text = "Default Text";
	String units = "";
	MCalculator value = new MCalculator_Parametric("0");
	GColor fontColor = new GColor();
	int fontSize = 12;
	MCalculator xpath = new MCalculator_Parametric("0");
	MCalculator ypath = new MCalculator_Parametric("0");
	boolean showValue = true;
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

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public MCalculator getValue() {
		return value;
	}

	public void setValue(MCalculator value) {
		this.value = value;
	}

	public MCalculator getXpath() {
		return xpath;
	}

	public void setXpath(MCalculator xpath) {
		this.xpath = xpath;
	}

	public MCalculator getYpath() {
		return ypath;
	}

	public void setYpath(MCalculator ypath) {
		this.ypath = ypath;
	}

	public void setName(String name)
		throws InvalidObjectNameX
	{
		DObjectUtility.ensureName(name);
		this.name = name;
	}
	
	
	public boolean getShowValue() {
		return showValue;
	}

	public void setShowValue(boolean showValue) {
		this.showValue = showValue;
	}
	
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	// Interface: Xmlable
	public IWPObjectXmlCreator newXmlObjectCreator()
	{
		return new DObject_FloatingText_XMLCreator(this);
	}

	public IWPObjectXmlHandler newXmlObjectHandler()
	{
		return new DObject_FloatingText_XMLHandler();
	}

	// Interface: Desinable
	public GAccessor_designer getDesigner()
	{
		return new DObject_FloatingText_designer(this);
	}

	public String getIconFilename()
	{
		return "icon_DObject_FloatingText.gif";
	}

	// Interface: Calculated
	public Collection getProvidedSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList();
		out.add(getName());
		out.addAll(xpath.getProvidedSymbols());
		out.addAll(ypath.getProvidedSymbols());
		out.addAll(value.getProvidedSymbols());
		return out;
	}

	public Collection getRequiredSymbols() throws InvalidEquationException
	{
		ArrayList out = new ArrayList();
		out.addAll(xpath.getRequiredSymbols());
		out.addAll(ypath.getRequiredSymbols());
		out.addAll(value.getRequiredSymbols());
		return out;
	}

	
	// Animated
	public void tick(DProblemState state)
		throws UnknownVariableException, UnknownTickException, InvalidEquationException
	{
	
		state.vars().setAtCurrentTick(getName() + ".x"+MCalculator.SYMBOL_DISP, xpath.calculate(state.vars()) );
		state.vars().setAtCurrentTick(getName() + ".y"+MCalculator.SYMBOL_DISP, ypath.calculate(state.vars()) );
		
		double calcValue = value.calculate(state.vars());
		state.vars().setAtCurrentTick(getName(), calcValue );
		state.vars().setAtCurrentTick(getName() + ".value", calcValue );
		
		
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
		double nowValue = state.vars().getAtCurrentTick(getName());
		String text = this.getText();
		
		if ( this.getShowValue() ) { 
			if ( text.length() > 0 ) { text = text + " = "; }
			

			text = text + nf1.format(nowValue);	
		
			// only display units if show value
			if ( this.getUnits() != null && this.getUnits().length() > 0 ) { 
				text = text + " " + this.getUnits();
			}
		}
		
		// TODO , font size.
		
		drawer.drawString ( text,  fontSize,
				state.vars().getAtCurrentTick(getName()+".x"+MCalculator.SYMBOL_DISP),
				state.vars().getAtCurrentTick(getName()+".y"+MCalculator.SYMBOL_DISP) );
	}
	
	
}
