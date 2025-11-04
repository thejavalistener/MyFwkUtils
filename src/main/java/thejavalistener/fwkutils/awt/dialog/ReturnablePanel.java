package thejavalistener.fwkutils.awt.dialog;

public interface ReturnablePanel
{
	public void setReturnValue(Object returnValue);
	public Object getReturnValue();
	public void setMyDialog(MyDialog dlg);
}
