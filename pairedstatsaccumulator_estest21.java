package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest21 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-1418L), (-1672.7733723256124), 0.03, (-1418L), 0.03);
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, (-1418L));
        pairedStatsAccumulator0.addAll(pairedStats0);
        long long0 = pairedStatsAccumulator0.count();
        assertEquals((-1418L), long0);
    }
}
