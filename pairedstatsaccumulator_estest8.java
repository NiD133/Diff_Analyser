package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest8 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-2955L), (-2955L), 0.03, 1.0, 0.03);
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, (-648.72643));
        pairedStatsAccumulator0.addAll(pairedStats0);
        pairedStatsAccumulator0.addAll(pairedStats0);
        assertEquals((-2955L), pairedStats0.count());
    }
}
