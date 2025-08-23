package org.apache.commons.jxpath;

import org.junit.Test;

/**
 * Unit tests for the {@link BasicNodeSet} class.
 */
public class BasicNodeSetTest {

    /**
     * Verifies that calling add() with a null NodeSet argument
     * throws a NullPointerException. This ensures the method correctly
     * handles invalid input by failing fast.
     */
    @Test(expected = NullPointerException.class)
    public void addShouldThrowNullPointerExceptionForNullNodeSet() {
        // Given
        BasicNodeSet basicNodeSet = new BasicNodeSet();

        // When
        basicNodeSet.add((NodeSet) null);

        // Then: A NullPointerException is expected, as declared by the @Test annotation.
    }
}