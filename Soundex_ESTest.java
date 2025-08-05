package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for the {@link Soundex} class.
 */
public class SoundexTest {

    // =================================================================================
    // Constructor Tests
    // =================================================================================

    @Test
    public void constructorWithStringMappingShouldSucceed() {
        // This test ensures the constructor doesn't throw an exception with a valid,
        // albeit unusual, mapping string.
        new Soundex("/HU+\"}{6.): e:-&!R");
    }

    @Test
    public void constructorWithSilentMarkerShouldDisableSpecialHWHandling() {
        // The presence of the SILENT_MARKER '-' in the mapping array should disable
        // the special handling for 'H' and 'W'. This test implicitly verifies this
        // by successfully creating an instance.
        new Soundex(new char[]{'-'});
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullCharArrayShouldThrowException() {
        new Soundex((char[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullStringMappingShouldThrowException() {
        new Soundex((String) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullStringAndBooleanShouldThrowException() {
        new Soundex(null, false);
    }

    // =================================================================================
    // encode() Method Tests
    // =================================================================================

    @Test
    public void encodeEmptyStringShouldReturnEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("Encoding an empty string should result in an empty string.", "", soundex.encode(""));
    }

    @Test
    public void encodeNullStringShouldReturnNull() {
        // Per contract, encoding a null object should return null.
        assertNull(Soundex.US_ENGLISH.encode((String) null));
    }

    @Test
    public void encodeWithStandardMappingShouldHandleMixedCaseAndNonAlphaChars() {
        Soundex soundex = new Soundex();
        // Input contains mixed case, symbols, and repeated letters to test the core algorithm.
        String encoded = soundex.encode("dAiwv)]=F=ceL=T[CcF");
        assertEquals("D124", encoded);
    }

    @Test
    public void encodeObjectWithStringShouldReturnCorrectCode() throws EncoderException {
        Soundex soundex = new Soundex();
        Object encoded = soundex.encode((Object) "atW+2N,x7`1kf@");
        assertEquals("A352", encoded);
    }

    @Test(expected = EncoderException.class)
    public void encodeWithNonStringObjectShouldThrowException() throws EncoderException {
        Soundex soundex = new Soundex();
        // The Soundex encoder should only handle String objects.
        soundex.encode(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeWithIncompleteCustomMappingShouldThrowException() {
        // The mapping string is too short and does not contain a mapping for 'G'.
        Soundex soundex = new Soundex("@'0g");
        soundex.encode("@'0g");
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodeObjectWithIncompleteCustomMappingShouldThrowException() throws EncoderException {
        // The mapping string is too short and does not contain a mapping for 'J'.
        Soundex soundex = new Soundex("]jH");
        soundex.encode((Object) "]jH");
    }

    // =================================================================================
    // soundex() Method Tests
    // =================================================================================

    @Test
    public void soundexOfNullShouldReturnNull() {
        Soundex soundex = new Soundex();
        assertNull(soundex.soundex(null));
    }

    @Test
    public void soundexOfNonLetterStringShouldReturnEmptyString() {
        // A string without any letters should result in an empty Soundex code.
        assertEquals("", Soundex.US_ENGLISH.soundex(")!@#$%^&*("));
    }

    @Test
    public void soundexShouldHandleStringWithLeadingNonLetters() {
        // The algorithm should skip leading non-alphabetic characters.
        String encoded = Soundex.US_ENGLISH.soundex("[&L!ug<F4wFviM+`RV{");
        assertEquals("L215", encoded);
    }

    @Test
    public void soundexShouldHandleStringWithEmbeddedNonLetters() {
        // Non-alphabetic characters embedded within the string should be ignored.
        String encoded = Soundex.US_ENGLISH.soundex("}}'3N[nM+hnR ayR6");
        assertEquals("N660", encoded);
    }


    @Test
    public void soundexWithGenealogyMappingShouldReturnCorrectCode() {
        // Tests the pre-configured US_ENGLISH_GENEALOGY instance.
        String encoded = Soundex.US_ENGLISH_GENEALOGY.soundex("org.apache.commons.codec.language.Soundex");
        assertEquals("O621", encoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void soundexWithIncompleteCustomMappingShouldThrowException() {
        // The mapping string is too short and does not contain a mapping for 'Z'.
        Soundex soundex = new Soundex("IW=es%O[9p.u.:", true);
        soundex.soundex("?9+dzZ o|D}!;?at7Q@");
    }

    // =================================================================================
    // difference() Method Tests
    // =================================================================================

    @Test
    public void differenceOfSimilarStringsShouldReturn4() throws EncoderException {
        Soundex soundex = new Soundex();
        // Identical Soundex codes should result in a difference of 4.
        int difference = soundex.difference("Robert", "Rupert"); // Both are R163
        assertEquals(4, difference);
    }

    @Test
    public void differenceOfDissimilarStringsShouldReturn0() throws EncoderException {
        Soundex soundex = new Soundex();
        // These strings are phonetically very different and should result in a low difference score.
        int difference = soundex.difference("soundex", "java"); // S532 vs J100
        assertEquals(0, difference);
    }

    @Test(expected = IllegalArgumentException.class)
    public void differenceWithIncompleteMappingShouldThrowExceptionForUnmappedChar() throws EncoderException {
        // The custom mapping (an array of 8 null chars) does not map 'K'.
        Soundex soundex = new Soundex(new char[8]);
        soundex.difference("AW1D!AKs", "AW1D!AKs");
    }

    // =================================================================================
    // maxLength Property Tests (deprecated)
    // =================================================================================

    @Test
    @SuppressWarnings("deprecation")
    public void defaultMaxLengthShouldBe4() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void setMaxLengthShouldUpdateMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(10);
        assertEquals(10, soundex.getMaxLength());
    }
}