package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.file.CleaningPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.CountingPathVisitor;
import org.apache.commons.io.file.DeleteOption;
import org.apache.commons.io.file.DeletingPathVisitor;
import org.apache.commons.io.file.StandardDeleteOption;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CleaningPathVisitor_ESTest extends CleaningPathVisitor_ESTest_scaffolding {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Test(timeout = 4000)
    public void visitFile_ShouldContinue_WhenFileIsAccepted() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockFile file = new MockFile("", "valid_file.txt");
        Path path = file.toPath();
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        when(attrs.size()).thenReturn(0L);
        
        FileVisitResult result = visitor.visitFile(path, attrs);
        
        assertEquals("Should continue visiting files", FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void preVisitDirectory_ShouldThrowException_WhenPathIsNull() {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, new String[0]);
        
        try {
            visitor.preVisitDirectory(null, mock(BasicFileAttributes.class));
            fail("Expected NullPointerException for null path");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void constructor_ShouldThrowException_WithInvalidDeleteOptions() {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[2]; // Uninitialized options
        
        try {
            new CleaningPathVisitor(counters, deleteOptions, new String[0]);
            fail("Expected NullPointerException for invalid delete options");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void visitFile_ShouldThrowIOException_WhenFileSystemNotSupported() throws Throwable {
        DeleteOption[] deleteOptions = { StandardDeleteOption.OVERRIDE_READ_ONLY };
        CleaningPathVisitor visitor = new CleaningPathVisitor(null, deleteOptions, new String[0]);
        MockFile file = new MockFile("", "");
        Path path = file.toPath();
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        
        try {
            visitor.visitFile(path, attrs);
            fail("Expected IOException for unsupported file system");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("file operations not available"));
        }
    }

    @Test(timeout = 4000)
    public void visitFile_ShouldThrowException_WhenAttributesAreNull() {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockFile file = new MockFile("valid_dir", "valid_file.txt");
        Path path = file.toPath();
        
        try {
            visitor.visitFile(path, null);
            fail("Expected NullPointerException for null attributes");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void visitFile_ShouldThrowSecurityException_WhenDeletePermissionDenied() throws Exception {
        Future<?> future = executor.submit(() -> {
            CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
            MockFile file = new MockFile("");
            Path path = file.toPath();
            BasicFileAttributes attrs = mock(BasicFileAttributes.class);
            
            try {
                visitor.visitFile(path, attrs);
                fail("Expected SecurityException for delete permission");
            } catch (SecurityException e) {
                assertTrue(e.getMessage().contains("Security manager blocks"));
            } catch (IOException e) {
                fail("Unexpected IOException: " + e.getMessage());
            }
        });
        future.get(4000, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 4000)
    public void preVisitDirectory_ShouldSkipSubtree_WhenDirectoryInSkipList() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skipList = {"skip_this_dir"};
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipList);
        MockFile dir = new MockFile("", "skip_this_dir");
        Path path = dir.toPath();
        BasicFileAttributes attrs = mock(BasicFileAttributes.class);
        
        FileVisitResult result = visitor.preVisitDirectory(path, attrs);
        
        assertEquals("Should skip directory subtree", FileVisitResult.SKIP_SUBTREE, result);
    }

    @Test(timeout = 4000)
    public void equals_ShouldReturnTrue_ForEquivalentInstances() {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skipList = {"dir1", "dir2"};
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(counters, skipList);
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(counters, skipList);
        
        assertTrue("Equivalent instances should be equal", visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void equals_ShouldReturnFalse_ForDifferentDeleteOptions() {
        CountingPathVisitor visitor1 = CleaningPathVisitor.withBigIntegerCounters();
        DeleteOption[] options = {StandardDeleteOption.OVERRIDE_READ_ONLY};
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(
            CountingPathVisitor.defaultPathCounters(), 
            options,
            new String[0]
        );
        
        assertFalse("Different delete options should make instances unequal", visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void equals_ShouldReturnFalse_ForDifferentCounterTypes() {
        Counters.PathCounters counters1 = CountingPathVisitor.defaultPathCounters();
        Counters.PathCounters counters2 = new CountingPathVisitor.Builder().getPathCounters();
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(counters1, new String[0]);
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(counters2, new String[0]);
        
        assertFalse("Different counter types should make instances unequal", visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void equals_ShouldReturnTrue_ForSameInstance() {
        CleaningPathVisitor visitor = new CleaningPathVisitor(
            CountingPathVisitor.defaultPathCounters(),
            new String[0]
        );
        
        assertTrue("Instance should equal itself", visitor.equals(visitor));
    }

    @Test(timeout = 4000)
    public void equals_ShouldReturnFalse_ForDifferentObjectType() {
        CleaningPathVisitor visitor = new CleaningPathVisitor(
            CountingPathVisitor.defaultPathCounters(),
            new String[0]
        );
        
        assertFalse("Should not equal different object type", visitor.equals("string"));
    }

    @Test(timeout = 4000)
    public void constructor_ShouldAcceptNullSkipArray() {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, (String[]) null);
        assertNotNull("Should handle null skip array", visitor);
    }

    @Test(timeout = 4000)
    public void preVisitDirectory_ShouldContinue_ForNullPath() throws IOException {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        FileVisitResult result = visitor.preVisitDirectory(null, mock(BasicFileAttributes.class));
        assertEquals("Null path should continue", FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void withLongCounters_ShouldCreateVisitor() {
        CountingPathVisitor visitor = CleaningPathVisitor.withLongCounters();
        assertNotNull("Factory method should create instance", visitor);
    }

    @Test(timeout = 4000)
    public void hashCode_ShouldComputeConsistentValue() {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skipList = {"dir1", "dir2"};
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(counters, skipList);
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(counters, skipList);
        
        assertEquals("Equal instances should have same hashcode", 
                     visitor1.hashCode(), visitor2.hashCode());
    }
}