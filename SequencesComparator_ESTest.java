package org.apache.commons.collections4.sequence;

import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Readable tests for SequencesComparator focusing on typical usage and
 * clearly-defined edge cases.
 */
public class SequencesComparatorTest {

    /**
     * Comparing two empty lists should yield an empty edit script.
     */
    @Test
    public void emptyLists_produceEmptyScript() {
        List<Integer> a = Arrays.asList();
        List<Integer> b = Arrays.asList();

        EditScript<Integer> script = new SequencesComparator<>(a, b).getScript();

        assertEquals(0, script.getModifications());
    }

    /**
     * Comparing two identical lists should yield an empty edit script.
     */
    @Test
    public void identicalLists_produceEmptyScript() {
        List<Integer> a = Arrays.asList(1, 2, 3);
        List<Integer> b = Arrays.asList(1, 2, 3);

        EditScript<Integer> script = new SequencesComparator<>(a, b).getScript();

        assertEquals(0, script.getModifications());
    }

    /**
     * One insertion needed to transform [1] into [1, 2].
     */
    @Test
    public void insertion_isDetected() {
        List<Integer> from = Arrays.asList(1);
        List<Integer> to = Arrays.asList(1, 2);

        EditScript<Integer> script = new SequencesComparator<>(from, to).getScript();

        assertEquals(1, script.getModifications());
    }

    /**
     * One deletion needed to transform [1, 2] into [1].
     */
    @Test
    public void deletion_isDetected() {
        List<Integer> from = Arrays.asList(1, 2);
        List<Integer> to = Arrays.asList(1);

        EditScript<Integer> script = new SequencesComparator<>(from, to).getScript();

        assertEquals(1, script.getModifications());
    }

    /**
     * Replacing a single element is modeled as one delete + one insert.
     */
    @Test
    public void replacement_countsAsDeletePlusInsert() {
        List<Integer> from = Arrays.asList(1);
        List<Integer> to = Arrays.asList(2);

        EditScript<Integer> script = new SequencesComparator<>(from, to).getScript();

        assertEquals(2, script.getModifications());
    }

    /**
     * Verifies that a custom Equator is honored. If all elements are considered equal,
     * lists of the same length produce an empty script even if the values differ.
     */
    @Test
    public void customEquator_isHonored() {
        // Equator that treats any two objects as equal.
        Equator<Object> allEqual = new Equator<Object>() {
            @Override
            public boolean equate(Object o1, Object o2) {
                return true;
            }

            @Override
            public int hash(Object o) {
                return 0;
            }
        };

        List<Object> from = Arrays.asList("x", "y", "z");
        List<Object> to = Arrays.asList(1, 2, 3);

        EditScript<Object> script = new SequencesComparator<>(from, to, allEqual).getScript();

        assertEquals(0, script.getModifications());
    }

    /**
     * Passing null sequences is not supported and results in a NullPointerException
     * during construction (the comparator needs the sizes of both sequences).
     */
    @Test
    public void nullSequences_throwNPEInConstructor() {
        try {
            new SequencesComparator<>(null, null, DefaultEquator.defaultEquator());
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    /**
     * Passing a null Equator leads to a NullPointerException when running the comparison.
     */
    @Test
    public void nullEquator_throwsNPEOnGetScript() {
        List<String> a = Arrays.asList("a");
        List<String> b = Arrays.asList("a");

        SequencesComparator<String> cmp = new SequencesComparator<>(a, b, null);

        try {
            cmp.getScript();
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    /**
     * Using a subList whose backing list is modified afterwards triggers a
     * ConcurrentModificationException as per JDK subList semantics.
     */
    @Test
    public void subListBackedByModifiedList_throwsConcurrentModificationException() {
        List<Integer> backing = new ArrayList<>();
        List<Integer> sub = backing.subList(0, 0);

        // Structural modification of the backing list invalidates the subList view.
        backing.add(42);

        try {
            new SequencesComparator<>(sub, sub);
            fail("Expected ConcurrentModificationException");
        } catch (ConcurrentModificationException expected) {
            // expected
        }
    }
}