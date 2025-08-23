package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest27 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add((-1672.7733723256124), (-1672.7733723256124));
        pairedStatsAccumulator0.add((-1672.7733723256124), (-1672.7733723256124));
        // Undeclared exception!
        try {
            pairedStatsAccumulator0.leastSquaresFit();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
