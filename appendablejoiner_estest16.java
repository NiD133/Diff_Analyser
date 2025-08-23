package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableBiConsumer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link AppendableJoiner.Builder}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that an AppendableJoiner can be successfully built after setting a custom element appender.
     */
    @Test
    public void testBuildWithCustomElementAppender() {
        // Arrange
        // A "no-operation" appender that does nothing with the elements.
        final FailableBiConsumer<Appendable, String, IOException> nopAppender = FailableBiConsumer.nop();

        // Act
        // Use the builder to create a joiner with the custom appender.
        final AppendableJoiner<String> joiner = new AppendableJoiner.Builder<String>()
                .setElementAppender(nopAppender)
                .get();

        // Assert
        // The builder should successfully produce a non-null AppendableJoiner instance.
        assertNotNull("The created joiner should not be null.", joiner);
    }
}