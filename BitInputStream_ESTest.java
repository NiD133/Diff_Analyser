package org.apache.commons.compress.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.io.InputStream;
import java.nio.ByteOrder;

import static org.junit.Assert.fail;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class BitInputStreamTest {

    @Test(timeout = 4000)
    public void testConstructorWithNullInputStream() {
        // Test case: Attempt to create a BitInputStream with a null InputStream.
        // Expected behavior: A NoClassDefFoundError should be thrown because BitInputStream depends on
        // BoundedInputStream, which may not be available in the test environment. This verifies
        // that BitInputStream correctly handles a null input stream in the absence of its dependency.
        ByteOrder byteOrder = ByteOrder.nativeOrder();
        BitInputStream bitInputStream = null;
        try {
            bitInputStream = new BitInputStream((InputStream) null, byteOrder);
            fail("Expected NoClassDefFoundError due to missing BoundedInputStream dependency");
        } catch (NoClassDefFoundError e) {
            // Expected exception
            String expectedErrorMessage = "org/apache/commons/io/input/BoundedInputStream";
            org.junit.Assert.assertEquals(expectedErrorMessage, e.getMessage()); // Corrected assertion
        }
    }
}