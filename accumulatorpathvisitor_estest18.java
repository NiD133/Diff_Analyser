package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}, focusing on the equals() and hashCode() contract.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that two newly created visitors are equal, even if they use different
     * underlying counter implementations (long vs. BigInteger).
     * <p>
     * This is because their initial state (all counters at zero and empty path lists) is identical.
     * The test also verifies that their hash codes are consistent with the equals contract.
     * </p>
     */
    @Test
    public void testEqualsAndHashCodeForNewVisitorsWithDifferentCounterTypes() {
        // Arrange: Create two new visitors with different underlying counter types.
        // Both are in their initial state, meaning their counters are zero and their
        // file/directory lists are empty.
        final AccumulatorPathVisitor visitorWithLongCounters = AccumulatorPathVisitor.withLongCounters();
        final AccumulatorPathVisitor visitorWithBigIntegerCounters = AccumulatorPathVisitor.withBigIntegerCounters();

        // Assert:
        // 1. Sanity check that we are comparing two distinct objects.
        assertNotSame("Visitors should be different instances",
            visitorWithLongCounters, visitorWithBigIntegerCounters);

        // 2. HashCode contract: equal objects must have equal hash codes.
        assertEquals("Hash codes should be equal for equal objects",
            visitorWithLongCounters.hashCode(), visitorWithBigIntegerCounters.hashCode());

        // 3. Equals contract:
        //    - They should be equal because their observable state is identical.
        //    - Test for symmetry (a.equals(b) == b.equals(a)).
        assertEquals("A visitor with long counters should equal one with BigInteger counters when both are new",
            visitorWithLongCounters, visitorWithBigIntegerCounters);
        assertEquals("The equals comparison should be symmetrical",
            visitorWithBigIntegerCounters, visitorWithLongCounters);
    }
}