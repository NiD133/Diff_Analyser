package com.google.common.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class PairedStatsAccumulatorTest {

  @Test
  public void initialState_hasZeroCountAndZeroSums() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    assertEquals(0L, acc.count());
    assertEquals(0.0, acc.xStats().sum(), 0.0);
    assertEquals(0.0, acc.yStats().sum(), 0.0);
  }

  @Test
  public void add_updatesCountAndStats() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 2.0);
    acc.add(3.0, 5.0);

    assertEquals(2L, acc.count());
    assertEquals(4.0, acc.xStats().sum(), 0.0);
    assertEquals(7.0, acc.yStats().sum(), 0.0);
  }

  @Test(expected = IllegalStateException.class)
  public void populationCovariance_empty_throws() {
    new PairedStatsAccumulator().populationCovariance();
  }

  @Test
  public void populationCovariance_singlePair_isZero() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(10.0, -4.0);

    assertEquals(0.0, acc.populationCovariance(), 0.0);
  }

  @Test
  public void covariance_twoPoints_matchesExpected() {
    // Points (0,0) and (2,4)
    // population covariance = 2, sample covariance = 4
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(0.0, 0.0);
    acc.add(2.0, 4.0);

    assertEquals(2.0, acc.populationCovariance(), 1e-12);
    assertEquals(4.0, acc.sampleCovariance(), 1e-12);
  }

  @Test(expected = IllegalStateException.class)
  public void sampleCovariance_empty_throws() {
    new PairedStatsAccumulator().sampleCovariance();
  }

  @Test(expected = IllegalStateException.class)
  public void sampleCovariance_singlePair_throws() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 2.0);

    acc.sampleCovariance();
  }

  @Test
  public void pearson_perfectPositiveCorrelation_isOne() {
    // (0,0), (2,4) lie on y = 2x
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(0.0, 0.0);
    acc.add(2.0, 4.0);

    assertEquals(1.0, acc.pearsonsCorrelationCoefficient(), 1e-12);
  }

  @Test
  public void pearson_perfectNegativeCorrelation_isMinusOne() {
    // (0,4), (2,0) lie on y = -2x + 4
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(0.0, 4.0);
    acc.add(2.0, 0.0);

    assertEquals(-1.0, acc.pearsonsCorrelationCoefficient(), 1e-12);
  }

  @Test(expected = IllegalStateException.class)
  public void pearson_zeroVarianceInX_throws() {
    // x is constant
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 1.0);
    acc.add(1.0, 2.0);

    acc.pearsonsCorrelationCoefficient();
  }

  @Test
  public void pearson_withNonFinite_returnsNaN() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 1.0);
    acc.add(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);

    assertTrue(Double.isNaN(acc.pearsonsCorrelationCoefficient()));
  }

  @Test
  public void leastSquaresFit_simpleLine_matchesTransform() {
    // Points (0,1) and (2,5) lie on y = 2x + 1
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(0.0, 1.0);
    acc.add(2.0, 5.0);

    LinearTransformation fit = acc.leastSquaresFit();

    assertNotNull(fit);
    assertEquals(7.0, fit.transform(3.0), 1e-12); // y = 2*3 + 1
    assertEquals(1.0, fit.transform(0.0), 1e-12);
  }

  @Test
  public void leastSquaresFit_horizontalWhenYConstant() {
    // y is constant = 10, x varies
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(0.0, 10.0);
    acc.add(5.0, 10.0);
    acc.add(10.0, 10.0);

    LinearTransformation fit = acc.leastSquaresFit();

    assertNotNull(fit);
    assertEquals(10.0, fit.transform(-100.0), 1e-12);
    assertEquals(10.0, fit.transform(0.0), 1e-12);
    assertEquals(10.0, fit.transform(100.0), 1e-12);
  }

  @Test(expected = IllegalStateException.class)
  public void leastSquaresFit_empty_throws() {
    new PairedStatsAccumulator().leastSquaresFit();
  }

  @Test(expected = IllegalStateException.class)
  public void leastSquaresFit_singlePair_throws() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 2.0);

    acc.leastSquaresFit();
  }

  @Test(expected = IllegalStateException.class)
  public void leastSquaresFit_zeroVarianceInBoth_throws() {
    // Multiple identical points -> zero variance in both x and y
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(3.14, 2.71);
    acc.add(3.14, 2.71);

    acc.leastSquaresFit();
  }

  @Test
  public void addAll_mergesFromAnotherAccumulator() {
    PairedStatsAccumulator source = new PairedStatsAccumulator();
    source.add(1.0, 10.0);
    source.add(2.0, 20.0);

    PairedStats snapshot = source.snapshot();

    PairedStatsAccumulator target = new PairedStatsAccumulator();
    target.add(3.0, 30.0);

    target.addAll(snapshot);

    assertEquals(3L, target.count());
    assertEquals(1.0 + 2.0 + 3.0, target.xStats().sum(), 1e-12);
    assertEquals(10.0 + 20.0 + 30.0, target.yStats().sum(), 1e-12);
  }

  @Test(expected = NullPointerException.class)
  public void addAll_null_throws() {
    new PairedStatsAccumulator().addAll(null);
  }

  @Test
  public void snapshot_isImmutableViewOfCurrentStats() {
    PairedStatsAccumulator acc = new PairedStatsAccumulator();

    acc.add(1.0, 1.0);
    PairedStats snap1 = acc.snapshot();

    acc.add(2.0, 2.0);
    PairedStats snap2 = acc.snapshot();

    assertEquals(1L, snap1.count());
    assertEquals(2L, snap2.count());
  }
}