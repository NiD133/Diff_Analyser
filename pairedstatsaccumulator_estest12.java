package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest12 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-481L), 1660997.3216492385, 1660997.3216492385, 1660997.3216492385, (-481L));
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, (-116.2946));
        pairedStatsAccumulator0.addAll(pairedStats0);
        Stats stats1 = pairedStatsAccumulator0.yStats();
        assertEquals((-7.989397117132837E8), stats1.sum(), 0.01);
    }
}
