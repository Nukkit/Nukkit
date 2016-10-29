package cn.nukkit.command.data;


public class CommandParameter {

    public final static String ARG_TYPE_STRING = "string";
    public final static String ARG_TYPE_STRING_ENUM = "stringenum";
    public final static String ARG_TYPE_BOOL = "bool";
    public final static String ARG_TYPE_TARGET = "target";
    public final static String ARG_TYPE_PLAYER = "target";
    public final static String ARG_TYPE_BLOCK_POS = "blockpos";
    public final static String ARG_TYPE_RAW_TEXT = "rawtext";
    public final static String ARG_TYPE_INT = "int";

    private String name;
    private String type;
    private CommandParameterOptional optional;

    public CommandParameter(String name, String type, CommandParameterOptional optional) {
        this.name = name;
        this.type = type;
        this.optional = optional;
    }

    public CommandParameter(String name, CommandParameterOptional optional) {
        this(name, ARG_TYPE_RAW_TEXT, optional);
    }

    public CommandParameter(String name) {
        this(name, CommandParameterOptional.FALSE);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public CommandParameterOptional getOptional() {
        return optional;
    }
}
