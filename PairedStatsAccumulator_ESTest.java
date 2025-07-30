package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.common.math.LinearTransformation;
import com.google.common.math.PairedStats;
import com.google.common.math.PairedStatsAccumulator;
import com.google.common.math.Stats;
import java.util.ArrayDeque;
import java.util.Iterator;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class PairedStatsAccumulator_ESTest extends PairedStatsAccumulator_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testLeastSquaresFitWithZeroData() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(0.0, 0.0);

        try {
            accumulator.leastSquaresFit();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testPearsonsCorrelationCoefficientWithIdenticalData() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.add(-1672.7733723256124, -1672.7733723256124);

        try {
            accumulator.pearsonsCorrelationCoefficient();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("com.google.common.base.Preconditions", e);
        }
    }

    @Test(timeout = 4000)
    public void testSampleCovarianceWithMultipleDataPoints() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(-1.0, -1.0);
        accumulator.add(-1313.1264, 1.0);
        accumulator.add(-1.0, -1313.1264);
        double covariance = accumulator.sampleCovariance();
        assertEquals(-287820.6991961601, covariance, 0.01);
    }

    @Test(timeout = 4000)
    public void testPopulationCovarianceWithAddedStats() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        Stats stats = new Stats(-8253L, -8253L, -8253L, 2242.352921728372, -8253L);
        PairedStats pairedStats = new PairedStats(stats, stats, -8253L);
        accumulator.addAll(pairedStats);
        double covariance = accumulator.populationCovariance();
        assertEquals(1.0, covariance, 0.01);
    }

    @Test(timeout = 4000)
    public void testSnapshotAndAddAll() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(863464.0975884801, 38.358751);
        accumulator.add(-2001.999, -2001.999);
        PairedStats snapshot = accumulator.snapshot();
        accumulator.add(1.0, 779.3943325);
        accumulator.addAll(snapshot);
        assertEquals(2L, snapshot.count());
    }

    @Test(timeout = 4000)
    public void testYStatsSum() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(4.9E-324, 1.0);
        Stats yStats = accumulator.yStats();
        assertEquals(1.0, yStats.sum(), 0.01);
    }

    @Test(timeout = 4000)
    public void testXStatsSum() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(863464.0975884801, 38.358751);
        Stats xStats = accumulator.xStats();
        assertEquals(863464.0975884801, xStats.sum(), 0.01);
    }

    @Test(timeout = 4000)
    public void testLeastSquaresFitWithValidData() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.add(316907.19778532686, -1672.7733723256124);
        LinearTransformation transformation = accumulator.leastSquaresFit();
        assertNotNull(transformation);
    }

    @Test(timeout = 4000)
    public void testCountWithNoData() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        long count = accumulator.count();
        assertEquals(0L, count);
    }

    // Additional tests can be added here following the same pattern.
}