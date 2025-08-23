package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests a {@link ComparatorChain} with a mixture of forward (ascending) and
 * reverse (descending) sort orders.
 * <p>
 * This class extends {@link AbstractComparatorTest} to leverage its framework
 * for testing a comparator against a known-good, ordered list of objects.
 * </p>
 */
public class ComparatorChainMixedSortTest extends AbstractComparatorTest<ComparatorChainMixedSortTest.PseudoRow> {

    private static final int COL_1_INDEX = 0;
    private static final int COL_2_INDEX = 1;
    private static final int COL_3_INDEX = 2;

    /**
     * Creates the comparator to be tested. The sorting logic is:
     * <ul>
     *   <li>Column 1: Ascending</li>
     *   <li>Column 2: Descending</li>
     *   <li>Column 3: Ascending</li>
     * </ul>
     *
     * @return A {@link ComparatorChain} with the specified mixed-order sorting.
     */
    @Override
    public Comparator<PseudoRow> makeObject() {
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(COL_1_INDEX));
        // Add second comparator in reverse (descending) order
        chain.addComparator(new ColumnComparator(COL_2_INDEX), true);
        // Add third comparator in forward (ascending) order
        chain.addComparator(new ColumnComparator(COL_3_INDEX), false);
        return chain;
    }

    /**
     * Provides a list of objects that are pre-sorted according to the rules
     * defined in {@link #makeObject()}.
     *
     * @return An ordered list of {@link PseudoRow} objects.
     */
    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // The expected order is based on sorting by:
        // Column 1 (ASC), then Column 2 (DESC), then Column 3 (ASC).

        final PseudoRow row1 = new PseudoRow(1, 2, 3);
        final PseudoRow row2 = new PseudoRow(2, 3, 5);
        // row3 and row4 have the same values for col 1 and 2.
        // They are ordered by col 3 ascending (4 < 8).
        final PseudoRow row3 = new PseudoRow(2, 2, 4);
        final PseudoRow row4 = new PseudoRow(2, 2, 8);
        final PseudoRow row5 = new PseudoRow(3, 1, 0);
        final PseudoRow row6 = new PseudoRow(4, 4, 4);
        // row7 has the same values as row6 for cols 1 and 2.
        // It is ordered by col 3 ascending (4 < 7).
        final PseudoRow row7 = new PseudoRow(4, 4, 7);

        // Note on row2 vs row3:
        // Col 1 is equal (2).
        // Col 2 is sorted descending, so 3 comes before 2.
        // Therefore, row2 [2,3,5] comes before row3 [2,2,4].
        return Arrays.asList(row1, row2, row3, row4, row5, row6, row7);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Tests that a ComparatorChain with a single comparator behaves exactly
     * like the comparator it contains.
     */
    @Test
    void testChainWithSingleComparatorDelegatesToUnderlyingComparator() {
        // Arrange
        final Comparator<Integer> naturalOrder = new ComparableComparator<>();
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);

        final Integer four = 4;
        final Integer six = 6;

        // Act & Assert
        // The chain should behave exactly like the single comparator it contains.
        assertEquals(naturalOrder.compare(four, six), chain.compare(four, six),
                "Comparing a smaller to a larger value should be negative.");
        assertEquals(naturalOrder.compare(six, four), chain.compare(six, four),
                "Comparing a larger to a smaller value should be positive.");
        assertEquals(naturalOrder.compare(four, four), chain.compare(four, four),
                "Comparing an equal value should be zero.");
    }

    // --- Test Helper Classes ---

    /**
     * A test-specific Comparator that compares PseudoRow objects based on a single column.
     */
    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;
        protected final int colIndex;

        ColumnComparator(final int colIndex) {
            this.colIndex = colIndex;
        }

        @Override
        public int compare(final PseudoRow r1, final PseudoRow r2) {
            return Integer.compare(r1.getColumn(colIndex), r2.getColumn(colIndex));
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
     * A test-specific data object representing a row with three integer columns.
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
            return "PseudoRow" + Arrays.toString(cols);
        }
    }
}