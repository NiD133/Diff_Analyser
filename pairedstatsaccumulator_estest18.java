package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest18 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(1896976.6974008642, 969.9234729266376);
        pairedStatsAccumulator0.add(2.0994296721195953E179, 0.0);
        double double0 = pairedStatsAccumulator0.pearsonsCorrelationCoefficient();
        assertEquals(-0.0, double0, 0.01);
    }
}