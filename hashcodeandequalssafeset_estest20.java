package org.mockito.internal.util.collections;

import org.junit.Test;
import java.util.Collection;

/**
 * Test suite for {@link HashCodeAndEqualsSafeSet}.
 * This class contains the refactored test case.
 */
// The original test class name is preserved as per the prompt's context.
// A more conventional name would be HashCodeAndEqualsSafeSetTest.
public class HashCodeAndEqualsSafeSet_ESTestTest20 {

    /**
     * Verifies that the retainAll() method throws an IllegalArgumentException
     * when a null collection is passed as an argument, enforcing its non-null contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void retainAll_shouldThrowIllegalArgumentException_forNullCollection() {
        // Given
        HashCodeAndEqualsSafeSet safeSet = new HashCodeAndEqualsSafeSet();
        Collection<?> nullCollection = null;

        // When
        safeSet.retainAll(nullCollection);

        // Then: an IllegalArgumentException is expected (verified by the @Test annotation).
    }
}