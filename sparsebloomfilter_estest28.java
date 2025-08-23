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

public class SparseBloomFilter_ESTestTest28 extends SparseBloomFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        SparseBloomFilter sparseBloomFilter0 = null;
        try {
            sparseBloomFilter0 = new SparseBloomFilter((Shape) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // shape
            //
            verifyException("java.util.Objects", e);
        }
    }
}
