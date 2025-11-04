package thejavalistener.fwkutils.awt.variuos;

import javax.swing.JComponent;

public interface MyComponent
{
	public JComponent c();
	public Object getValue();
	public void resetValue();
	public void requestFocus();
	public void setEnabled(boolean b);
}
