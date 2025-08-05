package org.apache.commons.collections4.sequence;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.Equator;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.PredicateTransformer;
import org.apache.commons.collections4.sequence.EditScript;
import org.apache.commons.collections4.sequence.SequencesComparator;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class SequencesComparatorTest {

    @Test(timeout = 4000)
    public void testEditScriptWithDifferentSequences() throws Throwable {
        LinkedList<Object> firstList = new LinkedList<>();
        LinkedList<Object> secondList = new LinkedList<>();

        Object commonObject = new Object();
        Integer number = -74;

        firstList.add(commonObject);
        firstList.addLast(number);
        firstList.add(number);

        secondList.add(new Object());
        secondList.add(commonObject);
        secondList.add(number);

        SequencesComparator<Object> comparator = new SequencesComparator<>(firstList, secondList);
        EditScript<Object> editScript = comparator.getScript();

        assertEquals(3, editScript.getModifications());
    }

    @Test(timeout = 4000)
    public void testEditScriptWithIdenticalSequences() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        Object commonObject = new Object();

        list.add(commonObject);
        list.add(commonObject);

        SequencesComparator<Object> comparator = new SequencesComparator<>(list, list);
        EditScript<Object> editScript = comparator.getScript();

        assertEquals(0, editScript.getModifications());
    }

    @Test(timeout = 4000)
    public void testConcurrentModificationException() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        List<Object> sublist = list.subList(0, 0);

        list.add(sublist);

        SequencesComparator<Object> comparator = new SequencesComparator<>(list, list);

        try {
            comparator.getScript();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowError() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        list.add(list);

        SequencesComparator<Object> comparator = new SequencesComparator<>(list, list);

        try {
            comparator.getScript();
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullEquator() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        list.add(null);

        SequencesComparator<Object> comparator = new SequencesComparator<>(list, list, null);

        try {
            comparator.getScript();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testArrayIndexOutOfBoundsException() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        SequencesComparator<Object> comparator = new SequencesComparator<>(list, list);

        list.add(comparator);

        try {
            comparator.getScript();
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testNullPointerExceptionWithNullLists() throws Throwable {
        DefaultEquator<Object> equator = DefaultEquator.defaultEquator();

        try {
            new SequencesComparator<>(null, null, equator);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testPredicateTransformer() throws Throwable {
        LinkedList<Object> list = new LinkedList<>();
        List<Object> sublist = list.subList(0, 0);

        PredicateTransformer<Object> transformer = new PredicateTransformer<>(NotNullPredicate.notNullPredicate());
        Boolean transformedValue = transformer.transform(0);

        list.add(transformedValue);

        try {
            new SequencesComparator<>(list, sublist);
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEditScriptWithSelfReferencingLists() throws Throwable {
        LinkedList<Object> firstList = new LinkedList<>();
        LinkedList<Object> secondList = new LinkedList<>();

        firstList.offerFirst(secondList);
        secondList.add(new Object());
        secondList.add(firstList);
        secondList.add(new Object());

        SequencesComparator<Object> comparator = new SequencesComparator<>(firstList, secondList);
        EditScript<Object> editScript = comparator.getScript();

        assertEquals(4, editScript.getModifications());
    }

    @Test(timeout = 4000)
    public void testEditScriptWithNestedLists() throws Throwable {
        LinkedList<Object> firstList = new LinkedList<>();
        LinkedList<Object> secondList = new LinkedList<>(firstList);

        firstList.add(secondList);
        firstList.offerFirst(new Object());

        SequencesComparator<Object> comparator = new SequencesComparator<>(secondList, firstList);
        EditScript<Object> editScript = comparator.getScript();

        assertEquals(4, editScript.getModifications());
    }
}