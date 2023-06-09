package com.moyskleytech.mc.BuildBattle.utils;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.moyskleytech.mc.BuildBattle.service.Service;

public class Logger extends Service {
    private static Logger instance;
    private Level level;
    private java.util.logging.Logger logger;
    private boolean testMode;

    public static void mockMode() {
        instance = new Logger();
        instance.testMode = true;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static void init(JavaPlugin plugin) {
        instance = new Logger();
        instance.level = Level.ERROR;
        instance.logger = plugin.getLogger();
    }

    protected static void mockDebug(String message, Object... params) {
        System.out.println(getMessage(message, params));
    }

    public static void info( String message, Object... params) {
        if (instance.testMode) {
            mockDebug(message, params);
            return;
        }
        instance.logger.info(getMessage(message, params));
    }

    public static void trace( String message, Object... params) {
        if (instance.testMode) {
            mockDebug(message, params);
            return;
        }
        if (instance.level.getLevel() >= Level.TRACE.getLevel()) {
            instance.logger.info(getMessage(message, params));
        }
    }

    public static void warn( String message, Object... params) {
        if (instance.testMode) {
            mockDebug(message, params);
            return;
        }
        if (instance.level.getLevel() >= Level.WARNING.getLevel()) {
            instance.logger.warning(getMessage(message, params));
        }
    }

    public static void error( String message, Object... params) {
        if (instance.testMode) {
            mockDebug(message, params);
            return;
        }
        if (instance.level.getLevel() >= Level.ERROR.getLevel()) {
            instance.logger.warning(getMessage(message, params));
        }
    }

    public static void send( String message, Object... params) {
        if (instance.testMode) {
            mockDebug(message, params);
            return;
        }
        instance.logger.info(getMessage(message, params));
    }

    private static String getMessage(String message, Object... params) {
        for (var param : params) {
            if (param == null) {
                param = "NULL";
            }
            if (!(param instanceof String)) {
                if (param instanceof Component)
                {
                    param = LegacyComponentSerializer.legacySection().serialize((Component)param);
                }
                else
                    param = param.toString();
            }

            message = message.replaceFirst(Pattern.quote("{}"), Matcher.quoteReplacement((String) param));
        }
        return message;
    }

    public static void setMode(Level level) {
        if (instance != null)
            instance.level = level;
    }

    public enum Level {
        DISABLED(0),
        ERROR(1),
        WARNING(2),
        TRACE(3),
        ALL(4);

        @Getter
        private final int level;

        Level(int level) {
            this.level = level;
        }
    }
}
