/*
 * Tests for the DecoderException class.
 */

package org.apache.commons.codec;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.DecoderException;

/**
 * Test suite for the DecoderException class.
 */
public class DecoderExceptionTest {

    /**
     * Tests that two exceptions are not equal, even if one is constructed with the other as a cause.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithCauseIsNotEqualToOriginal()  throws Throwable  {
        DecoderException originalException = new DecoderException();
        DecoderException causedException = new DecoderException(originalException);
        assertFalse("An exception with a cause should not be equal to its cause", causedException.equals((Object)originalException));
    }

    /**
     * Tests that a DecoderException can be constructed with a message.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithMessage()  throws Throwable  {
        DecoderException decoderException = new DecoderException("");
        assertNotNull(decoderException.getMessage());
    }

    /**
     * Tests that two exceptions with different causes are not equal.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithDifferentCausesAreNotEqual()  throws Throwable  {
        DecoderException originalException = new DecoderException();
        DecoderException causedException = new DecoderException("[tYw", originalException);
        assertFalse("Exceptions with different messages and causes should not be equal", causedException.equals((Object)originalException));
    }
}