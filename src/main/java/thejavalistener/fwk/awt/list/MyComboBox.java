package thejavalistener.fwk.awt.list;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import thejavalistener.fwk.awt.MyComponent;
import thejavalistener.fwk.awt.MyException;

public class MyComboBox<T> implements MyComponent
{
	private JComboBox<String> comboBox;
	private Function<T,String> tToString;
	private List<T> data=new ArrayList<>();
	private String specialItem=null;
	private MyComboBoxListener comboBoxListener;

	private Integer prevItemIndex=null;
	private Integer prevPrevItemIndex=null;

	private boolean listenerWorking=true;

	public MyComboBox()
	{
		this(null,null,true);
	}

	public MyComboBox(MyComboBoxListener comboBoxListener)
	{
		this(comboBoxListener,true);
	}

	public MyComboBox(MyComboBoxListener comboBoxListener,boolean listenerWorking)
	{
		this(null,comboBoxListener,listenerWorking);
	}

	public MyComboBox(Function<T,String> tToString)
	{
		this(tToString,null,true);
	}

	public MyComboBox(Function<T,String> tToString,MyComboBoxListener comboBoxListener,boolean listenerWorking)
	{
		this.tToString=tToString;
		this.comboBoxListener=comboBoxListener;
		this.comboBox=new JComboBox<String>();
		this.comboBox.addItemListener(new EscuchaItem());
		this.comboBox.addPopupMenuListener(new EscuchaPopup());
		this.listenerWorking=listenerWorking;
	}

	public void addItem(T t)
	{
		addItem(t,false);
	}
	
	public void setTooltipRender(Function<T,String> tToString)
	{
		MyComboBoxTooltipRenderer<T> render = new MyComboBoxTooltipRenderer<>(tToString,data);
		this.comboBox.setRenderer(render);
	}

	public void addItem(T t, boolean selected)
	{
		boolean prev=setListenerWorking(false);
		data.add(t);
		String ts=tToString!=null?tToString.apply(t):t.toString();
		comboBox.addItem(ts);

		if(selected)
		{
			setSelectedItem(x -> tToString.apply(x).equals(ts));
		}

		setListenerWorking(prev);
	}

	public void addItem(T t, int idx)
	{
		boolean prev=setListenerWorking(false);
		data.add(idx,t);
		String ts=tToString!=null?tToString.apply(t):t.toString();
		comboBox.insertItemAt(ts,idx);
		setListenerWorking(prev);
	}

	public void setSelectedItem(int i)
	{
		prevPrevItemIndex=prevItemIndex;
		prevItemIndex=comboBox.getSelectedIndex();
		comboBox.setSelectedIndex(i);
	}

	public T getPreviousSelectedItem()
	{
		if(prevItemIndex==null||prevItemIndex<0)
		{
			return null;
		}
		else
		{
			return getItemAt(prevItemIndex);
		}
	}

	public void restorePreviousSelectedItem()
	{
		boolean x=setListenerWorking(false);

		if(prevItemIndex==null||prevItemIndex<0)
		{
			setUnselected();
		}
		else
		{
			comboBox.setSelectedIndex(prevItemIndex);
		}

		setListenerWorking(x);
	}

	public void setSelectedItem(Function<T,Boolean> tEqT)
	{
		boolean prev=setListenerWorking(false);
		int i=0;
		boolean fin=false;
		while(i<data.size()&&!fin)
		{
			T d=data.get(i);
			if(d!=null)
			{
				if(tEqT.apply(d))
				{
					fin=true;
				}
				else
				{
					i++;
				}
			}
			else
			{
				i++;
			}
		}

		if(i<data.size())
		{
			setSelectedItem(i);
		}

		setListenerWorking(prev);
	}

	public void setTToString(Function<T,String> f)
	{
		this.tToString=f;
	}

	public void setSpecialItem(String spi)
	{
		// si aun no fue seteado, lo agrego...
		if(specialItem==null&&spi!=null)
		{
			boolean prev=setListenerWorking(false);
			comboBox.insertItemAt(spi,0);
			data.add(0,null);
			this.specialItem=spi;
			setListenerWorking(prev);
			return;
		}

		// si ya habia sido seteado, lo reemplazo...
		if(specialItem!=null&&spi!=null)
		{
			if(comboBox.getItemCount()>0)
			{
				boolean prev=setListenerWorking(false);
				comboBox.removeItemAt(0);
				comboBox.insertItemAt(spi,0);
				this.specialItem=spi;
				setListenerWorking(prev);
				return;
			}
		}

		// si spi es null, lo anula...
		if(spi==null)
		{
			if(specialItem!=null&&data.size()>0)
			{
				boolean prev=setListenerWorking(false);
				data.remove(0);
				comboBox.removeItemAt(0);
				specialItem=null;
				setListenerWorking(prev);
			}
		}
	}

	public void selectSpecialItem()
	{
		if(specialItem!=null)
		{
			boolean prev=setListenerWorking(false);
			comboBox.setSelectedIndex(0);
			setListenerWorking(prev);
		}
		else
		{
			throw new RuntimeException("No se ha especificado el specialItem");
		}
	}

	public boolean isSpecialItemSelected()
	{
		if(specialItem!=null)
		{
			return getSelectedIndex()==0;
		}
		else
		{
			throw new RuntimeException("No se ha especificado el specialItem");
		}
	}

	/** Deja en blanco el combo, sin ningun item seleccionado */
	public void setUnselected()
	{
		boolean prev=setListenerWorking(false);
		comboBox.setSelectedIndex(-1);
		setListenerWorking(prev);
	}

	public T getSelectedItem()
	{
		int idx=getSelectedIndex();
		return idx>=0?data.get(idx):null;
	}

	public int getSelectedIndex()
	{
		return comboBox.getSelectedIndex();
	}

	public T getItemAt(int i)
	{
		return data.get(i);
	}

	public T removeItemAt(int i)
	{
		i=comboBoxListener==null?i:i+1;
		return _removeItemAt(i);
	}

	private T _removeItemAt(int i)
	{
		boolean prev=setListenerWorking(false);
		T t=data.remove(i);
		comboBox.removeItemAt(i);
		setListenerWorking(prev);
		return t;
	}

	public boolean removeItem(Function<T,Boolean> tEqT)
	{
		int i=specialItem==null?0:1;
		while(i<data.size()&&!tEqT.apply(getItemAt(i)))
		{
			i++;
		}

		if(i<data.size())
		{
			_removeItemAt(i);
			return true;
		}
		else
		{
			return false;
		}
	}

	public void forceItemEvent()
	{
		T t=getSelectedItem();
		ItemEvent e=new ItemEvent(c(),1,t,ItemEvent.ITEM_STATE_CHANGED);
		comboBoxListener.itemStateChanged(e);
	}

	public List<T> getItems()
	{
		return data;
	}

	public T removeSelectedItem()
	{
		boolean prev=setListenerWorking(false);
		T t=removeItemAt(getSelectedIndex());
		setListenerWorking(prev);
		return t;
	}

	public void removeAllItems()
	{
		boolean prev=setListenerWorking(false);

		int desde=specialItem==null?0:1;
		while(data.size()>desde)
		{
			comboBox.removeItemAt(desde);
			data.remove(desde);
		}

		setListenerWorking(prev);
	}

	public void setItems(List<T> items)
	{
		boolean prev=setListenerWorking(false);

		removeAllItems();

		if(specialItem!=null)
		{
			setSpecialItem(specialItem);
		}

		for(T t:items)
		{
			addItem(t);
		}

		setListenerWorking(prev);
	}

	public boolean isUnselected()
	{
		return getSelectedIndex()<0;
	}

	public void validateNotUnselected(String mssg, String title) throws MyException
	{
		if(isUnselected())
		{
			requestFocus();
			throw new MyException(mssg,title,MyException.ERROR);
		}
	}

	public void setComboBoxListener(MyComboBoxListener lst)
	{
		setComboBoxListener(lst,true);
	}

	public void setComboBoxListener(MyComboBoxListener lst, boolean listenerIsWorking)
	{
		this.comboBoxListener=lst;
		this.listenerWorking=listenerIsWorking;
	}

	public void removeListener()
	{
		this.comboBoxListener=null;
	}

	public boolean setListenerWorking(boolean working)
	{
		boolean prev=listenerWorking;
		listenerWorking=working;
		return prev;
	}

	public boolean isListenerWorking()
	{
		return listenerWorking;
	}

	public JComboBox<String> c()
	{
		return comboBox;
	}

	class CellRender extends DefaultListCellRenderer
	{
		Function<Object,String> tToString = null;
		CellRender(Function<Object,String> tToString)
		{
			this.tToString = tToString;
		}
		
		@Override
		public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JLabel label=(JLabel)super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
			label.setToolTipText(value!=null?tToString.apply(value):null);
			return label;
		}
	}

	/**
	 * Si isSpecialItemSelected o IsUnselected retorna null, de otro modo
	 * retorna el item
	 */
	public Object getValue()
	{
		if(isSpecialItemSelected()||isUnselected())
		{
			return null;
		}
		else
		{
			return getSelectedItem();
		}
	}

	class EscuchaItem implements ItemListener
	{
		int veces=0;

		@Override
		public void itemStateChanged(ItemEvent e)
		{
			if(comboBoxListener!=null&&listenerWorking)
			{
				veces++;
				if(veces==1)
				{
					comboBoxListener.itemStateChanged(e);
				}
				else
				{
					if(veces>1)
					{
						veces=0;
					}
				}
			}
		}
	}

	class EscuchaPopup implements PopupMenuListener
	{

		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent e)
		{
			if(comboBoxListener!=null&&listenerWorking)
			{
				comboBoxListener.popupMenuWillBecomeVisible(e);
			}
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
		{
			if(comboBoxListener!=null&&listenerWorking)
			{
				comboBoxListener.popupMenuWillBecomeInvisible(e);
			}
		}

		@Override
		public void popupMenuCanceled(PopupMenuEvent e)
		{
			if(comboBoxListener!=null&&listenerWorking)
			{
				comboBoxListener.popupMenuCanceled(e);
			}
		}

	}

	public void setEnabled(boolean b)
	{
		comboBox.setEnabled(b);
	}

	public void resetValue()
	{
		setUnselected();
	}

	public void requestFocus()
	{
		comboBox.requestFocus();
	}

	public void sort(BiFunction<T,T,Integer> cmp)
	{
		int desde=specialItem!=null?1:0;

		ArrayList<T> data2=new ArrayList<>(data.subList(desde,data.size()));
		for(int i=0; i<data2.size(); i++)
		{
			for(int j=0; j<data2.size()-1; j++)
			{
				if(cmp.apply(data2.get(j),data2.get(j+1))>0)
				{
					T aux=data2.get(j);
					data2.set(j,data2.get(j+1));
					data2.set(j+1,aux);
				}
			}
		}

		setItems(data2);
		if(specialItem!=null)
		{
			setSpecialItem(specialItem);
		}
	}
	
	
	//----------------------------------------------------------------------------------


}
