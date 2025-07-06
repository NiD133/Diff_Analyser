package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class SoundexTest {

    private Soundex defaultSoundex;
    private Soundex simplifiedSoundex;
    private Soundex genealogySoundex;

    @Before
    public void setUp() {
        defaultSoundex = Soundex.US_ENGLISH;
        simplifiedSoundex = Soundex.US_ENGLISH_SIMPLIFIED;
        genealogySoundex = Soundex.US_ENGLISH_GENEALOGY;
    }

    @After
    public void tearDown() {
        defaultSoundex = null;
        simplifiedSoundex = null;
        genealogySoundex = null;
    }

    @Test
    public void testDifferenceReturnsZeroForDissimilarStrings() {
        String str1 = "oJ'N*32WF]@/JLK";
        String str2 = ":ae";
        int difference = defaultSoundex.US_ENGLISH.difference(str1, str2);
        assertEquals("Difference between dissimilar strings should be 0", 0, difference);
    }

    @Test
    public void testConstructorWithMappingStringAndSpecialCaseHWFalse() {
        Soundex soundex = new Soundex("01230120022455012623010202", false);
        assertEquals("Max length should be 4 by default.", 4, soundex.getMaxLength());
    }

    @Test
    public void testSetMaxLength() {
        defaultSoundex.setMaxLength(0);
        assertEquals("Max length should be updated to 0", 0, defaultSoundex.getMaxLength());
        defaultSoundex.setMaxLength(4); // Reset to default for other tests
    }

    @Test
    public void testEncodeEmptyString() {
        String encoded = defaultSoundex.encode("");
        assertEquals("Encoding an empty string should return an empty string", "", encoded);
    }

    @Test
    public void testDifferenceReturnsZeroForDifferentStringsSimplified() {
        String str1 = "The character is not mapped: ";
        String str2 = "sDstPNyqQnJ?7";
        int difference = simplifiedSoundex.US_ENGLISH_GENEALOGY.difference(str1, str2);
        assertEquals("Difference between dissimilar strings should be 0", 0, difference);
    }

    @Test
    public void testDifferenceReturnsFourForSameStrings() {
        String str1 = "0jj2pNN4aBXE6a/UG\"";
        String str2 = "0jj2pNN4aBXE6a/UG\"";
        int difference = simplifiedSoundex.difference(str1, str2);
        assertEquals("Difference between identical strings should be 4", 4, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundexThrowsIllegalArgumentExceptionForInvalidMapping() {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        soundex.soundex("96;Siw");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullCharArrayThrowsNullPointerException() {
        new Soundex((char[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullStringThrowsNullPointerException() {
        new Soundex((String) null);
    }

    @Test
    public void testSoundexEncoding() {
        String input = "Xk8U,D.6D__ua)Hc~|Z";
        String encoded = defaultSoundex.soundex(input);
        assertEquals("Soundex encoding is incorrect", "X320", encoded);
    }

    @Test
    public void testSoundexEncodingSimplifiedVariant() {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        String input = ":t+2T#;4]b5I JeK";
        String encoded = soundex.US_ENGLISH_SIMPLIFIED.soundex(input);
        assertEquals("Soundex encoding for simplified variant is incorrect", "T122", encoded);
    }

    @Test
    public void testSoundexWithNullInputReturnsNull() {
        String encoded = defaultSoundex.US_ENGLISH.soundex((String) null);
        assertNull("Soundex encoding with null input should return null", encoded);
    }

    @Test
    public void testSoundexEncodingWithCustomMapping() {
        Soundex soundex = new Soundex("R87iF!K{&\"n2g_SRCD");
        String input = "J@eG[Fft)C";
        String encoded = soundex.soundex(input);
        assertEquals("Soundex encoding with custom mapping is incorrect", "JFK!", encoded);
    }

    @Test
    public void testEncodeReturnsCorrectValue() {
        String input = "The character is not mapped: ";
        String encoded = simplifiedSoundex.US_ENGLISH.encode(input);
        assertEquals("Encode method returns the wrong value", "T262", encoded);
    }

    @Test
    public void testSoundexEncodingGenealogyVariant() {
        String input = "org.apache.commons.codec.language.SoundexUtils";
        String encoded = genealogySoundex.US_ENGLISH_GENEALOGY.soundex(input);
        assertEquals("Soundex encoding for genealogy variant is incorrect", "O621", encoded);
    }

    @Test
    public void testSoundexEncodingEmptyStringGenealogy() {
        String encoded = genealogySoundex.US_ENGLISH_GENEALOGY.soundex("");
        assertEquals("Encoding empty string should return empty string", "", encoded);
    }

    @Test
    public void testEncodeNullStringGenealogyVariant() {
        String encoded = genealogySoundex.US_ENGLISH_GENEALOGY.encode((String) null);
        assertNull("Encoding null should return null", encoded);
    }

    @Test(expected = Exception.class)
    public void testEncodeNonStringObjectThrowsException() throws Exception {
        Object object = new Object();
        simplifiedSoundex.encode(object);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeThrowsIllegalArgumentExceptionForInvalidMapping() {
        Soundex soundex = new Soundex("(G} al2\"Xwod?rQzh");
        soundex.encode("(G} al2\"Xwod?rQzh");
    }

    @Test
    public void testConstructorWithStringArgument() {
        Soundex soundex = new Soundex("p$oAP-;-^");
        assertEquals("Max length should be 4 by default.", 4, soundex.getMaxLength());
    }

    @Test
    public void testConstructorWithCharArray() {
        char[] charArray = new char[4];
        Soundex soundex = new Soundex(charArray);
        assertEquals("Max length should be 4 by default.", 4, soundex.getMaxLength());
    }

    @Test
    public void testConstructorWithCharArrayContainingHyphen() {
        char[] charArray = new char[1];
        charArray[0] = '-';
        Soundex soundex = new Soundex(charArray);
        assertEquals("Max length should be 4 by default.", 4, soundex.getMaxLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferenceThrowsIllegalArgumentExceptionForInvalidMapping() {
         Soundex soundex = new Soundex("(G} al2\"Xwod?rQzh");
         soundex.difference("(G} al2\"Xwod?rQzh", "(G} al2\"Xwod?rQzh");
    }

    @Test
    public void testEncodeObject() {
        Object encoded = simplifiedSoundex.US_ENGLISH_GENEALOGY.encode((Object) "F424");
        assertEquals("Object should be encoded correctly", "F000", encoded);
        assertNotNull(encoded);
    }

    @Test
    public void testGetMaxLengthReturnsCorrectValue() {
        assertEquals("Max length should be 4", 4, simplifiedSoundex.getMaxLength());
    }

     @Test(expected = NullPointerException.class)
    public void testConstructorWithNullStringAndBooleanThrowsNullPointerException() {
        new Soundex((String) null, true);
    }
}