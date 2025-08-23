package com.google.common.io;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Tests that reading from a CharSequenceReader into the same CharBuffer instance that
     * backs the reader causes an IndexOutOfBoundsException.
     *
     * <p>This occurs due to an interaction between how CharSequenceReader reads and how CharBuffer
     * implements the CharSequence interface:
     * <ol>
     *   <li>The reader is backed by a CharBuffer (`sourceAndTargetBuffer`).
     *   <li>The `read(CharBuffer)` method is called with the *same* buffer as the target.
     *   <li>Inside the `read` loop, `target.put()` advances the buffer's position.
     *   <li>Because the source and target are the same object, the source's position is also advanced.
     *   <li>Crucially, `CharBuffer.charAt(i)` is relative to the buffer's *current position*
     *       (it effectively calls `get(position() + i)`).
     *   <li>As the position advances with each `put`, the index for the subsequent `get` operation
     *       grows faster than the loop counter, eventually attempting to access an index beyond
     *       the buffer's limit and throwing an {@link IndexOutOfBoundsException}.
     * </ol>
     */
    @Test
    public void read_whenTargetIsSameBufferAsSource_throwsIndexOutOfBoundsException() {
        // Arrange
        // A buffer to be used as both the source for the reader and the target of the read.
        CharBuffer sourceAndTargetBuffer = CharBuffer.wrap(new char[10]);
        CharSequenceReader reader = new CharSequenceReader(sourceAndTargetBuffer);

        // Act & Assert
        try {
            reader.read(sourceAndTargetBuffer);
            fail("Expected IndexOutOfBoundsException when reading from a CharBuffer into itself.");
        } catch (IndexOutOfBoundsException expected) {
            // This is the expected behavior due to the conflicting state changes
            // of the shared buffer during the read operation.
        } catch (IOException e) {
            // The specific behavior throws a runtime exception, not an IOException.
            fail("Caught an unexpected IOException instead of the expected IndexOutOfBoundsException: " + e);
        }
    }
}