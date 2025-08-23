package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorChain}, specifically for version 4 compatibility.
 * <p>
 * This class extends {@link AbstractComparatorTest} to leverage a suite of
 * generic comparator tests.
 * </p>
 */
public class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    /**
     * Provides a list of {@link PseudoRow} objects in the exact order expected
     * from the comparator created by {@link #makeObject()}.
     */
    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // The ordering is based on a chain that sorts by:
        // column 0 in ascending order,
        // then column 1 in descending order,
        // then column 2 in ascending order.
        return Arrays.asList(
            new PseudoRow(1, 2, 3),
            new PseudoRow(2, 3, 5),
            new PseudoRow(2, 2, 4),
            new PseudoRow(2, 2, 8),
            new PseudoRow(3, 1, 0),
            new PseudoRow(4, 4, 4),
            new PseudoRow(4, 4, 7)
        );
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates an instance of the {@link ComparatorChain} to be tested.
     */
    @Override
    public Comparator<PseudoRow> makeObject() {
        // Create a chain that sorts by:
        // 1. column 0 (ascending)
        // 2. column 1 (descending)
        // 3. column 2 (ascending)
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true);  // true = descending order
        chain.addComparator(new ColumnComparator(2), false); // false = ascending order
        return chain;
    }

    /**
     * A helper comparator that compares two {@link PseudoRow} objects based on a single column.
     */
    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;
        protected final int colIndex;

        public ColumnComparator(final int colIndex) {
            this.colIndex = colIndex;
        }

        @Override
        public int compare(final PseudoRow o1, final PseudoRow o2) {
            return Integer.compare(o1.getColumn(colIndex), o2.getColumn(colIndex));
        }

        @Override
        public boolean equals(final Object that) {
            return that instanceof ColumnComparator && colIndex == ((ColumnComparator) that).colIndex;
        }

        @Override
        public int hashCode() {
            return colIndex;
        }
    }

    /**
     * A helper class for tests that represents a row of data with three integer columns.
     */
    public static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        public final int[] cols = new int[3];

        public PseudoRow(final int col1, final int col2, final int col3) {
            cols[0] = col1;
            cols[1] = col2;
            cols[2] = col3;
        }

        public int getColumn(final int colIndex) {
            return cols[colIndex];
        }

        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof PseudoRow)) {
                return false;
            }
            final PseudoRow row = (PseudoRow) o;
            return Arrays.equals(cols, row.cols);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cols);
        }

        @Override
        public String toString() {
            return Arrays.toString(cols);
        }
    }

    /**
     * Tests that a ComparatorChain constructed from a list containing a single
     * comparator behaves identically to that single comparator.
     */
    @Test
    void testChainConstructedFromList_withSingleComparator() {
        // Arrange
        final Integer value1 = 4;
        final Integer value2 = 6;
        final List<Comparator<Integer>> comparators = Arrays.asList(new ComparableComparator<>());
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparators);

        // Act
        final int actualResult = chain.compare(value1, value2);

        // Assert
        final int expectedResult = value1.compareTo(value2);
        assertEquals(expectedResult, actualResult, "A chain with one comparator should behave like that comparator.");
    }
}