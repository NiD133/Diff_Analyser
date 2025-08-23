package com.google.common.primitives;

import static org.junit.Assert.assertNotNull;

import java.util.Comparator;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void lexicographicalComparator_shouldReturnNonNullInstance() {
        // When the lexicographicalComparator method is called
        Comparator<byte[]> comparator = SignedBytes.lexicographicalComparator();

        // Then a non-null Comparator instance should be returned
        assertNotNull("The lexicographical comparator should never be null.", comparator);
    }
}