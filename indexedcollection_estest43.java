package org.apache.commons.collections4.collection;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.AllPredicate;
// ... many other unused imports
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest43 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        HashMap<Integer, Integer> hashMap0 = new HashMap<Integer, Integer>();
        Collection<Integer> collection0 = hashMap0.values();
        Transformer<Integer, Object> transformer0 = ExceptionTransformer.exceptionTransformer();
        IndexedCollection<Object, Integer> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection(collection0, transformer0);
        Integer integer0 = new Integer((-2641));
        // Undeclared exception!
        try {
            indexedCollection0.add(integer0);
            fail("Expecting exception: UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("java.util.AbstractCollection", e);
        }
    }
}