package org.apache.commons.collections4.collection;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class IndexedCollection_ESTest extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAddAllToNonUniqueIndexedCollection() throws Throwable {
        LinkedList<Integer> sourceList = new LinkedList<>();
        sourceList.add(-3039);

        Predicate<Integer> notNullPredicate = NotNullPredicate.notNullPredicate();
        Transformer<Integer, Predicate<Integer>> constantTransformer = new ConstantTransformer<>(notNullPredicate);

        LinkedList<Integer> targetList = new LinkedList<>();
        IndexedCollection<Predicate<Integer>, Integer> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(targetList, constantTransformer);

        boolean result = indexedCollection.addAll(sourceList);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testUniqueIndexedCollectionContainsValue() throws Throwable {
        LinkedList<LinkedList<Integer>> listOfLists = new LinkedList<>();
        Integer key = 1;

        Transformer<LinkedList<Integer>, Integer> constantTransformer = ConstantTransformer.constantTransformer(key);
        IndexedCollection<Integer, LinkedList<Integer>> indexedCollection = IndexedCollection.uniqueIndexedCollection(listOfLists, constantTransformer);

        LinkedList<Integer> innerList = new LinkedList<>();
        indexedCollection.add(innerList);

        Collection<LinkedList<Integer>> values = indexedCollection.values(key);
        assertTrue(values.contains(innerList));
        assertNotNull(values);
    }

    @Test(timeout = 4000)
    public void testRemoveAllFromUniqueIndexedCollection() throws Throwable {
        LinkedList<Integer> integerList = new LinkedList<>();
        Transformer<Integer, Integer> nullTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = IndexedCollection.uniqueIndexedCollection(integerList, nullTransformer);

        boolean result = indexedCollection.removeAll(integerList);
        assertFalse(result);
    }

    @Test(timeout = 4000)
    public void testRemoveNullFromUniqueIndexedCollection() throws Throwable {
        LinkedList<Integer> integerList = new LinkedList<>();
        Transformer<Integer, Integer> nullTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Integer, Integer> indexedCollection = IndexedCollection.uniqueIndexedCollection(integerList, nullTransformer);

        integerList.add(null);
        boolean result = indexedCollection.remove(null);
        assertTrue(result);
    }

    @Test(timeout = 4000)
    public void testGetFromNonUniqueIndexedCollection() throws Throwable {
        LinkedList<Predicate<Object>> predicateList = new LinkedList<>();
        Transformer<Predicate<Object>, Predicate<Object>> nopTransformer = NOPTransformer.nopTransformer();

        AllPredicate<Object> allPredicate = new AllPredicate<>(null);
        predicateList.add(allPredicate);

        IndexedCollection<Predicate<Object>, Predicate<Object>> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(predicateList, nopTransformer);
        Predicate<Object> result = indexedCollection.get(allPredicate);

        assertTrue(predicateList.contains(result));
    }

    @Test(timeout = 4000)
    public void testContainsInNonUniqueIndexedCollection() throws Throwable {
        LinkedList<Integer> integerList = new LinkedList<>();
        Integer value = 3;
        integerList.add(value);

        Predicate<Integer> notNullPredicate = NotNullPredicate.notNullPredicate();
        Transformer<Integer, Predicate<Integer>> constantTransformer = new ConstantTransformer<>(notNullPredicate);

        IndexedCollection<Predicate<Integer>, Integer> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(integerList, constantTransformer);

        boolean containsResult = indexedCollection.contains(value);
        assertTrue(containsResult);
    }

    @Test(timeout = 4000)
    public void testContainsInEmptyUniqueIndexedCollection() throws Throwable {
        LinkedList<Object> objectList = new LinkedList<>();
        Transformer<Object, Object> nullTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection = IndexedCollection.uniqueIndexedCollection(objectList, nullTransformer);

        boolean containsResult = indexedCollection.contains(objectList);
        assertFalse(containsResult);
    }

    @Test(timeout = 4000)
    public void testValuesThrowsStackOverflowError() throws Throwable {
        LinkedList<Object> objectList = new LinkedList<>();
        Transformer<Object, Object> nullTransformer = ConstantTransformer.nullTransformer();
        IndexedCollection<Object, Object> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(objectList, nullTransformer);

        objectList.add(nullTransformer);
        indexedCollection.add(objectList);

        try {
            indexedCollection.values(objectList);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testUniqueIndexedCollectionThrowsRuntimeException() throws Throwable {
        LinkedList<Integer> integerList = new LinkedList<>();
        integerList.add(null);

        Transformer<Integer, Integer> exceptionTransformer = ExceptionTransformer.exceptionTransformer();

        try {
            IndexedCollection.uniqueIndexedCollection(integerList, exceptionTransformer);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            verifyException("org.apache.commons.collections4.functors.ExceptionTransformer", e);
        }
    }

    // Additional test cases can be refactored similarly...

}