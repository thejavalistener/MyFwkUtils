package thejavalistener.fwkutils.console;

import javax.swing.text.StyledDocument;

import thejavalistener.fwkutils.string.MyString;

public class ProgressBar2 extends Progress
{
    private int size;
    private long top;

    private int currPercent = 0;

    private int barStart;
    private int messageStart;

    private StyledDocument doc;

    private static final int MESSAGE_WIDTH = 20;

    public ProgressBar2(MyConsoleBase c, int size, long top)
    {
        super(c);
        this.size = size;
        this.top = top;
    }

    @Override
    public Progress begin()
    {
        curr = 0;
        currPercent = 0;

        doc = console.getTextPane().c().getStyledDocument();

        console.print("[");
        barStart = console.getCaretPosition();

        console.print(MyString.replicate(' ', size));
        console.print("]");

        messageStart = console.getCaretPosition();

        console.print(MyString.replicate(' ', MESSAGE_WIDTH));

        initProgressTime = System.currentTimeMillis();
        return this;
    }

    @Override
    public void increase(String mssg)
    {
        if (top <= 0) return;

        curr++;

        int pct = (int)((curr * 100L) / top);

        setPercent(pct, mssg);
    }

    @Override
    public void increase()
    {
        increase("");
    }

    @Override
    public void setPercent(int pct, String mssg)
    {
        if (pct < 0) pct = 0;
        if (pct > 100) pct = 100;

        int prevBlocks = (currPercent * size) / 100;
        int newBlocks  = (pct * size) / 100;

        int toFill = newBlocks - prevBlocks;

        if (toFill > 0)
        {
            try
            {
                doc.remove(barStart + prevBlocks, toFill);
                doc.insertString(
                        barStart + prevBlocks,
                        MyString.replicate('#', toFill),
                        null
                );
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }

        currPercent = pct;

        updateMessage(mssg);
    }

    private void updateMessage(String mssg)
    {
        if (mssg == null) return;

        if (mssg.length() > MESSAGE_WIDTH)
            mssg = mssg.substring(0, MESSAGE_WIDTH);

        String padded = String.format("%-" + MESSAGE_WIDTH + "s", mssg);

        try
        {
            doc.remove(messageStart, MESSAGE_WIDTH);
            doc.insertString(messageStart, padded, null);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
