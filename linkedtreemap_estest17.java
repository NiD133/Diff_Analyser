package com.google.gson.internal;

import org.junit.Test;

/**
 * This test class contains unit tests for the LinkedTreeMap class.
 * This specific test focuses on the behavior of the package-private `removeInternal` method.
 */
public class LinkedTreeMapTest {

    /**
     * Verifies that calling the `removeInternal` method with a null node
     * correctly throws a NullPointerException. This ensures robust handling of
     * invalid arguments for this internal-use method.
     */
    @Test(expected = NullPointerException.class)
    public void removeInternal_whenNodeIsNull_throwsNullPointerException() {
        // Arrange: Create an empty LinkedTreeMap instance.
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();

        // Act: Attempt to remove a null node.
        // The 'expected' attribute of the @Test annotation handles the assertion,
        // ensuring that a NullPointerException is thrown by this call.
        map.removeInternal(null, true);
    }
}