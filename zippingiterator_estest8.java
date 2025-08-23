package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that the two-argument constructor throws a NullPointerException
     * if an iterator argument is null.
     */
    @Test
    public void testTwoArgumentConstructorThrowsOnNullIterator() {
        // The constructor is expected to throw a NullPointerException because it does not
        // accept null iterators. This behavior is documented and enforced by a null check.
        try {
            // The original test passed two nulls. The internal check will fail on the first one.
            // The explicit cast on the second null is needed to resolve constructor ambiguity.
            new ZippingIterator<>(null, (Iterator<Object>) null);
            fail("Expected a NullPointerException to be thrown, but no exception was caught.");
        } catch (final NullPointerException e) {
            // The source class uses Objects.requireNonNull(iterator, "iterator"),
            // which results in this specific exception message.
            assertEquals("iterator", e.getMessage());
        }
    }
}