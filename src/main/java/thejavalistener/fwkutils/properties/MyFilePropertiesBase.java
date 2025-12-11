package thejavalistener.fwkutils.properties;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyFilePropertiesBase
{
    private static final String DEFAULT_FILE_NAME;
    private static final Path   DEFAULT_FILE_PATH;

    static {
        DEFAULT_FILE_NAME = computeDefaultFileName();
        DEFAULT_FILE_PATH = computeDefaultFilePath(DEFAULT_FILE_NAME);
    }

    protected final Path fullFilename;

    // Constructor explícito (usa ~/.myfwk/fileName)
    public MyFilePropertiesBase(String fileName) {
        this.fullFilename = Paths.get(
            System.getProperty("user.home"),
            ".myfwk",
            fileName
        );
    }

    // Constructor automático (usa ~/.myfwk/<main_class>.properties)
    public MyFilePropertiesBase() {
        this.fullFilename = DEFAULT_FILE_PATH;
    }

    public static String getDefaultFileName() {
        return DEFAULT_FILE_NAME;
    }

    public static Path getDefaultFilePath() {
        return DEFAULT_FILE_PATH;
    }

    private static String computeDefaultFileName() {
        String mainClass = detectMainClass();
        return mainClass.replace('.', '_') + ".properties";
    }

    private static Path computeDefaultFilePath(String fileName) {
        String home = System.getProperty("user.home");
        Path folder = Paths.get(home, ".myfwk");

        try {
            Files.createDirectories(folder);
        } catch(Exception ignored) {}

        return folder.resolve(fileName);
    }

    private static String detectMainClass() {
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            if ("main".equals(e.getMethodName())) {
                return e.getClassName();
            }
        }
        return "unknownApp";
    }
}
