package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class GeneratedTestCase {

    @Test
    public void testNextWithException() {
        // GIVEN: A reader that always throws an IOException when readLine() is called.
        final Reader reader = new BufferedReader(new StringReader("")) {
            @Override
            public String readLine() throws IOException {
                throw new IOException("Simulated read error");
            }
        };

        // WHEN:  We create a LineIterator from the faulty reader and then attempt to check if there's a next line.
        // THEN:  An IllegalStateException should be thrown, because the LineIterator catches the IOException from readLine()
        //        and re-throws it wrapped in an IllegalStateException.
        try (LineIterator li = new LineIterator(reader)) {
            assertThrows(IllegalStateException.class, li::hasNext,
                    "Expected hasNext() to throw IllegalStateException when readLine() throws IOException.");
        }
    }
}