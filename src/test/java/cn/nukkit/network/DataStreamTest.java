package cn.nukkit.network;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * The test class for <code>DataStream</code>.
 * Some test skipped.
 *
 * @author Nukkit Project Team
 */
public class DataStreamTest {

    private final DataStream stream = new DataStream();

    @Test
    public void testPutChar() {
        stream.putChar('!');
        assertEquals(stream.getChar(), '!');
    }

    @Test
    public void testPutInt() {
        stream.putInt(Integer.MAX_VALUE);
        assertEquals(stream.getInt(), Integer.MAX_VALUE);
    }

    @Test
    public void testPutLong() {
        stream.putLong(Long.MAX_VALUE);
        assertEquals(stream.getLong(), Long.MAX_VALUE);
    }

    @Test
    public void testPutShort() {
        stream.putShort(Short.MAX_VALUE);
        assertEquals(stream.getShort(), Short.MAX_VALUE);
    }

    @Test
    public void testPutString() {
        stream.putString("This is a test");
        assertEquals(stream.getString(), "This is a test");
    }

    @Test
    public void testPutDouble() {
        stream.putDouble(Double.MAX_VALUE);
        assertTrue(stream.getDouble() == Double.MAX_VALUE);
    }

    @Test
    public void testSetCursor() {
        stream.setCursor(0);
        stream.putInt(233);
        assertEquals(stream.getCursor(), 4);
    }

    @Test
    public void testPutUUID() {
        UUID uuid = UUID.randomUUID();
        stream.putUUID(uuid);
        assertEquals(uuid, stream.getUUID());
    }

    @Test
    public void testGetByteArray() {
        assertNotNull(stream.toByteArray());
    }

    @Test
    public void testCreateWithGivenBuffer() {
        stream.putInt(233);
        DataStream temp = new DataStream(stream.toByteArray());
        assertEquals(temp.getInt(), stream.getInt());
    }

}
