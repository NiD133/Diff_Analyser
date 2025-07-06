package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.commons.codec.language.Soundex;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SoundexTest extends Soundex_ESTest_scaffolding {

    // Test for Soundex difference method with non-matching strings
    @Test(timeout = 4000)
    public void testDifferenceWithNonMatchingStrings() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        int difference = soundex.US_ENGLISH.difference("oJ'N*32WF]@/JLK", ":ae");
        assertEquals(0, difference);
    }

    // Test for Soundex constructor with custom mapping
    @Test(timeout = 4000)
    public void testCustomMappingConstructor() throws Throwable {
        Soundex soundex = new Soundex("01230120022455012623010202", false);
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for setting max length to zero
    @Test(timeout = 4000)
    public void testSetMaxLengthToZero() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH;
        soundex.setMaxLength(0);
        assertEquals(0, soundex.getMaxLength());
    }

    // Test for encoding an empty string
    @Test(timeout = 4000)
    public void testEncodeEmptyString() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("", soundex.encode(""));
    }

    // Test for Soundex difference method with non-matching strings using genealogy mapping
    @Test(timeout = 4000)
    public void testDifferenceWithGenealogyMapping() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        int difference = soundex.US_ENGLISH_GENEALOGY.difference("The character is not mapped: ", "sDstPNyqQnJ?7");
        assertEquals(0, difference);
    }

    // Test for Soundex difference method with identical strings
    @Test(timeout = 4000)
    public void testDifferenceWithIdenticalStrings() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        int difference = soundex.difference("0jj2pNN4aBXE6a/UG\"", "0jj2pNN4aBXE6a/UG\"");
        assertEquals(4, difference);
    }

    // Test for soundex method with unmapped character
    @Test(timeout = 4000)
    public void testSoundexWithUnmappedCharacter() throws Throwable {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        try {
            soundex.soundex("96;Siw");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for Soundex constructor with null character array
    @Test(timeout = 4000)
    public void testConstructorWithNullCharArray() throws Throwable {
        try {
            new Soundex((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for Soundex constructor with null string
    @Test(timeout = 4000)
    public void testConstructorWithNullString() throws Throwable {
        try {
            new Soundex((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for soundex method with a complex string
    @Test(timeout = 4000)
    public void testSoundexWithComplexString() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH;
        assertEquals("X320", soundex.soundex("Xk8U,D.6D__ua)Hc~|Z"));
    }

    // Test for soundex method with simplified mapping
    @Test(timeout = 4000)
    public void testSoundexWithSimplifiedMapping() throws Throwable {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        assertEquals("T122", soundex.US_ENGLISH_SIMPLIFIED.soundex(":t+2T#;4]b5I JeK"));
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for soundex method with null string
    @Test(timeout = 4000)
    public void testSoundexWithNullString() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH;
        assertNull(soundex.US_ENGLISH.soundex((String) null));
    }

    // Test for soundex method with special characters
    @Test(timeout = 4000)
    public void testSoundexWithSpecialCharacters() throws Throwable {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        assertEquals("JFK!", soundex.soundex("J@eG[Fft)C"));
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for encode method with genealogy mapping
    @Test(timeout = 4000)
    public void testEncodeWithGenealogyMapping() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals("T262", soundex.US_ENGLISH.encode("The character is not mapped: "));
    }

    // Test for soundex method with a long string
    @Test(timeout = 4000)
    public void testSoundexWithLongString() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("O621", soundex.soundex("org.apache.commons.codec.language.SoundexUtils"));
    }

    // Test for soundex method with empty string using genealogy mapping
    @Test(timeout = 4000)
    public void testSoundexWithEmptyStringGenealogy() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("", soundex.US_ENGLISH_GENEALOGY.soundex(""));
    }

    // Test for encode method with null string using genealogy mapping
    @Test(timeout = 4000)
    public void testEncodeWithNullStringGenealogy() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        assertNull(soundex.US_ENGLISH_GENEALOGY.encode((String) null));
    }

    // Test for encode method with non-string object
    @Test(timeout = 4000)
    public void testEncodeWithNonStringObject() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        Object object = new Object();
        try {
            soundex.encode(object);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for encode method with unmapped character
    @Test(timeout = 4000)
    public void testEncodeWithUnmappedCharacter() throws Throwable {
        Soundex soundex = new Soundex("(G} al2\"Xwod?rQzh");
        try {
            soundex.encode((Object) "(G} al2\"Xwod?rQzh");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for Soundex constructor with custom mapping and max length
    @Test(timeout = 4000)
    public void testCustomMappingAndMaxLength() throws Throwable {
        Soundex soundex = new Soundex("p$oAP-;-^");
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for Soundex constructor with character array
    @Test(timeout = 4000)
    public void testConstructorWithCharArray() throws Throwable {
        char[] charArray = new char[4];
        Soundex soundex = new Soundex(charArray);
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for Soundex constructor with character array containing silent marker
    @Test(timeout = 4000)
    public void testConstructorWithCharArrayContainingSilentMarker() throws Throwable {
        char[] charArray = new char[1];
        charArray[0] = '-';
        Soundex soundex = new Soundex(charArray);
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for difference method with unmapped character
    @Test(timeout = 4000)
    public void testDifferenceWithUnmappedCharacter() throws Throwable {
        Soundex soundex = new Soundex("(G} al2\"Xwod?rQzh");
        try {
            soundex.difference("(G} al2\"Xwod?rQzh", "(G} al2\"Xwod?rQzh");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    // Test for encode method with valid string
    @Test(timeout = 4000)
    public void testEncodeWithValidString() throws Throwable {
        Soundex soundex = new Soundex();
        Object encoded = soundex.US_ENGLISH_GENEALOGY.encode((Object) "F424");
        assertEquals("F000", encoded);
        assertNotNull(encoded);
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for getting max length from simplified mapping
    @Test(timeout = 4000)
    public void testGetMaxLengthFromSimplifiedMapping() throws Throwable {
        Soundex soundex = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals(4, soundex.getMaxLength());
    }

    // Test for Soundex constructor with null string and special case HW
    @Test(timeout = 4000)
    public void testConstructorWithNullStringAndSpecialCaseHW() throws Throwable {
        try {
            new Soundex((String) null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}