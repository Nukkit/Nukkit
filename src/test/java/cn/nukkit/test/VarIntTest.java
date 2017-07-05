package cn.nukkit.test;

import cn.nukkit.utils.VarInt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * By lmlstarqaq http://snake1999.com/
 * Creation time: 2017/7/5 23:22.
 */
@DisplayName("VarInt")
class VarIntTest {

	@DisplayName("ZigZag")
	@Test
	void testZigZag() {
		assertAll(
				() -> assertEquals(0x2468acf0, VarInt.encodeZigZag32(0x12345678)),
				() -> assertEquals(0x2b826b1d, VarInt.encodeZigZag32(0xea3eca71)),
				() -> assertEquals(0x12345678, VarInt.decodeZigZag32(0x2468acf0)),
				() -> assertEquals(0xea3eca71, VarInt.decodeZigZag32(0x2b826b1d)),
				() -> assertEquals(new BigInteger("2623536930346282224"), VarInt.encodeZigZag64(0x1234567812345678L)),
				() -> assertEquals(new BigInteger("3135186066796324391"), VarInt.encodeZigZag64(0xea3eca710becececL)),
				() -> assertEquals(BigInteger.valueOf(0x1234567812345678L), VarInt.decodeZigZag64(new BigInteger("2623536930346282224"))),
				() -> assertEquals(BigInteger.valueOf(0xea3eca710becececL), VarInt.decodeZigZag64(new BigInteger("3135186066796324391")))
		);
	}
}
