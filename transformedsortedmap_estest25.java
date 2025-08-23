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

public class TransformedSortedMap_ESTestTest25 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        TreeMap<Object, Object> treeMap0 = new TreeMap<Object, Object>();
        Transformer<Object, Object> transformer0 = MapTransformer.mapTransformer((Map<? super Object, ?>) treeMap0);
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(1, 1).when(comparator0).compare(any(), any());
        TreeMap<Object, Integer> treeMap1 = new TreeMap<Object, Integer>(comparator0);
        Integer integer0 = new Integer(61);
        ConstantTransformer<Integer, Integer> constantTransformer0 = new ConstantTransformer<Integer, Integer>(integer0);
        treeMap1.put(integer0, integer0);
        TransformedSortedMap<Object, Integer> transformedSortedMap0 = TransformedSortedMap.transformedSortedMap((SortedMap<Object, Integer>) treeMap1, (Transformer<? super Object, ?>) transformer0, (Transformer<? super Integer, ? extends Integer>) constantTransformer0);
        Object object0 = transformedSortedMap0.firstKey();
        assertNull(object0);
    }
}
