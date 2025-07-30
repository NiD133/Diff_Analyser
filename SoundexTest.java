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
 * <p>Ensure this file is saved in UTF-8 encoding for proper Javadoc processing.</p>
 */
class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    void testEncodingForB650() throws EncoderException {
        String[] names = {
            "BARHAM", "BARONE", "BARRON", "BERNA", "BIRNEY", "BIRNIE", "BOOROM", "BOREN", "BORN",
            "BOURN", "BOURNE", "BOWRON", "BRAIN", "BRAME", "BRANN", "BRAUN", "BREEN", "BRIEN",
            "BRIM", "BRIMM", "BRINN", "BRION", "BROOM", "BROOME", "BROWN", "BROWNE", "BRUEN",
            "BRUHN", "BRUIN", "BRUMM", "BRUN", "BRUNO", "BRYAN", "BURIAN", "BURN", "BURNEY",
            "BYRAM", "BYRNE", "BYRON", "BYRUM"
        };
        checkEncodingVariations("B650", names);
    }

    @Test
    void testEncodingWithBadCharacters() {
        assertEquals("H452", getStringEncoder().encode("HOL>MES"));
    }

    @Test
    void testDifferenceCalculation() throws EncoderException {
        // Edge cases
        assertEquals(0, getStringEncoder().difference(null, null));
        assertEquals(0, getStringEncoder().difference("", ""));
        assertEquals(0, getStringEncoder().difference(" ", " "));
        
        // Normal cases
        assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(2, getStringEncoder().difference("Ann", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
        assertEquals(0, getStringEncoder().difference("Janet", "Margaret"));
        
        // Examples from documentation
        assertEquals(4, getStringEncoder().difference("Green", "Greene"));
        assertEquals(0, getStringEncoder().difference("Blotchet-Halls", "Greene"));
        assertEquals(4, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(4, getStringEncoder().difference("Smithers", "Smythers"));
        assertEquals(2, getStringEncoder().difference("Anothers", "Brothers"));
    }

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

    @Test
    void testEncodingBatch2() {
        String[] names = {
            "Allricht", "Eberhard", "Engebrethson", "Heimbach", "Hanselmann",
            "Hildebrand", "Kavanagh", "Lind", "Lukaschowsky", "McDonnell",
            "McGee", "Opnian", "Oppenheimer", "Riedemanas", "Zita", "Zitzmeinn"
        };
        String[] expectedCodes = {
            "A462", "E166", "E521", "H512", "H524", "H431", "K152", "L530",
            "L222", "M235", "M200", "O155", "O155", "R355", "Z300", "Z325"
        };
        for (int i = 0; i < names.length; i++) {
            assertEquals(expectedCodes[i], getStringEncoder().encode(names[i]));
        }
    }

    @Test
    void testEncodingBatch3() {
        String[] names = {
            "Washington", "Lee", "Gutierrez", "Pfister", "Jackson", "Tymczak", "VanDeusen"
        };
        String[] expectedCodes = {
            "W252", "L000", "G362", "P236", "J250", "T522", "V532"
        };
        for (int i = 0; i < names.length; i++) {
            assertEquals(expectedCodes[i], getStringEncoder().encode(names[i]));
        }
    }

    @Test
    void testEncodingBatch4() {
        String[] names = {
            "HOLMES", "ADOMOMI", "VONDERLEHR", "BALL", "SHAW", "JACKSON", "SCANLON", "SAINTJOHN"
        };
        String[] expectedCodes = {
            "H452", "A355", "V536", "B400", "S000", "J250", "S545", "S532"
        };
        for (int i = 0; i < names.length; i++) {
            assertEquals(expectedCodes[i], getStringEncoder().encode(names[i]));
        }
    }

    @Test
    void testEncodingIgnoringApostrophes() throws EncoderException {
        String[] names = {
            "OBrien", "'OBrien", "O'Brien", "OB'rien", "OBr'ien", "OBri'en", "OBrie'n", "OBrien'"
        };
        checkEncodingVariations("O165", names);
    }

    @Test
    void testEncodingIgnoringHyphens() throws EncoderException {
        String[] names = {
            "KINGSMITH", "-KINGSMITH", "K-INGSMITH", "KI-NGSMITH", "KIN-GSMITH",
            "KING-SMITH", "KINGS-MITH", "KINGSM-ITH", "KINGSMI-TH", "KINGSMIT-H", "KINGSMITH-"
        };
        checkEncodingVariations("K525", names);
    }

    @Test
    void testEncodingIgnoringWhitespace() {
        assertEquals("W252", getStringEncoder().encode(" \t\n\r Washington \t\n\r "));
    }

    @Test
    void testGenealogyEncoding() {
        final Soundex genealogySoundex = Soundex.US_ENGLISH_GENEALOGY;
        assertEquals("H251", genealogySoundex.encode("Heggenburger"));
        assertEquals("B425", genealogySoundex.encode("Blackman"));
        assertEquals("S530", genealogySoundex.encode("Schmidt"));
        assertEquals("L150", genealogySoundex.encode("Lippmann"));
        assertEquals("D200", genealogySoundex.encode("Dodds"));
        assertEquals("D200", genealogySoundex.encode("Dhdds"));
        assertEquals("D200", genealogySoundex.encode("Dwdds"));
    }

    @Test
    void testHWRuleExample1() {
        assertEquals("A261", getStringEncoder().encode("Ashcraft"));
        assertEquals("A261", getStringEncoder().encode("Ashcroft"));
        assertEquals("Y330", getStringEncoder().encode("yehudit"));
        assertEquals("Y330", getStringEncoder().encode("yhwdyt"));
    }

    @Test
    void testHWRuleExample2() {
        assertEquals("B312", getStringEncoder().encode("BOOTHDAVIS"));
        assertEquals("B312", getStringEncoder().encode("BOOTH-DAVIS"));
    }

    @Test
    void testHWRuleExample3() throws EncoderException {
        assertEquals("S460", getStringEncoder().encode("Sgler"));
        assertEquals("S460", getStringEncoder().encode("Swhgler"));
        String[] names = {
            "SAILOR", "SALYER", "SAYLOR", "SCHALLER", "SCHELLER", "SCHILLER",
            "SCHOOLER", "SCHULER", "SCHUYLER", "SEILER", "SEYLER", "SHOLAR",
            "SHULER", "SILAR", "SILER", "SILLER"
        };
        checkEncodingVariations("S460", names);
    }

    @Test
    void testMsSqlServerExample1() {
        assertEquals("S530", getStringEncoder().encode("Smith"));
        assertEquals("S530", getStringEncoder().encode("Smythe"));
    }

    @Test
    void testMsSqlServerExample2() throws EncoderException {
        String[] names = {"Erickson", "Erickson", "Erikson", "Ericson", "Ericksen", "Ericsen"};
        checkEncodingVariations("E625", names);
    }

    @Test
    void testMsSqlServerExample3() {
        String[] names = {"Ann", "Andrew", "Janet", "Margaret", "Steven", "Michael", "Robert", "Laura", "Anne"};
        String[] expectedCodes = {"A500", "A536", "J530", "M626", "S315", "M240", "R163", "L600", "A500"};
        for (int i = 0; i < names.length; i++) {
            assertEquals(expectedCodes[i], getStringEncoder().encode(names[i]));
        }
    }

    @Test
    void testNewSoundexInstance() {
        assertEquals("W452", new Soundex().soundex("Williams"));
    }

    @Test
    void testNewSoundexInstanceWithCustomMapping() {
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("Williams"));
    }

    @Test
    void testNewSoundexInstanceWithStringMapping() {
        assertEquals("W452", new Soundex(Soundex.US_ENGLISH_MAPPING_STRING).soundex("Williams"));
    }

    @Test
    void testSimplifiedSoundexEncoding() {
        final Soundex simplifiedSoundex = Soundex.US_ENGLISH_SIMPLIFIED;
        assertEquals("W452", simplifiedSoundex.encode("WILLIAMS"));
        assertEquals("B625", simplifiedSoundex.encode("BARAGWANATH"));
        assertEquals("D540", simplifiedSoundex.encode("DONNELL"));
        assertEquals("L300", simplifiedSoundex.encode("LLOYD"));
        assertEquals("W422", simplifiedSoundex.encode("WOOLCOCK"));
        assertEquals("D320", simplifiedSoundex.encode("Dodds"));
        assertEquals("D320", simplifiedSoundex.encode("Dwdds"));
        assertEquals("D320", simplifiedSoundex.encode("Dhdds"));
    }

    @Test
    void testSoundexUtilsConstruction() {
        new SoundexUtils();
    }

    @Test
    void testSoundexUtilsNullHandling() {
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));
    }

    @Test
    void testUsEnglishStaticInstance() {
        assertEquals("W452", Soundex.US_ENGLISH.soundex("Williams"));
    }

    @Test
    void testUsMappingEWithAcute() {
        assertEquals("E000", getStringEncoder().encode("e"));
        if (Character.isLetter('\u00e9')) { // e-acute
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00e9"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00e9"));
        }
    }

    @Test
    void testUsMappingOWithDiaeresis() {
        assertEquals("O000", getStringEncoder().encode("o"));
        if (Character.isLetter('\u00f6')) { // o-umlaut
            assertThrows(IllegalArgumentException.class, () -> getStringEncoder().encode("\u00f6"));
        } else {
            assertEquals("", getStringEncoder().encode("\u00f6"));
        }
    }

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