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
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest54 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test53() throws Throwable {
        Transformer<Object, Object> transformer0 = ConstantTransformer.nullTransformer();
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        IndexedCollection<Object, Object> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection((Collection<Object>) linkedList0, transformer0);
        boolean boolean0 = indexedCollection0.removeIf((java.util.function.Predicate<? super Object>) null);
        assertFalse(boolean0);
    }
}