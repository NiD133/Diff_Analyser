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

public class TransformedSortedMap_ESTestTest9 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        TreeMap<Closure<Object>, Object> treeMap0 = new TreeMap<Closure<Object>, Object>();
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 7);
        InvokerTransformer<Object, Closure<Object>> invokerTransformer0 = new InvokerTransformer<Object, Closure<Object>>("org.apache.commons.collections4.functors.ComparatorPredicate", classArray0, classArray0);
        TransformedSortedMap<Closure<Object>, Object> transformedSortedMap0 = new TransformedSortedMap<Closure<Object>, Object>(treeMap0, invokerTransformer0, invokerTransformer0);
        TransformedSortedMap<Closure<Object>, Object> transformedSortedMap1 = TransformedSortedMap.transformingSortedMap((SortedMap<Closure<Object>, Object>) transformedSortedMap0, (Transformer<? super Closure<Object>, ? extends Closure<Object>>) invokerTransformer0, (Transformer<? super Object, ?>) invokerTransformer0);
        SortedMap<Closure<Object>, Object> sortedMap0 = transformedSortedMap1.getSortedMap();
        assertEquals(0, sortedMap0.size());
    }
}
