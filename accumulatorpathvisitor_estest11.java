package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.file.AccumulatorPathVisitor.Builder;
import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor.Builder}.
 *
 * Note: This class retains the original name and inheritance structure for context,
 * while the test method inside has been rewritten for clarity.
 */
public class AccumulatorPathVisitor_ESTestTest11 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that a new instance of the {@link Builder} can be created successfully.
     */
    @Test
    public void builderShouldBeCreatedSuccessfully() {
        // Act
        final Builder builder = new AccumulatorPathVisitor.Builder();

        // Assert
        assertNotNull("A new Builder instance should not be null.", builder);
    }
}