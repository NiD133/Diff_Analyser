package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.Test;

public class MetaphoneTestTest21 extends AbstractStringEncoderTest<Metaphone> {

    public void assertIsMetaphoneEqual(final String source, final String[] matches) {
        // match source to all matches
        for (final String matche : matches) {
            assertTrue(getStringEncoder().isMetaphoneEqual(source, matche), "Source: " + source + ", should have same Metaphone as: " + matche);
        }
        // match to each other
        for (final String matche : matches) {
            for (final String matche2 : matches) {
                assertTrue(getStringEncoder().isMetaphoneEqual(matche, matche2));
            }
        }
    }

    public void assertMetaphoneEqual(final String[][] pairs) {
        validateFixture(pairs);
        for (final String[] pair : pairs) {
            final String name0 = pair[0];
            final String name1 = pair[1];
            final String failMsg = "Expected match between " + name0 + " and " + name1;
            assertTrue(getStringEncoder().isMetaphoneEqual(name0, name1), failMsg);
            assertTrue(getStringEncoder().isMetaphoneEqual(name1, name0), failMsg);
        }
    }

    @Override
    protected Metaphone createStringEncoder() {
        return new Metaphone();
    }

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

    @Test
    void testPHTOF() {
        assertEquals("FX", getStringEncoder().metaphone("PHISH"));
    }
}
