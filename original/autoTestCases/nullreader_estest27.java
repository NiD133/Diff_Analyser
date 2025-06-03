package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NullReaderTest {

    @Test
    void testSkipBeyondEndCausesEOFException() throws IOException {
        // Arrange: Create a NullReader with initial size -348 (irrelevant as skip is called first),
        // that throws EOFException on beyond-end reads, and does *not* throw an exception if the size is negative.
        NullReader reader = new NullReader(-348L, true, true);

        // Act: Skip a negative number of bytes (-348).  This is allowed by the NullReader implementation, and resets the position to 0.
        reader.skip(-348L);

        // Act & Assert: Attempting to skip another negative number of bytes should now throw an EOFException because the reader's internal state
        // determines that any further read is "beyond the end".

        // Expected behaviour: Skipping again with the same amount should now throw an EOFException.
        assertThrows(EOFException.class, () -> {
            reader.skip(-348L);
        }, "Skipping beyond the effective end of the reader should throw an EOFException.");
    }
}