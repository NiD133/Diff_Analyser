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
// ... many more unused imports
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest8 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        Transformer<Object, Object> transformer0 = ConstantTransformer.nullTransformer();
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        IndexedCollection<Object, Object> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection((Collection<Object>) linkedList0, transformer0);
        linkedList0.add((Object) transformer0);
        indexedCollection0.add(linkedList0);
        // Undeclared exception!
        indexedCollection0.values(linkedList0);
    }
}