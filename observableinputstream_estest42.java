package org.apache.commons.io.input;

import org.apache.commons.io.input.MessageDigestInputStream.MessageDigestMaintainingObserver;
import org.junit.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Tests the behavior of {@link ObservableInputStream.Observer} implementations.
 * This test focuses on the lifecycle callbacks of an observer.
 */
public class ObservableInputStream_ESTestTest42 {

    /**
     * Verifies that calling the closed() callback on a MessageDigestMaintainingObserver
     * executes successfully without throwing an exception. This ensures that observer
     * implementations correctly handle the stream closure event.
     */
    @Test
    public void observerClosedCallbackShouldNotThrowException() throws NoSuchAlgorithmException, IOException {
        // Arrange: Create a MessageDigest and an observer that uses it.
        // The MessageDigestMaintainingObserver is a concrete implementation of
        // the abstract ObservableInputStream.Observer.
        final MessageDigest digest = MessageDigest.getInstance("MD5");
        final MessageDigestMaintainingObserver observer = new MessageDigestMaintainingObserver(digest);

        // Act: Simulate the stream-closed event by invoking the observer's callback.
        observer.closed();

        // Assert: The test passes if the 'Act' phase completes without throwing an exception.
        // This is an implicit assertion of correct behavior.
    }
}