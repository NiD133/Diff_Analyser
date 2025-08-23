package org.apache.commons.io.file;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link AccumulatorPathVisitor#equals(Object)}.
 */
public class AccumulatorPathVisitorEqualsTest {

    @Test
    public void testEquals_returnsFalse_whenVisitorsHaveDifferentFileLists() throws IOException {
        // Arrange
        // Create a visitor and have it visit a file. This modifies its internal state
        // by adding the file to its list and updating its counters.
        final AccumulatorPathVisitor visitorWithFile = new AccumulatorPathVisitor();
        final Path visitedPath = Path.of("test-file.txt");
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);
        doReturn(100L).when(mockAttributes).size();

        visitorWithFile.visitFile(visitedPath, mockAttributes);

        // Create a second, new visitor that has not visited any files.
        final AccumulatorPathVisitor emptyVisitor = new AccumulatorPathVisitor();

        // Act & Assert
        // The two visitors should not be equal because their internal states (file lists and counters) differ.
        assertNotEquals(visitorWithFile, emptyVisitor);
    }
}