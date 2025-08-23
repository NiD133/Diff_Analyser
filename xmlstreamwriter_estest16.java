package org.apache.commons.io.output;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Tests for the {@link XmlStreamWriter} class, focusing on constructor validation.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the constructor {@code XmlStreamWriter(File)} throws a
     * NullPointerException when the provided file is null. This is the expected
     * behavior as a null file cannot be used to create an output stream.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullFileShouldThrowNullPointerException() throws FileNotFoundException {
        // This line is expected to throw a NullPointerException because the constructor
        // attempts to create a FileOutputStream with a null File argument.
        new XmlStreamWriter((File) null);
    }
}