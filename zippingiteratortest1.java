package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    // These lists are used by the inherited tests from AbstractIteratorTest via makeObject()
    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fibonacci;

    @BeforeEach
    public void setUp() {
        evens = new ArrayList<>();
        odds = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                evens.add(i);
            } else {
                odds.add(i);
            }
        }

        fibonacci = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    /**
     * Creates an empty iterator for the abstract test suite.
     */
    @Override
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.emptyIterator());
    }

    /**
     * Creates a complex iterator for the abstract test suite to test the main
     * interleaving functionality.
     */
    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fibonacci.iterator());
    }

    /**
     * Tests that a ZippingIterator wrapping a single iterator behaves the same
     * as the iterator it wraps.
     */
    @Test
    void testZippingSingleIterator() {
        // Arrange
        final List<Integer> expected = Arrays.asList(0, 2, 4, 6, 8, 10);
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(expected.iterator());

        // Act
        final List<Integer> actual = IteratorUtils.toList(zippingIterator);

        // Assert
        assertEquals(expected, actual);
    }
}