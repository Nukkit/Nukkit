package cn.nukkit.network;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        stream.put('!');
        assertEquals(stream.getChar(), '!');
    }

    @Test
    public void testPutInteger() {
        stream.put(Integer.MAX_VALUE);
        assertEquals(stream.getInteger(), Integer.MAX_VALUE);
    }

    @Test
    public void testPutLong() {
        stream.put(Long.MAX_VALUE);
        assertEquals(stream.getLong(), Long.MAX_VALUE);
    }

    @Test
    public void testPutShort() {
        stream.put(Short.MAX_VALUE);
        assertEquals(stream.getShort(), Short.MAX_VALUE);
    }

    @Test
    public void testPutString() {
        stream.put("This is a test");
        assertEquals(stream.getString(), "This is a test");
    }

    @Test
    public void testSetCursor() {
        stream.setCursor(0);
        stream.put(233);
        assertEquals(stream.getCursor(), 4);
    }

    @Test
    public void testGetByteArray() {
        assertNotNull(stream.getByteArray());
    }

    @Test
    public void testCreateWithGivenBuffer() {
        stream.put(233);
        DataStream temp = new DataStream(stream.getByteArray());
        assertEquals(temp.getInteger(), stream.getInteger());
    }

}
