package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest34 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        pairedStatsAccumulator0.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double double0 = pairedStatsAccumulator0.pearsonsCorrelationCoefficient();
        assertEquals(Double.NaN, double0, 0.01);
    }
}
