package thejava.listener.fwkutils.log;

import java.io.File;

import thejavalistener.fwkutils.various.MyFile;

public final class MyLogs
{
    private static volatile MyLog INSTANCE;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (INSTANCE != null) {
                INSTANCE.close();
            }
        }));
    }

    public static MyLog get()
    {
        if (INSTANCE == null)
        {
            synchronized (MyLogs.class)
            {
                if (INSTANCE == null)
                {
                    String file = System.getenv("MYLOG_FILE");
                    if (file != null && file.isBlank()) file = null;

                    boolean append = Boolean.parseBoolean(
                        System.getenv().getOrDefault("MYLOG_APPEND", "true")
                    );

                    INSTANCE = new MyLog(file, append);
                }
            }
        }
        return INSTANCE;
    }    
}
