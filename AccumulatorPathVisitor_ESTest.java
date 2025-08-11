package org.apache.commons.io.file;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.filefilter.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class AccumulatorPathVisitor_ESTest extends AccumulatorPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCanReadAndPrefixFileFilter() throws Throwable {
        // Test AccumulatorPathVisitor with CanReadFileFilter and PrefixFileFilter
        CanReadFileFilter canReadFilter = CanReadFileFilter.CAN_READ;
        String[] prefixes = new String[7];
        PrefixFileFilter prefixFilter = new PrefixFileFilter(prefixes);
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(canReadFilter, prefixFilter);
        assertNotNull(visitor);
    }

    @Test(timeout = 4000)
    public void testRelativizeFilesWithMockFile() throws Throwable {
        // Test relativizing files with a mock file
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile mockFile = new MockFile("q`;c", "CgHQW32?+phrjUX\"");
        Path path = mockFile.toPath();
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        List<Path> result = visitor.relativizeFiles(path, false, comparator);
        assertFalse(result.contains(path));
    }

    @Test(timeout = 4000)
    public void testUpdateFileCounters() throws Throwable {
        // Test updating file counters with mock attributes
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        visitor.updateFileCounters(path, attributes);
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        List<Path> result = visitor.relativizeFiles(path, true, comparator);
        assertFalse(result.contains(path));
    }

    @Test(timeout = 4000)
    public void testRegexFileFilter() throws Throwable {
        // Test AccumulatorPathVisitor with RegexFileFilter
        Pattern pattern = Pattern.compile(">", Pattern.UNICODE_CASE);
        RegexFileFilter regexFilter = new RegexFileFilter(pattern);
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(null, regexFilter);
        MockFile mockFile = new MockFile("org.apache.commons.io.file.AccumulatorPathVisitor$1");
        Path path = mockFile.toPath();
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        List<Path> result = visitor.relativizeDirectories(path, false, comparator);
        assertFalse(result.contains(path));
    }

    @Test(timeout = 4000)
    public void testUpdateDirCounterWithIOException() throws Throwable {
        // Test updating directory counter with IOException
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockIOException ioException = new MockIOException("CgHQW32?+phrjUX\"");
        MockFile mockFile = new MockFile("q`;c", "CgHQW32?+phrjUX\"");
        Path path = mockFile.toPath();
        visitor.updateDirCounter(path, ioException);
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        List<Path> result = visitor.relativizeDirectories(path, false, comparator);
        assertEquals(1, result.size());
    }

    @Test(timeout = 4000)
    public void testGetFileListAfterUpdate() throws Throwable {
        // Test getting file list after updating file counters
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        visitor.updateFileCounters(path, attributes);
        List<Path> fileList = visitor.getFileList();
        assertFalse(fileList.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetDirListAfterUpdate() throws Throwable {
        // Test getting directory list after updating directory counter
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockIOException ioException = new MockIOException();
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        visitor.updateDirCounter(path, ioException);
        List<Path> dirList = visitor.getDirList();
        assertEquals(1, dirList.size());
    }

    @Test(timeout = 4000)
    public void testHiddenFileFilter() throws Throwable {
        // Test AccumulatorPathVisitor with HiddenFileFilter
        HiddenFileFilter hiddenFilter = HiddenFileFilter.HIDDEN;
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(hiddenFilter, hiddenFilter);
        MockFile mockFile = new MockFile("org.apache.commons.io.file.AccumulatorPathVisitor$Builder", "9|ng{v.vnRUd<'l0fW");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        visitor.updateFileCounters(path, attributes);
        MockFile mockFile1 = new MockFile("9|ng{v.vnRUd<'l0fW");
        Path path1 = mockFile1.toPath();
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());

        // Expecting IllegalArgumentException when relativizing files
        try {
            visitor.relativizeFiles(path1, true, comparator);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRelativizeDirectoriesWithNullComparator() throws Throwable {
        // Test relativizing directories with null comparator
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        MockIOException ioException = new MockIOException();
        visitor.updateDirCounter(path, ioException);
        MockFile mockFile1 = new MockFile("c+{ [lS|rLx6");
        Path path1 = mockFile1.toPath();

        // Expecting IllegalArgumentException when relativizing directories
        try {
            visitor.relativizeDirectories(path1, true, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testBuilderWithNullPathCounters() throws Throwable {
        // Test AccumulatorPathVisitor.Builder with null PathCounters
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();

        // Expecting NullPointerException when creating AccumulatorPathVisitor
        try {
            new AccumulatorPathVisitor(pathCounters, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testBuilderCreation() throws Throwable {
        // Test creating a new AccumulatorPathVisitor.Builder
        AccumulatorPathVisitor.Builder builder = new AccumulatorPathVisitor.Builder();
        assertNotNull(builder);
    }

    @Test(timeout = 4000)
    public void testGetFileListInitiallyEmpty() throws Throwable {
        // Test getting file list initially empty
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        List<Path> fileList = visitor.getFileList();
        assertEquals(0, fileList.size());
    }

    @Test(timeout = 4000)
    public void testVisitFileUpdatesCounters() throws Throwable {
        // Test visiting a file updates counters
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        visitor.visitFile(path, attributes);
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();
        boolean isEqual = visitor2.equals(visitor);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testPostVisitDirectoryUpdatesCounters() throws Throwable {
        // Test post-visit directory updates counters
        AccumulatorPathVisitor visitor1 = new AccumulatorPathVisitor();
        AccumulatorPathVisitor visitor2 = new AccumulatorPathVisitor();
        assertTrue(visitor2.equals(visitor1));

        MockFile mockFile = new MockFile("+By\"=^s%<");
        Path path = mockFile.toPath();
        MockIOException ioException = new MockIOException();
        visitor2.postVisitDirectory(path, ioException);
        boolean isEqual = visitor1.equals(visitor2);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentVisitorTypes() throws Throwable {
        // Test equals method with different visitor types
        CountingPathVisitor countingVisitor = CountingPathVisitor.withBigIntegerCounters();
        AccumulatorPathVisitor accumulatorVisitor = AccumulatorPathVisitor.withBigIntegerCounters();
        boolean isEqual = accumulatorVisitor.equals(countingVisitor);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithBuilder() throws Throwable {
        // Test equals method with builder
        AccumulatorPathVisitor.Builder builder = AccumulatorPathVisitor.builder();
        AccumulatorPathVisitor visitor = builder.get();
        boolean isEqual = visitor.equals(builder);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        // Test equals method with the same instance
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        boolean isEqual = visitor.equals(visitor);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentCounterTypes() throws Throwable {
        // Test equals method with different counter types
        AccumulatorPathVisitor visitor1 = AccumulatorPathVisitor.withLongCounters();
        AccumulatorPathVisitor visitor2 = AccumulatorPathVisitor.withBigIntegerCounters();
        boolean isEqual = visitor1.equals(visitor2);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testGetDirListInitiallyEmpty() throws Throwable {
        // Test getting directory list initially empty
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(pathCounters);
        List<Path> dirList = visitor.getDirList();
        assertEquals(0, dirList.size());
    }

    @Test(timeout = 4000)
    public void testConstructorWithPathCountersAndFilters() throws Throwable {
        // Test constructor with PathCounters and filters
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        CanExecuteFileFilter canExecuteFilter = CanExecuteFileFilter.CAN_EXECUTE;
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(pathCounters, canExecuteFilter, canExecuteFilter);
        assertNotNull(visitor);
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullVisitFileFailedFunction() throws Throwable {
        // Test constructor with null visitFileFailedFunction
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        CanWriteFileFilter canWriteFilter = CanWriteFileFilter.CAN_WRITE;

        // Expecting NullPointerException when creating AccumulatorPathVisitor
        try {
            new AccumulatorPathVisitor(pathCounters, canWriteFilter, canWriteFilter, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateFileCountersWithNullAttributes() throws Throwable {
        // Test updating file counters with null attributes
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("h?d:E`l.T");
        Path path = mockFile.toPath();

        // Expecting NullPointerException when updating file counters
        try {
            visitor.updateFileCounters(path, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCode() throws Throwable {
        // Test hashCode method
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        assertNotNull(visitor.hashCode());
    }

    @Test(timeout = 4000)
    public void testRelativizeDirectoriesWithNullPath() throws Throwable {
        // Test relativizing directories with null path
        AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();

        // Expecting NullPointerException when relativizing directories
        try {
            visitor.relativizeDirectories(null, true, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testRelativizeFilesWithNullPath() throws Throwable {
        // Test relativizing files with null path
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();

        // Expecting NullPointerException when relativizing files
        try {
            visitor.relativizeFiles(null, true, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testUpdateDirCounterWithNullPath() throws Throwable {
        // Test updating directory counter with null path
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        MockIOException ioException = new MockIOException();

        // Expecting NullPointerException when updating directory counter
        try {
            visitor.updateDirCounter(null, ioException);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
            verifyException("org.apache.commons.io.file.AccumulatorPathVisitor", e);
        }
    }
}