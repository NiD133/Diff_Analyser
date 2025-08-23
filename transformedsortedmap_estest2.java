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

public class TransformedSortedMap_ESTestTest2 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        Integer integer0 = new Integer(3005);
        Factory<Integer> factory0 = ConstantFactory.constantFactory(integer0);
        FactoryTransformer<Object, Integer> factoryTransformer0 = new FactoryTransformer<Object, Integer>(factory0);
        TreeMap<Integer, Integer> treeMap0 = new TreeMap<Integer, Integer>();
        Class<Integer>[] classArray0 = (Class<Integer>[]) Array.newInstance(Class.class, 7);
        InvokerTransformer<Object, Integer> invokerTransformer0 = new InvokerTransformer<Object, Integer>("\"3Kz{N`TV^sYZ s\"p5Q", classArray0, classArray0);
        TransformedSortedMap<Integer, Integer> transformedSortedMap0 = TransformedSortedMap.transformedSortedMap((SortedMap<Integer, Integer>) treeMap0, (Transformer<? super Integer, ? extends Integer>) invokerTransformer0, (Transformer<? super Integer, ? extends Integer>) factoryTransformer0);
        SortedMap<Integer, Integer> sortedMap0 = transformedSortedMap0.headMap(integer0);
        assertTrue(sortedMap0.isEmpty());
    }
}
