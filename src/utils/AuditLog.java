// package utils;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.logging.*;

// public class AuditLog {
//     private static final List<String> logs = new ArrayList<>();


//     // ✅ دالة getter للسجلات
//     public static List<String> getLogs() {
//         return new ArrayList<>(logs);
//     }
//     public static void log(String msg) {
//         // simplistic audit logger
//         System.out.println("[AUDIT] " + System.currentTimeMillis() + " - " + msg);
//     }
// }


//public class AuditLog {
//
//    private static final java.util.logging.Logger auditLogger = java.util.logging.Logger.getLogger("AuditLogger");
//
//    static {
//        try {
//            Handler fileHandler = new FileHandler("logs/audit.log", true);
//            fileHandler.setFormatter(new SimpleFormatter());
//            auditLogger.addHandler(fileHandler);
//
//            auditLogger.setUseParentHandlers(false);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void log(String msg) {
//        auditLogger.log(Level.INFO, msg);
//    }
//}
package utils;

import java.io.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AuditLog {

    // ====== Log Entry Structure ======
    public static class LogEntry {
        private final Instant timestamp;
        private final String type;
        private final String message;

        public LogEntry(Instant timestamp, String type, String message) {
            this.timestamp = timestamp;
            this.type = type;
            this.message = message;
        }

        public Instant getTimestamp() { return timestamp; }
        public String getType() { return type; }
        public String getMessage() { return message; }

        public String getFormattedTime() {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());
            return fmt.format(timestamp);
        }

        @Override
        public String toString() {
            return timestamp.toEpochMilli() + ";" + type + ";" + message;
        }

        public static LogEntry fromString(String line) {
            try {
                String[] p = line.split(";", 3);
                long time = Long.parseLong(p[0]);
                return new LogEntry(Instant.ofEpochMilli(time), p[1], p[2]);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static final List<LogEntry> logs = Collections.synchronizedList(new ArrayList<>());
    private static final String FILE_NAME = "logs.txt";

    // ========== BASIC LOGGING ==========
    public static void log(String msg) {
        log("INFO", msg);
    }

    public static void log(String type, String msg) {
        LogEntry log = new LogEntry(Instant.now(), type, msg);
        logs.add(log);
        saveToFile();  // <==== حفظ تلقائي
        System.out.println("[AUDIT] " + log.getFormattedTime() + " - " + type + " - " + msg);
    }

    public static List<LogEntry> getLogs() {
        synchronized (logs) {
            return new ArrayList<>(logs);
        }
    }

    public static void clear() {
        synchronized (logs) {
            logs.clear();
            saveToFile();
        }
    }

    public static Map<String, Integer> countByType() {
        Map<String, Integer> map = new HashMap<>();
        synchronized (logs) {
            for (LogEntry e : logs)
                map.put(e.getType(), map.getOrDefault(e.getType(), 0) + 1);
        }
        return map;
    }

    // ========== SAVE LOGS ==========
    private static void saveToFile() {
        try (FileWriter fw = new FileWriter(FILE_NAME)) {
            synchronized (logs) {
                for (LogEntry e : logs) {
                    fw.write(e.toString() + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== LOAD LOGS ==========
    public static void loadFromFile() {
        File f = new File(FILE_NAME);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            logs.clear();
            String line;
            while ((line = br.readLine()) != null) {
                LogEntry e = LogEntry.fromString(line);
                if (e != null) logs.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
