package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.net.PercentCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class PercentCodec_ESTest extends PercentCodec_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEncodeWithNonAsciiByte() {
        byte[] input = new byte[5];
        input[3] = (byte) 70;
        PercentCodec codec = new PercentCodec(input, true);
        byte[] encoded = codec.encode(input);
        assertEquals(15, encoded.length);
    }

    @Test(timeout = 4000)
    public void testDecodeWithNonAsciiByte() {
        byte[] input = new byte[5];
        input[3] = (byte) 70;
        PercentCodec codec = new PercentCodec(input, true);
        byte[] decoded = codec.decode(input);
        assertArrayEquals(new byte[]{0, 0, 0, 70, 0}, decoded);
    }

    @Test(timeout = 4000)
    public void testEncodeAndDecode() {
        byte[] input = new byte[9];
        input[0] = (byte) (-19);
        PercentCodec codec = new PercentCodec(new byte[5], true);
        byte[] encoded = codec.encode(input);
        byte[] decoded = codec.decode(encoded);
        assertEquals(9, decoded.length);
        assertEquals(27, encoded.length);
        assertArrayEquals(new byte[]{-19, 0, 0, 0, 0, 0, 0, 0, 0}, decoded);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyArray() {
        byte[] input = new byte[5];
        input[3] = (byte) 70;
        PercentCodec codec = new PercentCodec(new byte[0], true);
        byte[] encoded = codec.encode(input);
        assertSame(encoded, input);
    }

    @Test(timeout = 4000)
    public void testDecodeEmptyArray() {
        PercentCodec codec = new PercentCodec();
        byte[] decoded = codec.decode(new byte[0]);
        assertEquals(0, decoded.length);
    }

    @Test(timeout = 4000)
    public void testIllegalArgumentExceptionForNegativeByte() {
        byte[] input = new byte[]{-50};
        try {
            new PercentCodec(input, true);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithSpecialCharacter() {
        byte[] input = new byte[7];
        input[6] = (byte) 63;
        PercentCodec codec = new PercentCodec(new byte[5], true);
        byte[] encoded = codec.encode(input);
        assertEquals(19, encoded.length);
    }

    @Test(timeout = 4000)
    public void testEncodeNullObject() {
        PercentCodec codec = new PercentCodec();
        Object encoded = codec.encode((Object) null);
        assertNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeInvalidObject() {
        PercentCodec codec = new PercentCodec();
        try {
            codec.encode((Object) codec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithDefaultCodec() {
        PercentCodec codec = new PercentCodec();
        byte[] input = new byte[2];
        byte[] encoded = codec.encode(input);
        assertSame(encoded, input);
    }

    @Test(timeout = 4000)
    public void testEncodeNullByteArray() {
        PercentCodec codec = new PercentCodec();
        byte[] encoded = codec.encode((byte[]) null);
        assertNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeWithSpecificByte() {
        byte[] input = new byte[9];
        input[2] = (byte) 22;
        PercentCodec codec = new PercentCodec(new byte[5], true);
        byte[] encoded = codec.encode(input);
        assertEquals(25, encoded.length);
    }

    @Test(timeout = 4000)
    public void testDecodeNullObject() {
        PercentCodec codec = new PercentCodec(new byte[5], true);
        Object decoded = codec.decode((Object) null);
        assertNull(decoded);
    }

    @Test(timeout = 4000)
    public void testDecodeInvalidObject() {
        PercentCodec codec = new PercentCodec();
        try {
            codec.decode((Object) codec);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeInvalidPercentEncoding() {
        PercentCodec codec = new PercentCodec();
        byte[] input = new byte[6];
        input[5] = (byte) 37;
        try {
            codec.decode(input);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.net.PercentCodec", e);
        }
    }

    @Test(timeout = 4000)
    public void testDecodeNullByteArray() {
        PercentCodec codec = new PercentCodec();
        byte[] decoded = codec.decode((byte[]) null);
        assertNull(decoded);
    }

    @Test(timeout = 4000)
    public void testEncodeWithNullAlwaysEncodeChars() {
        byte[] input = new byte[1];
        PercentCodec codec = new PercentCodec(null, true);
        byte[] encoded = codec.encode(input);
        assertSame(encoded, input);
        assertNotNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeAndDecodeSingleByte() {
        byte[] input = new byte[]{43};
        PercentCodec codec = new PercentCodec(input, true);
        byte[] decoded = codec.decode(input);
        byte[] encoded = codec.encode(decoded);
        assertArrayEquals(new byte[]{43}, encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyByteArray() {
        byte[] input = new byte[0];
        PercentCodec codec = new PercentCodec(input, true);
        byte[] encoded = codec.encode(input);
        assertSame(encoded, input);
        assertNotNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeNegativeByteWithDefaultCodec() {
        PercentCodec codec = new PercentCodec();
        byte[] input = new byte[]{-114, 0};
        byte[] encoded = codec.encode(input);
        assertArrayEquals(new byte[]{37, 56, 69, 0}, encoded);
    }
}