package com.google.common.graph;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link EndpointPair#equals(Object)} and {@link EndpointPair#hashCode()}. */
@RunWith(JUnit4.class)
public class EndpointPairTest {

  /**
   * The equals contract for EndpointPair is more nuanced than for a typical object. It depends on
   * whether the pair is ordered or unordered. This test verifies:
   *
   * <p>1. An ordered pair is not equal to another with the source and target swapped.
   * <p>2. An unordered pair is equal to another with the nodes swapped.
   * <p>3. An ordered pair is never equal to an unordered pair, even with the same nodes.
   */
  @Test
  public void equals_verifiesCorrectnessForOrderedAndUnorderedPairs() {
    // For ordered pairs, the order of nodes matters.
    EndpointPair<String> orderedPair = EndpointPair.ordered("a", "b");
    EndpointPair<String> orderedPairReversed = EndpointPair.ordered("b", "a");

    // For unordered pairs, the order of nodes does not matter.
    EndpointPair<String> unorderedPair = EndpointPair.unordered("a", "b");
    EndpointPair<String> unorderedPairReversed = EndpointPair.unordered("b", "a");

    new EqualsTester()
        // Group 1: An ordered pair is only equal to itself.
        .addEqualityGroup(orderedPair)
        // Group 2: A reversed ordered pair is in a different group.
        .addEqualityGroup(orderedPairReversed)
        // Group 3: An unordered pair and its reversed version are equal.
        .addEqualityGroup(unorderedPair, unorderedPairReversed)
        .testEquals();
  }
}