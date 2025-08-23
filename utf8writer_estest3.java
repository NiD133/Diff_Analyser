package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer#convertSurrogate(int)} method,
 * focusing on its behavior with invalid surrogate pair states.
 */
public class UTF8WriterImprovedTest {

    /**
     * This test verifies the raw calculation of the `convertSurrogate` method
     * when it is invoked with a low surrogate value but without a preceding
     * high surrogate having been set.
     *
     * <p>In this scenario, the internal high surrogate field defaults to 0. The
     * method is expected to proceed with its calculation, producing a
     * mathematically correct result based on the invalid input, rather than
     * throwing an exception. This confirms the method's "garbage-in, garbage-out"
     * behavior for this specific edge case.
     */
    @Test(timeout = 4000)
    public void testConvertSurrogateWithUnsetHighSurrogateReturnsCalculatedValue() {
        // Arrange: Create a UTF8Writer instance. The IOContext and OutputStream
        // are necessary for instantiation but can be minimal for this test.
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, (OutputStream) null);

        // Define the low surrogate part of an incomplete pair.
        // 0xDC00 is the first valid low surrogate character.
        int lowSurrogate = UTF8Writer.SURR2_FIRST;

        // Calculate the expected result based on the internal formula of UTF8Writer,
        // assuming the unset high surrogate is treated as 0.
        // Formula: (highSurrogate << 10) + lowSurrogate + SURROGATE_BASE
        int expectedValue = (0 << 10) + lowSurrogate + UTF8Writer.SURROGATE_BASE;

        // Act: Call the method under test.
        int actualValue = 0;
        try {
            // The method is not expected to throw an exception here, despite the invalid state.
            actualValue = utf8Writer.convertSurrogate(lowSurrogate);
        } catch (IOException e) {
            fail("Did not expect an IOException for a simple surrogate conversion calculation.");
        }

        // Assert: Check if the actual result matches the calculated garbage value.
        assertEquals(
                "The method should return the result of its internal calculation, even with an unset high surrogate.",
                expectedValue,
                actualValue
        );
    }
}