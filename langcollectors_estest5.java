package org.apache.commons.lang3.stream;

import org.junit.Test;

import java.util.stream.Stream;

/**
 * Unit tests for {@link LangCollectors}.
 */
public class LangCollectorsTest {

    /**
     * Tests that using the collector from {@link LangCollectors#joining(CharSequence)}
     * with a null delimiter throws a {@link NullPointerException} during the terminal
     * collection operation.
     * <p>
     * The collector itself can be created with a null delimiter, but the operation fails
     * upon use because the underlying {@link java.util.StringJoiner} requires a non-null
     * delimiter. This test verifies this runtime behavior.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testJoiningWithNullDelimiterShouldThrowNPEWhenUsed() {
        // Arrange: Create a stream of elements to be joined.
        Stream<String> stream = Stream.of("a", "b", "c");

        // Act: Attempt to collect the stream using the collector with a null delimiter.
        // This terminal operation is expected to throw a NullPointerException.
        stream.collect(LangCollectors.joining(null));
    }
}