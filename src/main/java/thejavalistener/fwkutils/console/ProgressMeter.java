
package thejavalistener.fwkutils.console;

public class ProgressMeter extends Progress
{
    private int currPercent = 0;

    public ProgressMeter(MyConsoleBase c, long top)
    {
        super(c);
        this.top = top;
    }

    @Override
    public Progress begin()
    {
        initProgressTime = System.currentTimeMillis();
        curr = 0;
        currPercent = 0;

        console.print("00%");
        console.skipBkp(3);

        return this;
    }

    @Override
    public void increase(String mssg)
    {
        _verifyThread();

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

        if (pct <= currPercent) return;

        currPercent = pct;

        if (pct < 100)
        {
            String txt = (pct < 10 ? "0" : "") + pct + "%";

            console.print(txt);

            int pos = console.getCaretPosition();

            if (mssg != null && !mssg.isEmpty())
            {
                console.print(" " + mssg);
            }

            console.setCaretPosition(pos);
            console.skipBkp(3);
        }
        else
        {
            console.print("100% ");
            console.skipFwd();
        }
    }
}
