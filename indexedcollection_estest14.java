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
// ... many more imports
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest14 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        LinkedList<Integer> linkedList0 = new LinkedList<Integer>();
        Transformer<Integer, Integer> transformer0 = InvokerTransformer.invokerTransformer("");
        IndexedCollection<Integer, Integer> indexedCollection0 = IndexedCollection.uniqueIndexedCollection((Collection<Integer>) linkedList0, transformer0);
        Integer integer0 = new Integer(2314);
        linkedList0.add(integer0);
        // Undeclared exception!
        try {
            indexedCollection0.retainAll(indexedCollection0);
            fail("Expecting exception: RuntimeException");
        } catch (RuntimeException e) {
            //
            // InvokerTransformer: The method '' on 'class java.lang.Integer' does not exist
            //
            verifyException("org.apache.commons.collections4.functors.InvokerTransformer", e);
        }
    }
}