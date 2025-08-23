package org.apache.commons.lang3;

import org.junit.Test;
import java.util.function.Consumer;

/**
 * This test class contains tests for the CharRange class.
 * The original test was auto-generated and has been improved for clarity.
 */
public class CharRange_ESTestTest34 extends CharRange_ESTest_scaffolding {

    /**
     * Tests that iterating over a large negated character range using forEach
     * is a very slow operation that should result in a timeout.
     *
     * A negated range like {@code CharRange.isNot('S')} contains every character
     * except for 'S', which amounts to 65,535 characters. The forEach method
     * will iterate through all of them. This test verifies that such an
     * operation does not complete within a reasonable time, highlighting a
     * potential performance pitfall.
     */
    @Test(timeout = 4000)
    public void forEachOnLargeNegatedRangeShouldTimeOut() {
        // ARRANGE: Create a range representing all characters EXCEPT 'S'.
        // This results in a very large set of 65,535 characters.
        final CharRange largeNegatedRange = CharRange.isNot('S');
        final Consumer<Character> noOpConsumer = character -> {
            // This is a no-op consumer, used as a sink for the forEach loop.
        };

        // ACT & ASSERT: Attempt to iterate over every character in the vast range.
        // The test is expected to be terminated by the JUnit framework
        // for exceeding the 4000ms timeout. The timeout itself serves as the assertion.
        largeNegatedRange.forEach(noOpConsumer);
    }
}