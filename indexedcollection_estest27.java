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
import org.apache.commons.collections4.functors.AnyPredicate;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.CloneTransformer;
import org.apache.commons.collections4.functors.ClosureTransformer;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.DefaultEquator;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.FalsePredicate;
import org.apache.commons.collections4.functors.ForClosure;
import org.apache.commons.collections4.functors.IfTransformer;
import org.apache.commons.collections4.functors.InstanceofPredicate;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.NOPClosure;
import org.apache.commons.collections4.functors.NOPTransformer;
import org.apache.commons.collections4.functors.NonePredicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.NullIsFalsePredicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.apache.commons.collections4.functors.SwitchTransformer;
import org.apache.commons.collections4.functors.TransformedPredicate;
import org.apache.commons.collections4.functors.TransformerClosure;
import org.apache.commons.collections4.functors.TransformerPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class IndexedCollection_ESTestTest27 extends IndexedCollection_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        LinkedList<Object> linkedList0 = new LinkedList<Object>();
        ConstantTransformer<Object, Integer> constantTransformer0 = new ConstantTransformer<Object, Integer>((Integer) null);
        IndexedCollection<Integer, Object> indexedCollection0 = IndexedCollection.nonUniqueIndexedCollection((Collection<Object>) linkedList0, (Transformer<Object, Integer>) constantTransformer0);
        IndexedCollection<Integer, Object> indexedCollection1 = IndexedCollection.uniqueIndexedCollection((Collection<Object>) indexedCollection0, (Transformer<Object, Integer>) constantTransformer0);
        TransformerClosure<Object> transformerClosure0 = new TransformerClosure<Object>(constantTransformer0);
        Object object0 = new Object();
        linkedList0.push(object0);
        indexedCollection0.add(transformerClosure0);
        // Undeclared exception!
        try {
            indexedCollection1.reindex();
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // Duplicate key in uniquely indexed collection.
            //
            verifyException("org.apache.commons.collections4.collection.IndexedCollection", e);
        }
    }
}
