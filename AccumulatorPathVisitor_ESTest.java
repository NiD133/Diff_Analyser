package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.file.AccumulatorPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.CountingPathVisitor;
import org.apache.commons.io.file.PathFilter;
import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;

/**
 * Test suite for AccumulatorPathVisitor functionality.
 * Tests the visitor's ability to accumulate and track file system paths during traversal.
 */
public class AccumulatorPathVisitorTest {

    // Factory Method Tests
    
    @Test
    public void testCreateVisitorWithBigIntegerCountersAndFilters() {
        // Given: File filters for readable files and prefix-based filtering
        CanReadFileFilter readableFilter = CanReadFileFilter.CAN_READ;
        String[] prefixes = new String[7]; // Array of null prefixes
        PrefixFileFilter prefixFilter = new PrefixFileFilter(prefixes);
        
        // When: Creating visitor with BigInteger counters and filters
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(
            readableFilter, prefixFilter);
        
        // Then: Visitor should be created successfully
        assertNotNull("Visitor should be created with filters", visitor);
    }

    @Test
    public void testCreateVisitorWithLongCountersAndNullFileFilter() {
        // Given: A regex filter and null file filter
        Pattern pattern = Pattern.compile(">", -1);
        RegexFileFilter regexFilter = new RegexFileFilter(pattern);
        
        // When: Creating visitor with null file filter
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(
            null, regexFilter);
        
        // Then: Visitor should be created successfully
        assertNotNull("Visitor should handle null file filter", visitor);
    }

    // Path Accumulation Tests
    
    @Test
    public void testFileListStartsEmpty() {
        // Given: A new visitor
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        
        // When: Getting file list without visiting any files
        List<Path> fileList = visitor.getFileList();
        
        // Then: List should be empty
        assertEquals("File list should start empty", 0, fileList.size());
    }

    @Test
    public void testDirectoryListStartsEmpty() {
        // Given: A visitor with path counters
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(counters);
        
        // When: Getting directory list without visiting any directories
        List<Path> dirList = visitor.getDirList();
        
        // Then: List should be empty
        assertEquals("Directory list should start empty", 0, dirList.size());
    }

    @Test
    public void testAccumulateFileAfterVisiting() {
        // Given: A visitor and a mock file
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path filePath = mockFile.toPath();
        BasicFileAttributes attributes = createMockFileAttributes(0L);
        
        // When: Updating file counters (simulates visiting a file)
        visitor.updateFileCounters(filePath, attributes);
        
        // Then: File should be added to the list
        List<Path> fileList = visitor.getFileList();
        assertFalse("File list should not be empty after visiting file", fileList.isEmpty());
    }

    @Test
    public void testAccumulateDirectoryAfterError() {
        // Given: A visitor and a directory with an error
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile mockDir = new MockFile("", "");
        Path dirPath = mockDir.toPath();
        MockIOException ioException = new MockIOException();
        
        // When: Updating directory counter with an exception
        visitor.updateDirCounter(dirPath, ioException);
        
        // Then: Directory should be added to the list
        List<Path> dirList = visitor.getDirList();
        assertEquals("Directory should be added after error", 1, dirList.size());
    }

    // Path Relativization Tests
    
    @Test
    public void testRelativizeFilesWithoutSorting() {
        // Given: A visitor with accumulated files
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile baseDir = new MockFile("baseDir", "subDir");
        Path basePath = baseDir.toPath();
        
        // Add a file to the visitor
        MockFile file = new MockFile("", "");
        Path filePath = file.toPath();
        BasicFileAttributes attributes = createMockFileAttributes(0L);
        visitor.updateFileCounters(filePath, attributes);
        
        // When: Relativizing files without sorting
        Comparator<Object> comparator = mock(Comparator.class);
        List<Path> relativizedFiles = visitor.relativizeFiles(basePath, false, comparator);
        
        // Then: Should return relativized paths (original path not included due to different base)
        assertFalse("Relativized list should not contain original path", 
                   relativizedFiles.contains(filePath));
    }

    @Test
    public void testRelativizeDirectoriesWithoutSorting() {
        // Given: A visitor with accumulated directories
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile baseDir = new MockFile("baseDir");
        Path basePath = baseDir.toPath();
        
        // Add a directory to the visitor
        MockFile dir = new MockFile("testDir", "subDir");
        Path dirPath = dir.toPath();
        MockIOException ioException = new MockIOException("Test error");
        visitor.updateDirCounter(dirPath, ioException);
        
        // When: Relativizing directories without sorting
        Comparator<Object> comparator = mock(Comparator.class);
        List<Path> relativizedDirs = visitor.relativizeDirectories(basePath, false, comparator);
        
        // Then: Should return relativized directory paths
        assertEquals("Should contain one relativized directory", 1, relativizedDirs.size());
    }

    // Error Handling Tests
    
    @Test(expected = IllegalArgumentException.class)
    public void testRelativizeFilesWithIncompatiblePaths() {
        // Given: A visitor with files and an incompatible base path
        HiddenFileFilter hiddenFilter = HiddenFileFilter.HIDDEN;
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(
            hiddenFilter, hiddenFilter);
        
        // Add a file with specific path structure
        MockFile file = new MockFile("org.apache.commons.io.file.AccumulatorPathVisitor$Builder", 
                                   "9|ng{v.vnRUd<'l0fW");
        Path filePath = file.toPath();
        BasicFileAttributes attributes = createMockFileAttributes(0L);
        visitor.updateFileCounters(filePath, attributes);
        
        // When: Trying to relativize with incompatible base path
        MockFile incompatibleBase = new MockFile("9|ng{v.vnRUd<'l0fW");
        Path incompatibleBasePath = incompatibleBase.toPath();
        Comparator<Object> comparator = mock(Comparator.class);
        
        // Then: Should throw IllegalArgumentException
        visitor.relativizeFiles(incompatibleBasePath, true, comparator);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorRejectsNullFileFilter() {
        // Given: Valid counters but null file filter
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        Counters.PathCounters counters = builder.getPathCounters();
        
        // When: Creating visitor with null file filter
        // Then: Should throw NullPointerException
        new AccumulatorPathVisitor(counters, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorRejectsNullVisitFileFailedFunction() {
        // Given: Valid parameters except null visit failed function
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CanWriteFileFilter writeFilter = CanWriteFileFilter.CAN_WRITE;
        
        // When: Creating visitor with null visit failed function
        // Then: Should throw NullPointerException
        new AccumulatorPathVisitor(counters, writeFilter, writeFilter, null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateFileCountersRejectsNullAttributes() {
        // Given: A visitor and a path with null attributes
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("testFile");
        Path filePath = mockFile.toPath();
        
        // When: Updating file counters with null attributes
        // Then: Should throw NullPointerException
        visitor.updateFileCounters(filePath, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRelativizeDirectoriesRejectsNullPath() {
        // Given: A visitor
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        
        // When: Relativizing with null path
        // Then: Should throw NullPointerException
        visitor.relativizeDirectories(null, true, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRelativizeFilesRejectsNullPath() {
        // Given: A visitor
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        
        // When: Relativizing with null path
        // Then: Should throw NullPointerException
        visitor.relativizeFiles(null, true, null);
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateDirCounterRejectsNullPath() {
        // Given: A visitor and an exception
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        MockIOException ioException = new MockIOException();
        
        // When: Updating directory counter with null path
        // Then: Should throw NullPointerException
        visitor.updateDirCounter(null, ioException);
    }

    // Equality and Hash Code Tests
    
    @Test
    public void testEqualityWithSameState() {
        // Given: Two identical visitors
        AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withBigIntegerCounters();
        AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withLongCounters();
        
        // When: Comparing for equality
        boolean areEqual = visitor1.equals(visitor2);
        
        // Then: Should be equal (same initial state)
        assertTrue("Visitors with same state should be equal", areEqual);
    }

    @Test
    public void testEqualityWithSelf() {
        // Given: A visitor
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        
        // When: Comparing with itself
        boolean isEqual = visitor.equals(visitor);
        
        // Then: Should be equal
        assertTrue("Visitor should equal itself", isEqual);
    }

    @Test
    public void testInequalityAfterFileVisit() {
        // Given: Two identical visitors
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();
        assertTrue("Visitors should start equal", visitor2.equals(visitor1));
        
        // When: One visitor processes a file
        MockFile mockFile = new MockFile("", "");
        Path filePath = mockFile.toPath();
        BasicFileAttributes attributes = createMockFileAttributes(0L);
        visitor1.visitFile(filePath, attributes);
        
        // Then: Visitors should no longer be equal
        boolean areEqual = visitor2.equals(visitor1);
        assertFalse("Visitors should not be equal after one processes a file", areEqual);
    }

    @Test
    public void testInequalityAfterDirectoryVisit() {
        // Given: Two identical visitors
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();
        assertTrue("Visitors should start equal", visitor2.equals(visitor1));
        
        // When: One visitor processes a directory with error
        MockFile mockDir = new MockFile("testDir");
        Path dirPath = mockDir.toPath();
        MockIOException ioException = new MockIOException();
        visitor2.postVisitDirectory(dirPath, ioException);
        
        // Then: Visitors should no longer be equal
        boolean areEqual = visitor1.equals(visitor2);
        assertFalse("Visitors should not be equal after directory processing", areEqual);
    }

    @Test
    public void testInequalityWithDifferentType() {
        // Given: An AccumulatorPathVisitor and a CountingPathVisitor
        AccumulatorPathVisitor accumulator = AccumulatorPathVisitor.withBigIntegerCounters();
        CountingPathVisitor counter = CountingPathVisitor.withBigIntegerCounters();
        
        // When: Comparing different types
        boolean areEqual = accumulator.equals(counter);
        
        // Then: Should not be equal
        assertFalse("Different visitor types should not be equal", areEqual);
    }

    @Test
    public void testInequalityWithNonVisitorObject() {
        // Given: A visitor and a builder
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        AccumulatorPathVisitor visitor = builder.get();
        
        // When: Comparing visitor with builder
        boolean areEqual = visitor.equals(builder);
        
        // Then: Should not be equal
        assertFalse("Visitor should not equal builder", areEqual);
    }

    // Builder Tests
    
    @Test
    public void testBuilderCreation() {
        // Given: Nothing
        // When: Creating a builder
        AccumulatorPathVisitor.Builder builder = new AccumulatorPathVisitor.Builder();
        
        // Then: Builder should be created successfully
        assertNotNull("Builder should be created", builder);
    }

    @Test
    public void testBuilderFactoryMethod() {
        // Given: Nothing
        // When: Using builder factory method
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        
        // Then: Builder should be created successfully
        assertNotNull("Builder factory should create builder", builder);
    }

    // Utility Tests
    
    @Test
    public void testHashCodeGeneration() {
        // Given: A visitor
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        
        // When: Getting hash code
        int hashCode = visitor.hashCode();
        
        // Then: Should generate hash code without error
        // Note: We don't test specific hash code values as they may vary
        assertNotNull("Hash code should be generated", hashCode);
    }

    @Test
    public void testConstructorWithExecuteFilter() {
        // Given: Path counters and execute filter
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CanExecuteFileFilter executeFilter = CanExecuteFileFilter.CAN_EXECUTE;
        
        // When: Creating visitor with execute filter
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(
            counters, executeFilter, executeFilter);
        
        // Then: Visitor should be created successfully
        assertNotNull("Visitor should be created with execute filter", visitor);
    }

    // Helper Methods
    
    /**
     * Creates a mock BasicFileAttributes with the specified size.
     */
    private BasicFileAttributes createMockFileAttributes(long size) {
        BasicFileAttributes attributes = mock(BasicFileAttributes.class);
        when(attributes.size()).thenReturn(size);
        return attributes;
    }
}