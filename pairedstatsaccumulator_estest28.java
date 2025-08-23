package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class PairedStatsAccumulator_ESTestTest28 extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test27() throws Throwable {
        PairedStatsAccumulator pairedStatsAccumulator0 = new PairedStatsAccumulator();
        pairedStatsAccumulator0.add((-1672.7733723256124), (-1672.7733723256124));
        pairedStatsAccumulator0.add(316907.19778532686, (-1672.7733723256124));
        LinearTransformation linearTransformation0 = pairedStatsAccumulator0.leastSquaresFit();
        assertNotNull(linearTransformation0);
    }
}