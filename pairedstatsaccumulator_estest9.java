package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest9 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = new Stats((-2859L), 1736.0, (-205.958587102), (-205.958587102), Double.NaN);
        PairedStats pairedStats0 = new PairedStats(stats0, stats0, 69.34955793);
        pairedStatsAccumulator0.addAll(pairedStats0);
        pairedStatsAccumulator0.add(0.0, (-1.0));
    }
}
