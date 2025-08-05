/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
 * This class tests the functionality of the ComparatorChain, including its
 * construction, exception handling for invalid states, and complex sorting logic.
 * </p>
 */
@DisplayName("ComparatorChain")
class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    //--- Test Fixture: Helper classes for testing multi-column sorting ---

    /**
     * A simple data object with three integer columns for comparison tests.
     */
    public static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        public int[] cols = new int[3];

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
            final PseudoRow row = (PseudoRow) o;
            return getColumn(0) == row.getColumn(0) &&
                   getColumn(1) == row.getColumn(1) &&
                   getColumn(2) == row.getColumn(2);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cols);
        }

        @Override
        public String toString() {
            return "[" + cols[0] + "," + cols[1] + "," + cols[2] + "]";
        }
    }

    /**
     * A Comparator that compares PseudoRow objects based on a single column index.
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
            return that instanceof ColumnComparator && colIndex == ((ColumnComparator) that).colIndex;
        }

        @Override
        public int hashCode() {
            return colIndex;
        }
    }

    //--- AbstractComparatorTest implementation ---

    @Override
    public Comparator<PseudoRow> makeObject() {
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true); // reverse sort on the second column
        chain.addComparator(new ColumnComparator(2), false); // forward sort on the third column
        return chain;
    }

    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // The ordering is based on the chain created in makeObject():
        // Column 0: Ascending
        // Column 1: Descending
        // Column 2: Ascending
        return new LinkedList<>(Arrays.asList(
            new PseudoRow(1, 2, 3),
            // Sort by col 0 (asc): 1 < 2
            new PseudoRow(2, 3, 5),
            // Sort by col 0: 2 == 2. Then col 1 (desc): 3 comes before 2
            new PseudoRow(2, 2, 4),
            // Sort by col 0: 2 == 2. Then col 1: 2 == 2. Then col 2 (asc): 4 < 8
            new PseudoRow(2, 2, 8),
            // Sort by col 0 (asc): 2 < 3
            new PseudoRow(3, 1, 0),
            // Sort by col 0 (asc): 3 < 4
            new PseudoRow(4, 4, 4),
            // Sort by col 0: 4 == 4. Then col 1: 4 == 4. Then col 2 (asc): 4 < 7
            new PseudoRow(4, 4, 7)
        ));
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //--- Individual Test Cases ---

    @Test
    @DisplayName("should throw exception when compare() is called on an empty chain")
    void compareOnEmptyChainShouldThrowException() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> chain.compare(4, 6));
    }

    @Test
    @DisplayName("should throw exception when compare() is called on a chain from an empty list")
    void compareOnChainInitializedWithEmptyListShouldThrowException() {
        // Arrange
        final List<Comparator<Integer>> emptyList = new LinkedList<>();
        final ComparatorChain<Integer> chain = new ComparatorChain<>(emptyList);

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () -> chain.compare(4, 6));
    }

    @Test
    @DisplayName("should sort correctly with a single comparator added after construction")
    void chainWithSingleAddedComparatorShouldSortCorrectly() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(new ComparableComparator<>());
        final Integer smaller = 4;
        final Integer larger = 6;

        // Act & Assert
        assertTrue(chain.compare(smaller, larger) < 0, "4 should be less than 6");
        assertTrue(chain.compare(larger, smaller) > 0, "6 should be greater than 4");
        assertEquals(0, chain.compare(smaller, smaller), "4 should be equal to 4");
    }

    @Test
    @DisplayName("should sort correctly when initialized with a list of comparators")
    void chainInitializedWithListOfComparatorsShouldSortCorrectly() {
        // Arrange
        final List<Comparator<Integer>> comparators = new LinkedList<>();
        comparators.add(new ComparableComparator<>());
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparators);
        final Integer smaller = 4;
        final Integer larger = 6;

        // Act & Assert
        assertTrue(chain.compare(smaller, larger) < 0, "4 should be less than 6");
        assertTrue(chain.compare(larger, smaller) > 0, "6 should be greater than 4");
        assertEquals(0, chain.compare(smaller, smaller), "4 should be equal to 4");
    }

    @Test
    @DisplayName("should correctly handle reversing a comparator that returns Integer.MIN_VALUE")
    void reversedComparatorReturningIntegerMinValueShouldWorkCorrectly() {
        // This test addresses an edge case with reversing a comparator's result.
        // The reversal is implemented as `result * -1`. If `result` is `Integer.MIN_VALUE`,
        // `-1 * Integer.MIN_VALUE` results in `Integer.MIN_VALUE` due to integer overflow.
        // The test ensures the chain correctly interprets this reversed negative result.

        // Arrange
        final Comparator<Integer> minMaxComparator = (a, b) -> {
            final int result = a.compareTo(b);
            if (result < 0) {
                return Integer.MIN_VALUE; // e.g., for (4, 5)
            }
            if (result > 0) {
                return Integer.MAX_VALUE; // e.g., for (5, 4)
            }
            return 0; // e.g., for (4, 4)
        };

        final ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(minMaxComparator, true); // Add the comparator in reverse order

        // Act & Assert
        // Normal compare(4, 5) -> MIN_VALUE (< 0). Reversed should be > 0.
        assertTrue(chain.compare(4, 5) > 0, "Reversed 'less than' should be 'greater than'");

        // Normal compare(5, 4) -> MAX_VALUE (> 0). Reversed should be < 0.
        assertTrue(chain.compare(5, 4) < 0, "Reversed 'greater than' should be 'less than'");

        // Normal compare(4, 4) -> 0. Reversed should be 0.
        assertEquals(0, chain.compare(4, 4), "Reversed 'equal' should be 'equal'");
    }
}