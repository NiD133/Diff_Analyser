package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.EncoderException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Soundex_ESTest extends Soundex_ESTest_scaffolding {

    private static final String UNMAPPED_CHARACTER_EXCEPTION_MESSAGE = "The character is not mapped: ";
    private static final String NULL_POINTER_EXCEPTION_MESSAGE = "no message in exception (getMessage() returned null)";
    
    @Test(timeout = 4000)
    public void testDifferenceWithUnmappedCharacter() {
        char[] customMapping = new char[5];
        Soundex soundex = new Soundex(customMapping);
        try {
            soundex.difference("EF\"kniaAVspLJDz", "$I<`&-Dq*{");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetAndGetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(-2841);
        int maxLength = soundex.getMaxLength();
        assertEquals(-2841, maxLength);
    }

    @Test(timeout = 4000)
    public void testEncodeNullString() {
        char[] customMapping = new char[3];
        Soundex soundex = new Soundex(customMapping);
        String encodedString = soundex.US_ENGLISH_GENEALOGY.encode(null);
        assertNull(encodedString);
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        String encodedString = soundex.encode("dAiwv)]=F=ceL=T[CcF");
        assertEquals("D124", encodedString);
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testSoundexWithUnmappedCharacter() {
        Soundex soundex = new Soundex("IW=es%O[9p.u.:", true);
        try {
            soundex.soundex("?9+dzZ o|D}!;?at7Q@");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeWithUnmappedCharacter() {
        Soundex soundex = new Soundex("@'0g");
        try {
            soundex.encode("@'0g");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeObjectWithUnmappedCharacter() {
        Soundex soundex = new Soundex("]jH");
        try {
            soundex.encode((Object) "]jH");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexConstructorWithNullCharArray() {
        try {
            new Soundex((char[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexConstructorWithNullString() {
        try {
            new Soundex((String) null, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexConstructorWithNullStringDefault() {
        try {
            new Soundex((String) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testSoundexUS_ENGLISH() {
        Soundex soundex = new Soundex();
        String encodedString = soundex.US_ENGLISH.soundex("[&L!ug<F4wFviM+`RV{");
        assertEquals("L215", encodedString);
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testSoundexUS_ENGLISHInstance() {
        Soundex soundex = Soundex.US_ENGLISH;
        String encodedString = soundex.soundex("}}'3N[nM+hnR ayR6");
        assertEquals("N660", encodedString);
    }

    @Test(timeout = 4000)
    public void testSoundexWithSingleCharacter() {
        Soundex soundex = Soundex.US_ENGLISH;
        String encodedString = soundex.US_ENGLISH.soundex(")");
        assertEquals("", encodedString);
    }

    @Test(timeout = 4000)
    public void testDifferenceWithIdenticalStrings() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("Hr~hEi", "Hr~hEi");
        assertEquals(4, soundex.getMaxLength());
        assertEquals(4, difference);
    }

    @Test(timeout = 4000)
    public void testSoundexUS_ENGLISH_GENEALOGY() {
        Soundex soundex = Soundex.US_ENGLISH_GENEALOGY;
        String encodedString = soundex.soundex("org.apache.commons.codec.language.Soundex");
        assertEquals("O621", encodedString);
    }

    @Test(timeout = 4000)
    public void testDifferenceWithDifferentStrings() {
        Soundex soundex = new Soundex();
        int difference = soundex.difference("01230120022455012623010202", "6]5]'j=[IE=9");
        assertEquals(4, soundex.getMaxLength());
        assertEquals(0, difference);
    }

    @Test(timeout = 4000)
    public void testSoundexWithNullString() {
        Soundex soundex = new Soundex();
        soundex.soundex(null);
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        Object encodedObject = soundex.encode((Object) "atW+2N,x7`1kf@");
        assertNotNull(encodedObject);
        assertEquals(4, soundex.getMaxLength());
        assertEquals("A352", encodedObject);
    }

    @Test(timeout = 4000)
    public void testSoundexConstructorWithCustomMapping() {
        Soundex soundex = new Soundex("/HU+\"}{6.):e:-&!R");
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testSoundexConstructorWithSingleCharMapping() {
        char[] customMapping = new char[1];
        customMapping[0] = '-';
        Soundex soundex = new Soundex(customMapping);
        assertEquals(4, soundex.getMaxLength());
    }

    @Test(timeout = 4000)
    public void testDifferenceWithUnmappedCharacterInBothStrings() {
        char[] customMapping = new char[8];
        Soundex soundex = new Soundex(customMapping);
        try {
            soundex.difference("AW1D!AKs", "AW1D!AKs");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }

    @Test(timeout = 4000)
    public void testEncodeEmptyString() {
        char[] customMapping = new char[8];
        Soundex soundex = new Soundex(customMapping);
        String encodedString = soundex.encode("");
        assertEquals(4, soundex.getMaxLength());
        assertEquals("", encodedString);
        assertNotNull(encodedString);
    }

    @Test(timeout = 4000)
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        assertEquals(4, maxLength);
    }

    @Test(timeout = 4000)
    public void testEncodeObjectWithInvalidType() {
        Soundex soundex = new Soundex("org.apache.commons.codec.EncoderException", false);
        Object invalidObject = new Object();
        try {
            soundex.US_ENGLISH_SIMPLIFIED.encode(invalidObject);
            fail("Expecting exception: Exception");
        } catch (Exception e) {
            verifyException("org.apache.commons.codec.language.Soundex", e);
        }
    }
}