package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.Set;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that the constructor throws a NullPointerException when a null Set
     * is passed via its varargs parameter. The constructor should reject null
     * sets to ensure the integrity of the composite collection.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullSetInVarArgsShouldThrowNullPointerException() {
        // Arrange: Create an array of Sets containing a null element.
        // This simulates passing a null to the varargs constructor.
        @SuppressWarnings("unchecked") // Necessary for creating a generic array
        final Set<String>[] setsWithNull = new Set[] { null };

        // Act: Attempt to create a CompositeSet with the invalid input.
        // Assert: Expect a NullPointerException, as declared in the @Test annotation.
        new CompositeSet<>(setsWithNull);
    }
}