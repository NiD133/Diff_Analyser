package org.apache.commons.io.input;

import org.junit.Test;

/**
 * Unit tests for the {@link ObservableInputStream} class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that the constructor throws a NullPointerException when a null builder is provided.
     * This constructor is package-private and relies on the superclass {@link ProxyInputStream}
     * to handle the builder, which is where the NullPointerException is expected to originate.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullBuilderShouldThrowNullPointerException() {
        // The constructor under test is package-private, so this test must be in the same package.
        // It is expected to throw a NullPointerException because the superclass constructor
        // will attempt to access the null builder reference.
        new ObservableInputStream((ObservableInputStream.AbstractBuilder<?>) null);
    }
}