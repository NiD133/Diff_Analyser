package org.apache.commons.lang3;

import org.junit.Test;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;

public class AppendableJoiner_ESTestTest8 extends AppendableJoiner_ESTest_scaffolding {

    /**
     * Tests that joinA throws a ReadOnlyBufferException when the target Appendable is a read-only buffer.
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void testJoinAThrowsExceptionWhenTargetIsReadOnlyBuffer() {
        // Arrange
        // 1. Create a standard joiner instance.
        final AppendableJoiner<String> joiner = AppendableJoiner.builder().get();

        // 2. Create a read-only CharBuffer to act as the Appendable target.
        //    CharBuffer.wrap(CharSequence) is documented to create a read-only buffer.
        final CharBuffer readOnlyBuffer = CharBuffer.wrap("cannot-be-modified");

        // 3. Define an array of elements to join. The content is not critical for this test.
        final String[] elementsToJoin = {"a", "b", "c"};

        // Act & Assert
        // The following call is expected to throw a ReadOnlyBufferException because it
        // attempts to write to a read-only buffer. The exception is caught and verified
        // by the @Test(expected=...) annotation.
        joiner.joinA(readOnlyBuffer, elementsToJoin);
    }
}