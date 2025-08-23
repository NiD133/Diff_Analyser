package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest38 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(863464.0975884801, 38.358751);
        double double0 = pairedStatsAccumulator0.populationCovariance();
        assertEquals(0.0, double0, 0.01);
    }
}
