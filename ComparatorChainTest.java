package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the ComparatorChain class.
 */
class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    /**
     * Comparator for comparing PseudoRow objects based on a specific column index.
     */
    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;
        private final int columnIndex;

        ColumnComparator(final int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(final PseudoRow row1, final PseudoRow row2) {
            return Integer.compare(row1.getColumn(columnIndex), row2.getColumn(columnIndex));
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ColumnComparator)) return false;
            ColumnComparator other = (ColumnComparator) obj;
            return columnIndex == other.columnIndex;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(columnIndex);
        }
    }

    /**
     * Represents a row with three integer columns.
     */
    public static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        private final int[] columns = new int[3];

        PseudoRow(final int col1, final int col2, final int col3) {
            columns[0] = col1;
            columns[1] = col2;
            columns[2] = col3;
        }

        public int getColumn(final int index) {
            return columns[index];
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof PseudoRow)) return false;
            PseudoRow other = (PseudoRow) obj;
            return Arrays.equals(columns, other.columns);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(columns);
        }

        @Override
        public String toString() {
            return Arrays.toString(columns);
        }
    }

    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
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

    @Override
    public Comparator<PseudoRow> makeObject() {
        ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true); // Reverse the second column
        chain.addComparator(new ColumnComparator(2), false);
        return chain;
    }

    @Test
    void testEmptyComparatorChainThrowsException() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        assertThrows(UnsupportedOperationException.class, () -> chain.compare(4, 6),
            "An exception should be thrown when a chain contains zero comparators.");
    }

    @Test
    void testComparatorChainWithMinValueHandling() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator((a, b) -> {
            int result = a.compareTo(b);
            if (result < 0) return Integer.MIN_VALUE;
            if (result > 0) return Integer.MAX_VALUE;
            return 0;
        }, true);

        assertTrue(chain.compare(4, 5) > 0, "Expected 4 to be greater than 5 due to reverse order.");
        assertTrue(chain.compare(5, 4) < 0, "Expected 5 to be less than 4 due to reverse order.");
        assertEquals(0, chain.compare(4, 4), "Expected equal values to compare as 0.");
    }

    @Test
    void testListBasedComparatorChain() {
        List<Comparator<Integer>> comparators = new LinkedList<>();
        comparators.add(new ComparableComparator<>());
        ComparatorChain<Integer> chain = new ComparatorChain<>(comparators);

        int expectedComparison = Integer.compare(4, 6);
        assertEquals(expectedComparison, chain.compare(4, 6), "Comparison should return the correct order.");
    }

    @Test
    void testSingleComparatorChain() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(new ComparableComparator<>());

        int expectedComparison = Integer.compare(4, 6);
        assertEquals(expectedComparison, chain.compare(4, 6), "Comparison should return the correct order.");
    }
}