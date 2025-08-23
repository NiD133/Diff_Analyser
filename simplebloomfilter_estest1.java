package org.apache.commons.collections4.bloomfilter;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

public class SimpleBloomFilter_ESTestTest1 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        Shape shape0 = Shape.fromKM(1, 1540);
        int[] intArray0 = new int[8];
        intArray0[0] = 5023;
        IndexExtractor indexExtractor0 = IndexExtractor.fromIndexArray(intArray0);
        LayerManager<SparseBloomFilter> layerManager0 = (LayerManager<SparseBloomFilter>) mock(LayerManager.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(layerManager0).processBloomFilters(any(java.util.function.Predicate.class));
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter0 = new LayeredBloomFilter<SparseBloomFilter>(shape0, layerManager0);
        SimpleBloomFilter simpleBloomFilter0 = layeredBloomFilter0.flatten();
        // Undeclared exception!
        try {
            simpleBloomFilter0.merge(indexExtractor0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //
            // IndexExtractor should only send values in the range[0,1540)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SimpleBloomFilter", e);
        }
    }
}
