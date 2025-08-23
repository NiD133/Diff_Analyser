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

public class SparseBloomFilter_ESTestTest26 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        Shape shape0 = Shape.fromNM(3, 3);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        BitMapExtractor bitMapExtractor0 = BitMapExtractor.fromIndexExtractor(sparseBloomFilter0, Integer.MAX_VALUE);
        // Undeclared exception!
        sparseBloomFilter0.contains(bitMapExtractor0);
    }
}
