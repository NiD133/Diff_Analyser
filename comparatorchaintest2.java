package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparatorChain} using a custom object {@link PseudoRow}
 * and a multi-level sorting criteria with mixed ordering.
 * <p>
 * This class extends {@link AbstractComparatorTest} to leverage its
 * standardized comparator tests. The sorting logic being tested is:
 * <ul>
 *     <li>Column 0: Ascending</li>
 *     <li>Column 1: Descending</li>
 *     <li>Column 2: Ascending</li>
 * </ul>
 */
public class ComparatorChainMultiColumnSortTest extends AbstractComparatorTest<ComparatorChainMultiColumnSortTest.PseudoRow> {

    /**
     * Creates the {@link ComparatorChain} to be tested.
     *
     * @return a comparator that sorts by column 0 (asc), then 1 (desc), then 2 (asc).
     */
    @Override
    public Comparator<PseudoRow> makeObject() {
        // The boolean flag for addComparator(comparator, reverse) indicates the sort order.
        final boolean ASCENDING = false;  // reverse = false
        final boolean DESCENDING = true; // reverse = true

        // Set up a chain that sorts on three columns with mixed ordering.
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0)); // Defaults to ascending
        chain.addComparator(new ColumnComparator(1), DESCENDING);
        chain.addComparator(new ColumnComparator(2), ASCENDING);
        return chain;
    }

    /**
     * Provides a list of objects pre-sorted according to the logic defined in {@link #makeObject()}.
     *
     * @return an ordered list of {@link PseudoRow} objects.
     */
    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // The expected order is based on a multi-column sort:
        // - First, by column 0 in ascending order.
        // - Then, by column 1 in descending order.
        // - Finally, by column 2 in ascending order.
        return new LinkedList<>(Arrays.asList(
            new PseudoRow(1, 2, 3),
            new PseudoRow(2, 3, 5), // Tie on col 0 (2), sorted by col 1 desc (3 > 2).
            new PseudoRow(2, 2, 4),
            new PseudoRow(2, 2, 8), // Tie on col 0,1 (2,2), sorted by col 2 asc (4 < 8).
            new PseudoRow(3, 1, 0),
            new PseudoRow(4, 4, 4),
            new PseudoRow(4, 4, 7)  // Tie on col 0,1 (4,4), sorted by col 2 asc (4 < 7).
        ));
    }

    /**
     * Required for compatibility with {@link AbstractComparatorTest}.
     */
    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    // --- Standalone Test cases ---

    @Test
    void compareOnEmptyChainShouldThrowException() {
        final ComparatorChain<Integer> emptyChain = new ComparatorChain<>();
        final Integer object1 = 4;
        final Integer object2 = 6;

        assertThrows(UnsupportedOperationException.class,
            () -> emptyChain.compare(object1, object2),
            "Calling compare() on an empty chain should throw UnsupportedOperationException.");
    }

    // --- Helper Classes ---

    /**
     * A test data object representing a row with three integer columns.
     * Used to test multi-column sorting.
     */
    private static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        private final int[] cols = new int[3];

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
            return "PseudoRow" + Arrays.toString(cols);
        }
    }

    /**
     * A simple comparator that compares {@link PseudoRow} objects based on the
     * integer value in a specific column.
     */
    private static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;
        private final int colIndex;

        ColumnComparator(final int colIndex) {
            this.colIndex = colIndex;
        }

        @Override
        public int compare(final PseudoRow o1, final PseudoRow o2) {
            return Integer.compare(o1.getColumn(colIndex), o2.getColumn(colIndex));
        }

        @Override
        public boolean equals(final Object that) {
            if (this == that) return true;
            if (!(that instanceof ColumnComparator)) return false;
            final ColumnComparator other = (ColumnComparator) that;
            return this.colIndex == other.colIndex;
        }

        @Override
        public int hashCode() {
            return colIndex;
        }
    }
}