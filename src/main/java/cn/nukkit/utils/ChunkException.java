package cn.nukkit.utils;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ChunkException extends RuntimeException {
    public ChunkException(String message) {
        super(message);
    }

    public Throwable fillInStackTrace() {
        return this; // Faster not to fill in the stacktrace
    }
}
