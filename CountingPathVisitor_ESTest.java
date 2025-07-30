package org.apache.commons.io.file;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.UnaryOperator;

import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CountingPathVisitor_ESTest extends CountingPathVisitor_ESTest_scaffolding {

    // Test AccumulatorPathVisitor's file counter update
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorFileCounterUpdate() throws Throwable {
        AccumulatorPathVisitor.Builder builder = new AccumulatorPathVisitor.Builder();
        AccumulatorPathVisitor visitor = builder.get();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        visitor.updateFileCounters(path, attributes);
    }

    // Test CountingPathVisitor Builder's path counter setting
    @Test(timeout = 4000)
    public void testCountingPathVisitorBuilderPathCounterSetting() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        CountingPathVisitor.Builder updatedBuilder = builder.setPathCounters(counters);
        assertSame(updatedBuilder, builder);
    }

    // Test CountingPathVisitor Builder's directory filter setting
    @Test(timeout = 4000)
    public void testCountingPathVisitorBuilderDirectoryFilterSetting() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        NotFileFilter notFileFilter = (NotFileFilter) CanWriteFileFilter.CANNOT_WRITE;
        CountingPathVisitor.Builder updatedBuilder = builder.setDirectoryFilter(notFileFilter);
        assertSame(updatedBuilder, builder);
    }

    // Test CountingPathVisitor Builder's file filter setting
    @Test(timeout = 4000)
    public void testCountingPathVisitorBuilderFileFilterSetting() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        PathFilter pathFilter = builder.getFileFilter();
        CountingPathVisitor.Builder updatedBuilder = builder.setFileFilter(pathFilter);
        assertSame(builder, updatedBuilder);
    }

    // Test CountingPathVisitor's visitFile method with valid attributes
    @Test(timeout = 4000)
    public void testCountingPathVisitorVisitFileWithValidAttributes() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        FileVisitResult result = visitor.visitFile(path, attributes);
        assertEquals(FileVisitResult.CONTINUE, result);
    }

    // Test CountingPathVisitor creation with default path counters and empty file filter
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithDefaultPathCounters() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        EmptyFileFilter emptyFileFilter = (EmptyFileFilter) EmptyFileFilter.EMPTY;
        new CountingPathVisitor(counters, emptyFileFilter, emptyFileFilter);
    }

    // Test CountingPathVisitor creation with custom file and directory filters
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithCustomFilters() throws Throwable {
        IOFileFilter fileFilter = CountingPathVisitor.defaultFileFilter();
        NotFileFilter directoryFilter = (NotFileFilter) CanWriteFileFilter.CANNOT_WRITE;
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        new CountingPathVisitor(counters, directoryFilter, fileFilter);
    }

    // Test default directory transformer
    @Test(timeout = 4000)
    public void testDefaultDirectoryTransformer() throws Throwable {
        UnaryOperator<Path> transformer = CountingPathVisitor.defaultDirectoryTransformer();
        assertNotNull(transformer);
    }

    // Test AccumulatorPathVisitor's accept method with null attributes
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorAcceptWithNullAttributes() throws Throwable {
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("96UM\"T>:Dps");
        Path path = mockFile.toPath();
        boolean accepted = visitor.accept(path, null);
        assertFalse(accepted);
    }

    // Test DeletingPathVisitor's visitFile method with null attributes
    @Test(timeout = 4000)
    public void testDeletingPathVisitorVisitFileWithNullAttributes() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[3];
        StandardDeleteOption standardDeleteOption = StandardDeleteOption.OVERRIDE_READ_ONLY;
        deleteOptions[2] = standardDeleteOption;
        String[] stringArray = new String[0];
        DeletingPathVisitor visitor = new DeletingPathVisitor(counters, deleteOptions, stringArray);
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        try {
            visitor.visitFile(path, null);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.file.PathUtils", e);
        }
    }

    // Test CleaningPathVisitor's preVisitDirectory method with null path
    @Test(timeout = 4000)
    public void testCleaningPathVisitorPreVisitDirectoryWithNullPath() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] stringArray = new String[3];
        stringArray[0] = "org.apache.commons.io.file.CountingPathVisitor$AbstractBuilder";
        stringArray[1] = "Z'flPCE";
        stringArray[2] = "";
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, null, stringArray);
        try {
            visitor.preVisitDirectory(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test AccumulatorPathVisitor's preVisitDirectory method with valid attributes
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorPreVisitDirectoryWithValidAttributes() throws Throwable {
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        PathEqualsFileFilter pathEqualsFilter = new PathEqualsFileFilter(path);
        NotFileFilter notFileFilter = (NotFileFilter) HiddenFileFilter.VISIBLE;
        IOFileFilter combinedFilter = pathEqualsFilter.or(notFileFilter);
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(combinedFilter, notFileFilter);
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        try {
            visitor.preVisitDirectory(path, attributes);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    // Test DeletingPathVisitor's postVisitDirectory method with IOException
    @Test(timeout = 4000)
    public void testDeletingPathVisitorPostVisitDirectoryWithIOException() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] stringArray = new String[3];
        stringArray[0] = "hCUJ&KIcnhn";
        stringArray[1] = "*Qus;#HzV)I)";
        stringArray[2] = "&V8coOO1NH$";
        DeletingPathVisitor visitor = new DeletingPathVisitor(counters, stringArray);
        File file = MockFile.createTempFile("-i8e'2;3AOg#yY!", "fileFilter");
        Path path = file.toPath();
        MockIOException mockIOException = new MockIOException();
        try {
            visitor.postVisitDirectory(path, mockIOException);
            fail("Expecting exception: NoSuchFileException");
        } catch (NoSuchFileException e) {
            // Expected exception
        }
    }

    // Test DeletingPathVisitor's postVisitDirectory method with null path
    @Test(timeout = 4000)
    public void testDeletingPathVisitorPostVisitDirectoryWithNullPath() throws Throwable {
        DeletingPathVisitor visitor = DeletingPathVisitor.withLongCounters();
        MockIOException mockIOException = new MockIOException();
        try {
            visitor.postVisitDirectory(null, mockIOException);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.nio.file.Files", e);
        }
    }

    // Test CountingPathVisitor's accept method with null path
    @Test(timeout = 4000)
    public void testCountingPathVisitorAcceptWithNullPath() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor visitor = builder.get();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        try {
            visitor.accept(null, attributes);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test CountingPathVisitor creation with null builder
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithNullBuilder() throws Throwable {
        try {
            new CountingPathVisitor((CountingPathVisitor.AbstractBuilder<?, ?>) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.file.SimplePathVisitor$AbstractBuilder", e);
        }
    }

    // Test CountingPathVisitor's updateDirCounter method with valid path
    @Test(timeout = 4000)
    public void testCountingPathVisitorUpdateDirCounterWithValidPath() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor visitor = builder.getUnchecked();
        MockFile mockFile = new MockFile("pathCounters", "pathCounters");
        Path path = mockFile.toPath();
        visitor.updateDirCounter(path, null);
    }

    // Test CountingPathVisitor's visitFile method with null attributes
    @Test(timeout = 4000)
    public void testCountingPathVisitorVisitFileWithNullAttributes() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        try {
            visitor.visitFile(path, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    // Test AccumulatorPathVisitor's preVisitDirectory method with null path
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorPreVisitDirectoryWithNullPath() throws Throwable {
        SuffixFileFilter suffixFilter = new SuffixFileFilter("org.apache.commons.io.file.CountingPathVisitor");
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(suffixFilter, suffixFilter);
        FileVisitResult result = visitor.preVisitDirectory(null, null);
        assertEquals(FileVisitResult.SKIP_SUBTREE, result);
    }

    // Test AccumulatorPathVisitor's equals method with different object
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorEqualsWithDifferentObject() throws Throwable {
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockIOException mockIOException = new MockIOException();
        boolean isEqual = visitor.equals(mockIOException);
        assertFalse(isEqual);
    }

    // Test CountingPathVisitor's equals method with itself
    @Test(timeout = 4000)
    public void testCountingPathVisitorEqualsWithItself() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        boolean isEqual = visitor.equals(visitor);
        assertTrue(isEqual);
    }

    // Test CountingPathVisitor's equals method with different visitor
    @Test(timeout = 4000)
    public void testCountingPathVisitorEqualsWithDifferentVisitor() throws Throwable {
        AccumulatorPathVisitor accumulatorVisitor = AccumulatorPathVisitor.withBigIntegerCounters();
        CountingPathVisitor countingVisitor = CleaningPathVisitor.withBigIntegerCounters();
        boolean isEqual = countingVisitor.equals(accumulatorVisitor);
        assertFalse(isEqual);
    }

    // Test AccumulatorPathVisitor's visitFile method with valid attributes
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorVisitFileWithValidAttributes() throws Throwable {
        SuffixFileFilter suffixFilter = new SuffixFileFilter("");
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(suffixFilter, suffixFilter);
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        FileVisitResult result = visitor.visitFile(path, attributes);
        assertEquals(FileVisitResult.CONTINUE, result);
    }

    // Test CountingPathVisitor's accept method with valid attributes
    @Test(timeout = 4000)
    public void testCountingPathVisitorAcceptWithValidAttributes() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        boolean accepted = visitor.accept(path, null);
        assertTrue(accepted);
    }

    // Test CountingPathVisitor creation with null path counters
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithNullPathCounters() throws Throwable {
        new CountingPathVisitor((Counters.PathCounters) null);
    }

    // Test CountingPathVisitor Builder's directory post transformer setting
    @Test(timeout = 4000)
    public void testCountingPathVisitorBuilderDirectoryPostTransformerSetting() throws Throwable {
        UnaryOperator<Path> identityTransformer = UnaryOperator.identity();
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor.Builder updatedBuilder = builder.setDirectoryPostTransformer(identityTransformer);
        assertSame(updatedBuilder, builder);
    }

    // Test CountingPathVisitor Builder's directory post transformer setting with null
    @Test(timeout = 4000)
    public void testCountingPathVisitorBuilderDirectoryPostTransformerSettingWithNull() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor.Builder updatedBuilder = builder.setDirectoryPostTransformer(null);
        assertSame(updatedBuilder, builder);
    }

    // Test AccumulatorPathVisitor creation with default directory filter
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorCreationWithDefaultDirectoryFilter() throws Throwable {
        IOFileFilter directoryFilter = CountingPathVisitor.defaultDirectoryFilter();
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(directoryFilter, directoryFilter);
        assertNotNull(visitor);
    }

    // Test AccumulatorPathVisitor's toString method
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorToString() throws Throwable {
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        String description = visitor.toString();
        assertEquals("0 files, 0 directories, 0 bytes", description);
    }

    // Test CountingPathVisitor creation with null file filter
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithNullFileFilter() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        try {
            new CountingPathVisitor(counters, null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    // Test CountingPathVisitor's hashCode method
    @Test(timeout = 4000)
    public void testCountingPathVisitorHashCode() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        visitor.hashCode();
    }

    // Test CleaningPathVisitor's postVisitDirectory method with IOException
    @Test(timeout = 4000)
    public void testCleaningPathVisitorPostVisitDirectoryWithIOException() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockIOException mockIOException = new MockIOException();
        FileVisitResult result = visitor.postVisitDirectory(null, mockIOException);
        assertEquals(FileVisitResult.CONTINUE, result);
    }

    // Test CountingPathVisitor's updateFileCounters method with null path
    @Test(timeout = 4000)
    public void testCountingPathVisitorUpdateFileCountersWithNullPath() throws Throwable {
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        try {
            visitor.updateFileCounters(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    // Test CountingPathVisitor creation with null visitFileFailed function
    @Test(timeout = 4000)
    public void testCountingPathVisitorCreationWithNullVisitFileFailedFunction() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        FileFileFilter fileFilter = (FileFileFilter) FileFileFilter.INSTANCE;
        try {
            new CountingPathVisitor(counters, fileFilter, fileFilter, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    // Test AccumulatorPathVisitor's getPathCounters method
    @Test(timeout = 4000)
    public void testAccumulatorPathVisitorGetPathCounters() throws Throwable {
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(null, null);
        Counters.PathCounters counters = visitor.getPathCounters();
        assertNotNull(counters);
    }
}