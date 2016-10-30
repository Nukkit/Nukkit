package cn.nukkit.utils;

import java.math.BigInteger;

public class VarInt {

    private static final BigInteger UNSIGNED_LONG_MAX_VALUE = new BigInteger("FFFFFFFFFFFFFFFF", 16);

    static void _assert(BigInteger integer) {
        if (integer == null) {
            throw new IllegalArgumentException("The value should not be null");
        }

        if (integer.compareTo(UNSIGNED_LONG_MAX_VALUE) > 0) {
            throw new IllegalArgumentException("The value is too big");
        }
    }

    /**
     * @param v Signed int
     * @return Unsigned encoded int
     */
    public static long encodeZigZag32(int v) {
        // Note:  the right-shift must be arithmetic
        return (long) ((v << 1) ^ (v >> 31));
    }

    /**
     * @param v Unsigned encoded int
     * @return Signed decoded int
     */
    public static int decodeZigZag32(long v) {
        return (int) (v >> 1) ^ -(int) (v & 1);
    }

    /**
     * @param v Signed long
     * @return Unsigned encoded long
     */
    public static BigInteger encodeZigZag64(long v) {
        BigInteger origin = BigInteger.valueOf(v);
        BigInteger left = origin.shiftLeft(1);
        BigInteger right = origin.shiftRight(63);
        return left.xor(right);
    }

    /**
     * @param v Signed encoded long
     * @return Unsigned decoded long
     */
    public static BigInteger decodeZigZag64(long v) {
        return decodeZigZag64(BigInteger.valueOf(v).and(UNSIGNED_LONG_MAX_VALUE));
    }

    /**
     * @param v Unsigned encoded long
     * @return Unsigned decoded long
     */
    public static BigInteger decodeZigZag64(BigInteger v) {
        BigInteger left = v.shiftRight(1);
        BigInteger right = v.and(BigInteger.ONE).negate();
        return left.xor(right);
    }

    private static BigInteger _readVarInt(BinaryStream stream, int maxSize) {
        BigInteger result = BigInteger.ZERO;
        int offset = 0;
        int b;

        do {
            b = stream.getByte();
            result = result.or(BigInteger.valueOf((b & 0x7f) << offset));
            offset += 7;

            if (offset >= maxSize) {
                throw new IllegalArgumentException("VarInt too big");
            }
        } while ((b & 0x80) > 0);

        return result;
    }

    public static int readVarInt(BinaryStream stream) {
        return decodeZigZag32(readUnsignedVarInt(stream));
    }

    public static long readUnsignedVarInt(BinaryStream stream) {
        return _readVarInt(stream, 5).longValue();
    }

    public static long readVarLong(BinaryStream stream) {
        return decodeZigZag64(readUnsignedVarInt(stream)).longValue();
    }

    public static BigInteger readUnsignedVarLong(BinaryStream stream) {
        return _readVarInt(stream, 10);
    }

    static void _writeVarInt(BinaryStream stream, BigInteger v) {
        BigInteger BIX7F = BigInteger.valueOf(0x7f);
        BigInteger BIX80 = BigInteger.valueOf(0x80);
        do {
            BigInteger var = v.and(BIX7F);
            if (!var.shiftRight(7).equals(BigInteger.ZERO)) {
                var = v.or(BIX80);
            }

            stream.putByte(var.byteValue());
            v = v.shiftRight(7);
        } while (v.compareTo(BigInteger.ZERO) > 0);
    }

    public static void writeVarInt(BinaryStream stream, int value) {
        writeUnsignedVarInt(stream, encodeZigZag32(value));
    }

    public static void writeUnsignedVarInt(BinaryStream stream, long value) {
        _writeVarInt(stream, BigInteger.valueOf(value));
    }

    public static void writeVarLong(BinaryStream stream, long value) {
        writeUnsignedVarLong(stream, encodeZigZag64(value));
    }

    public static void writeUnsignedVarLong(BinaryStream stream, BigInteger value) {
        _writeVarInt(stream, value);
    }

}