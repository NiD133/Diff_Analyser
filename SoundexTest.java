package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Soundex} class.
 *
 * <p>Ensure this file is saved with UTF-8 encoding for proper Javadoc processing.</p>
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    /**
     * Test encoding variations for names that should all map to "B650".
     */
    @Test
    void testEncodingVariationsForB650() throws EncoderException {
        String[] names = {
            "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM", "BOREN",
            "BORN", "BOURN", "BOURNE", "BOWRON", "BRAIN", "BRAME", "BRANN", "BRAUN",
            "BREEN", "BRIEN", "BRIM", "BRIMM", "BRINN", "BRION", "BROOM", "BROOME",
            "BROWN", "BROWNE", "BRUEN", "BRUHN", "BRUIN", "BRUMM", "BRUN", "BRUNO",
            "BRYAN", "BURIAN", "BURN", "BURNEY", "BYRAM", "BYRNE", "BYRON", "BYRUM"
        };
        checkEncodingVariations("B650", names);
    }

    /**
     * Test encoding with bad characters.
     */
    @Test
    void testEncodingWithBadCharacters() {
        assertEquals("H452", getStringEncoder().encode("HOL>MES"));
    }

    /**
     * Test the difference method for various cases.
     */
    @Test
    void testDifferenceMethod() throws EncoderException {
        // Edge cases
        assertEquals(0, getStringEncoder().difference(null, null));
        assertEquals(0, getStringEncoder().difference("", ""));
        assertEquals(0, getStringEncoder().difference(" ", " "));

        // Normal cases
        assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(2, getStringEncoder().difference("Ann", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
        assertEquals(0, getStringEncoder().difference("Janet", "Margaret"));

        // Examples from external sources
        assertEquals(4, getStringEncoder().difference("Green", "Greene"));
        assertEquals(0, getStringEncoder().difference("Blotchet-Halls", "Greene"));
        assertEquals(4, getStringEncoder().difference("Smithers", "Smythers"));
        assertEquals(2, getStringEncoder().difference("Anothers", "Brothers"));
    }

    /**
     * Test basic encoding functionality.
     */
    @Test
    void testBasicEncoding() {
        assertEquals("T235", getStringEncoder().encode("testing"));
        assertEquals("T000", getStringEncoder().encode("The"));
        assertEquals("Q200", getStringEncoder().encode("quick"));
        assertEquals("B650", getStringEncoder().encode("brown"));
        assertEquals("F200", getStringEncoder().encode("fox"));
        assertEquals("J513", getStringEncoder().encode("jumped"));
        assertEquals("O160", getStringEncoder().encode("over"));
        assertEquals("T000", getStringEncoder().encode("the"));
        assertEquals("L200", getStringEncoder().encode("lazy"));
        assertEquals("D200", getStringEncoder().encode("dogs"));
    }

    /**
     * Test encoding with a batch of names from an external source.
     */
    @Test
    void testEncodingBatchFromExternalSource() {
        assertEquals("A462", getStringEncoder().encode("Allricht"));
        assertEquals("E166", getStringEncoder().encode("Eberhard"));
        assertEquals("E521", getStringEncoder().encode("Engebrethson"));
        assertEquals("H512", getStringEncoder().encode("Heimbach"));
        assertEquals("H524", getStringEncoder().encode("Hanselmann"));
        assertEquals("H431", getStringEncoder().encode("Hildebrand"));
        assertEquals("K152", getStringEncoder().encode("Kavanagh"));
        assertEquals("L530", getStringEncoder().encode("Lind"));
        assertEquals("L222", getStringEncoder().encode("Lukaschowsky"));
        assertEquals("M235", getStringEncoder().encode("McDonnell"));
        assertEquals("M200", getStringEncoder().encode("McGee"));
        assertEquals("O155", getStringEncoder().encode("Opnian"));
        assertEquals("O155", getStringEncoder().encode("Oppenheimer"));
        assertEquals("R355", getStringEncoder().encode("Riedemanas"));
        assertEquals("Z300", getStringEncoder().encode("Zita"));
        assertEquals("Z325", getStringEncoder().encode("Zitzmeinn"));
    }

    /**
     * Test encoding with names from a specific genealogy source.
     */
    @Test
    void testGenealogyEncoding() {
        final Soundex s = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("H251", s.encode("Heggenburger"));
        assertEquals("B425", s.encode("Blackman"));
        assertEquals("S530", s.encode("Schmidt"));
        assertEquals("L150", s.encode("Lippmann"));
        assertEquals("D200", s.encode("Dodds"));
        assertEquals("D200", s.encode("Dhdds"));
        assertEquals("D200", s.encode("Dwdds"));
    }

    /**
     * Test encoding with silent characters.
     */
    @Test
    void testEncodingWithSilentCharacters() throws EncoderException {
        checkEncodingVariations("O165", new String[]{
            "OBrien", "'OBrien", "O'Brien", "OB'rien", "OBr'ien", "OBri'en", "OBrie'n", "OBrien'"
        });
    }

    /**
     * Test encoding with hyphens.
     */
    @Test
    void testEncodingWithHyphens() throws EncoderException {
        checkEncodingVariations("K525", new String[]{
            "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH", "KING-SMITH",
            "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-"
        });
    }

    /**
     * Test encoding with trimmable characters.
     */
    @Test
    void testEncodingWithTrimmableCharacters() {
        assertEquals("W252", getStringEncoder().encode(" \t\n\r Washington \t\n\r "));
    }

    /**
     * Test encoding with special HW rule.
     */
    @Test
    void testEncodingWithHWRule() {
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        assertEquals("Y330", getStringEncoder().encode("yehudit"));
        assertEquals("Y330", getStringEncoder().encode("yhwdyt"));
    }

    /**
     * Test encoding with MS SQL Server examples.
     */
    @Test
    void testMsSqlServerEncoding() {
        assertEquals("S530", getStringEncoder().encode("Smith"));
        assertEquals("S530", getStringEncoder().encode("Smythe"));
    }

    /**
     * Test encoding with MS SQL Server variations.
     */
    @Test
    void testMsSqlServerVariations() throws EncoderException {
        checkEncodingVariations("E625", new String[]{
            "Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen"
        });
    }

    /**
     * Test encoding with MS SQL Server names.
     */
    @Test
    void testMsSqlServerNames() {
        assertEquals("A500", getStringEncoder().encode("Ann"));
        assertEquals("A536", getStringEncoder().encode("Andrew"));
        assertEquals("J530", getStringEncoder().encode("Janet"));
        assertEquals("M626", getStringEncoder().encode("Margaret"));
        assertEquals("S315", getStringEncoder().encode("Steven"));
        assertEquals("M240", getStringEncoder().encode("Michael"));
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("L600", getStringEncoder().encode("Laura"));
        assertEquals("A500", getStringEncoder().encode("Anne"));
    }

    /**
     * Test creating new Soundex instances.
     */
    @Test
    void testNewSoundexInstances() {
        assertEquals("W452", new Soundex().soundex("Williams"));
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
    }

    /**
     * Test simplified Soundex encoding.
     */
    @Test
    void testSimplifiedSoundexEncoding() {
        final Soundex s = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals("W452", s.encode("WILLIAMS"));
        assertEquals("B625", s.encode("BARAGWANATH"));
        assertEquals("D540", s.encode("DONNELL"));
        assertEquals("L300", s.encode("LLOYD"));
        assertEquals("W422", s.encode("WOOLCOCK"));
        assertEquals("D320", s.encode("Dodds"));
        assertEquals("D320", s.encode("Dwdds"));
        assertEquals("D320", s.encode("Dhdds"));
    }

    /**
     * Test SoundexUtils construction and null behavior.
     */
    @Test
    void testSoundexUtils() {
        new SoundexUtils();
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));
    }

    /**
     * Test US English static Soundex instance.
     */
    @Test
    void testUsEnglishStaticInstance() {
        assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
    }

    /**
     * Test encoding with fancy characters not mapped by default US mapping.
     */
    @Test
    void testEncodingWithFancyCharacters() {
        assertEquals("E000", getStringEncoder().encode("e"));
        if (Character.isLetter('\u00e9')) { // e-acute
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00e9"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00e9"));
        }
    }

    /**
     * Test encoding with fancy characters not mapped by default US mapping.
     */
    @Test
    void testEncodingWithOWithDiaeresis() {
        assertEquals("O000", getStringEncoder().encode("o"));
        if (Character.isLetter('\u00f6')) { // o-umlaut
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00f6"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00f6"));
        }
    }

    /**
     * Test examples from Wikipedia's American Soundex page.
     */
    @Test
    void testWikipediaAmericanSoundexExamples() {
        assertEquals("R163", getStringEncoder().encode("Robert"));
        assertEquals("R163", getStringEncoder().encode("Rupert"));
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        assertEquals("T522", getStringEncoder().encode("Tymczak"));
        assertEquals("P236", getStringEncoder().encode("Pfister"));
    }
}