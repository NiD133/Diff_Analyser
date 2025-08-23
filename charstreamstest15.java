package com.google.common.io;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.Writer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CharStreams#nullWriter()}.
 */
@RunWith(JUnit4.class)
public class CharStreamsTest {

    /**
     * The primary contract of nullWriter() is that its write and append operations
     * should do nothing and not throw exceptions. This test verifies that behavior
     * for all standard operations.
     */
    @Test
    public void testNullWriter_writeAndAppendOperations_doNotThrow() throws IOException {
        Writer nullWriter = CharStreams.nullWriter();
        
        // All of these operations should be successful no-ops.
        // The test passes if no exceptions are thrown.
        nullWriter.write('a');
        nullWriter.write("test");
        nullWriter.write("test".toCharArray());
        nullWriter.write("test", 1, 2);
        nullWriter.write("test".toCharArray(), 1, 2);
        nullWriter.append('a');
        nullWriter.append("test");
        nullWriter.append("test", 1, 3);
        nullWriter.flush();
        nullWriter.close();
    }

    /**
     * The append methods handle a null CharSequence by treating it as the string "null".
     * This test verifies that these calls are handled gracefully without throwing a
     * NullPointerException.
     */
    @Test
    public void testNullWriter_appendNullCharSequence_isHandledGracefully() throws IOException {
        Writer nullWriter = CharStreams.nullWriter();

        // Appending a null CharSequence should not throw.
        nullWriter.append(null);

        // Appending a subsequence of a null CharSequence is treated as appending a
        // subsequence of "null". This should succeed as the indices are valid for "null".
        nullWriter.append(null, 0, 4); // Equivalent to "null".substring(0, 4)
    }

    /**
     * While most operations are no-ops, the nullWriter must still adhere to the contract
     * of its parent classes. This test verifies that calls with invalid arguments, such
     * as out-of-bounds indices, throw the appropriate exceptions.
     */
    @Test
    public void testNullWriter_append_throwsForInvalidIndices() {
        Writer nullWriter = CharStreams.nullWriter();
        String testString = "test"; // length 4

        // Test with a non-null string
        assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(testString, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(testString, 0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(testString, 2, 1));

        // Test with a null CharSequence, which is treated as "null" (length 4)
        assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, -1, 2));
        assertThrows(IndexOutOfBoundsException.class, () -> nullWriter.append(null, 0, 5));
    }

    /**
     * The documentation for nullWriter() implies it returns a singleton instance.
     * This test verifies that multiple calls return the exact same object.
     */
    @Test
    public void testNullWriter_isSingleton() {
        Writer writer1 = CharStreams.nullWriter();
        Writer writer2 = CharStreams.nullWriter();
        assertSame("CharStreams.nullWriter() should return a singleton instance", writer1, writer2);
    }
}