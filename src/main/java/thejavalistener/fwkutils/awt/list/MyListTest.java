package thejavalistener.fwkutils.awt.list;

import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import thejavalistener.fwkutils.awt.panel.MyScrollPane;
import thejavalistener.fwkutils.awt.testui.MyTestUI;
import thejavalistener.fwkutils.awt.variuos.MyAwt;

public class MyListTest
{
	public static void main(String[] args)
	{
		MyAwt.setWindowsLookAndFeel();
		
		MyList<String> lst = new MyList<>();
		List<String> items = List.of("Pablo","Carlos","Pedro","Cacho","Rolo","Tito","Beni","Cumpa");
		lst.setItems(items);

		MyTestUI ui = MyTestUI.test(new MyScrollPane(lst.c()));
		ui.addTextField("tfItem")
		   .addButton("Add",l->lst.addItem(ui.getString("tfItem")))
		   .run();
		
		lst.setListListener(e->{
			MyListEvent<String> evt = (MyListEvent)e;
			
			switch( evt.getClickCount() )
			{
				case 1:
					System.out.println("Que cappooo:"+evt.getItem());
					break;
				case 2:
					String item = lst.removeSelectedItem();
					ui.getTextField("tfItem").setText(item);
			}
			
		});
		
	}
	
	static class EscuchaLista implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
		}
	}
}
