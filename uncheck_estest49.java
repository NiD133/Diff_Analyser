package org.apache.commons.io.function;

import org.junit.Test;

/**
 * Tests for the {@link Uncheck} utility class.
 */
public class UncheckTest {

    /**
     * Tests that calling {@link Uncheck#accept(IOConsumer, Object)} with a null
     * consumer throws a {@link NullPointerException}.
     * <p>
     * This behavior is expected because a null functional interface is a
     * programming error (precondition violation) and should fail fast, rather
     * than be wrapped in an {@link java.io.UncheckedIOException}.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testAcceptThrowsNpeForNullConsumer() {
        // The cast to IOConsumer<String> is required to resolve the correct method overload.
        Uncheck.accept((IOConsumer<String>) null, "test-input");
    }
}