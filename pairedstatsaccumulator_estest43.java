package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest43 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test42() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        Stats stats0 = pairedStatsAccumulator0.yStats();
        assertEquals(0.0, stats0.sum(), 0.01);
    }
}
