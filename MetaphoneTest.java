package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Metaphone class.
 */
class MetaphoneTest extends AbstractStringEncoderTest<Metaphone> {

    /**
     * Asserts that the Metaphone encoding of the source string matches all the expected strings.
     */
    public void assertIsMetaphoneEqual(final String source, final String[] expectedMatches) {
        // Check if source matches each expected string
        for (final String expected : expectedMatches) {
            assertTrue(getStringEncoder().isMetaphoneEqual(source, expected),
                "Source: " + source + ", should have same Metaphone as: " + expected);
        }
        // Check if each expected string matches every other expected string
        for (final String expected1 : expectedMatches) {
            for (final String expected2 : expectedMatches) {
                assertTrue(getStringEncoder().isMetaphoneEqual(expected1, expected2));
            }
        }
    }

    /**
     * Asserts that all pairs of strings have equal Metaphone encodings.
     */
    public void assertMetaphoneEqual(final String[][] pairs) {
        validateFixture(pairs);
        for (final String[] pair : pairs) {
            final String first = pair[0];
            final String second = pair[1];
            final String failMsg = "Expected match between " + first + " and " + second;
            assertTrue(getStringEncoder().isMetaphoneEqual(first, second), failMsg);
            assertTrue(getStringEncoder().isMetaphoneEqual(second, first), failMsg);
        }
    }

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

    @Test
    void testDiscardOfSCEOrSCIOrSCY() {
        assertEquals("SNS", getStringEncoder().metaphone("SCIENCE"));
        assertEquals("SN", getStringEncoder().metaphone("SCENE"));
        assertEquals("S", getStringEncoder().metaphone("SCY"));
    }

    @Test
    void testDiscardOfSilentGN() {
        assertEquals("N", getStringEncoder().metaphone("GNU"));
        assertEquals("SNT", getStringEncoder().metaphone("SIGNED"));
    }

    @Test
    void testDiscardOfSilentHAfterG() {
        assertEquals("KNT", getStringEncoder().metaphone("GHENT"));
        assertEquals("B", getStringEncoder().metaphone("BAUGH"));
    }

    @Test
    void testExceedLength() {
        assertEquals("AKSK", getStringEncoder().metaphone("AXEAXE"));
    }

    @Test
    void testIsMetaphoneEqual1() {
        assertMetaphoneEqual(new String[][] {
            { "Case", "case" },
            { "CASE", "Case" },
            { "caSe", "cAsE" },
            { "quick", "cookie" }
        });
    }

    @Test
    void testIsMetaphoneEqual2() {
        assertMetaphoneEqual(new String[][] {
            { "Lawrence", "Lorenza" },
            { "Gary", "Cahra" }
        });
    }

    @Test
    void testIsMetaphoneEqualAero() {
        assertIsMetaphoneEqual("Aero", new String[] { "Eure" });
    }

    @Test
    void testIsMetaphoneEqualAlbert() {
        assertIsMetaphoneEqual("Albert", new String[] {
            "Ailbert", "Alberik", "Albert", "Alberto", "Albrecht"
        });
    }

    @Test
    void testIsMetaphoneEqualGary() {
        assertIsMetaphoneEqual("Gary", new String[] {
            "Cahra", "Cara", "Carey", "Cari", "Caria", "Carie", "Caro", "Carree", "Carri", "Carrie", "Carry", "Cary",
            "Cora", "Corey", "Cori", "Corie", "Correy", "Corri", "Corrie", "Corry", "Cory", "Gray", "Kara", "Kare",
            "Karee", "Kari", "Karia", "Karie", "Karrah", "Karrie", "Karry", "Kary", "Keri", "Kerri", "Kerrie", "Kerry",
            "Kira", "Kiri", "Kora", "Kore", "Kori", "Korie", "Korrie", "Korry"
        });
    }

    @Test
    void testIsMetaphoneEqualJohn() {
        assertIsMetaphoneEqual("John", new String[] {
            "Gena", "Gene", "Genia", "Genna", "Genni", "Gennie", "Genny", "Giana", "Gianna", "Gina", "Ginni", "Ginnie",
            "Ginny", "Jaine", "Jan", "Jana", "Jane", "Janey", "Jania", "Janie", "Janna", "Jany", "Jayne", "Jean",
            "Jeana", "Jeane", "Jeanie", "Jeanna", "Jeanne", "Jeannie", "Jen", "Jena", "Jeni", "Jenn", "Jenna", "Jennee",
            "Jenni", "Jennie", "Jenny", "Jinny", "Jo Ann", "Jo-Ann", "Jo-Anne", "Joan", "Joana", "Joane", "Joanie",
            "Joann", "Joanna", "Joanne", "Joeann", "Johna", "Johnna", "Joni", "Jonie", "Juana", "June", "Junia", "Junie"
        });
    }

    @Test
    void testIsMetaphoneEqualKnight() {
        assertIsMetaphoneEqual("Knight", new String[] {
            "Hynda", "Nada", "Nadia", "Nady", "Nat", "Nata", "Natty", "Neda", "Nedda", "Nedi", "Netta", "Netti",
            "Nettie", "Netty", "Nita", "Nydia"
        });
    }

    @Test
    void testIsMetaphoneEqualMary() {
        assertIsMetaphoneEqual("Mary", new String[] {
            "Mair", "Maire", "Mara", "Mareah", "Mari", "Maria", "Marie", "Mary", "Maura", "Maure", "Meara", "Merrie",
            "Merry", "Mira", "Moira", "Mora", "Moria", "Moyra", "Muire", "Myra", "Myrah"
        });
    }

    @Test
    void testIsMetaphoneEqualParis() {
        assertIsMetaphoneEqual("Paris", new String[] {
            "Pearcy", "Perris", "Piercy", "Pierz", "Pryse"
        });
    }

    @Test
    void testIsMetaphoneEqualPeter() {
        assertIsMetaphoneEqual("Peter", new String[] {
            "Peadar", "Peder", "Pedro", "Peter", "Petr", "Peyter", "Pieter", "Pietro", "Piotr"
        });
    }

    @Test
    void testIsMetaphoneEqualRay() {
        assertIsMetaphoneEqual("Ray", new String[] {
            "Ray", "Rey", "Roi", "Roy", "Ruy"
        });
    }

    @Test
    void testIsMetaphoneEqualSusan() {
        assertIsMetaphoneEqual("Susan", new String[] {
            "Siusan", "Sosanna", "Susan", "Susana", "Susann", "Susanna", "Susannah", "Susanne", "Suzann", "Suzanna",
            "Suzanne", "Zuzana"
        });
    }

    @Test
    void testIsMetaphoneEqualWhite() {
        assertIsMetaphoneEqual("White", new String[] {
            "Wade", "Wait", "Waite", "Wat", "Whit", "Wiatt", "Wit", "Wittie", "Witty", "Wood", "Woodie", "Woody"
        });
    }

    @Test
    void testIsMetaphoneEqualWright() {
        assertIsMetaphoneEqual("Wright", new String[] {
            "Rota", "Rudd", "Ryde"
        });
    }

    @Test
    void testIsMetaphoneEqualXalan() {
        assertIsMetaphoneEqual("Xalan", new String[] {
            "Celene", "Celina", "Celine", "Selena", "Selene", "Selina", "Seline", "Suellen", "Xylina"
        });
    }

    @Test
    void testMetaphone() {
        assertEquals("HL", getStringEncoder().metaphone("howl"));
        assertEquals("TSTN", getStringEncoder().metaphone("testing"));
        assertEquals("0", getStringEncoder().metaphone("The"));
        assertEquals("KK", getStringEncoder().metaphone("quick"));
        assertEquals("BRN", getStringEncoder().metaphone("brown"));
        assertEquals("FKS", getStringEncoder().metaphone("fox"));
        assertEquals("JMPT", getStringEncoder().metaphone("jumped"));
        assertEquals("OFR", getStringEncoder().metaphone("over"));
        assertEquals("0", getStringEncoder().metaphone("the"));
        assertEquals("LS", getStringEncoder().metaphone("lazy"));
        assertEquals("TKS", getStringEncoder().metaphone("dogs"));
    }

    @Test
    void testPHTOF() {
        assertEquals("FX", getStringEncoder().metaphone("PHISH"));
    }

    @Test
    void testSetMaxLengthWithTruncation() {
        getStringEncoder().setMaxCodeLen(6);
        assertEquals("AKSKSK", getStringEncoder().metaphone("AXEAXEAXE"));
    }

    @Test
    void testSHAndSIOAndSIAToX() {
        assertEquals("XT", getStringEncoder().metaphone("SHOT"));
        assertEquals("OTXN", getStringEncoder().metaphone("ODSIAN"));
        assertEquals("PLXN", getStringEncoder().metaphone("PULSION"));
    }

    @Test
    void testTCH() {
        assertEquals("RX", getStringEncoder().metaphone("RETCH"));
        assertEquals("WX", getStringEncoder().metaphone("WATCH"));
    }

    @Test
    void testTIOAndTIAToX() {
        assertEquals("OX", getStringEncoder().metaphone("OTIA"));
        assertEquals("PRXN", getStringEncoder().metaphone("PORTION"));
    }

    @Test
    void testTranslateOfSCHAndCH() {
        assertEquals("SKTL", getStringEncoder().metaphone("SCHEDULE"));
        assertEquals("SKMT", getStringEncoder().metaphone("SCHEMATIC"));
        assertEquals("KRKT", getStringEncoder().metaphone("CHARACTER"));
        assertEquals("TX", getStringEncoder().metaphone("TEACH"));
    }

    @Test
    void testTranslateToJOfDGEOrDGIOrDGY() {
        assertEquals("TJ", getStringEncoder().metaphone("DODGY"));
        assertEquals("TJ", getStringEncoder().metaphone("DODGE"));
        assertEquals("AJMT", getStringEncoder().metaphone("ADGIEMTI"));
    }

    @Test
    void testWhy() {
        assertEquals("", getStringEncoder().metaphone("WHY"));
    }

    @Test
    void testWordEndingInMB() {
        assertEquals("KM", getStringEncoder().metaphone("COMB"));
        assertEquals("TM", getStringEncoder().metaphone("TOMB"));
        assertEquals("WM", getStringEncoder().metaphone("WOMB"));
    }

    @Test
    void testWordsWithCIA() {
        assertEquals("XP", getStringEncoder().metaphone("CIAPO"));
    }

    /**
     * Validates the test fixture for correct structure.
     */
    public void validateFixture(final String[][] pairs) {
        if (pairs.length == 0) {
            fail("Test fixture is empty");
        }
        for (int i = 0; i < pairs.length; i++) {
            if (pairs[i].length != 2) {
                fail("Error in test fixture in the data array at index " + i);
            }
        }
    }
}