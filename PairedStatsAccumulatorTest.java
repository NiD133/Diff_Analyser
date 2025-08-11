/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.math;

import static com.google.common.math.StatsTesting.ALLOWED_ERROR;
import static com.google.common.math.StatsTesting.ALL_MANY_VALUES;
import static com.google.common.math.StatsTesting.EMPTY_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.MANY_VALUES_STATS_ITERABLE;
import static com.google.common.math.StatsTesting.MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_STATS;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE_STATS;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES_STATS;
import static com.google.common.math.StatsTesting.TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.assertDiagonalLinearTransformation;
import static com.google.common.math.StatsTesting.assertHorizontalLinearTransformation;
import static com.google.common.math.StatsTesting.assertLinearTransformationNaN;
import static com.google.common.math.StatsTesting.assertStatsApproxEqual;
import static com.google.common.math.StatsTesting.assertVerticalLinearTransformation;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

import com.google.common.math.StatsTesting.ManyValues;
import java.util.Collections;
import junit.framework.TestCase;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for {@link PairedStatsAccumulator}. This tests the stats methods for instances built with
 * {@link PairedStatsAccumulator#add}, and various error cases of that method. For tests of the
 * {@link PairedStatsAccumulator#snapshot} method which returns {@link PairedStats} instances, see
 * {@link PairedStatsTest}.
 *
 * @author Pete Gillin
 */
@NullUnmarked
public class PairedStatsAccumulatorTest extends TestCase {

  // Test data scenarios - each has both direct accumulation and addAll() variants
  private TestScenario emptyData;
  private TestScenario singleValue;
  private TestScenario twoValues;
  private TestScenario manyValues;
  private TestScenario horizontalLine; // Same Y values, varying X
  private TestScenario verticalLine;   // Same X values, varying Y  
  private TestScenario constantPoint;  // Same X and Y values

  /**
   * Holds both direct accumulation and addAll() variants of the same data scenario
   */
  private static class TestScenario {
    final PairedStatsAccumulator direct;
    final PairedStatsAccumulator viaAddAll;
    
    TestScenario(PairedStatsAccumulator direct, PairedStatsAccumulator viaAddAll) {
      this.direct = direct;
      this.viaAddAll = viaAddAll;
    }
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    emptyData = createEmptyScenario();
    singleValue = createSingleValueScenario();
    twoValues = createTwoValuesScenario();
    manyValues = createManyValuesScenario();
    horizontalLine = createHorizontalLineScenario();
    verticalLine = createVerticalLineScenario();
    constantPoint = createConstantPointScenario();
  }

  private TestScenario createEmptyScenario() {
    PairedStatsAccumulator empty = new PairedStatsAccumulator();
    
    PairedStatsAccumulator emptyViaAddAll = new PairedStatsAccumulator();
    emptyViaAddAll.addAll(empty.snapshot());
    
    return new TestScenario(empty, emptyViaAddAll);
  }

  private TestScenario createSingleValueScenario() {
    PairedStatsAccumulator single = new PairedStatsAccumulator();
    single.add(ONE_VALUE, OTHER_ONE_VALUE);

    PairedStatsAccumulator singleViaAddAll = new PairedStatsAccumulator();
    singleViaAddAll.add(ONE_VALUE, OTHER_ONE_VALUE);
    singleViaAddAll.addAll(new PairedStatsAccumulator().snapshot()); // Add empty stats
    
    return new TestScenario(single, singleViaAddAll);
  }

  private TestScenario createTwoValuesScenario() {
    PairedStatsAccumulator two = createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES);
    PairedStatsAccumulator twoViaAddAll = 
        createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1);
    
    return new TestScenario(two, twoViaAddAll);
  }

  private TestScenario createManyValuesScenario() {
    PairedStatsAccumulator many = createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES);
    PairedStatsAccumulator manyViaAddAll = 
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2);
    
    return new TestScenario(many, manyViaAddAll);
  }

  private TestScenario createHorizontalLineScenario() {
    // Varying X values, constant Y value
    Iterable<Double> constantYValues = Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE);
    
    PairedStatsAccumulator horizontal = createFilledPairedStatsAccumulator(MANY_VALUES, constantYValues);
    PairedStatsAccumulator horizontalViaAddAll = 
        createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, constantYValues, 2);
    
    return new TestScenario(horizontal, horizontalViaAddAll);
  }

  private TestScenario createVerticalLineScenario() {
    // Constant X value, varying Y values
    Iterable<Double> constantXValues = Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE);
    
    PairedStatsAccumulator vertical = createFilledPairedStatsAccumulator(constantXValues, OTHER_MANY_VALUES);
    PairedStatsAccumulator verticalViaAddAll = 
        createPartitionedFilledPairedStatsAccumulator(constantXValues, OTHER_MANY_VALUES, 2);
    
    return new TestScenario(vertical, verticalViaAddAll);
  }

  private TestScenario createConstantPointScenario() {
    // Same X and Y values repeated
    Iterable<Double> constantXValues = Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE);
    Iterable<Double> constantYValues = Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE);
    
    PairedStatsAccumulator constant = createFilledPairedStatsAccumulator(constantXValues, constantYValues);
    PairedStatsAccumulator constantViaAddAll = 
        createPartitionedFilledPairedStatsAccumulator(constantXValues, constantYValues, 2);
    
    return new TestScenario(constant, constantViaAddAll);
  }

  public void testCount() {
    assertCount(emptyData, 0);
    assertCount(singleValue, 1);
    assertCount(twoValues, 2);
    assertCount(manyValues, MANY_VALUES_COUNT);
  }

  private void assertCount(TestScenario scenario, long expectedCount) {
    assertThat(scenario.direct.count()).isEqualTo(expectedCount);
    assertThat(scenario.viaAddAll.count()).isEqualTo(expectedCount);
  }

  public void testCountOverflow_doesNotThrow() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    
    // Double the accumulator size repeatedly to cause overflow
    for (int power = 1; power < Long.SIZE - 1; power++) {
      accumulator.addAll(accumulator.snapshot());
    }
    
    // This should overflow without throwing
    accumulator.addAll(accumulator.snapshot());
    assertThat(accumulator.count()).isLessThan(0L);
  }

  public void testXStats() {
    assertXStats(emptyData, EMPTY_STATS_ITERABLE);
    assertXStats(singleValue, ONE_VALUE_STATS);
    assertXStats(twoValues, TWO_VALUES_STATS);
    assertXStats(manyValues, MANY_VALUES_STATS_ITERABLE);
  }

  private void assertXStats(TestScenario scenario, Stats expectedStats) {
    assertStatsApproxEqual(expectedStats, scenario.direct.xStats());
    assertStatsApproxEqual(expectedStats, scenario.viaAddAll.xStats());
  }

  public void testYStats() {
    assertYStats(emptyData, EMPTY_STATS_ITERABLE);
    assertYStats(singleValue, OTHER_ONE_VALUE_STATS);
    assertYStats(twoValues, OTHER_TWO_VALUES_STATS);
    assertYStats(manyValues, OTHER_MANY_VALUES_STATS);
  }

  private void assertYStats(TestScenario scenario, Stats expectedStats) {
    assertStatsApproxEqual(expectedStats, scenario.direct.yStats());
    assertStatsApproxEqual(expectedStats, scenario.viaAddAll.yStats());
  }

  public void testPopulationCovariance() {
    // Empty data should throw
    assertThrows(IllegalStateException.class, () -> emptyData.direct.populationCovariance());
    assertThrows(IllegalStateException.class, () -> emptyData.viaAddAll.populationCovariance());

    // Single value should return 0
    assertPopulationCovariance(singleValue, 0.0);

    // Two values
    double expectedTwoValues = TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / 2;
    assertPopulationCovariance(twoValues, expectedTwoValues);

    // Many values
    double expectedManyValues = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    assertPopulationCovariance(manyValues, expectedManyValues);

    // Test with various finite/non-finite combinations
    testPopulationCovarianceWithNonFiniteValues();

    // Special cases should have zero covariance
    assertPopulationCovariance(horizontalLine, 0.0);
    assertPopulationCovariance(verticalLine, 0.0);
    assertPopulationCovariance(constantPoint, 0.0);
  }

  private void assertPopulationCovariance(TestScenario scenario, double expected) {
    assertThat(scenario.direct.populationCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    assertThat(scenario.viaAddAll.populationCovariance()).isWithin(ALLOWED_ERROR).of(expected);
  }

  private void testPopulationCovarianceWithNonFiniteValues() {
    double expectedForFinite = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / MANY_VALUES_COUNT;
    
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator direct = createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator viaAddAll = createPartitionedFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES, 2);
      
      double directResult = direct.populationCovariance();
      double viaAddAllResult = viaAddAll.populationCovariance();
      
      if (values.hasAnyNonFinite()) {
        assertWithMessage("population covariance with non-finite values: " + values)
            .that(directResult).isNaN();
        assertWithMessage("population covariance via addAll with non-finite values: " + values)
            .that(viaAddAllResult).isNaN();
      } else {
        assertWithMessage("population covariance with finite values: " + values)
            .that(directResult).isWithin(ALLOWED_ERROR).of(expectedForFinite);
        assertWithMessage("population covariance via addAll with finite values: " + values)
            .that(viaAddAllResult).isWithin(ALLOWED_ERROR).of(expectedForFinite);
      }
    }
  }

  public void testSampleCovariance() {
    // Empty and single value should throw
    assertThrows(IllegalStateException.class, () -> emptyData.direct.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> emptyData.viaAddAll.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> singleValue.direct.sampleCovariance());
    assertThrows(IllegalStateException.class, () -> singleValue.viaAddAll.sampleCovariance());

    // Two values (denominator is n-1 = 1)
    assertSampleCovariance(twoValues, TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS);

    // Many values (denominator is n-1)
    double expectedManyValues = MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1);
    assertSampleCovariance(manyValues, expectedManyValues);

    // Special cases should have zero covariance
    assertSampleCovariance(horizontalLine, 0.0);
    assertSampleCovariance(verticalLine, 0.0);
    assertSampleCovariance(constantPoint, 0.0);
  }

  private void assertSampleCovariance(TestScenario scenario, double expected) {
    assertThat(scenario.direct.sampleCovariance()).isWithin(ALLOWED_ERROR).of(expected);
    assertThat(scenario.viaAddAll.sampleCovariance()).isWithin(ALLOWED_ERROR).of(expected);
  }

  public void testPearsonsCorrelationCoefficient() {
    // Empty and single value should throw
    assertThrows(IllegalStateException.class, () -> emptyData.direct.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> emptyData.viaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> singleValue.direct.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> singleValue.viaAddAll.pearsonsCorrelationCoefficient());

    // Test correlation coefficient calculation for valid cases
    assertCorrelationCoefficient(twoValues);
    assertCorrelationCoefficient(manyValues);

    // Test with non-finite values
    testCorrelationCoefficientWithNonFiniteValues();

    // Cases with zero variance should throw (no correlation possible)
    assertThrows(IllegalStateException.class, () -> horizontalLine.direct.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> horizontalLine.viaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> verticalLine.direct.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> verticalLine.viaAddAll.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> constantPoint.direct.pearsonsCorrelationCoefficient());
    assertThrows(IllegalStateException.class, () -> constantPoint.viaAddAll.pearsonsCorrelationCoefficient());
  }

  private void assertCorrelationCoefficient(TestScenario scenario) {
    double expectedDirect = calculateExpectedCorrelation(scenario.direct);
    double expectedViaAddAll = calculateExpectedCorrelation(scenario.viaAddAll);
    
    assertThat(scenario.direct.pearsonsCorrelationCoefficient()).isWithin(ALLOWED_ERROR).of(expectedDirect);
    assertThat(scenario.viaAddAll.pearsonsCorrelationCoefficient()).isWithin(ALLOWED_ERROR).of(expectedViaAddAll);
  }

  private double calculateExpectedCorrelation(PairedStatsAccumulator accumulator) {
    return accumulator.populationCovariance() / 
           (accumulator.xStats().populationStandardDeviation() * 
            accumulator.yStats().populationStandardDeviation());
  }

  private void testCorrelationCoefficientWithNonFiniteValues() {
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator direct = createFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable());
      PairedStatsAccumulator viaAddAll = createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, values.asIterable(), 2);
      
      double directResult = direct.pearsonsCorrelationCoefficient();
      double viaAddAllResult = viaAddAll.pearsonsCorrelationCoefficient();
      
      if (values.hasAnyNonFinite()) {
        assertWithMessage("correlation coefficient with non-finite values: " + values)
            .that(directResult).isNaN();
        assertWithMessage("correlation coefficient via addAll with non-finite values: " + values)
            .that(viaAddAllResult).isNaN();
      } else {
        double expectedDirect = calculateExpectedCorrelation(direct);
        double expectedViaAddAll = calculateExpectedCorrelation(viaAddAll);
        
        assertWithMessage("correlation coefficient with finite values: " + values)
            .that(directResult).isWithin(ALLOWED_ERROR).of(expectedDirect);
        assertWithMessage("correlation coefficient via addAll with finite values: " + values)
            .that(viaAddAllResult).isWithin(ALLOWED_ERROR).of(expectedViaAddAll);
      }
    }
  }

  public void testLeastSquaresFit() {
    // Empty and single value should throw
    assertThrows(IllegalStateException.class, () -> emptyData.direct.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> emptyData.viaAddAll.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> singleValue.direct.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> singleValue.viaAddAll.leastSquaresFit());

    // Test diagonal transformations (normal case)
    assertDiagonalFit(twoValues);
    assertDiagonalFit(manyValues);

    // Test with non-finite values
    testLeastSquaresFitWithNonFiniteValues();

    // Special cases
    assertHorizontalLinearTransformation(horizontalLine.direct.leastSquaresFit(), horizontalLine.direct.yStats().mean());
    assertHorizontalLinearTransformation(horizontalLine.viaAddAll.leastSquaresFit(), horizontalLine.viaAddAll.yStats().mean());
    
    assertVerticalLinearTransformation(verticalLine.direct.leastSquaresFit(), verticalLine.direct.xStats().mean());
    assertVerticalLinearTransformation(verticalLine.viaAddAll.leastSquaresFit(), verticalLine.viaAddAll.xStats().mean());
    
    // Constant values should throw (no variance in either dimension)
    assertThrows(IllegalStateException.class, () -> constantPoint.direct.leastSquaresFit());
    assertThrows(IllegalStateException.class, () -> constantPoint.viaAddAll.leastSquaresFit());
  }

  private void assertDiagonalFit(TestScenario scenario) {
    assertDiagonalLinearTransformation(
        scenario.direct.leastSquaresFit(),
        scenario.direct.xStats().mean(),
        scenario.direct.yStats().mean(),
        scenario.direct.xStats().populationVariance(),
        scenario.direct.populationCovariance());
        
    assertDiagonalLinearTransformation(
        scenario.viaAddAll.leastSquaresFit(),
        scenario.viaAddAll.xStats().mean(),
        scenario.viaAddAll.yStats().mean(),
        scenario.viaAddAll.xStats().populationVariance(),
        scenario.viaAddAll.populationCovariance());
  }

  private void testLeastSquaresFitWithNonFiniteValues() {
    for (ManyValues values : ALL_MANY_VALUES) {
      PairedStatsAccumulator direct = createFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES);
      PairedStatsAccumulator viaAddAll = createPartitionedFilledPairedStatsAccumulator(values.asIterable(), OTHER_MANY_VALUES, 2);
      
      LinearTransformation directFit = direct.leastSquaresFit();
      LinearTransformation viaAddAllFit = viaAddAll.leastSquaresFit();
      
      if (values.hasAnyNonFinite()) {
        assertLinearTransformationNaN(directFit);
        assertLinearTransformationNaN(viaAddAllFit);
      } else {
        assertDiagonalLinearTransformation(
            directFit,
            direct.xStats().mean(),
            direct.yStats().mean(),
            direct.xStats().populationVariance(),
            direct.populationCovariance());
            
        assertDiagonalLinearTransformation(
            viaAddAllFit,
            viaAddAll.xStats().mean(),
            viaAddAll.yStats().mean(),
            viaAddAll.xStats().populationVariance(),
            viaAddAll.populationCovariance());
      }
    }
  }
}