package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

/**
 * Contains an improved test case for the {@link AccumulatorPathVisitor} class.
 */
public class AccumulatorPathVisitor_ESTestTest6 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that calling {@link AccumulatorPathVisitor#updateFileCounters(Path, BasicFileAttributes)}
     * correctly adds the provided file path to its internal list of files.
     */
    @Test
    public void updateFileCounters_whenCalled_shouldAddFileToList() {
        // Arrange
        // 1. Create the visitor to be tested.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();

        // 2. Create a mock file path and its attributes.
        // The size() method is called by the parent class to update counters, so it needs to be mocked.
        final Path testFile = new MockFile("test-file.txt").toPath();
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);
        doReturn(128L).when(mockAttributes).size();

        // Act
        // 3. Call the method under test. This should add the file to the visitor's internal list.
        visitor.updateFileCounters(testFile, mockAttributes);

        // Assert
        // 4. Verify that the file was added to the list as expected.
        final List<Path> fileList = visitor.getFileList();

        assertNotNull("The file list should not be null.", fileList);
        assertEquals("The file list should contain exactly one entry.", 1, fileList.size());
        assertEquals("The file list should contain the path that was passed.", testFile, fileList.get(0));
    }
}