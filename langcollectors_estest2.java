package org.apache.commons.lang3.stream;

import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Collector;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link LangCollectors}.
 */
// The original test class name "LangCollectors_ESTestTest2" and its inheritance
// have been removed for clarity and to eliminate dependence on generated scaffolding.
public class LangCollectorsTest {

    /**
     * Tests that creating a joining Collector with a null 'toString' mapping function
     * does not throw an exception upon creation. The factory method should successfully
     * return a Collector instance.
     */
    @Test
    public void joiningWithNullMapperShouldCreateCollector() {
        // Arrange: Define clear, simple inputs for the collector.
        // The key part of this test is providing a null mapping function.
        final CharSequence delimiter = ",";
        final CharSequence prefix = "[";
        final CharSequence suffix = "]";
        final Function<Object, String> nullToStringMapper = null;

        // Act: Call the factory method under test.
        final Collector<Object, ?, String> collector = LangCollectors.joining(
                delimiter, prefix, suffix, nullToStringMapper);

        // Assert: Verify that the factory method returned a non-null collector.
        // This confirms that the collector can be instantiated even with a null mapper.
        assertNotNull("The collector should be successfully created even with a null mapping function.", collector);
    }
}