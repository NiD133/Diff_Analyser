package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorChain}.
 * <p>
 * This class includes general sorting behavior tests inherited from
 * {@link AbstractComparatorTest} and a specific test for an edge case involving
 * {@code Integer.MIN_VALUE}.
 * </p>
 */
@DisplayName("ComparatorChain")
public class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    //--- Test Setup for AbstractComparatorTest ---

    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // The ordering is determined by the ComparatorChain created in makeObject():
        // 1. Sort by column 0, ascending.
        // 2. For ties, sort by column 1, descending.
        // 3. For further ties, sort by column 2, ascending.
        //
        // Applying this logic to the initial list results in the following final order:
        //
        // Initial Grouping by Column 0 (Ascending):
        // - [1,2,3]
        // - [2,3,5], [2,2,4], [2,2,8]  (Group of 2s)
        // - [3,1,0]
        // - [4,4,4], [4,4,7]          (Group of 4s)
        //
        // Sorting Group of 2s by Column 1 (Descending):
        // - [2,3,5] (3 is largest)
        // - [2,2,4], [2,2,8] (tie on 2)
        //
        // Sorting the tied 2s by Column 2 (Ascending):
        // - [2,2,4]
        // - [2,2,8]
        //
        // This results in the final, expected order.
        return new LinkedList<>(Arrays.asList(
                new PseudoRow(1, 2, 3),
                new PseudoRow(2, 3, 5),
                new PseudoRow(2, 2, 4),
                new PseudoRow(2, 2, 8),
                new PseudoRow(3, 1, 0),
                new PseudoRow(4, 4, 4),
                new PseudoRow(4, 4, 7)
        ));
    }

    @Override
    public Comparator<PseudoRow> makeObject() {
        // Create a chain that sorts by:
        // 1. column 0 ascending
        // 2. then column 1 descending
        // 3. then column 2 ascending
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true);  // true for reverse/descending
        chain.addComparator(new ColumnComparator(2), false); // false for forward/ascending
        return chain;
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //--- Test Helper Classes ---

    /**
     * A simple data object with three integer columns for testing purposes.
     */
    public static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        public final int[] cols = new int[3];

        PseudoRow(final int col1, final int col2, final int col3) {
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
            final PseudoRow that = (PseudoRow) o;
            return Arrays.equals(this.cols, that.cols);
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
     * Compares {@link PseudoRow} objects based on a single column index.
     */
    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;
        protected final int colIndex;

        ColumnComparator(final int colIndex) {
            this.colIndex = colIndex;
        }

        @Override
        public int compare(final PseudoRow o1, final PseudoRow o2) {
            return Integer.compare(o1.getColumn(colIndex), o2.getColumn(colIndex));
        }

        @Override
        public boolean equals(final Object that) {
            return that instanceof ColumnComparator && this.colIndex == ((ColumnComparator) that).colIndex;
        }

        @Override
        public int hashCode() {
            return colIndex;
        }
    }

    //--- Specific Test Cases ---

    @Test
    @DisplayName("A reversed chain should correctly handle a comparator returning Integer.MIN_VALUE")
    void reversedChainShouldCorrectlyHandleComparatorReturningIntegerMinValue() {
        // This test addresses an edge case with reversing a comparison result.
        // When a comparator returns Integer.MIN_VALUE, negating it with "-result"
        // results in Integer.MIN_VALUE due to integer overflow.
        // A correct reverse implementation must handle this and produce a positive value.
        final ComparatorChain<Integer> reversedChain = new ComparatorChain<>();

        // A comparator that returns MIN_VALUE for "less than" results.
        final Comparator<Integer> minValComparator = (a, b) -> {
            final int result = a.compareTo(b);
            if (result < 0) {
                return Integer.MIN_VALUE;
            }
            if (result > 0) {
                return Integer.MAX_VALUE;
            }
            return 0;
        };

        // Add the comparator to the chain in reverse order.
        reversedChain.addComparator(minValComparator, true);

        // When 4 is compared to 5, the inner comparator returns MIN_VALUE.
        // The reversed chain should return a positive number.
        assertTrue(reversedChain.compare(4, 5) > 0,
                "4 < 5, so reversed comparison should be positive");

        // When 5 is compared to 4, the inner comparator returns MAX_VALUE.
        // The reversed chain should return a negative number.
        assertTrue(reversedChain.compare(5, 4) < 0,
                "5 > 4, so reversed comparison should be negative");

        // When 4 is compared to 4, the inner comparator returns 0.
        // The chain should return 0, as reversing has no effect.
        assertEquals(0, reversedChain.compare(4, 4),
                "4 == 4, so comparison should be 0");
    }
}