package org.apache.commons.collections4.iterators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentModificationException;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class FilterListIterator_ESTest extends FilterListIterator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testConcurrentModificationException() {
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(nullPredicate);
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        linkedList.add(null);
        linkedList.add(5795);
        
        ListIterator<Integer> listIterator = linkedList.listIterator(1);
        filterListIterator.setListIterator(listIterator);
        
        linkedList.add(null);  // This should cause a ConcurrentModificationException
        
        try {
            filterListIterator.hasPrevious();
            fail("Expecting exception: ConcurrentModificationException");
        } catch (ConcurrentModificationException e) {
            verifyException("java.util.LinkedList$ListItr", e);
        }
    }

    @Test(timeout = 4000)
    public void testNoSuchElementException() {
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(nullPredicate);
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        linkedList.add(null);
        linkedList.add(5795);
        
        ListIterator<Integer> listIterator = linkedList.listIterator(1);
        filterListIterator.setListIterator(listIterator);
        
        filterListIterator.hasNext();
        
        try {
            filterListIterator.previous();
            fail("Expecting exception: NoSuchElementException");
        } catch (NoSuchElementException e) {
            verifyException("org.apache.commons.collections4.iterators.FilterListIterator", e);
        }
    }

    @Test(timeout = 4000)
    public void testRuntimeExceptionWithExceptionPredicate() {
        Predicate<Object> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        linkedList.add(null);
        
        ListIterator<Integer> listIterator = linkedList.listIterator(1);
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(listIterator, exceptionPredicate);
        
        try {
            filterListIterator.hasNext();
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionPredicate", e);
        }
    }

    @Test(timeout = 4000)
    public void testNextIndex() {
        Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();
        FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(nullPredicate);
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        linkedList.add(null);
        
        ListIterator<Integer> listIterator = linkedList.listIterator(1);
        filterListIterator.setListIterator(listIterator);
        
        assertEquals(1, filterListIterator.nextIndex());
        
        filterListIterator.previous();
        assertEquals(-1, filterListIterator.previousIndex());
    }
    
    // Additional tests can be added here following the same pattern

}