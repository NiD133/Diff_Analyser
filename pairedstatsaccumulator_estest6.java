package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest6 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-8253L), (-8253L), (-8253L), 2242.352921728372, (-8253L));
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, (-8253L));
        pairedStatsAccumulator0.addAll(pairedStats0);
        double double0 = pairedStatsAccumulator0.populationCovariance();
        assertEquals(1.0, double0, 0.01);
    }
}
