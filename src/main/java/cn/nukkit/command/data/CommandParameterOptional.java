package cn.nukkit.command.data;

/**
 * A simple wrapper class for optional command parameter
 * Created by Lin Mulan on 2016/10/29 0029.
 */
public enum CommandParameterOptional {
    TRUE(true),
    FALSE(false);

    private boolean value;

    CommandParameterOptional(boolean v) {
        value = v;
    }

    public boolean getValue() {
        return value;
    }
}
