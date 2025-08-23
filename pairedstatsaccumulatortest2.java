package com.google.common.math;

import static com.google.common.math.StatsTesting.ONE_VALUE;
import static com.google.common.math.StatsTesting.OTHER_ONE_VALUE;
import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link PairedStatsAccumulator} focusing on count overflow behavior.
 */
@RunWith(JUnit4.class)
public class PairedStatsAccumulatorCountOverflowTest {

  @Test
  public void count_overflowsGracefullyByWrapping() {
    // ARRANGE: Create an accumulator and increase its internal count close to Long.MAX_VALUE.
    // The count is a `long`. We want to test that it wraps around on overflow, as expected for
    // standard integer arithmetic, rather than throwing an exception.
    PairedStatsAccumulator accumulator = new PairedStatsAccumulator();
    accumulator.add(ONE_VALUE, OTHER_ONE_VALUE); // Start with count = 1.

    // By repeatedly adding the accumulator's snapshot to itself, we double the effective count
    // in each iteration. We do this until the count is 2^62. For a 64-bit long, this is
    // one doubling away from overflowing.
    for (int i = 0; i < Long.SIZE - 2; i++) {
      accumulator.addAll(accumulator.snapshot());
    }
    // Sanity check that the count is 2^62 before the final doubling.
    assertThat(accumulator.count()).isEqualTo(1L << (Long.SIZE - 2));

    // ACT: Double the count one last time to trigger an overflow.
    // The count will become 2^63, which overflows a signed long to Long.MIN_VALUE.
    accumulator.addAll(accumulator.snapshot());

    // ASSERT: The count should wrap around to a negative number, not throw an exception.
    // The specific value for a 2^63 overflow is Long.MIN_VALUE.
    assertThat(accumulator.count()).isEqualTo(Long.MIN_VALUE);
  }
}