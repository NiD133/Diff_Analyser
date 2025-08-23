package org.apache.commons.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link HexDump}.
 * This class focuses on providing clear, understandable test cases.
 */
public class HexDumpTest {

    /**
     * Tests that calling dump() with a null Appendable throws a NullPointerException.
     * This is the expected behavior as per the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void dumpWithNullAppendableShouldThrowNullPointerException() throws IOException {
        // Arrange: Create some arbitrary data to pass to the method.
        // The content and size of this array do not matter for this test case,
        // as the null check should happen before the data is processed.
        final byte[] data = new byte[16];

        // Act: Call the method under test with a null Appendable.
        // The @Test(expected) annotation will automatically handle the assertion.
        HexDump.dump(data, null);
    }
}