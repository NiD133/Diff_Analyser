package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest15 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test14() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(1.9393330267238755E12, 1.9393330267238755E12);
        pairedStatsAccumulator0.add(1.9393330267238755E12, 1.9393330267238755E12);
        double double0 = pairedStatsAccumulator0.sampleCovariance();
        assertEquals(0.0, double0, 0.01);
    }
}
