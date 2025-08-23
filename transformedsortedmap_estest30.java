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

public class TransformedSortedMap_ESTestTest30 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test29() throws Throwable {
        String string0 = "i]oNU\\u7I%x";
        Transformer<Object, Integer> transformer0 = InvokerTransformer.invokerTransformer(string0);
        Comparator<Integer> comparator0 = (Comparator<Integer>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn((String) null).when(comparator0).toString();
        TreeMap<Integer, Integer> treeMap0 = new TreeMap<Integer, Integer>(comparator0);
        TransformedSortedMap<Integer, Integer> transformedSortedMap0 = TransformedSortedMap.transformedSortedMap((SortedMap<Integer, Integer>) treeMap0, (Transformer<? super Integer, ? extends Integer>) transformer0, (Transformer<? super Integer, ? extends Integer>) transformer0);
        Comparator<? super Integer> comparator1 = transformedSortedMap0.comparator();
        assertNotNull(comparator1);
    }
}
