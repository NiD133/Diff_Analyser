package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.CodecPolicy;
import org.apache.commons.codec.binary.Base16;
import org.apache.commons.codec.binary.BaseNCodec;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Base16_ESTest extends Base16_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIsInAlphabet_withInvalidByte() {
        Base16 base16 = new Base16();
        boolean result = base16.isInAlphabet((byte) 71);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testEncode_withNegativeByte() {
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        Base16 base16 = new Base16(false, codecPolicy);
        byte[] input = new byte[]{0, -23, 0};
        byte[] expectedOutput = new byte[]{48, 48, 69, 57, 48, 48};
        
        byte[] result = base16.encode(input);
        
        assertArrayEquals(expectedOutput, result);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testEncode_withExcessiveLength() {
        byte[] input = new byte[6];
        Base16 base16 = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.encode(input, 2131, Integer.MAX_VALUE, context);
    }

    @Test(timeout = 4000)
    public void testEncode_withZeroLength() {
        Base16 base16 = new Base16();
        byte[] input = new byte[4];
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.encode(input, 0, 0, context);
        
        assertArrayEquals(new byte[]{0, 0, 0, 0}, input);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withInvalidString() {
        Base16 base16 = new Base16();
        
        base16.decode("Gw");
    }

    @Test(timeout = 4000)
    public void testDecode_withValidString() {
        Base16 base16 = new Base16();
        byte[] expectedOutput = new byte[]{33};
        
        byte[] result = base16.decode("21");
        
        assertArrayEquals(expectedOutput, result);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withInvalidByteArray() {
        Base16 base16 = new Base16();
        byte[] input = new byte[7];
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.decode(input, 6, 6, context);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testDecode_withOutOfBoundsIndex() {
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        Base16 base16 = new Base16(false, codecPolicy);
        byte[] input = new byte[3];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = -23;
        
        base16.decode(input, 4, 64, context);
    }

    @Test(timeout = 4000)
    public void testDecode_withNegativeOffsetAndLength() {
        Base16 base16 = new Base16();
        byte[] input = new byte[3];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = -4039;
        
        base16.decode(input, -3424, -3424, context);
        
        assertArrayEquals(new byte[]{0, 0, 0}, input);
    }

    @Test(timeout = 4000)
    public void testEncode_withLargeOffset() {
        Base16 base16 = new Base16();
        byte[] input = new byte[5];
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.encode(input, Integer.MAX_VALUE - 9, 484, context);
        
        assertEquals(76, BaseNCodec.MIME_CHUNK_SIZE);
    }

    @Test(timeout = 4000)
    public void testPemChunkSize() {
        Base16 base16 = new Base16(false);
        assertEquals(64, BaseNCodec.PEM_CHUNK_SIZE);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withInvalidOctet() {
        Base16 base16 = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 64;
        byte[] input = new byte[7];
        input[5] = 69;
        
        base16.decode(input, 5, 513, context);
    }

    @Test(timeout = 4000)
    public void testIsInAlphabet_withBackslash() {
        Base16 base16 = new Base16();
        boolean result = base16.isInAlphabet((byte) 92);
        assertFalse(result);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testEncode_withNullContext() {
        Base16 base16 = new Base16();
        byte[] input = new byte[3];
        
        base16.encode(input, 76, 0, null);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testEncode_withOutOfBoundsLength() {
        byte[] input = new byte[6];
        Base16 base16 = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.encode(input, 76, 76, context);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testDecode_withNullByteArray() {
        Base16 base16 = new Base16();
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.decode(null, 64, 64, context);
    }

    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void testDecode_withInvalidLength() {
        CodecPolicy codecPolicy = CodecPolicy.LENIENT;
        Base16 base16 = new Base16(true, codecPolicy);
        byte[] input = new byte[1];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 76;
        
        base16.decode(input, 1, 1, context);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testConstructor_withNullCodecPolicy() {
        new Base16(true, null);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withSingleCharacter() {
        CodecPolicy codecPolicy = CodecPolicy.STRICT;
        Base16 base16 = new Base16(false, codecPolicy);
        
        base16.decode("F");
    }

    @Test(timeout = 4000)
    public void testIsInAlphabet_withZero() {
        Base16 base16 = new Base16();
        boolean result = base16.isInAlphabet((byte) 48);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testContainsAlphabetOrPad_withChunkSeparator() {
        Base16 base16 = new Base16();
        byte[] input = BaseNCodec.CHUNK_SEPARATOR;
        
        boolean result = base16.containsAlphabetOrPad(input);
        
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testEncode_withNegativeOffsetAndLength() {
        byte[] input = new byte[5];
        Base16 base16 = new Base16();
        
        byte[] result = base16.encode(input, -1832, -1832);
        
        assertNotSame(input, result);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withInvalidStringBDT() {
        Base16 base16 = new Base16();
        
        base16.decode("BDT");
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testDecode_withInvalidOctetInArray() {
        CodecPolicy codecPolicy = CodecPolicy.LENIENT;
        Base16 base16 = new Base16(true, codecPolicy);
        byte[] input = new byte[19];
        BaseNCodec.Context context = new BaseNCodec.Context();
        context.ibitWorkArea = 76;
        
        base16.decode(input, 0, 0, context);
    }

    @Test(timeout = 4000)
    public void testDecode_withNegativeOffsetAndLengthTwice() {
        Base16 base16 = new Base16();
        byte[] input = new byte[10];
        BaseNCodec.Context context = new BaseNCodec.Context();
        
        base16.decode(input, -1800, -1800, context);
        base16.decode(input, -1800, -1800, context);
        
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, input);
    }
}