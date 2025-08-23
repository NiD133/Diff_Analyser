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

public class SimpleBloomFilter_ESTestTest39 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        Shape shape0 = Shape.fromNM(294, 294);
        LayerManager<SparseBloomFilter> layerManager0 = (LayerManager<SparseBloomFilter>) mock(LayerManager.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(layerManager0).processBloomFilters(any(java.util.function.Predicate.class));
        LayeredBloomFilter<SparseBloomFilter> layeredBloomFilter0 = new LayeredBloomFilter<SparseBloomFilter>(shape0, layerManager0);
        SimpleBloomFilter simpleBloomFilter0 = layeredBloomFilter0.flatten();
        LongBiPredicate longBiPredicate0 = mock(LongBiPredicate.class, new ViolatedAssumptionAnswer());
        doReturn(false).when(longBiPredicate0).test(anyLong(), anyLong());
        boolean boolean0 = simpleBloomFilter0.processBitMapPairs(simpleBloomFilter0, longBiPredicate0);
        assertFalse(boolean0);
    }
}
