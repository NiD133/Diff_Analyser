package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest4 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add((-1.0), (-1.0));
        pairedStatsAccumulator0.add((-1313.1264), 1.0);
        pairedStatsAccumulator0.add((-1.0), (-1313.1264));
        double double0 = pairedStatsAccumulator0.sampleCovariance();
        assertEquals((-287820.6991961601), double0, 0.01);
    }
}
