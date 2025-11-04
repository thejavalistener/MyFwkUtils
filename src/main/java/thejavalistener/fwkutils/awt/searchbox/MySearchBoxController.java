package thejavalistener.fwkutils.awt.searchbox;

import java.util.List;

public interface MySearchBoxController<T>
{
	public List<T> dataRequested(String toSearch);
}
