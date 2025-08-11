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

import org.junit.jupiter.api.Test;

/**
 * Tests for ComparatorChain.
 *
 * The domain used here is a "row" with three integer columns.
 * The default chain used by AbstractComparatorTest sorts by:
 * - column 0: ascending
 * - column 1: descending
 * - column 2: ascending
 */
class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    /**
     * Simple comparator that compares PseudoRow values by a single column index.
     */
    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;

        private final int columnIndex;

        ColumnComparator(final int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(final PseudoRow left, final PseudoRow right) {
            return Integer.compare(left.getColumn(columnIndex), right.getColumn(columnIndex));
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof ColumnComparator
                && columnIndex == ((ColumnComparator) other).columnIndex;
        }

        @Override
        public int hashCode() {
            return columnIndex;
        }
    }

    /**
     * Simple value-object with three integer "columns".
     */
    public static class PseudoRow implements Serializable {

        private static final long serialVersionUID = 8085570439751032499L;

        // Column values in order: 0, 1, 2.
        private final int[] columns = new int[3];

        PseudoRow(final int col0, final int col1, final int col2) {
            columns[0] = col0;
            columns[1] = col1;
            columns[2] = col2;
        }

        public int getColumn(final int index) {
            return columns[index];
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof PseudoRow)) {
                return false;
            }
            final PseudoRow other = (PseudoRow) obj;
            return getColumn(0) == other.getColumn(0)
                && getColumn(1) == other.getColumn(1)
                && getColumn(2) == other.getColumn(2);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(columns);
        }

        @Override
        public String toString() {
            return "[" + columns[0] + "," + columns[1] + "," + columns[2] + "]";
        }
    }

    // Serialization compatibility helper (kept for reference).
    // To regenerate, uncomment and run locally:
    // void testCreate() throws Exception {
    //     writeExternalFormToDisk((java.io.Serializable) makeObject(), "src/test/resources/data/test/ComparatorChain.version4.obj");
    // }

    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // Expected order when sorting by:
        // - column 0 ascending
        // - column 1 descending
        // - column 2 ascending
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
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Comparator<PseudoRow> makeObject() {
        // Build the chain used by AbstractComparatorTest:
        // 0th: ascending, 1st: descending, 2nd: ascending.
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true);   // reverse the second column (descending)
        chain.addComparator(new ColumnComparator(2), false);  // third column ascending
        return chain;
    }

    @Test
    void compareThrowsWhenChainConstructedWithEmptyComparatorList() {
        // Arrange
        final List<Comparator<Integer>> emptyComparators = new LinkedList<>();
        final ComparatorChain<Integer> chainUnderTest = new ComparatorChain<>(emptyComparators);
        final Integer left = 4;
        final Integer right = 6;

        // Act + Assert
        assertThrows(UnsupportedOperationException.class, () -> chainUnderTest.compare(left, right));
    }

    @Test
    void compareThrowsWhenChainHasNoComparatorsAdded() {
        // Arrange
        final ComparatorChain<Integer> chainUnderTest = new ComparatorChain<>();
        final Integer left = 4;
        final Integer right = 6;

        // Act + Assert
        assertThrows(
            UnsupportedOperationException.class,
            () -> chainUnderTest.compare(left, right),
            "An exception should be thrown when a chain contains zero comparators."
        );
    }

    @Test
    void compareWithReversedComparatorHandlesIntegerMinSentinelCorrectly() {
        // Arrange
        // This comparator deliberately returns Integer.MIN_VALUE/Integer.MAX_VALUE
        // for less/greater results. When reversed, ComparatorChain must not negate
        // Integer.MIN_VALUE (which would overflow). It should still give the correct sign.
        final ComparatorChain<Integer> chainUnderTest = new ComparatorChain<>();
        chainUnderTest.addComparator((a, b) -> {
            final int result = a.compareTo(b);
            if (result < 0) {
                return Integer.MIN_VALUE;
            }
            if (result > 0) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }, true); // reversed

        // Act + Assert
        assertTrue(chainUnderTest.compare(4, 5) > 0, "4 vs 5 should be 'greater' when reversed");
        assertTrue(chainUnderTest.compare(5, 4) < 0, "5 vs 4 should be 'less' when reversed");
        assertEquals(0, chainUnderTest.compare(4, 4), "Equal values should compare as 0");
    }

    @Test
    void compareDelegatesToComparatorProvidedViaListConstructor() {
        // Arrange
        final List<Comparator<Integer>> comparators = new LinkedList<>();
        comparators.add(new ComparableComparator<>());
        final ComparatorChain<Integer> chainUnderTest = new ComparatorChain<>(comparators);
        final Integer left = 4;
        final Integer right = 6;

        final int expected = left.compareTo(right);

        // Act
        final int actual = chainUnderTest.compare(left, right);

        // Assert
        assertEquals(expected, actual, "Comparison returns the right order");
    }

    @Test
    void compareDelegatesToComparatorAddedAfterDefaultConstructor() {
        // Arrange
        final ComparatorChain<Integer> chainUnderTest = new ComparatorChain<>();
        chainUnderTest.addComparator(new ComparableComparator<>());
        final Integer left = 4;
        final Integer right = 6;

        final int expected = left.compareTo(right);

        // Act
        final int actual = chainUnderTest.compare(left, right);

        // Assert
        assertEquals(expected, actual, "Comparison returns the right order");
    }
}