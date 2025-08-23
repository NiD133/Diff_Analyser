package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

// The test class and runner would ideally be renamed and simplified,
// but the focus here is on improving the test case itself.
// e.g., @RunWith(JUnit4.class) public class CharSequenceReaderTest { ... }
public class CharSequenceReader_ESTestTest19 extends CharSequenceReader_ESTest_scaffolding {

    @Test
    public void mark_whenReaderIsClosed_throwsIOException() throws IOException {
        // Arrange: Create a reader and immediately close it.
        // The actual content of the reader is irrelevant for this test.
        CharSequenceReader reader = new CharSequenceReader("any content");
        reader.close();

        // Act & Assert: Verify that calling mark() on a closed reader throws an exception.
        try {
            reader.mark(1); // The read-ahead limit does not matter.
            fail("Expected an IOException to be thrown because the reader is closed.");
        } catch (IOException expected) {
            // The checkOpen() method is expected to throw an IOException with this specific message.
            assertEquals("reader closed", expected.getMessage());
        }
    }
}