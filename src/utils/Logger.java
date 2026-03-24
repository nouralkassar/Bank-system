package utils;
import java.io.IOException;
import java.util.logging.*;

public class Logger {
    public static void info(String msg) {
        System.out.println("[INFO] " + msg);
    }
    public static void error(String msg, Throwable t) {
        System.err.println("[ERROR] " + msg);
        if (t != null) t.printStackTrace();
    }
}


//public class Logger {
//
//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("AppLogger");
//
//    static {
//        try {
//            // تحديد مكان حفظ الملف
//            Handler fileHandler = new FileHandler("logs/app.log", true); // true = append
//            fileHandler.setFormatter(new SimpleFormatter());
//            logger.addHandler(fileHandler);
//
//            // منع ظهور التكرار في console
//            logger.setUseParentHandlers(false);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void info(String msg) {
//        logger.log(Level.INFO, msg);
//    }
//
//    public static void warn(String msg) {
//        logger.log(Level.WARNING, msg);
//    }
//
//    public static void error(String msg, Throwable t) {
//        logger.log(Level.SEVERE, msg, t);
//    }
//}
