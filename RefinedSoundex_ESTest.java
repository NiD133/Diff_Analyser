package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.language.RefinedSoundex;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the RefinedSoundex class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class RefinedSoundexTest extends RefinedSoundex_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDifferenceWithIdenticalStrings() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("U>");
        int difference = refinedSoundex.difference(
            "org.apache.commons.codec.EncoderException",
            "org.apache.commons.codec.EncoderException"
        );
        assertEquals(3, difference);
    }

    @Test(timeout = 4000)
    public void testConstructorWithEmptyCharArray() throws Throwable {
        char[] emptyCharArray = new char[0];
        RefinedSoundex refinedSoundex = new RefinedSoundex(emptyCharArray);
        assertNotNull(refinedSoundex);
    }

    @Test(timeout = 4000)
    public void testGetMappingCodeForY() throws Throwable {
        RefinedSoundex refinedSoundex = RefinedSoundex.US_ENGLISH;
        char mappingCode = refinedSoundex.getMappingCode('y');
        assertEquals('0', mappingCode);
    }

    @Test(timeout = 4000)
    public void testGetMappingCodeForX() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        char mappingCode = refinedSoundex.getMappingCode('X');
        assertEquals('c', mappingCode);
    }

    @Test(timeout = 4000)
    public void testEncodeNullString() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        String encoded = refinedSoundex.encode((String) null);
        assertNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeNonPrintableCharacter() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        String encoded = refinedSoundex.encode("\u007F");
        assertEquals("", encoded);
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullStringThrowsException() throws Throwable {
        try {
            new RefinedSoundex((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.RefinedSoundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexWithUS_EnglishMapping() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        String soundex = refinedSoundex.US_ENGLISH.soundex("org.apache.commons.codec.language.RefinedSoundex");
        assertEquals("O09401030308083060370840409020806308605", soundex);
    }

    @Test(timeout = 4000)
    public void testSoundexWithEmptyString() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        String soundex = refinedSoundex.soundex("");
        assertEquals("", soundex);
    }

    @Test(timeout = 4000)
    public void testSoundexWithNullString() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        String soundex = refinedSoundex.soundex((String) null);
        assertNull(soundex);
    }

    @Test(timeout = 4000)
    public void testDifferenceWithNullStrings() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        int difference = refinedSoundex.difference((String) null, (String) null);
        assertEquals(0, difference);
    }

    @Test(timeout = 4000)
    public void testGetMappingCodeForPlusSign() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        char mappingCode = refinedSoundex.getMappingCode('+');
        assertEquals('\u0000', mappingCode);
    }

    @Test(timeout = 4000)
    public void testEncodeObjectWithUS_EnglishMapping() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        Object encoded = refinedSoundex.US_ENGLISH.encode((Object) "O0");
        assertEquals("O0", encoded);
        assertNotNull(encoded);
    }

    @Test(timeout = 4000)
    public void testEncodeString() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("org.apache.commons.codec.language.RefinedSoundex");
        String encoded = refinedSoundex.encode("org.apache.commons.codec.language.RefinedSoundex");
        assertEquals("Omsaogcagmom.gm.agcomaoasaphma.mom.ac", encoded);
        assertNotNull(encoded);
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullCharArrayThrowsException() throws Throwable {
        try {
            new RefinedSoundex((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.RefinedSoundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeNullObjectThrowsException() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex();
        try {
            refinedSoundex.encode((Object) null);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.language.RefinedSoundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexWithSpecialCharacters() throws Throwable {
        RefinedSoundex refinedSoundex = new RefinedSoundex("U>");
        String soundex = refinedSoundex.soundex("<+Eq|qK!wg0f\u0006n_~");
        assertEquals("E", soundex);
        assertNotNull(soundex);
    }
}