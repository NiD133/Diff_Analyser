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

public class SparseBloomFilter_ESTestTest25 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        Shape shape0 = Shape.fromNM(1014, 1014);
        SparseBloomFilter sparseBloomFilter0 = new SparseBloomFilter(shape0);
        // Undeclared exception!
        try {
            sparseBloomFilter0.contains((IndexExtractor) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.apache.commons.collections4.bloomfilter.SparseBloomFilter", e);
        }
    }
}
