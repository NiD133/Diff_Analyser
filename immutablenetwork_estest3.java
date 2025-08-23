package com.google.common.graph;

import org.junit.Test;

/**
 * Tests for {@link ImmutableNetwork}.
 */
public class ImmutableNetworkTest {

    /**
     * Tests that calling the deprecated {@code copyOf(ImmutableNetwork)} with a null argument
     * throws a {@link NullPointerException}, as is standard for Guava's static factory methods.
     */
    @Test(expected = NullPointerException.class)
    public void copyOf_withNullImmutableNetwork_throwsNullPointerException() {
        // The cast to ImmutableNetwork is necessary to resolve the correct overloaded method,
        // as there is also a copyOf(Network).
        ImmutableNetwork.copyOf((ImmutableNetwork<?, ?>) null);
    }
}