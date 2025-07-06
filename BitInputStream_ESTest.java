package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.compress.utils.BitInputStream;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for the BitInputStream class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class BitInputStream_ESTest extends BitInputStream_ESTest_scaffolding {

    /**
     * Test to verify that a NoClassDefFoundError is thrown when attempting to
     * create a BitInputStream with a null InputStream.
     */
    @Test(timeout = 4000)
    public void testBitInputStreamWithNullInputStream() throws Throwable {
        // Get the native byte order of the system
        ByteOrder nativeByteOrder = ByteOrder.nativeOrder();
        
        // Attempt to create a BitInputStream with a null InputStream
        try {
            BitInputStream bitInputStream = new BitInputStream(null, nativeByteOrder);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            // Verify that the exception is related to the missing BoundedInputStream class
            verifyException("org.apache.commons.compress.utils.BitInputStream", e);
        }
    }
}