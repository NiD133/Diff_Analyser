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
package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SequencesComparator}.
 *
 * This test suite focuses on verifying the correctness of the sequence comparison
 * algorithm under various conditions, including simple cases, complex differences,
 * and edge cases like empty lists, concurrent modifications, and circular dependencies.
 */
public class SequencesComparatorTest {

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructor_withNullFirstSequence_shouldThrowNullPointerException() {
        // Arrange
        final List<String> sequenceB = Collections.emptyList();

        // Act
        new SequencesComparator<>(null, sequenceB);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullSecondSequence_shouldThrowNullPointerException() {
        // Arrange
        final List<String> sequenceA = Collections.emptyList();

        // Act
        new SequencesComparator<>(sequenceA, null);
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullEquator_shouldThrowNullPointerExceptionOnConstruction() {
        // Arrange
        final List<String> sequenceA = Collections.emptyList();
        final List<String> sequenceB = Collections.emptyList();

        // Act
        new SequencesComparator<>(sequenceA, sequenceB, null);
    }

    @Test(expected = ConcurrentModificationException.class)
    public void constructor_whenSourceListIsModifiedViaSublist_throwsConcurrentModificationException() {
        // Arrange
        final List<String> originalList = new LinkedList<>(Collections.singletonList("a"));
        final List<String> subList = originalList.subList(0, 0);

        // Act: Modifying the original list invalidates the subList.
        originalList.add("b");

        // Assert: The constructor, which iterates the lists, should fail.
        new SequencesComparator<>(originalList, subList);
    }

    // --- getScript() Happy Path Tests ---

    @Test
    public void getScript_forTwoEmptyLists_shouldReturnEmptyScript() {
        // Arrange
        final SequencesComparator<Object> comparator =
                new SequencesComparator<>(Collections.emptyList(), Collections.emptyList());

        // Act
        final EditScript<Object> script = comparator.getScript();

        // Assert
        assertEquals("Comparing two empty lists should result in zero modifications", 0, script.getModifications());
    }

    @Test
    public void getScript_forTwoIdenticalLists_shouldReturnOnlyKeepCommands() {
        // Arrange
        final List<String> sequenceA = Arrays.asList("a", "b", "c");
        final List<String> sequenceB = new LinkedList<>(sequenceA); // Use a copy
        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act
        final EditScript<String> script = comparator.getScript();

        // Assert
        assertEquals("Comparing two identical lists should result in zero modifications", 0, script.getModifications());
        assertEquals("LCS length should be the size of the lists", sequenceA.size(), script.getLCSLength());
    }

    @Test
    public void getScript_forSimpleInsertion_shouldReturnOneInsertCommand() {
        // Arrange
        final List<String> sequenceA = Arrays.asList("a", "b");
        final List<String> sequenceB = Arrays.asList("a", "b", "c");
        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act
        final EditScript<String> script = comparator.getScript();

        // Assert
        // To transform A to B, we only need to insert "c".
        assertEquals("A simple append should result in one modification", 1, script.getModifications());
    }

    @Test
    public void getScript_forSimpleDeletion_shouldReturnOneDeleteCommand() {
        // Arrange
        final List<String> sequenceA = Arrays.asList("a", "b", "c");
        final List<String> sequenceB = Arrays.asList("a", "b");
        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act
        final EditScript<String> script = comparator.getScript();

        // Assert
        // To transform A to B, we only need to delete "c".
        assertEquals("A simple deletion should result in one modification", 1, script.getModifications());
    }

    @Test
    public void getScript_forCompleteReplacement_shouldReturnAllDeletesAndInserts() {
        // Arrange
        final List<String> sequenceA = Arrays.asList("a", "b", "c");
        final List<Object> sequenceB = Collections.singletonList(sequenceA); // A list containing the first list
        final SequencesComparator<Object> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act
        final EditScript<Object> script = comparator.getScript();

        // Assert
        // To transform A to B, we must delete "a", "b", "c" (3 mods) and insert the list object (1 mod).
        assertEquals("A complete replacement should have N+M modifications", 4, script.getModifications());
    }

    @Test
    public void getScript_forComplexDifference_shouldReturnCorrectEditScript() {
        // Arrange
        final List<String> sequenceA = Arrays.asList("A", "B", "C", "D", "E");
        final List<String> sequenceB = Arrays.asList("A", "X", "C", "Y", "E");
        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB);

        // Act
        final EditScript<String> script = comparator.getScript();

        // Assert
        // To transform A to B: Keep A, Delete B, Insert X, Keep C, Delete D, Insert Y, Keep E.
        // This is 2 deletions and 2 insertions.
        assertEquals("Complex diff should result in 4 modifications", 4, script.getModifications());
        assertEquals("LCS of [A, C, E] has length 3", 3, script.getLCSLength());
    }

    // --- getScript() Edge Case and Exception Tests ---

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getScript_whenListIsGrownAfterConstruction_canCauseInternalError() {
        // Arrange
        // The comparator's internal arrays are sized at construction.
        final List<Object> list = new LinkedList<>();
        final SequencesComparator<Object> comparator = new SequencesComparator<>(list, list);

        // Act: Modifying the list after construction can corrupt the comparator's state
        // if the list grows, as the internal arrays are now too small.
        list.add("some new object");
        comparator.getScript(); // This should fail with an internal error.
    }

    @Test
    public void getScript_withCircularListReferences_throwsStackOverflowError() {
        // Arrange
        final List<Object> listA = new LinkedList<>();
        final List<Object> listB = new LinkedList<>();
        listA.add(listB);
        listB.add(listA);
        final SequencesComparator<Object> comparator = new SequencesComparator<>(listA, listB);

        // Act & Assert
        // The default .equals() on lists will recurse infinitely, causing a StackOverflowError.
        try {
            comparator.getScript();
            fail("Expected a StackOverflowError due to circular list references");
        } catch (final StackOverflowError e) {
            // Expected
        }
    }

    @Test
    public void getScript_withCustomEquator_shouldUseEquatorForComparison() {
        // Arrange: Use a case-insensitive equator
        final Equator<String> caseInsensitiveEquator = (s1, s2) ->
                s1 != null && s1.equalsIgnoreCase(s2);

        final List<String> sequenceA = Arrays.asList("apple", "banana");
        final List<String> sequenceB = Arrays.asList("APPLE", "BANANA");

        final SequencesComparator<String> comparator = new SequencesComparator<>(sequenceA, sequenceB, caseInsensitiveEquator);

        // Act
        final EditScript<String> script = comparator.getScript();

        // Assert
        // With the custom equator, the lists are identical.
        assertEquals("With custom equator, lists should be identical", 0, script.getModifications());
    }
}