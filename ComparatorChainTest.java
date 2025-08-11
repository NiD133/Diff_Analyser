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
 * Tests for ComparatorChain - a comparator that chains multiple comparators together
 * for multi-level sorting (similar to SQL ORDER BY with multiple columns).
 */
class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.TestRow> {

    // Test data constants for better readability
    private static final TestRow ROW_1_2_3 = new TestRow(1, 2, 3);
    private static final TestRow ROW_2_3_5 = new TestRow(2, 3, 5);
    private static final TestRow ROW_2_2_4 = new TestRow(2, 2, 4);
    private static final TestRow ROW_2_2_8 = new TestRow(2, 2, 8);
    private static final TestRow ROW_3_1_0 = new TestRow(3, 1, 0);
    private static final TestRow ROW_4_4_4 = new TestRow(4, 4, 4);
    private static final TestRow ROW_4_4_7 = new TestRow(4, 4, 7);

    /**
     * A comparator that compares rows by a specific column index.
     * Used to simulate database-style column sorting.
     */
    public static class ColumnComparator implements Comparator<TestRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;

        private final int columnIndex;

        ColumnComparator(final int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(final TestRow row1, final TestRow row2) {
            return Integer.compare(row1.getColumn(columnIndex), row2.getColumn(columnIndex));
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof ColumnComparator && 
                   columnIndex == ((ColumnComparator) other).columnIndex;
        }

        @Override
        public int hashCode() {
            return columnIndex;
        }
    }

    /**
     * A test data structure representing a row with 3 columns.
     * Used to test multi-column sorting scenarios.
     */
    public static class TestRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        
        private final int[] columns = new int[3];

        TestRow(final int column1, final int column2, final int column3) {
            columns[0] = column1;
            columns[1] = column2;
            columns[2] = column3;
        }

        public int getColumn(final int columnIndex) {
            return columns[columnIndex];
        }

        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof TestRow)) {
                return false;
            }
            final TestRow otherRow = (TestRow) other;
            return getColumn(0) == otherRow.getColumn(0) && 
                   getColumn(1) == otherRow.getColumn(1) && 
                   getColumn(2) == otherRow.getColumn(2);
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

    @Override
    public List<TestRow> getComparableObjectsOrdered() {
        // Expected order when sorting by: column 0 ascending, column 1 descending, column 2 ascending
        return new LinkedList<>(Arrays.asList(
            ROW_1_2_3,  // [1,2,3] - lowest in column 0
            ROW_2_3_5,  // [2,3,5] - column 0=2, highest in column 1
            ROW_2_2_4,  // [2,2,4] - column 0=2, column 1=2, lowest in column 2
            ROW_2_2_8,  // [2,2,8] - column 0=2, column 1=2, highest in column 2
            ROW_3_1_0,  // [3,1,0] - column 0=3
            ROW_4_4_4,  // [4,4,4] - column 0=4, lowest in column 2
            ROW_4_4_7   // [4,4,7] - column 0=4, highest in column 2
        ));
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Comparator<TestRow> makeObject() {
        // Create a chain that sorts by: column 0 ascending, column 1 descending, column 2 ascending
        final ComparatorChain<TestRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true);  // true = reverse/descending order
        chain.addComparator(new ColumnComparator(2), false); // false = forward/ascending order
        return chain;
    }

    @Test
    void testEmptyComparatorChain_ShouldThrowException() {
        // Given: An empty comparator chain
        final ComparatorChain<Integer> emptyChain = new ComparatorChain<>();
        final Integer value1 = 4;
        final Integer value2 = 6;

        // When & Then: Comparing should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, 
            () -> emptyChain.compare(value1, value2),
            "An exception should be thrown when a chain contains zero comparators.");
    }

    @Test
    void testEmptyListComparatorChain_ShouldThrowException() {
        // Given: A comparator chain initialized with an empty list
        final List<Comparator<Integer>> emptyList = new LinkedList<>();
        final ComparatorChain<Integer> chainWithEmptyList = new ComparatorChain<>(emptyList);
        final Integer value1 = 4;
        final Integer value2 = 6;

        // When & Then: Comparing should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, 
            () -> chainWithEmptyList.compare(value1, value2));
    }

    @Test
    void testSingleComparatorChain_ShouldWorkCorrectly() {
        // Given: A chain with a single comparator
        final List<Comparator<Integer>> singleComparatorList = new LinkedList<>();
        singleComparatorList.add(new ComparableComparator<>());
        final ComparatorChain<Integer> singleComparatorChain = new ComparatorChain<>(singleComparatorList);
        
        final Integer smallerValue = 4;
        final Integer largerValue = 6;

        // When: Comparing the values
        final int comparisonResult = singleComparatorChain.compare(smallerValue, largerValue);
        final int expectedResult = smallerValue.compareTo(largerValue);

        // Then: Should return the same result as direct comparison
        assertEquals(expectedResult, comparisonResult, 
            "Single comparator chain should return the same result as direct comparison");
    }

    @Test
    void testAddingComparatorToEmptyChain_ShouldWork() {
        // Given: An initially empty chain
        final ComparatorChain<Integer> initiallyEmptyChain = new ComparatorChain<>();
        final Integer smallerValue = 4;
        final Integer largerValue = 6;
        
        // When: Adding a comparator to the empty chain
        initiallyEmptyChain.addComparator(new ComparableComparator<>());
        final int comparisonResult = initiallyEmptyChain.compare(smallerValue, largerValue);
        final int expectedResult = smallerValue.compareTo(largerValue);

        // Then: Should work correctly after adding the comparator
        assertEquals(expectedResult, comparisonResult, 
            "Chain should work correctly after adding a comparator to an empty chain");
    }

    @Test
    void testComparatorChainWithIntegerMinValue_ShouldHandleEdgeCase() {
        // Given: A chain with a comparator that returns Integer.MIN_VALUE
        // This tests the edge case where -1 * Integer.MIN_VALUE is still negative
        final ComparatorChain<Integer> chainWithMinValueComparator = new ComparatorChain<>();
        chainWithMinValueComparator.addComparator((value1, value2) -> {
            final int naturalComparisonResult = value1.compareTo(value2);
            if (naturalComparisonResult < 0) {
                return Integer.MIN_VALUE;  // Edge case: -1 * Integer.MIN_VALUE < 0
            }
            if (naturalComparisonResult > 0) {
                return Integer.MAX_VALUE;
            }
            return 0;
        }, true); // Reverse order

        // When & Then: Should handle the edge case correctly
        assertTrue(chainWithMinValueComparator.compare(4, 5) > 0, 
            "4 should be greater than 5 in reverse order");
        assertTrue(chainWithMinValueComparator.compare(5, 4) < 0, 
            "5 should be less than 4 in reverse order");
        assertEquals(0, chainWithMinValueComparator.compare(4, 4), 
            "Equal values should return 0");
    }
}