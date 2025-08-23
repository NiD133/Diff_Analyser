package com.google.common.collect;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import org.junit.Test;

/**
 * Tests for {@link CompactLinkedHashSet}.
 */
public class CompactLinkedHashSetTest {

    @Test
    public void resizeEntries_withUninitializedLinkArrays_throwsNullPointerException() throws Exception {
        // Arrange: Create a set and use reflection to put it into an invalid state.
        // The constructor correctly initializes all internal arrays. We manually set
        // the 'predecessor' array to null to simulate a corrupted state. This is a
        // white-box test targeting the robustness of the package-private resizeEntries method.
        CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(1);

        Field predecessorField = CompactLinkedHashSet.class.getDeclaredField("predecessor");
        predecessorField.setAccessible(true);
        predecessorField.set(set, null);

        // Act & Assert: Verify that calling resizeEntries on the corrupted object
        // throws a NullPointerException.
        try {
            set.resizeEntries(54);
            fail("Expected NullPointerException was not thrown.");
        } catch (NullPointerException expected) {
            // This is the expected behavior. The method fails fast when its internal
            // state invariants are violated.
        }
    }
}