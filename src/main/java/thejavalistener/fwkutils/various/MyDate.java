package thejavalistener.fwkutils.various;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyDate implements Comparable<MyDate>
{
    private final LocalDateTime ldt;

    /* =====================
       CONSTRUCTORES
       ===================== */

    public MyDate(Date d)
    {
        this.ldt = Instant.ofEpochMilli(d.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    public MyDate()
    {
    	this(System.currentTimeMillis());
    }

    public MyDate(java.sql.Date d)
    {
        this.ldt = d.toLocalDate().atStartOfDay();
    }

    public MyDate(Timestamp ts)
    {
        this.ldt = ts.toLocalDateTime();
    }

    public MyDate(long millis)
    {
        this.ldt = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public MyDate(int year, int month, int day)
    {
        this.ldt = LocalDateTime.of(year, month, day, 0, 0, 0, 0);
    }

    public MyDate(String s)
    {
        this.ldt = parseFlexibleDate(s).atStartOfDay();
    }

    public MyDate(String s, boolean isIso8601)
    {
        if (isIso8601)
            this.ldt = LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
        else
            this.ldt = parseFlexibleDate(s).atStartOfDay();
    }

    private MyDate(LocalDateTime ldt)
    {
        this.ldt = ldt;
    }

    /* =====================
       PARSING
       ===================== */

    private static LocalDate parseFlexibleDate(String s)
    {
        Pattern p1 = Pattern.compile("(\\d{4}).(\\d{2}).(\\d{2})"); // yyyyXmmXdd
        Pattern p2 = Pattern.compile("(\\d{2}).(\\d{2}).(\\d{4})"); // ddXmmXyyyy

        Matcher m = p1.matcher(s);
        if (m.matches())
        {
            return LocalDate.of(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3))
            );
        }

        m = p2.matcher(s);
        if (m.matches())
        {
            return LocalDate.of(
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(1))
            );
        }

        throw new IllegalArgumentException("Formato de fecha inválido: " + s);
    }

    /* =====================
       CONVERSIONES
       ===================== */

    public java.sql.Date toSqlDate()
    {
        return java.sql.Date.valueOf(ldt.toLocalDate());
    }

    public Timestamp toSqlTimestamp()
    {
        return Timestamp.valueOf(ldt);
    }

    public String toIso8601()
    {
        return ldt.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /* =====================
       OPERACIONES
       ===================== */

    public MyDate addDays(int n)
    {
        return new MyDate(ldt.plusDays(n));
    }

    public MyDate addMonth(int n)
    {
        return new MyDate(ldt.plusMonths(n));
    }

    public MyDate addYears(int n)
    {
        return new MyDate(ldt.plusYears(n));
    }

    public MyDate addHours(int n)
    {
        return new MyDate(ldt.plusHours(n));
    }

    public MyDate addMinutes(int n)
    {
        return new MyDate(ldt.plusMinutes(n));
    }

    public MyDate addSeconds(int n)
    {
        return new MyDate(ldt.plusSeconds(n));
    }

    public MyDate addMillis(long n)
    {
        return new MyDate(ldt.plusNanos(n * 1_000_000L));
    }

    /* =====================
       COMPARABLE
       ===================== */

    @Override
    public int compareTo(MyDate o)
    {
        return this.ldt.isBefore(o.ldt) ? -1 :
               this.ldt.isAfter(o.ldt)  ?  1 : 0;
    }
}
