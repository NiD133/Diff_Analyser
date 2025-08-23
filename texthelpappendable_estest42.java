package org.apache.commons.cli.help;

import org.junit.Test;
import java.nio.CharBuffer;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that {@code makeColumnQueue} fails with an OutOfMemoryError when given an
     * extremely large input CharSequence. This test simulates a resource exhaustion
     * scenario to check the method's behavior under extreme load.
     *
     * <p><b>Note:</b> Testing for {@code OutOfMemoryError} can be fragile as it depends
     * on the specific JVM memory configuration. This test uses a very large buffer
     * to make the error likely, but it may not fail consistently in all environments.
     * Its primary purpose is to document the expected behavior under memory pressure.
     */
    @Test
    public void makeColumnQueueWithVeryLargeInputThrowsOutOfMemoryError() {
        // Arrange: Create a TextHelpAppendable and an extremely large input buffer
        // to intentionally exhaust memory during processing.
        final int VERY_LARGE_BUFFER_SIZE = 50_000_000; // Large enough to likely cause OOM

        TextHelpAppendable helpAppendable = TextHelpAppendable.systemOut();
        CharBuffer largeInput = CharBuffer.allocate(VERY_LARGE_BUFFER_SIZE);
        TextStyle defaultStyle = TextStyle.DEFAULT;

        // Act & Assert: Verify that the method throws an OutOfMemoryError.
        try {
            helpAppendable.makeColumnQueue(largeInput, defaultStyle);
            fail("Expected an OutOfMemoryError to be thrown, but it was not.");
        } catch (OutOfMemoryError e) {
            // This is the expected outcome. The test passes.
        }
    }
}