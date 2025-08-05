package org.apache.commons.lang3.stream;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.nio.CharBuffer;
import java.util.function.Function;
import java.util.stream.Collector;
import org.apache.commons.lang3.stream.LangCollectors;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

/**
 * Test suite for LangCollectors class.
 */
@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class LangCollectors_ESTest extends LangCollectors_ESTest_scaffolding {

    /**
     * Test joining with null delimiter, prefix, and suffix.
     */
    @Test(timeout = 4000)
    public void testJoiningWithNullDelimiterPrefixSuffix() throws Throwable {
        char[] charArray = new char[5];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        
        // Create a collector with null delimiter, prefix, and suffix
        Collector<Object, ?, String> collector = LangCollectors.joining(null, null, charBuffer);
        
        // Verify that the collector is not null
        assertNotNull(collector);
    }

    /**
     * Test joining with CharBuffer as delimiter, prefix, and suffix, and null toString function.
     */
    @Test(timeout = 4000)
    public void testJoiningWithCharBufferAndNullToString() throws Throwable {
        char[] charArray = new char[1];
        CharBuffer charBuffer = CharBuffer.wrap(charArray);
        
        // Create a collector with CharBuffer as delimiter, prefix, and suffix, and null toString function
        Collector<Object, ?, String> collector = LangCollectors.joining(charBuffer, charBuffer, charBuffer, null);
        
        // Verify that the collector is not null
        assertNotNull(collector);
    }

    /**
     * Test joining with no arguments.
     */
    @Test(timeout = 4000)
    public void testJoiningWithoutArguments() throws Throwable {
        // Create a collector with no arguments
        Collector<Object, ?, String> collector = LangCollectors.joining();
        
        // Verify that the collector is not null
        assertNotNull(collector);
    }

    /**
     * Test collect method with null collector and null array, expecting NullPointerException.
     */
    @Test(timeout = 4000)
    public void testCollectWithNullCollectorAndArray() throws Throwable {
        try {
            // Attempt to collect with null collector and array, expecting NullPointerException
            LangCollectors.collect(null, (CharBuffer[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Verify that the exception is a NullPointerException
            verifyException("java.util.Objects", e);
        }
    }

    /**
     * Test joining with null delimiter.
     */
    @Test(timeout = 4000)
    public void testJoiningWithNullDelimiter() throws Throwable {
        // Create a collector with null delimiter
        Collector<Object, ?, String> collector = LangCollectors.joining(null);
        
        // Verify that the collector is not null
        assertNotNull(collector);
    }
}