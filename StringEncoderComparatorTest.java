package org.apache.commons.codec;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the StringEncoderComparator.
 */
class StringEncoderComparatorTest {

    /**
     * Tests the StringEncoderComparator with DoubleMetaphone encoder.
     * Verifies that the list of names is sorted according to the DoubleMetaphone encoding.
     */
    @Test
    void testComparatorWithDoubleMetaphone() {
        // Create a comparator using DoubleMetaphone
        final StringEncoderComparator comparator = new StringEncoderComparator(new DoubleMetaphone());

        // List of names to be sorted
        final List<String> names = Arrays.asList("Jordan", "Sosa", "Prior", "Pryor");

        // Expected order after sorting
        final String[] expectedOrder = { "Jordan", "Prior", "Pryor", "Sosa" };

        // Sort the list using the comparator
        names.sort(comparator);

        // Convert the sorted list to an array
        final String[] sortedNames = names.toArray(ArrayUtils.EMPTY_STRING_ARRAY);

        // Assert that each element in the sorted array matches the expected order
        for (int i = 0; i < sortedNames.length; i++) {
            assertEquals(expectedOrder[i], sortedNames[i], "Mismatch at index: " + i);
        }
    }

    /**
     * Tests the StringEncoderComparator with DoubleMetaphone encoder using invalid input.
     * Verifies that comparing non-string objects returns zero.
     */
    @Test
    void testComparatorWithDoubleMetaphoneAndInvalidInput() {
        // Create a comparator using DoubleMetaphone
        final StringEncoderComparator comparator = new StringEncoderComparator(new DoubleMetaphone());

        // Compare two non-string objects
        final int comparisonResult = comparator.compare(Double.valueOf(3.0d), Long.valueOf(3));

        // Assert that the comparison result is zero
        assertEquals(0, comparisonResult, "Comparison of non-string objects should return zero");
    }

    /**
     * Tests the StringEncoderComparator with Soundex encoder.
     * Verifies that similar sounding names are considered equal.
     */
    @Test
    void testComparatorWithSoundex() {
        // Create a comparator using Soundex
        final StringEncoderComparator comparator = new StringEncoderComparator(new Soundex());

        // Compare two similar sounding names
        final int comparisonResult = comparator.compare("O'Brien", "O'Brian");

        // Assert that the comparison result is zero, indicating they are considered equal
        assertEquals(0, comparisonResult, "Names 'O'Brien' and 'O'Brian' should be considered equal by Soundex");
    }
}