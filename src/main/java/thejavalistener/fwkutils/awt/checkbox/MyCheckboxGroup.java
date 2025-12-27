package thejavalistener.fwkutils.awt.checkbox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import thejavalistener.fwkutils.various.MyCollection;

public class MyCheckboxGroup
{
    private List<JCheckBox> checkboxes = new ArrayList<>();
    private JCheckBox selected;

    private ActionListener listener;
    private boolean listenerWorking = true;

    public void addCheckbox(JCheckBox cb)
    {
        boolean prev = setListenerWorking(false);

        cb.addActionListener(e -> onCheckboxClicked(cb));
        cb.setSelected(false);
        checkboxes.add(cb);

        setListenerWorking(prev);
    }

    private void onCheckboxClicked(JCheckBox cb)
    {
        if (cb == selected)
        {
            cb.setSelected(true);
            return;
        }

        if (selected != null)
        {
            selected.setSelected(false);
        }

        selected = cb;
        selected.setSelected(true);

        if (listener != null && listenerWorking)
        {
            listener.actionPerformed(
                new ActionEvent(cb, 1, cb.getActionCommand())
            );
        }
    }

    public void setSelected(int idx)
    {
        setSelected(idx, false);
    }

    public void setSelected(int idx, boolean throwEvent)
    {
        if (selected != null)
        {
            selected.setSelected(false);
        }

        selected = checkboxes.get(idx);
        selected.setSelected(true);

        if (listener != null && listenerWorking && throwEvent)
        {
            listener.actionPerformed(
                new ActionEvent(selected, 1, selected.getActionCommand())
            );
        }
    }

    public JCheckBox getSelected()
    {
        return selected;
    }

    public int getSelectedIndex()
    {
        return MyCollection.findPos(checkboxes, cb -> cb.isSelected());
    }

    public void setActionListener(ActionListener listener)
    {
        this.listener = listener;
    }

    public boolean setListenerWorking(boolean b)
    {
        boolean prev = listenerWorking;
        listenerWorking = b;
        return prev;
    }

    public List<JCheckBox> getCheckboxes()
    {
        return checkboxes;
    }

    public void removeAll()
    {
        checkboxes.clear();
        selected = null;
    }
}
