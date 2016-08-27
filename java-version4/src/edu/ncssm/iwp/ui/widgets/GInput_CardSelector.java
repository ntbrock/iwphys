// Interactive Web Physics (IWP)
// Copyright (C) 1999 Nathaniel T. Brockman
// 
// For full copyright information, see main source file,
// "iwp.java"

package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GInput_CardSelector extends GInput implements ItemListener
{
	private static final long serialVersionUID = 1L;
	GInput_Card[] cards;

	JComboBox selectBox;

	JPanel cardPanel = new JPanel();

	public GInput_CardSelector(String iLabel, GInput_Card[] iCards)
	{
		super(iLabel);
		setLayout(new BorderLayout());

		cards = iCards;

		selectBox = new JComboBox(cardToString(cards));
		selectBox.addItemListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1));
		
		if ( isLabelDefined() ) { infoPanel.add(getLabel()); }
		infoPanel.add(selectBox);
		
		JPanel topInfoPanel = new JPanel();
		topInfoPanel.setLayout(new BorderLayout());
		topInfoPanel.add(BorderLayout.NORTH,infoPanel);
		
		
		JPanel topCardPanel = new JPanel();
		topCardPanel.setLayout(new BorderLayout());
		topCardPanel.add(BorderLayout.NORTH, cardPanel);
		
		JScrollPane scroll_CardPanel = new JScrollPane(topCardPanel);
		setLayout(new BorderLayout());
		
		add(BorderLayout.WEST, topInfoPanel);
		add(BorderLayout.CENTER, scroll_CardPanel );
		
		setCard(cards[0].toString() );
		
		
	}	
	

	public void setCard(String iCard)
	{
		int i = 0;
	
		for (int c=0; c<cards.length; c++) {
			if (iCard.equals(cards[c].toString() )) {
				i =	c;
			}
		}

		if (i >= 0 && i < cards.length) {

			cardPanel.removeAll();
			cardPanel.setLayout(new BorderLayout());
			cardPanel.add("Center",cards[i]);

/*
			cardPanel.doLayout();
			cards[i].doLayout();			
			doLayout();
			repaint();
*/

			invalidate();
			validate();
			repaint();

		}
	}


	public String getCardString()
	{
		return ((GInput_Card)(selectBox.getSelectedItem())).toString();
	}


	public String[] cardToString(GInput_Card[] iCards)
	{
		String[] returnArray = new String[iCards.length];

		for (int c=0; c<iCards.length; c++) {
			returnArray[c] = iCards[c].toString();
		}
		
		return returnArray;
	}


	public void itemStateChanged(ItemEvent e)
	{
		if(e.getStateChange() == ItemEvent.SELECTED) {
			setCard((String)e.getItem());
		}
	}

}
