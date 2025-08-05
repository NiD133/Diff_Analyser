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
 */
class ComparatorChainTest extends AbstractComparatorTest<ComparatorChainTest.PseudoRow> {

    public static class ColumnComparator implements Comparator<PseudoRow>, Serializable {
        private static final long serialVersionUID = -2284880866328872105L;

        protected int colIndex;

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

    public static class PseudoRow implements Serializable {
        private static final long serialVersionUID = 8085570439751032499L;
        public int[] cols = new int[3];

        PseudoRow(final int col1, final int col2, final int col3) {
            cols[0] = col1;
            cols[1] = col2;
            cols[2] = col3;
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

        public int getColumn(final int colIndex) {
            return cols[colIndex];
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cols);
        }

        @Override
        public String toString() {
            return "Row[col0=" + cols[0] + ", col1=" + cols[1] + ", col2=" + cols[2] + "]";
        }
    }

    @Override
    public List<PseudoRow> getComparableObjectsOrdered() {
        // Expected order for "0th forward, 1st reverse, 2nd forward" sort
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
        return createSampleChain();
    }

    /**
     * Creates a standard test chain with:
     * - Forward sort on column 0
     * - Reverse sort on column 1
     * - Forward sort on column 2
     */
    private ComparatorChain<PseudoRow> createSampleChain() {
        final ComparatorChain<PseudoRow> chain = new ComparatorChain<>(new ColumnComparator(0));
        chain.addComparator(new ColumnComparator(1), true);  // Reverse second column
        chain.addComparator(new ColumnComparator(2), false); // Forward third column
        return chain;
    }

    @Test
    void testCompare_WhenComparatorListIsEmpty_ThrowsException() {
        final List<Comparator<Integer>> emptyList = new LinkedList<>();
        final ComparatorChain<Integer> chain = new ComparatorChain<>(emptyList);
        
        assertThrows(UnsupportedOperationException.class, 
            () -> chain.compare(4, 6),
            "Should throw when comparator list is empty"
        );
    }

    @Test
    void testCompare_WhenNoComparatorsAdded_ThrowsException() {
        final ComparatorChain<Integer> chain = new ComparatorChain<>();
        
        assertThrows(UnsupportedOperationException.class, 
            () -> chain.compare(4, 6),
            "Should throw when chain has no comparators"
        );
    }

    @Test
    void testReverseComparator_WithExtremeIntegerValues_ComparesCorrectly() {
        final ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(createExtremeValuesComparator(), true);
        
        // Original comparator: 4 < 5 -> returns MIN_VALUE, reversed should become positive
        assertTrue(chain.compare(4, 5) > 0, "4 should appear after 5 in reversed order");
        
        // Original comparator: 5 > 4 -> returns MAX_VALUE, reversed should become negative
        assertTrue(chain.compare(5, 4) < 0, "5 should appear before 4 in reversed order");
        
        assertEquals(0, chain.compare(4, 4), "Equal values should compare as 0");
    }

    /**
     * Creates comparator returning extreme values:
     * - Integer.MIN_VALUE when a < b
     * - Integer.MAX_VALUE when a > b
     * - 0 when equal
     */
    private Comparator<Integer> createExtremeValuesComparator() {
        return (a, b) -> {
            final int result = a.compareTo(b);
            if (result < 0) return Integer.MIN_VALUE;
            if (result > 0) return Integer.MAX_VALUE;
            return 0;
        };
    }

    @Test
    void testCompare_WhenConstructedWithComparatorList_ComparesCorrectly() {
        final List<Comparator<Integer>> comparators = List.of(new ComparableComparator<>());
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparators);
        
        final int expected = Integer.compare(4, 6);
        assertEquals(expected, chain.compare(4, 6), "Should return standard comparison result");
    }

    @Test
    void testCompare_AfterAddingComparator_ComparesCorrectly() {
        final ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(new ComparableComparator<>());
        
        final int expected = Integer.compare(4, 6);
        assertEquals(expected, chain.compare(4, 6), "Should return standard comparison result");
    }
}