package org.apache.commons.io.file;

import org.junit.Test;

import java.nio.file.Path;
import java.util.Comparator;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that {@link AccumulatorPathVisitor#relativizeDirectories(Path, boolean, Comparator)}
     * throws a {@link NullPointerException} when the parent path argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void relativizeDirectoriesShouldThrowNullPointerExceptionForNullParentPath() {
        // Arrange: Create a visitor instance.
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Act & Assert: Call the method with a null parent path.
        // This is expected to throw a NullPointerException, as Path.relativize()
        // does not accept a null argument.
        visitor.relativizeDirectories(null, true, null);
    }
}