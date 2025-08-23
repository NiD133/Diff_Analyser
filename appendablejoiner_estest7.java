package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableBiConsumer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collections;

/**
 * Tests for the static helper methods in {@link AppendableJoiner}.
 * This test was improved for readability from an auto-generated file.
 */
public class AppendableJoiner_ESTestTest7 extends AppendableJoiner_ESTest_scaffolding {

    /**
     * Tests that {@link AppendableJoiner#joinI(StringBuilder, CharSequence, CharSequence, CharSequence, FailableBiConsumer, Iterable)}
     * throws a NullPointerException when the target StringBuilder is null.
     */
    @Test(expected = NullPointerException.class)
    public void testJoinIWithNullStringBuilderThrowsNPE() {
        // Arrange
        final FailableBiConsumer<Appendable, StringBuilder, IOException> nopConsumer = FailableBiConsumer.nop();
        final Iterable<StringBuilder> emptyIterable = Collections.emptyList();

        // Act: Call the method with a null StringBuilder, which is the object to append to.
        // The other arguments are set to null as in the original test case.
        AppendableJoiner.joinI(null, null, null, null, nopConsumer, emptyIterable);

        // Assert: The @Test(expected) annotation verifies that a NullPointerException is thrown.
    }
}