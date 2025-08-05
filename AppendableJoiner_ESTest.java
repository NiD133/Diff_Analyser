package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.PipedWriter;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import org.apache.commons.lang3.function.FailableBiConsumer;

/**
 * Test suite for AppendableJoiner functionality.
 * Tests joining elements with various configurations of prefix, suffix, delimiter, and custom appenders.
 */
public class AppendableJoiner_ESTest {

    // ========== Basic Functionality Tests ==========

    @Test
    public void testJoinEmptyIterableWithCustomAppender() throws Throwable {
        // Given: Empty enum set and custom appender
        EnumSet<Locale.Category> emptyEnumSet = EnumSet.allOf(Locale.Category.class);
        FailableBiConsumer<Appendable, Locale.Category, IOException> noOpAppender = FailableBiConsumer.nop();
        StringBuilder target = new StringBuilder();
        
        // When: Joining empty collection
        StringBuilder result = AppendableJoiner.joinI(
            target, 
            "", // prefix
            "", // suffix  
            "", // delimiter
            noOpAppender, 
            emptyEnumSet
        );
        
        // Then: Result should be empty
        assertEquals("", result.toString());
    }

    @Test
    public void testJoinEmptyArrayWithBuilder() throws Throwable {
        // Given: Builder with suffix configured and empty array
        AppendableJoiner.Builder<Object> builder = AppendableJoiner.builder();
        StringBuilder suffix = new StringBuilder();
        AppendableJoiner<Object> joiner = builder.setSuffix(suffix).get();
        ArrayDeque<Object> emptyDeque = new ArrayDeque<>(1910);
        StringBuilder target = new StringBuilder();
        
        // When: Joining empty collection
        StringBuilder result = joiner.join(target, emptyDeque);
        
        // Then: Result should be empty
        assertEquals("", result.toString());
    }

    @Test
    public void testJoinArrayWithNullElements() throws Throwable {
        // Given: Array with null elements and custom delimiter
        StringBuilder[] arrayWithNulls = new StringBuilder[4];
        arrayWithNulls[2] = new StringBuilder(); // Only one non-null element
        
        AppendableJoiner.Builder<StringBuilder> builder = AppendableJoiner.builder();
        StringBuffer delimiter = new StringBuffer();
        AppendableJoiner<StringBuilder> joiner = builder.setDelimiter(delimiter).get();
        
        // When: Joining array with nulls
        joiner.join(arrayWithNulls[2], arrayWithNulls);
        
        // Then: Nulls should be converted to "null" strings
        assertEquals("nullnullnullnullnull", arrayWithNulls[2].toString());
    }

    // ========== Error Handling Tests ==========

    @Test
    public void testJoinArrayWithNullAppender_ThrowsNullPointerException() throws Throwable {
        // Given: Null appender
        StringBuilder target = new StringBuilder();
        StringBuilder[] elements = new StringBuilder[1];
        
        // When/Then: Should throw NullPointerException
        try {
            AppendableJoiner.joinA(
                target, 
                "", "", "", 
                null, // null appender
                elements
            );
            fail("Expected NullPointerException for null appender");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testJoinWithNullTarget_ThrowsNullPointerException() throws Throwable {
        // Given: Null target StringBuilder
        FailableBiConsumer<Appendable, StringBuilder, IOException> appender = FailableBiConsumer.nop();
        StringBuilder[] emptyArray = new StringBuilder[0];
        
        // When/Then: Should throw NullPointerException
        try {
            AppendableJoiner.joinSB(
                null, // null target
                "", null, "", 
                appender, 
                emptyArray
            );
            fail("Expected NullPointerException for null target");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test
    public void testJoinWithReadOnlyBuffer_ThrowsReadOnlyBufferException() throws Throwable {
        // Given: Read-only CharBuffer as target
        AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder().get();
        StringBuffer source = new StringBuffer();
        CharBuffer readOnlyBuffer = CharBuffer.wrap(source, 0, 0);
        StringBuilder[] elements = new StringBuilder[5];
        
        // When/Then: Should throw ReadOnlyBufferException
        try {
            joiner.joinA(readOnlyBuffer, elements);
            fail("Expected ReadOnlyBufferException for read-only buffer");
        } catch(ReadOnlyBufferException e) {
            // Expected behavior
        }
    }

    @Test
    public void testJoinWithDisconnectedPipedWriter_ThrowsIOException() throws Throwable {
        // Given: Disconnected PipedWriter
        AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder().get();
        PipedWriter disconnectedWriter = new PipedWriter();
        
        // When/Then: Should throw IOException
        try {
            joiner.joinA(disconnectedWriter, (StringBuilder[]) null);
            fail("Expected IOException for disconnected PipedWriter");
        } catch(IOException e) {
            assertEquals("Pipe not connected", e.getMessage());
        }
    }

    @Test
    public void testJoinWithBufferOverflow_ThrowsBufferOverflowException() throws Throwable {
        // Given: Zero-capacity CharBuffer
        AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder().get();
        CharBuffer zeroCapacityBuffer = CharBuffer.allocate(0);
        StringBuilder[] elements = new StringBuilder[6];
        
        // When/Then: Should throw BufferOverflowException
        try {
            joiner.joinA(zeroCapacityBuffer, elements);
            fail("Expected BufferOverflowException for zero-capacity buffer");
        } catch(BufferOverflowException e) {
            // Expected behavior
        }
    }

    // ========== Builder Configuration Tests ==========

    @Test
    public void testBuilderWithCustomElementAppender() throws Throwable {
        // Given: Builder with custom element appender
        AppendableJoiner.Builder<Object> builder = new AppendableJoiner.Builder<>();
        FailableBiConsumer<Appendable, Object, IOException> customAppender = FailableBiConsumer.nop();
        
        // When: Setting custom appender
        AppendableJoiner<Object> joiner = builder.setElementAppender(customAppender).get();
        
        // Then: Joiner should be created successfully
        assertNotNull(joiner);
    }

    @Test
    public void testBuilderWithPrefix() throws Throwable {
        // Given: Builder and prefix
        AppendableJoiner.Builder<StringBuilder> builder = AppendableJoiner.builder();
        StringBuffer prefix = new StringBuffer(1078);
        
        // When: Setting prefix
        AppendableJoiner.Builder<StringBuilder> result = builder.setPrefix(prefix);
        
        // Then: Should return same builder instance (fluent interface)
        assertSame(result, builder);
    }

    // ========== Successful Join Operations Tests ==========

    @Test
    public void testJoinEmptyArraySuccessfully() throws Throwable {
        // Given: Empty array and configured joiner
        StringBuilder target = new StringBuilder();
        FailableBiConsumer<Appendable, StringBuilder, IOException> appender = FailableBiConsumer.nop();
        StringBuilder[] emptyArray = new StringBuilder[6];
        
        // When: Joining empty array
        StringBuilder result = AppendableJoiner.joinA(
            target, "", "", "", 
            appender, 
            emptyArray
        );
        
        // Then: Should return same target instance
        assertSame(result, target);
    }

    @Test
    public void testJoinNullIterable() throws Throwable {
        // Given: Joiner and null iterable
        AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder().get();
        StringBuilder target = new StringBuilder();
        
        // When: Joining null iterable
        StringBuilder result = joiner.join(target, (Iterable<StringBuilder>) null);
        
        // Then: Should return empty result
        assertEquals("", result.toString());
    }

    @Test
    public void testJoinNullArray() throws Throwable {
        // Given: Joiner and null array
        AppendableJoiner<Object> joiner = AppendableJoiner.<Object>builder().get();
        StringBuilder target = new StringBuilder();
        
        // When: Joining null array
        StringBuilder result = joiner.joinA(target, (Object[]) null);
        
        // Then: Should return empty result
        assertEquals("", result.toString());
    }

    @Test
    public void testJoinEmptyCollection() throws Throwable {
        // Given: Empty collection
        AppendableJoiner<Object> joiner = AppendableJoiner.<Object>builder().get();
        StringBuilder target = new StringBuilder();
        LinkedHashSet<Object> emptySet = new LinkedHashSet<>();
        
        // When: Joining empty collection
        StringBuilder result = joiner.joinA(target, emptySet);
        
        // Then: Should return empty result
        assertEquals("", result.toString());
    }
}