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
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class CleaningPathVisitor_ESTest extends CleaningPathVisitor_ESTest_scaffolding {

    // Factory method tests
    
    @Test(timeout = 4000)
    public void testWithBigIntegerCounters_VisitFile_ReturnsCorrectResult() throws Throwable {
        // Given: A CleaningPathVisitor created with BigInteger counters
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        
        // And: A mock file with basic attributes
        MockFile mockFile = new MockFile("", "testFile.txt");
        Path filePath = mockFile.toPath();
        BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(fileAttributes).size();
        
        // When: Visiting the file
        FileVisitResult result = visitor.visitFile(filePath, fileAttributes);
        
        // Then: Should continue processing
        assertEquals(FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void testWithLongCounters_CreatesVisitorSuccessfully() throws Throwable {
        // When: Creating a CleaningPathVisitor with long counters
        CountingPathVisitor visitor = CleaningPathVisitor.withLongCounters();
        
        // Then: Should create successfully
        assertNotNull(visitor);
    }

    // Constructor tests with null parameters
    
    @Test(timeout = 4000)
    public void testConstructor_WithNullPath_ThrowsNullPointerException() throws Throwable {
        // Given: A visitor with skip patterns
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = new String[1];
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // When/Then: Calling preVisitDirectory with null should throw NPE
        try { 
            visitor.preVisitDirectory(null, null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
            verifyException("java.util.Arrays", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullDeleteOptions_ThrowsNullPointerException() throws Throwable {
        // Given: Path counters and arrays with null elements
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[2]; // Contains nulls
        String[] skipPatterns = new String[3];
        
        // When/Then: Constructor should throw NPE due to null delete options
        try {
            new CleaningPathVisitor(pathCounters, deleteOptions, skipPatterns);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullSkipPatterns_ThrowsNullPointerException() throws Throwable {
        // Given: Path counters and skip patterns with null elements
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        String[] skipPatterns = new String[10]; // Contains nulls
        
        // When/Then: Constructor should throw NPE due to null skip patterns
        try {
            new CleaningPathVisitor(pathCounters, skipPatterns);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullSkipArray_AcceptsNull() throws Throwable {
        // Given: Path counters and null skip array
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        
        // When: Creating visitor with null skip array
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, (String[]) null);
        
        // Then: Should create successfully (no exception)
        assertNotNull(visitor);
    }

    // File operation tests
    
    @Test(timeout = 4000)
    public void testVisitFile_WithOverrideReadOnly_ThrowsIOException() throws Throwable {
        // Given: A visitor configured to override read-only files
        DeleteOption[] deleteOptions = new DeleteOption[1];
        deleteOptions[0] = StandardDeleteOption.OVERRIDE_READ_ONLY;
        String[] skipPatterns = new String[0];
        CleaningPathVisitor visitor = new CleaningPathVisitor(null, deleteOptions, skipPatterns);
        
        // And: A mock file
        MockFile mockFile = new MockFile("", "");
        Path filePath = mockFile.toPath();
        BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        
        // When/Then: Visiting file should throw IOException
        try { 
            visitor.visitFile(filePath, fileAttributes);
            fail("Expected IOException");
        } catch(IOException e) {
            verifyException("org.apache.commons.io.file.PathUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testVisitFile_WithNullAttributes_ThrowsNullPointerException() throws Throwable {
        // Given: A visitor with BigInteger counters
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("testDir", "testFile");
        Path filePath = mockFile.toPath();
        
        // When/Then: Visiting file with null attributes should throw NPE
        try { 
            visitor.visitFile(filePath, null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    @Test(timeout = 4000)
    public void testVisitFile_WithSecurityRestrictions_ThrowsSecurityException() throws Throwable {
        // This test runs in a separate thread due to security manager restrictions
        Future<?> future = executor.submit(() -> { 
            try {
                // Given: A visitor and a file that cannot be deleted due to security restrictions
                MockFile mockFile = new MockFile("");
                BasicFileAttributes fileAttributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
                CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
                Path filePath = mockFile.toPath();
                
                // When/Then: Attempting to visit/delete should throw SecurityException
                try { 
                    visitor.visitFile(filePath, fileAttributes);
                    fail("Expected SecurityException");
                } catch(SecurityException e) {
                    verifyException("org.evosuite.runtime.sandbox.MSecurityManager", e);
                }
            } catch(Throwable t) {
                // Handle any other exceptions
            }
        });
        future.get(4000, TimeUnit.MILLISECONDS);
    }

    // Directory traversal tests
    
    @Test(timeout = 4000)
    public void testPreVisitDirectory_WithSkipPatterns_SkipsMatchingDirectory() throws Throwable {
        // Given: A visitor configured to skip certain directories
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = {
            "", "", "org.apache.commons.io.filefilter.AgeFileFilter", "", ">=",
            "org.apache.commons.io.file.Counters$NoopPathCounters", "",
            "org.apache.commons.io.file.CleaningPathVisitor", "egaoxgVGi-xQc=}|"
        };
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // And: A directory that matches a skip pattern
        MockFile mockDir = new MockFile("", "egaoxgVGi-xQc=}|");
        Path dirPath = mockDir.toPath();
        BasicFileAttributes dirAttributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        
        // When: Pre-visiting the directory
        FileVisitResult result = visitor.preVisitDirectory(dirPath, dirAttributes);
        
        // Then: Should skip the subtree
        assertEquals(FileVisitResult.SKIP_SUBTREE, result);
    }

    @Test(timeout = 4000)
    public void testPreVisitDirectory_WithNullParameters_ReturnsContinue() throws Throwable {
        // Given: A visitor with BigInteger counters
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        
        // When: Pre-visiting directory with null parameters
        FileVisitResult result = visitor.preVisitDirectory(null, null);
        
        // Then: Should continue processing
        assertEquals(FileVisitResult.CONTINUE, result);
    }

    // Equality and hash code tests
    
    @Test(timeout = 4000)
    public void testEquals_SameConfiguration_ReturnsTrue() throws Throwable {
        // Given: Two visitors with identical configuration
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = {"", "", "", "", "", ""};
        
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(pathCounters, skipPatterns);
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // When/Then: Should be equal
        assertTrue(visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void testEquals_SameInstance_ReturnsTrue() throws Throwable {
        // Given: A single visitor instance
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = new String[0];
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // When/Then: Should equal itself
        assertTrue(visitor.equals(visitor));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentConfiguration_ReturnsFalse() throws Throwable {
        // Given: Two visitors with different configurations
        CountingPathVisitor visitor1 = CleaningPathVisitor.withBigIntegerCounters();
        
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[4];
        deleteOptions[1] = StandardDeleteOption.OVERRIDE_READ_ONLY;
        String[] skipPatterns = new String[0];
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(pathCounters, deleteOptions, skipPatterns);
        
        // When/Then: Should not be equal
        assertFalse(visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentClass_ReturnsFalse() throws Throwable {
        // Given: A CleaningPathVisitor and a DeletingPathVisitor
        CountingPathVisitor cleaningVisitor = CleaningPathVisitor.withBigIntegerCounters();
        DeletingPathVisitor deletingVisitor = DeletingPathVisitor.withLongCounters();
        
        // When/Then: Should not be equal
        assertFalse(cleaningVisitor.equals(deletingVisitor));
    }

    @Test(timeout = 4000)
    public void testEquals_DifferentCounterTypes_ReturnsFalse() throws Throwable {
        // Given: Visitors with different counter types
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = {"", "", "", "", "", ""};
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        CountingPathVisitor visitor2 = CleaningPathVisitor.withBigIntegerCounters();
        
        // When/Then: Should not be equal
        assertFalse(visitor1.equals(visitor2));
        assertFalse(visitor2.equals(visitor1));
    }

    @Test(timeout = 4000)
    public void testEquals_WithString_ReturnsFalse() throws Throwable {
        // Given: A visitor and a string
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = {"", "", "", "", "", ""};
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // When/Then: Should not equal a string
        assertFalse(visitor.equals(""));
    }

    @Test(timeout = 4000)
    public void testVisitFile_WithMatchingSkipPattern_HandlesCorrectly() throws Throwable {
        // Given: A visitor configured to skip specific files
        Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        String[] skipPatterns = {",(-_DTfh #j%MqF^"};
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // And: A file matching the skip pattern
        MockFile mockFile = new MockFile(",(-_DTfh #j%MqF^");
        Path filePath = mockFile.toPath();
        
        // When/Then: Visiting with null attributes should throw NPE
        try { 
            visitor.visitFile(filePath, null);
            fail("Expected NullPointerException");
        } catch(NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCode_ExecutesWithoutError() throws Throwable {
        // Given: A visitor with configuration
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters pathCounters = builder.getPathCounters();
        String[] skipPatterns = {"", "", "", "", "", ""};
        CleaningPathVisitor visitor = new CleaningPathVisitor(pathCounters, skipPatterns);
        
        // When: Computing hash code
        int hashCode = visitor.hashCode();
        
        // Then: Should complete without error (hash code can be any value)
        // No assertion needed - just ensuring no exception is thrown
    }
}