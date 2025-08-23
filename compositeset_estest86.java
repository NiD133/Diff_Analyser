package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that calling toSet() on an empty CompositeSet returns a new, empty Set.
     */
    @Test
    public void toSetOnEmptyCompositeShouldReturnEmptySet() {
        // Arrange: Create an empty CompositeSet.
        // The specific element type (String) is chosen for simplicity.
        final CompositeSet<String> compositeSet = new CompositeSet<>();

        // Act: Convert the composite set to a standard Set.
        final Set<String> resultantSet = compositeSet.toSet();

        // Assert: The resulting set should be non-null and empty.
        assertNotNull("The returned set should not be null", resultantSet);
        assertEquals("The returned set should be empty", 0, resultantSet.size());
    }
}