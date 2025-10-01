package thejavalistener.fwk.awt.list;

import java.awt.Component;
import java.util.List;
import java.util.function.Function;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class MyComboBoxTooltipRenderer<T> extends DefaultListCellRenderer {
    private static final long serialVersionUID = 1L;

    private List<T> data;
    private final Function<? super T, String> tooltipFn;

    public MyComboBoxTooltipRenderer(Function<? super T, String> tToString,List<T> data) {
        this.tooltipFn = tToString;
        this.data = data;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    	
        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        if (value != null && index>=0) {
            T t = data.get(index);
            label.setToolTipText(tooltipFn.apply(t));
        } else {
            label.setToolTipText(null);
        }
        return label;
    }
}
