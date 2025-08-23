package org.apache.commons.collections4.map;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.ExceptionTransformer;
import org.apache.commons.collections4.functors.FactoryTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.functors.MapTransformer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class TransformedSortedMap_ESTestTest14 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        TreeMap<Object, Predicate<Integer>> treeMap0 = new TreeMap<Object, Predicate<Integer>>();
        TransformedSortedMap<Object, Predicate<Integer>> transformedSortedMap0 = TransformedSortedMap.transformingSortedMap((SortedMap<Object, Predicate<Integer>>) treeMap0, (Transformer<? super Object, ?>) null, (Transformer<? super Predicate<Integer>, ? extends Predicate<Integer>>) null);
        transformedSortedMap0.map = (Map<Object, Predicate<Integer>>) transformedSortedMap0;
        // Undeclared exception!
        try {
            TransformedSortedMap.transformedSortedMap((SortedMap<Object, Predicate<Integer>>) transformedSortedMap0, (Transformer<? super Object, ?>) null, (Transformer<? super Predicate<Integer>, ? extends Predicate<Integer>>) null);
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            //
            // no message in exception (getMessage() returned null)
            //
        }
    }
}
