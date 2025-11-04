package thejavalistener.myfwkutils.texttable;

public interface DataLoader
{
	public Object[] nextRow();
	public String[] headers();
}
