package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertThrows;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Tests for {@link ObservableInputStream}.
 * This test focuses on constructor behavior with invalid arguments.
 */
public class ObservableInputStream_ESTestTest33 { // Note: In a real project, this class would be renamed and merged.

    /**
     * Tests that the constructor {@code ObservableInputStream(InputStream, Observer...)}
     * throws a NullPointerException if the provided observer array is null.
     */
    @Test
    public void constructorWithNullObserverArrayShouldThrowNullPointerException() {
        // Arrange: Create a non-null dummy input stream, as the constructor requires one.
        final InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);

        // Act & Assert: Verify that instantiating with a null observer array throws NullPointerException.
        // The cast to (Observer[]) is necessary to resolve ambiguity for the varargs parameter.
        assertThrows(NullPointerException.class, () ->
                new ObservableInputStream(dummyInputStream, (ObservableInputStream.Observer[]) null)
        );
    }
}