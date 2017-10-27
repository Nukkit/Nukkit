package cn.nukkit.utils;

/**
 * author: boybook
 * Nukkit Project
 */
public class RuleData<T> {

    public static byte BOOLEAN_TYPE = 0x01;
    public static byte INT_TYPE = 0x02;
    public static byte FLOAT_TYPE = 0x03;

    public String name;

    @Deprecated
    public boolean unknown1;

    @Deprecated
    public boolean unknown2;

    public byte type;
    public T value;

    public RuleData() {

    }

    public RuleData(String name, T value, byte type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }
}
