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

public class TransformedSortedMap_ESTestTest3 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        TreeMap<Integer, Object> treeMap0 = new TreeMap<Integer, Object>();
        Integer integer0 = new Integer((-2921));
        ConstantTransformer<Integer, Integer> constantTransformer0 = new ConstantTransformer<Integer, Integer>(integer0);
        Class<Object>[] classArray0 = (Class<Object>[]) Array.newInstance(Class.class, 2);
        InvokerTransformer<Object, Integer> invokerTransformer0 = new InvokerTransformer<Object, Integer>((String) null, classArray0, (Object[]) null);
        TransformedSortedMap<Integer, Object> transformedSortedMap0 = TransformedSortedMap.transformedSortedMap((SortedMap<Integer, Object>) treeMap0, (Transformer<? super Integer, ? extends Integer>) constantTransformer0, (Transformer<? super Object, ?>) invokerTransformer0);
        SortedMap<Integer, Object> sortedMap0 = transformedSortedMap0.tailMap(integer0);
        assertTrue(sortedMap0.isEmpty());
    }
}
