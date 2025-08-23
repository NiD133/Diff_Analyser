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

public class SimpleBloomFilter_ESTestTest49 extends SimpleBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test48() throws Throwable {
        Shape shape0 = Shape.fromKM(19, 19);
        SimpleBloomFilter simpleBloomFilter0 = new SimpleBloomFilter(shape0);
        EnhancedDoubleHasher enhancedDoubleHasher0 = new EnhancedDoubleHasher(1956L, (-2388L));
        boolean boolean0 = simpleBloomFilter0.merge((Hasher) enhancedDoubleHasher0);
        IndexFilter.ArrayTracker indexFilter_ArrayTracker0 = new IndexFilter.ArrayTracker(shape0);
        IntPredicate intPredicate0 = indexFilter_ArrayTracker0.negate();
        boolean boolean1 = simpleBloomFilter0.processIndices(intPredicate0);
        assertFalse(boolean1 == boolean0);
        assertFalse(boolean1);
    }
}
