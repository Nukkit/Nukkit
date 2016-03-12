package cn.nukkit.utils;

import cn.nukkit.lang.BaseLang;

/**
 * author: ant3
 * Nukkit
 */
public class LocalisedLogger implements Logger {

	private MainLogger logger;
    private BaseLang language;

    
    public LocalisedLogger(MainLogger logger, BaseLang language) {
    	this.logger = logger;
        this.language = language;
        
        infoLocal("language.selected", language.getName(), language.getLang());
    }

    public void emergencyLocal(String messageKey, String... messageParams) {
        translateAndSend(TextFormat.RED + "[EMERGENCY] ", messageKey, messageParams);
    }

    public void alertLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.RED + "[ALERT] ", messageKey, messageParams);
    }

    public void criticalLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.RED + "[CRITICAL] ", messageKey, messageParams);
    }

    public void errorLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.DARK_RED + "[ERROR] ", messageKey, messageParams);
    }

    public void warningLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.YELLOW + "[WARNING] ", messageKey, messageParams);
    }

    public void noticeLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.AQUA + "[NOTICE] ", messageKey, messageParams);
    }

    public void infoLocal(String messageKey, String... messageParams) {
    	translateAndSend(TextFormat.WHITE + "[INFO] ", messageKey, messageParams);
    }

    public void debugLocal(String messageKey, String... messageParams) {
        if (!logger.logDebug) {
            return;
        }
        translateAndSend(TextFormat.GRAY + "[DEBUG] " , messageKey, messageParams);
    }

    public void setLogDebug(Boolean logDebug) {
        logger.setLogDebug(logDebug);
    }
    
    @Override
	public void emergency(String message) {
    	logger.emergency(message);
	}

	@Override
	public void alert(String message) {
		logger.alert(message);
	}

	@Override
	public void critical(String message) {
		logger.critical(message);
	}

	@Override
	public void error(String message) {
		logger.error(message);
	}

	@Override
	public void warning(String message) {
		logger.warning(message);
	}

	@Override
	public void notice(String message) {
		logger.notice(message);
	}

	@Override
	public void info(String message) {
		logger.info(message);
	}

	@Override
	public void debug(String message) {
		logger.debug(message);
	}

	@Override
	public void log(LogLevel level, String message) {
		logger.log(level, message);
	}

    public void logException(Exception ex) {
        this.logException(ex, ex.getStackTrace());
    }

    public void logException(Exception ex, StackTraceElement[] trace) {
        this.alert(Utils.getExceptionMessage(ex));
    }

    public void logLocal(LogLevel level, String messageKey, String... messageParams) {
        switch (level) {
            case EMERGENCY:
                this.emergencyLocal(messageKey, messageParams);
                break;
            case ALERT:
                this.alertLocal(messageKey, messageParams);
                break;
            case CRITICAL:
                this.criticalLocal(messageKey, messageParams);
                break;
            case ERROR:
                this.errorLocal(messageKey, messageParams);
                break;
            case WARNING:
                this.warningLocal(messageKey, messageParams);
                break;
            case NOTICE:
                this.noticeLocal(messageKey, messageParams);
                break;
            case INFO:
                this.infoLocal(messageKey, messageParams);
                break;
            case DEBUG:
                this.debugLocal(messageKey, messageParams);
                break;
        }
    }
    
    protected void translateAndSend(String prefix, String messageKey, String... messageParams) {
    	translateAndSend(prefix, -1, messageKey, messageParams);
    }
    
    protected void translateAndSend(String prefix, int level, String messageKey, String... messageParams) {
        logger.send(prefix + language.translateString(messageKey, messageParams), level);
    }
}