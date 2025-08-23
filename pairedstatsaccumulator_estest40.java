package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest40 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test39() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-1395L), 1375.1959817, (-1395L), 0.0, 0.03);
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, (-1395L));
        pairedStatsAccumulator0.addAll(pairedStats0);
        Stats stats1 = pairedStatsAccumulator0.xStats();
        assertEquals((-1395L), stats1.count());
    }
}
