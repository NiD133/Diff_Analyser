package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Locale;
import org.apache.commons.lang3.function.FailableBiConsumer;

/**
 * Tests for the static helper methods in {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that the package-private {@code joinI} method does not modify the
     * StringBuilder when provided with a "no-op" (no operation) appender,
     * even if the collection of elements is not empty.
     */
    @Test
    public void testJoinWithNopAppenderDoesNotAlterStringBuilder() {
        // Arrange
        final StringBuilder targetBuilder = new StringBuilder();
        final CharSequence emptyPrefix = "";
        final CharSequence emptySuffix = "";
        final CharSequence emptyDelimiter = "";

        // A non-empty collection of items to "join".
        final Iterable<Locale.Category> elements = EnumSet.allOf(Locale.Category.class);

        // A consumer that performs no action for each element.
        final FailableBiConsumer<Appendable, Locale.Category, IOException> nopAppender = FailableBiConsumer.nop();

        // Act
        // The joinI method is a package-private static helper method.
        final StringBuilder resultBuilder = AppendableJoiner.joinI(
                targetBuilder,
                emptyPrefix,
                emptySuffix,
                emptyDelimiter,
                nopAppender,
                elements);

        // Assert
        // 1. The method should return the same StringBuilder instance it was given.
        assertSame("The returned StringBuilder should be the same instance as the input.", targetBuilder, resultBuilder);

        // 2. The StringBuilder's content should be unchanged because all affixes were empty
        //    and the appender performed no action on the elements.
        assertEquals("StringBuilder content should be empty.", "", resultBuilder.toString());
    }
}