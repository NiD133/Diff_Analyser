package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.UnaryOperator;
import org.apache.commons.io.file.AccumulatorPathVisitor;
import org.apache.commons.io.file.CleaningPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.CountingPathVisitor;
import org.apache.commons.io.file.DeleteOption;
import org.apache.commons.io.file.DeletingPathVisitor;
import org.apache.commons.io.file.PathFilter;
import org.apache.commons.io.file.StandardDeleteOption;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.io.function.IOBiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true)
public class CountingPathVisitor_ESTest extends CountingPathVisitor_ESTest_scaffolding {

    // ========== Builder Pattern Tests ==========
    
    @Test(timeout = 4000)
    public void testBuilder_setPathCounters_returnsSameInstance() throws Throwable {
        // Given: A builder with existing path counters
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        
        // When: Setting the same path counters
        CountingPathVisitor.Builder result = builder.setPathCounters(pathCounters);
        
        // Then: Should return the same builder instance (fluent interface)
        assertSame("Builder should return same instance for fluent interface", result, builder);
    }

    @Test(timeout = 4000)
    public void testBuilder_setDirectoryFilter_returnsSameInstance() throws Throwable {
        // Given: A builder and a directory filter
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        NotFileFilter cannotWriteFilter = (NotFileFilter) CanWriteFileFilter.CANNOT_WRITE;
        
        // When: Setting directory filter
        CountingPathVisitor.Builder result = builder.setDirectoryFilter(cannotWriteFilter);
        
        // Then: Should return the same builder instance
        assertSame("Builder should return same instance for fluent interface", result, builder);
    }

    @Test(timeout = 4000)
    public void testBuilder_setFileFilter_returnsSameInstance() throws Throwable {
        // Given: A builder with existing file filter
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        PathFilter fileFilter = builder.getFileFilter();
        
        // When: Setting the same file filter
        CountingPathVisitor.Builder result = builder.setFileFilter(fileFilter);
        
        // Then: Should return the same builder instance
        assertSame("Builder should return same instance for fluent interface", builder, result);
    }

    @Test(timeout = 4000)
    public void testBuilder_setDirectoryPostTransformer_withIdentityFunction() throws Throwable {
        // Given: A builder and identity transformer
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        UnaryOperator<Path> identityTransformer = UnaryOperator.identity();
        
        // When: Setting directory post transformer
        CountingPathVisitor.Builder result = builder.setDirectoryPostTransformer(identityTransformer);
        
        // Then: Should return the same builder instance
        assertSame("Builder should return same instance for fluent interface", result, builder);
    }

    @Test(timeout = 4000)
    public void testBuilder_setDirectoryPostTransformer_withNull() throws Throwable {
        // Given: A builder
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        
        // When: Setting null directory post transformer
        CountingPathVisitor.Builder result = builder.setDirectoryPostTransformer(null);
        
        // Then: Should return the same builder instance
        assertSame("Builder should accept null transformer", result, builder);
    }

    // ========== Factory Method Tests ==========
    
    @Test(timeout = 4000)
    public void testWithLongCounters_createsVisitorSuccessfully() throws Throwable {
        // When: Creating visitor with long counters
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        
        // Then: Should create a valid visitor instance
        assertNotNull("Should create non-null visitor", visitor);
    }

    @Test(timeout = 4000)
    public void testWithBigIntegerCounters_createsVisitorSuccessfully() throws Throwable {
        // When: Creating visitor with BigInteger counters
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        
        // Then: Should create a valid visitor instance
        assertNotNull("Should create non-null visitor", visitor);
    }

    @Test(timeout = 4000)
    public void testDefaultDirectoryTransformer_returnsNonNull() throws Throwable {
        // When: Getting default directory transformer
        UnaryOperator<Path> transformer = CountingPathVisitor.defaultDirectoryTransformer();
        
        // Then: Should return a non-null transformer
        assertNotNull("Default directory transformer should not be null", transformer);
    }

    @Test(timeout = 4000)
    public void testDefaultDirectoryFilter_returnsNonNull() throws Throwable {
        // When: Getting default directory filter
        IOFileFilter filter = CountingPathVisitor.defaultDirectoryFilter();
        
        // Then: Should return a non-null filter
        assertNotNull("Default directory filter should not be null", filter);
    }

    @Test(timeout = 4000)
    public void testDefaultFileFilter_returnsNonNull() throws Throwable {
        // When: Getting default file filter
        IOFileFilter filter = CountingPathVisitor.defaultFileFilter();
        
        // Then: Should return a non-null filter
        assertNotNull("Default file filter should not be null", filter);
    }

    // ========== Constructor Tests ==========
    
    @Test(timeout = 4000)
    public void testConstructor_withPathCountersOnly() throws Throwable {
        // Given: Path counters
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        
        // When: Creating visitor with path counters only
        CountingPathVisitor visitor = new CountingPathVisitor(pathCounters);
        
        // Then: Should create successfully
        assertNotNull("Should create visitor with path counters", visitor);
    }

    @Test(timeout = 4000)
    public void testConstructor_withAllFilters() throws Throwable {
        // Given: Path counters and filters
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        EmptyFileFilter emptyFilter = (EmptyFileFilter) EmptyFileFilter.EMPTY;
        
        // When: Creating visitor with all parameters
        CountingPathVisitor visitor = new CountingPathVisitor(pathCounters, emptyFilter, emptyFilter);
        
        // Then: Should create successfully
        assertNotNull("Should create visitor with all filters", visitor);
    }

    @Test(timeout = 4000)
    public void testConstructor_withNullBuilder_throwsException() throws Throwable {
        // When & Then: Creating visitor with null builder should throw NPE
        try {
            new CountingPathVisitor((CountingPathVisitor.AbstractBuilder<?, ?>) null);
            fail("Should throw NullPointerException for null builder");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_withNullFileFilter_throwsException() throws Throwable {
        // Given: Valid path counters but null file filter
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        
        // When & Then: Should throw NPE for null file filter
        try {
            new CountingPathVisitor(pathCounters, null, null);
            fail("Should throw NullPointerException for null file filter");
        } catch (NullPointerException e) {
            assertEquals("Exception should mention fileFilter", "fileFilter", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_withNullVisitFileFailedFunction_throwsException() throws Throwable {
        // Given: Valid parameters except null visit failed function
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        FileFileFilter fileFilter = (FileFileFilter) FileFileFilter.INSTANCE;
        
        // When & Then: Should throw NPE for null visit failed function
        try {
            new CountingPathVisitor(pathCounters, fileFilter, fileFilter, 
                                  (IOBiFunction<Path, IOException, FileVisitResult>) null);
            fail("Should throw NullPointerException for null visitFileFailedFunction");
        } catch (NullPointerException e) {
            assertEquals("Exception should mention visitFileFailedFunction", 
                        "visitFileFailedFunction", e.getMessage());
        }
    }

    // ========== File Visiting Tests ==========
    
    @Test(timeout = 4000)
    public void testVisitFile_withValidFile_returnsContinue() throws Throwable {
        // Given: A visitor and mock file with attributes
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        
        // When: Visiting the file
        FileVisitResult result = visitor.visitFile(path, attributes);
        
        // Then: Should continue visiting
        assertEquals("Should continue after visiting file", FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void testVisitFile_withNullAttributes_throwsException() throws Throwable {
        // Given: A visitor and path with null attributes
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        
        // When & Then: Should throw NPE for null attributes
        try {
            visitor.visitFile(path, null);
            fail("Should throw NullPointerException for null attributes");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testPreVisitDirectory_withFilteredDirectory_skipsSubtree() throws Throwable {
        // Given: A visitor with suffix filter that won't match
        SuffixFileFilter suffixFilter = new SuffixFileFilter("org.apache.commons.io.file.CountingPathVisitor");
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(suffixFilter, suffixFilter);
        
        // When: Pre-visiting directory that doesn't match filter
        FileVisitResult result = visitor.preVisitDirectory(null, null);
        
        // Then: Should skip the subtree
        assertEquals("Should skip subtree for filtered directory", FileVisitResult.SKIP_SUBTREE, result);
    }

    @Test(timeout = 4000)
    public void testPostVisitDirectory_withException_returnsContinue() throws Throwable {
        // Given: A visitor and an IOException
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockIOException exception = new MockIOException();
        
        // When: Post-visiting directory with exception
        FileVisitResult result = visitor.postVisitDirectory(null, exception);
        
        // Then: Should continue despite exception
        assertEquals("Should continue after directory with exception", FileVisitResult.CONTINUE, result);
    }

    // ========== Accept Method Tests ==========
    
    @Test(timeout = 4000)
    public void testAccept_withValidPath_returnsTrue() throws Throwable {
        // Given: A visitor and valid path
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        
        // When: Testing if path is accepted
        boolean result = visitor.accept(path, null);
        
        // Then: Should accept the path
        assertTrue("Should accept valid path", result);
    }

    @Test(timeout = 4000)
    public void testAccept_withNullPath_throwsException() throws Throwable {
        // Given: A visitor
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor visitor = builder.get();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        
        // When & Then: Should throw NPE for null path
        try {
            visitor.accept(null, attributes);
            fail("Should throw NullPointerException for null path");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // ========== Update Counter Tests ==========
    
    @Test(timeout = 4000)
    public void testUpdateDirCounter_withValidPath() throws Throwable {
        // Given: A visitor and valid directory path
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        CountingPathVisitor visitor = builder.getUnchecked();
        MockFile mockFile = new MockFile("pathCounters", "pathCounters");
        Path path = mockFile.toPath();
        
        // When: Updating directory counter (should not throw)
        visitor.updateDirCounter(path, null);
        
        // Then: Operation should complete successfully (no exception)
    }

    @Test(timeout = 4000)
    public void testUpdateFileCounters_withNullPath_throwsException() throws Throwable {
        // Given: A visitor
        CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        
        // When & Then: Should throw NPE for null path
        try {
            visitor.updateFileCounters(null, null);
            fail("Should throw NullPointerException for null path");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testUpdateFileCounters_withValidPathAndAttributes() throws Throwable {
        // Given: An accumulator visitor, path, and attributes
        AccumulatorPathVisitor.Builder builder = new AccumulatorPathVisitor.Builder();
        AccumulatorPathVisitor visitor = builder.get();
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();
        
        // When: Updating file counters (should not throw)
        visitor.updateFileCounters(path, attributes);
        
        // Then: Operation should complete successfully
    }

    // ========== Equality and Hash Code Tests ==========
    
    @Test(timeout = 4000)
    public void testEquals_withSameInstance_returnsTrue() throws Throwable {
        // Given: A visitor instance
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        
        // When: Comparing with itself
        boolean result = visitor.equals(visitor);
        
        // Then: Should be equal
        assertTrue("Visitor should equal itself", result);
    }

    @Test(timeout = 4000)
    public void testEquals_withDifferentType_returnsFalse() throws Throwable {
        // Given: A visitor and different object type
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        MockIOException otherObject = new MockIOException();
        
        // When: Comparing with different type
        boolean result = visitor.equals(otherObject);
        
        // Then: Should not be equal
        assertFalse("Visitor should not equal different type", result);
    }

    @Test(timeout = 4000)
    public void testEquals_withDifferentVisitorType_returnsFalse() throws Throwable {
        // Given: Different types of visitors
        AccumulatorPathVisitor accumulator = AccumulatorPathVisitor.withBigIntegerCounters();
        CountingPathVisitor cleaner = CleaningPathVisitor.withBigIntegerCounters();
        
        // When: Comparing different visitor types
        boolean result = cleaner.equals(accumulator);
        
        // Then: Should not be equal
        assertFalse("Different visitor types should not be equal", result);
    }

    @Test(timeout = 4000)
    public void testHashCode_doesNotThrow() throws Throwable {
        // Given: A visitor
        CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        
        // When: Getting hash code (should not throw)
        visitor.hashCode();
        
        // Then: Operation should complete successfully
    }

    // ========== String Representation Tests ==========
    
    @Test(timeout = 4000)
    public void testToString_returnsExpectedFormat() throws Throwable {
        // Given: A new accumulator visitor
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters();
        
        // When: Getting string representation
        String result = visitor.toString();
        
        // Then: Should show initial counts
        assertEquals("Should show initial zero counts", "0 files, 0 directories, 0 bytes", result);
    }

    @Test(timeout = 4000)
    public void testGetPathCounters_returnsNonNull() throws Throwable {
        // Given: An accumulator visitor with null filters
        AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(null, null);
        
        // When: Getting path counters
        Counters.PathCounters counters = visitor.getPathCounters();
        
        // Then: Should return non-null counters
        assertNotNull("Path counters should not be null", counters);
    }

    // ========== Integration Tests with Specific Visitors ==========
    
    @Test(timeout = 4000)
    public void testDeletingPathVisitor_withInvalidPath_throwsIOException() throws Throwable {
        // Given: A deleting visitor with delete options
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[3];
        StandardDeleteOption standardOption = StandardDeleteOption.OVERRIDE_READ_ONLY;
        deleteOptions[2] = standardOption;
        String[] linkOptions = new String[0];
        DeletingPathVisitor visitor = new DeletingPathVisitor(pathCounters, deleteOptions, linkOptions);
        MockFile mockFile = new MockFile("");
        Path path = mockFile.toPath();
        
        // When & Then: Should throw IOException for invalid file operations
        try {
            visitor.visitFile(path, null);
            fail("Should throw IOException for invalid file operations");
        } catch (IOException e) {
            assertTrue("Should mention file operations not available", 
                      e.getMessage().contains("DOS or POSIX file operations not available"));
        }
    }

    @Test(timeout = 4000)
    public void testCleaningPathVisitor_withNullPath_throwsException() throws Throwable {
        // Given: A cleaning visitor
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] linkOptions = {"org.apache.commons.io.file.CountingPathVisitor$AbstractBuilder", "Z'flPCE", ""};
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, null, linkOptions);
        
        // When & Then: Should throw NPE for null path
        try {
            visitor.preVisitDirectory(null, null);
            fail("Should throw NullPointerException for null path");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDeletingPathVisitor_postVisitDirectory_withException() throws Throwable {
        // Given: A deleting visitor and temp file
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] linkOptions = {"hCUJ&KIcnhn", "*Qus;#HzV)I)", "&V8coOO1NH$"};
        DeletingPathVisitor visitor = new DeletingPathVisitor(pathCounters, linkOptions);
        File tempFile = MockFile.createTempFile("-i8e'2;3AOg#yY!", "fileFilter");
        Path path = tempFile.toPath();
        MockIOException exception = new MockIOException();
        
        // When & Then: Should throw NoSuchFileException
        try {
            visitor.postVisitDirectory(path, exception);
            fail("Should throw NoSuchFileException");
        } catch (NoSuchFileException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testDeletingPathVisitor_withNullPath_throwsException() throws Throwable {
        // Given: A deleting visitor with null path
        DeletingPathVisitor visitor = DeletingPathVisitor.withLongCounters();
        MockIOException exception = new MockIOException();
        
        // When & Then: Should throw NPE for null path
        try {
            visitor.postVisitDirectory(null, exception);
            fail("Should throw NullPointerException for null path");
        } catch (NullPointerException e) {
            // Expected exception from Files operations
        }
    }
}