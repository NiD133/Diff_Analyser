package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.lang.reflect.Array;
import java.util.*;

import org.apache.commons.collections4.*;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.iterators.IteratorChain;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
        mockJVMNonDeterminism = true,
        useVFS = true,
        useVNET = true,
        resetStaticState = true,
        separateClassLoader = true
)
public class CompositeSet_ESTest extends CompositeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAddAndRemoveWithExceptionPredicate() {
        // Create an empty LinkedHashSet
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        assertNotNull(linkedHashSet);
        assertTrue(linkedHashSet.isEmpty());

        // Add an integer to the set
        Integer testInteger = -3865;
        linkedHashSet.add(testInteger);
        assertTrue(linkedHashSet.contains(testInteger));
        assertEquals(1, linkedHashSet.size());

        // Create a CompositeSet with the LinkedHashSet
        CompositeSet<Integer> compositeSet = new CompositeSet<>(linkedHashSet);
        assertTrue(compositeSet.contains(testInteger));

        // Create an ExceptionPredicate
        Predicate<Integer> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        assertNotNull(exceptionPredicate);

        // Attempt to remove elements using the ExceptionPredicate, expecting a RuntimeException
        try {
            compositeSet.removeIf(exceptionPredicate);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionPredicate", e);
        }
    }

    @Test(timeout = 4000)
    public void testStackOverflowErrorWithCircularReference() {
        // Create an empty LinkedHashSet
        LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<>();
        assertNotNull(linkedHashSet);

        // Create an array of sets with circular references
        Set<Object>[] setArray = (Set<Object>[]) Array.newInstance(Set.class, 3);
        setArray[0] = linkedHashSet;
        setArray[1] = linkedHashSet;
        linkedHashSet.add(setArray[1]);

        // Attempt to create a CompositeSet with circular references, expecting StackOverflowError
        try {
            new CompositeSet<>(setArray);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testUniquePredicate() {
        // Create a UniquePredicate
        UniquePredicate<Integer> uniquePredicate = new UniquePredicate<>();
        assertNotNull(uniquePredicate);

        // Create an empty LinkedHashSet and add the UniquePredicate
        LinkedHashSet<Object> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(uniquePredicate);

        // Create a CompositeSet with the LinkedHashSet
        CompositeSet<Object> compositeSet = new CompositeSet<>(linkedHashSet);
        assertTrue(compositeSet.contains(uniquePredicate));

        // Remove the UniquePredicate from the CompositeSet
        boolean removed = compositeSet.remove(uniquePredicate);
        assertTrue(removed);
        assertTrue(linkedHashSet.isEmpty());
    }

    @Test(timeout = 4000)
    public void testCompositeSetEquality() {
        // Create two empty CompositeSets
        CompositeSet<Integer> compositeSet1 = new CompositeSet<>();
        CompositeSet<Integer> compositeSet2 = new CompositeSet<>();

        // Check equality of two empty CompositeSets
        assertTrue(compositeSet1.equals(compositeSet2));

        // Add a LinkedHashSet to one of the CompositeSets
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        compositeSet1.addComposited(linkedHashSet);

        // Clear one CompositeSet and check equality again
        compositeSet1.clear();
        assertTrue(compositeSet1.equals(compositeSet2));
    }

    @Test(timeout = 4000)
    public void testRemoveIfWithNullPredicate() {
        // Create a CompositeSet with a null set
        CompositeSet<Integer> compositeSet = new CompositeSet<>((Set<Integer>) null);

        // Attempt to remove elements with a null predicate
        boolean result = compositeSet.removeIf(null);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testAddCompositedWithCollision() {
        // Create a LinkedHashSet and add an integer
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>();
        Integer testInteger = 113;
        linkedHashSet.add(testInteger);

        // Create a CompositeSet and add the LinkedHashSet
        CompositeSet<Integer> compositeSet = new CompositeSet<>(linkedHashSet);

        // Attempt to add the same LinkedHashSet again, expecting UnsupportedOperationException
        try {
            compositeSet.addComposited(linkedHashSet);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            verifyException("org.apache.commons.collections4.set.CompositeSet", e);
        }
    }

    // Additional test cases can be added here following the same pattern
}