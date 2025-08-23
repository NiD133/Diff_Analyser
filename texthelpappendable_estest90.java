package org.apache.commons.cli.help;

import org.junit.Test;
import java.nio.CharBuffer;
import java.util.Collections;
import java.util.Collection;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link TextHelpAppendable} focusing on edge cases and error handling.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that appendList throws an OutOfMemoryError when processing a list
     * containing an extremely large CharSequence. This simulates a scenario where
     * internal buffering or string conversion could exhaust available heap space.
     */
    @Test
    public void appendListWithVeryLargeCharSequenceShouldThrowOutOfMemoryError() {
        // Arrange
        // The specific Appendable (System.out) is not important as we expect an error before any output.
        TextHelpAppendable helpFormatter = TextHelpAppendable.systemOut();

        // Create a CharSequence that is large enough to likely cause an OutOfMemoryError
        // when the appendList method attempts to process it.
        final int largeBufferSize = 18_000_000; // Approx. 18 million chars, requiring ~36MB memory
        CharBuffer largeCharSequence = CharBuffer.allocate(largeBufferSize);
        Collection<CharSequence> listWithLargeItem = Collections.singletonList(largeCharSequence);

        // Act & Assert
        // The method call is expected to fail with an OutOfMemoryError.
        assertThrows(OutOfMemoryError.class, () -> {
            helpFormatter.appendList(false, listWithLargeItem);
        });
    }
}