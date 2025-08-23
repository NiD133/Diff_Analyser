package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link ObservableInputStream} focusing on observer handling.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling read() on an ObservableInputStream throws a NullPointerException
     * if it was constructed with an array containing a null Observer. The stream must
     * not silently ignore null observers, as this could hide configuration errors.
     */
    @Test
    public void readShouldThrowNullPointerExceptionWhenObserverIsNull() {
        // Arrange: Create an ObservableInputStream with an observer array containing a null element.
        // The underlying input stream can be a simple, empty stream as its content is not relevant.
        InputStream dummyInputStream = new ByteArrayInputStream(new byte[0]);
        ObservableInputStream.Observer[] observersWithNull = { null };
        ObservableInputStream observableStream = new ObservableInputStream(dummyInputStream, observersWithNull);

        // Act & Assert: Expect a NullPointerException when read() is called,
        // because it will attempt to call a method on the null observer.
        assertThrows(NullPointerException.class, observableStream::read);
    }
}