package org.apache.commons.io.input;

import org.junit.Test;
import java.io.Reader;

/**
 * Tests for the {@link SequenceReader} class, focusing on constructor behavior.
 */
public class SequenceReaderTest {

    /**
     * Verifies that the constructor throws a NullPointerException when passed a null
     * array of Readers. The underlying call to Arrays.asList(null) is expected to
     * trigger this behavior.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullReaderArrayShouldThrowNPE() {
        // Attempt to construct a SequenceReader with a null varargs array.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        new SequenceReader((Reader[]) null);
    }
}