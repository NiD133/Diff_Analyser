package com.google.common.math;

import static com.google.common.math.StatsTesting.ALLOWED_ERROR;
import static com.google.common.math.StatsTesting.MANY_VALUES;
import static com.google.common.math.StatsTesting.MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES;
import static com.google.common.math.StatsTesting.OTHER_MANY_VALUES_COUNT;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES;
import static com.google.common.math.StatsTesting.TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS;
import static com.google.common.math.StatsTesting.createFilledPairedStatsAccumulator;
import static com.google.common.math.StatsTesting.createPartitionedFilledPairedStatsAccumulator;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.common.base.Supplier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for {@link PairedStatsAccumulator#sampleCovariance()}.
 *
 * <p>This test is parameterized to run all calculation scenarios against two functionally
 * equivalent accumulators: one built with direct {@code add} calls, and one built by partitioning
 * the data and using {@code addAll}. This verifies that both data input methods produce the same
 * statistical results.
 */
@RunWith(Parameterized.class)
public class PairedStatsAccumulatorSampleCovarianceTest {

  // A description of the test case, used for test runner output.
  private final String caseDescription;

  // A supplier to create the PairedStatsAccumulator under test.
  // Using a supplier defers object creation until the test method runs.
  private final Supplier<PairedStatsAccumulator> accumulatorSupplier;

  // The expected result of the sampleCovariance() calculation.
  private final double expectedSampleCovariance;

  public PairedStatsAccumulatorSampleCovarianceTest(
      String caseDescription,
      Supplier<PairedStatsAccumulator> accumulatorSupplier,
      double expectedSampleCovariance) {
    this.caseDescription = caseDescription;
    this.accumulatorSupplier = accumulatorSupplier;
    this.expectedSampleCovariance = expectedSampleCovariance;
  }

  @Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        new Object[][] {
          {
            "two values",
            (Supplier<PairedStatsAccumulator>)
                () -> createFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES),
            TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS
          },
          {
            "two values, via addAll",
            (Supplier<PairedStatsAccumulator>)
                () -> createPartitionedFilledPairedStatsAccumulator(TWO_VALUES, OTHER_TWO_VALUES, 1),
            TWO_VALUES_SUM_OF_PRODUCTS_OF_DELTAS
          },
          {
            "many values",
            (Supplier<PairedStatsAccumulator>)
                () -> createFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES),
            MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1)
          },
          {
            "many values, via addAll",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createPartitionedFilledPairedStatsAccumulator(MANY_VALUES, OTHER_MANY_VALUES, 2),
            MANY_VALUES_SUM_OF_PRODUCTS_OF_DELTAS / (MANY_VALUES_COUNT - 1)
          },
          {
            "horizontal data (y is constant)",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE)),
            0.0
          },
          {
            "horizontal data, via addAll",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createPartitionedFilledPairedStatsAccumulator(
                        MANY_VALUES, Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE), 2),
            0.0
          },
          {
            "vertical data (x is constant)",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE), OTHER_MANY_VALUES),
            0.0
          },
          {
            "vertical data, via addAll",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(OTHER_MANY_VALUES_COUNT, ONE_VALUE),
                        OTHER_MANY_VALUES,
                        2),
            0.0
          },
          {
            "constant data (x and y are constant)",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE)),
            0.0
          },
          {
            "constant data, via addAll",
            (Supplier<PairedStatsAccumulator>)
                () ->
                    createPartitionedFilledPairedStatsAccumulator(
                        Collections.nCopies(MANY_VALUES_COUNT, ONE_VALUE),
                        Collections.nCopies(MANY_VALUES_COUNT, OTHER_ONE_VALUE),
                        2),
            0.0
          }
        });
  }

  @Test
  public void sampleCovariance_withSufficientData_isCorrect() {
    PairedStatsAccumulator accumulator = accumulatorSupplier.get();
    assertThat(accumulator.sampleCovariance())
        .isWithin(ALLOWED_ERROR)
        .of(expectedSampleCovariance);
  }

  @Test
  public void sampleCovariance_withNoValues_throwsIllegalStateException() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    assertThrows(IllegalStateException.class, accumulator::sampleCovariance);
  }

  @Test
  public void sampleCovariance_withOneValue_throwsIllegalStateException() {
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE);
    assertThrows(IllegalStateException.class, accumulator::sampleCovariance);
  }
}