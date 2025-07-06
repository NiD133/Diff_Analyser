package org.apache.commons.codec;

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.DecoderException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the DecoderException class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DecoderException_ESTest extends DecoderException_ESTest_scaffolding {

    /**
     * Test to verify that a DecoderException with a cause is not equal to the cause itself.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithCause() throws Throwable {
        // Create a DecoderException with no message or cause
        DecoderException baseException = new DecoderException();
        
        // Create a new DecoderException using the baseException as the cause
        DecoderException exceptionWithCause = new DecoderException(baseException);
        
        // Assert that the exception with a cause is not equal to the base exception
        assertFalse(exceptionWithCause.equals(baseException));
    }

    /**
     * Test to verify that a DecoderException can be created with an empty message.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithEmptyMessage() throws Throwable {
        // Create a DecoderException with an empty string as the message
        DecoderException exceptionWithEmptyMessage = new DecoderException("");
        
        // Assert that the message of the exception is indeed an empty string
        assertEquals("", exceptionWithEmptyMessage.getMessage());
    }

    /**
     * Test to verify that a DecoderException with a message and cause is not equal to the cause.
     */
    @Test(timeout = 4000)
    public void testDecoderExceptionWithMessageAndCause() throws Throwable {
        // Create a DecoderException with no message or cause
        DecoderException baseException = new DecoderException();
        
        // Create a new DecoderException with a specific message and the baseException as the cause
        DecoderException exceptionWithMessageAndCause = new DecoderException("[tYw", baseException);
        
        // Assert that the exception with a message and cause is not equal to the base exception
        assertFalse(exceptionWithMessageAndCause.equals(baseException));
    }
}