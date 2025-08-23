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

public class SparseBloomFilter_ESTestTest24 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        Shape shape0 = Shape.fromKM(2147483639, 2147483639);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        IndexExtractor indexExtractor0 = IndexExtractor.fromBitMapExtractor(sparseBloomFilter0);
        // Undeclared exception!
        sparseBloomFilter0.contains(indexExtractor0);
    }
}
