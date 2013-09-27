/**
 *
 */
package de.htw.tojato.robotik.earthrover.logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tobias Kalmes
 */
public class RootLogger {
    private static Map<String, RootLogger> loggerMap;
    private        Logger                  logger;

    /**
     * Konstruktor
     *
     * @param loggerName Name des Loggers
     */
    private RootLogger(String loggerName) {
        try {
            this.logger = Logger.getLogger(loggerName);
            String folder = "./logging";

            if (!folder.endsWith("/")) {
                folder += "/";
            }

            // create folders if not existant
            File file = new File(folder);
            file.mkdirs();
            // max 10 files of each 10MB in append mode
            FileHandler fileHandler = new FileHandler(folder + loggerName + ".log",
                                                      10 * 1024 * 1024, 10, true);
            fileHandler.setEncoding("UTF-8");
            this.logger.addHandler(fileHandler);
            this.logger.setLevel(Level.ALL);
            fileHandler.setFormatter(new LoggingFormatter());
            this.log("---FIRST LINE---", Level.INFO);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Liefert den dem String entsprechenden Logger zur�ck oder erzeugt ihn, falls nicht vorhanden.
     *
     * @return logger
     */
    public static RootLogger getInstance(LoggerNames mainLogger) {
        if (loggerMap == null) {
            loggerMap = new HashMap<String, RootLogger>();
        } else {
            if (loggerMap.containsKey(mainLogger.getName())) {
                return loggerMap.get(mainLogger.getName());
            }
        }
        loggerMap.put(mainLogger.getName(), new RootLogger(mainLogger.getName()));
        return loggerMap.get(mainLogger.getName());
    }

    /**
     * F�gt allen Loggern eine End Line hinzu
     */
    public static void closeAllLoggers() {
        if (loggerMap != null) {
            for (Entry<String, RootLogger> logger : loggerMap.entrySet()) {
                RootLogger tempLogger = logger.getValue();
                tempLogger.log("---LAST LINE---", Level.INFO);
            }
        }
    }

    /**
     * logs the message with the given level to the file defined in the config file
     *
     * @param message message
     * @param level   log level
     */
    public void log(String message, Level level) {
        this.logger.log(level, message);
    }

    /**
     * logs the message and the exception with the given level to the file defined in the config
     * file
     *
     * @param message   message
     * @param level     log level
     * @param exception Exception
     */
    public void log(String message, Level level, Throwable exception) {
        this.logger.log(level, message, exception);
    }
}
