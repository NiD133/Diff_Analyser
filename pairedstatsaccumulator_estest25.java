package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest25 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test24() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        double[] doubleArray0 = new double[1];
        Stats stats0 = Stats.of(doubleArray0);
        Stats stats1 = pairedStatsAccumulator0.xStats();
        PairedStats pairedStats0 = new PairedStats(stats0, stats1, 5700.61466);
        pairedStatsAccumulator0.addAll(pairedStats0);
        // Undeclared exception!
        try {
            pairedStatsAccumulator0.add(7367227.571539006, 7367227.571539006);
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("com.google.common.base.Preconditions", e);
        }
    }
}
