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

public class TransformedSortedMap_ESTestTest5 extends TransformedSortedMap_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        TreeMap<Integer, Integer> treeMap0 = new TreeMap<Integer, Integer>();
        Integer integer0 = new Integer((-1));
        Integer integer1 = new Integer(3152);
        treeMap0.put(integer0, integer1);
        ConstantTransformer<Integer, Integer> constantTransformer0 = new ConstantTransformer<Integer, Integer>(integer0);
        TransformedSortedMap<Integer, Integer> transformedSortedMap0 = new TransformedSortedMap<Integer, Integer>(treeMap0, constantTransformer0, constantTransformer0);
        SortedMap<Integer, Integer> sortedMap0 = transformedSortedMap0.subMap(integer0, integer1);
        assertFalse(sortedMap0.isEmpty());
    }
}
