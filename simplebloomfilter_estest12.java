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

public class SimpleBloomFilter_ESTestTest12 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        Shape shape0 = Shape.fromNMK(64, 64, 1);
        LayerManager<SparseBloomFilter> layerManager0 = (LayerManager<SparseBloomFilter>) mock(LayerManager.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(layerManager0).processBloomFilters(any(java.util.function.Predicate.class));
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter0 = new LayeredBloomFilter<SparseBloomFilter>(shape0, layerManager0);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        boolean boolean0 = simpleBloomFilter0.contains((IndexExtractor) layeredBloomFilter0);
        assertFalse(boolean0);
    }
}