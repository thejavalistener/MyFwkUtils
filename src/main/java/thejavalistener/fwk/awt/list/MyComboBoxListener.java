package thejavalistener.fwk.awt.list;

import java.awt.event.ItemEvent;

import javax.swing.event.PopupMenuEvent;

public interface MyComboBoxListener
{
	public void itemStateChanged(ItemEvent e);
	public void popupMenuWillBecomeVisible(PopupMenuEvent e);
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e);
	public void popupMenuCanceled(PopupMenuEvent e);
}
