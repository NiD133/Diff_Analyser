package com.google.common.math;

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.math.LinearTransformation;
import com.google.common.math.PairedStats;
import com.google.common.math.PairedStatsAccumulator;
import com.google.common.math.Stats;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Test suite for PairedStatsAccumulator functionality.
 * Tests cover basic operations, statistical calculations, and edge cases.
 */
public class PairedStatsAccumulatorTest {

    // ========== Basic Operations Tests ==========
    
    @Test
    public void testEmptyAccumulator_HasZeroCount() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        assertEquals(0L, accumulator.count());
    }
    
    @Test
    public void testAddSinglePair_CountIsOne() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        
        assertEquals(1L, accumulator.count());
    }
    
    @Test
    public void testAddAllFromPairedStats_CountMatches() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        Stats stats = new Stats(-1418L, -1672.7733723256124, 0.03, -1418L, 0.03);
        PairedStats pairedStats = new PairedStats(stats, stats, -1418L);
        
        accumulator.addAll(pairedStats);
        
        assertEquals(-1418L, accumulator.count());
    }

    // ========== Statistics Access Tests ==========
    
    @Test
    public void testXStats_ReturnsCorrectSum() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        Stats xStats = accumulator.xStats();
        
        assertEquals(863464.0975884801, xStats.sum(), 0.01);
    }
    
    @Test
    public void testYStats_ReturnsCorrectSum() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(4.9E-324, 1.0);
        Stats yStats = accumulator.yStats();
        
        assertEquals(1.0, yStats.sum(), 0.01);
    }
    
    @Test
    public void testYStats_WithNegativeValue() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1.0, -1.0);
        Stats yStats = accumulator.yStats();
        
        assertEquals(-1.0, yStats.sum(), 0.01);
    }

    // ========== Covariance Tests ==========
    
    @Test
    public void testPopulationCovariance_SinglePoint_ReturnsZero() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        double covariance = accumulator.populationCovariance();
        
        assertEquals(0.0, covariance, 0.01);
    }
    
    @Test
    public void testPopulationCovariance_TwoPoints_CalculatesCorrectly() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        accumulator.add(-2001.999, -2001.999);
        double covariance = accumulator.populationCovariance();
        
        assertEquals(4.4146511460050505E8, covariance, 0.01);
    }
    
    @Test
    public void testPopulationCovariance_NegativeCorrelation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(4.9E-324, 1.0);
        accumulator.add(1.0, 4.9E-324);
        double covariance = accumulator.populationCovariance();
        
        assertEquals(-0.25, covariance, 0.01);
    }
    
    @Test
    public void testSampleCovariance_ThreePoints_CalculatesCorrectly() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1.0, -1.0);
        accumulator.add(-1313.1264, 1.0);
        accumulator.add(-1.0, -1313.1264);
        double sampleCovariance = accumulator.sampleCovariance();
        
        assertEquals(-287820.6991961601, sampleCovariance, 0.01);
    }
    
    @Test
    public void testSampleCovariance_IdenticalPoints_ReturnsZero() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(1.9393330267238755E12, 1.9393330267238755E12);
        accumulator.add(1.9393330267238755E12, 1.9393330267238755E12);
        double sampleCovariance = accumulator.sampleCovariance();
        
        assertEquals(0.0, sampleCovariance, 0.01);
    }

    // ========== Correlation Tests ==========
    
    @Test
    public void testPearsonsCorrelation_PerfectPositiveCorrelation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        accumulator.add(-2001.999, -2001.999);
        double correlation = accumulator.pearsonsCorrelationCoefficient();
        
        assertEquals(1.0, correlation, 0.01);
    }
    
    @Test
    public void testPearsonsCorrelation_PerfectNegativeCorrelation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1.0, -1.0);
        accumulator.add(-1313.1264, 1.0);
        double correlation = accumulator.pearsonsCorrelationCoefficient();
        
        assertEquals(-1.0, correlation, 0.01);
    }
    
    @Test
    public void testPearsonsCorrelation_NoCorrelation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(1896976.6974008642, 969.9234729266376);
        accumulator.add(2.0994296721195953E179, 0.0);
        double correlation = accumulator.pearsonsCorrelationCoefficient();
        
        assertEquals(-0.0, correlation, 0.01);
    }
    
    @Test
    public void testPearsonsCorrelation_InfiniteValues_ReturnsNaN() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        accumulator.add(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double correlation = accumulator.pearsonsCorrelationCoefficient();
        
        assertEquals(Double.NaN, correlation, 0.01);
    }

    // ========== Linear Regression Tests ==========
    
    @Test
    public void testLeastSquaresFit_TwoDifferentPoints_ReturnsTransformation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.add(316907.19778532686, -1672.7733723256124);
        LinearTransformation fit = accumulator.leastSquaresFit();
        
        assertNotNull(fit);
    }
    
    @Test
    public void testLeastSquaresFit_WithNaNValue_ReturnsTransformation() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(1018.9124, 1.0);
        accumulator.add(1.0, Double.NaN);
        LinearTransformation fit = accumulator.leastSquaresFit();
        
        assertNotNull(fit);
    }

    // ========== Exception Tests ==========
    
    @Test(expected = IllegalStateException.class)
    public void testLeastSquaresFit_EmptyAccumulator_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.leastSquaresFit();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testLeastSquaresFit_SinglePoint_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(0.0, 0.0);
        accumulator.leastSquaresFit();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testLeastSquaresFit_IdenticalPoints_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.leastSquaresFit();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testLeastSquaresFit_InfiniteValues_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(1896976.2003985401, 1896976.2003985401);
        accumulator.add(2.0994296721195953E179, 2.0994296721195953E179);
        accumulator.leastSquaresFit();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPearsonsCorrelation_EmptyAccumulator_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.pearsonsCorrelationCoefficient();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPearsonsCorrelation_SinglePoint_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(0.0, -2551.7129547187);
        accumulator.pearsonsCorrelationCoefficient();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPearsonsCorrelation_IdenticalPoints_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.add(-1672.7733723256124, -1672.7733723256124);
        accumulator.pearsonsCorrelationCoefficient();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSampleCovariance_EmptyAccumulator_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.sampleCovariance();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSampleCovariance_SinglePoint_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(0.0, 0.0);
        accumulator.sampleCovariance();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopulationCovariance_EmptyAccumulator_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.populationCovariance();
    }
    
    @Test(expected = NullPointerException.class)
    public void testAddAll_NullPairedStats_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.addAll(null);
    }

    // ========== Snapshot and Integration Tests ==========
    
    @Test
    public void testSnapshot_EmptyAccumulator_ReturnsEmptyStats() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        PairedStats snapshot = accumulator.snapshot();
        accumulator.addAll(snapshot);
        
        assertEquals(0L, snapshot.count());
    }
    
    @Test
    public void testSnapshot_WithData_PreservesEquality() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        Stats stats = new Stats(-1418L, -1672.7733723256124, 0.03, -1418L, 0.03);
        PairedStats originalStats = new PairedStats(stats, stats, -1418L);
        
        accumulator.addAll(originalStats);
        PairedStats snapshot = accumulator.snapshot();
        
        assertTrue(snapshot.equals(originalStats));
    }
    
    @Test
    public void testAddAll_ThenAdd_MaintainsConsistency() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        accumulator.add(863464.0975884801, 38.358751);
        accumulator.add(-2001.999, -2001.999);
        PairedStats snapshot = accumulator.snapshot();
        accumulator.add(1.0, 779.3943325);
        accumulator.addAll(snapshot);
        
        assertEquals(2L, snapshot.count());
    }

    // ========== Edge Case Tests ==========
    
    @Test(expected = IllegalStateException.class)
    public void testAddAll_IncompatibleCounts_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        accumulator.add(154.3044396684968, 154.3044396684968);
        
        // Create PairedStats with mismatched x and y counts
        Stats xStats = new Stats(-9223372036854775808L, -9223372036854775808L, 
                                -9223372036854775808L, -716.5114168133774, 1586.65153);
        ArrayDeque<Integer> emptyDeque = new ArrayDeque<>();
        Stats yStats = Stats.of(emptyDeque.descendingIterator());
        PairedStats incompatibleStats = new PairedStats(xStats, yStats, -716.5114168133774);
        
        accumulator.addAll(incompatibleStats);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAdd_AfterAddingEmptyStats_ThrowsException() {
        PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
        
        // Add empty stats first
        double[] singleValue = new double[1];
        Stats xStats = Stats.of(singleValue);
        Stats yStats = accumulator.xStats(); // Empty stats
        PairedStats emptyStats = new PairedStats(xStats, yStats, 5700.61466);
        accumulator.addAll(emptyStats);
        
        // Then try to add individual values
        accumulator.add(7367227.571539006, 7367227.571539006);
    }
}