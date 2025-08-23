package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest7 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add(863464.0975884801, 38.358751);
        pairedStatsAccumulator0.add((-2001.999), (-2001.999));
        PairedStats pairedStats0 = pairedStatsAccumulator0.snapshot();
        pairedStatsAccumulator0.add(1.0, 779.3943325);
        pairedStatsAccumulator0.addAll(pairedStats0);
        assertEquals(2L, pairedStats0.count());
    }
}
