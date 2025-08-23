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

public class TransformedSortedMap_ESTestTest18 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        TreeMap<Object, Object> treeMap0 = new TreeMap<Object, Object>();
        Transformer<Object, Object> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ?>) treeMap0);
        TransformedSortedMap<Object, Object> transformedSortedMap0 = TransformedSortedMap.transformedSortedMap((SortedMap<Object, Object>) treeMap0, (Transformer<? super Object, ?>) transformer0, (Transformer<? super Object, ?>) transformer0);
        // Undeclared exception!
        try {
            transformedSortedMap0.tailMap(treeMap0);
            fail("Expecting exception: ClassCastException");
        } catch (ClassCastException e) {
            //
            // java.util.TreeMap cannot be cast to java.lang.Comparable
            //
            verifyException("java.util.TreeMap", e);
        }
    }
}
