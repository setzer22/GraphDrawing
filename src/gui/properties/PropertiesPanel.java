package gui.properties;

import gui.model.GraphElement;

import javax.swing.JPanel;

public abstract class PropertiesPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void setValues();
	
	public abstract void setElement(GraphElement element);

}
