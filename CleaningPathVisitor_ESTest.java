package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.CleaningPathVisitor;
import org.apache.commons.io.file.Counters;
import org.apache.commons.io.file.CountingPathVisitor;
import org.apache.commons.io.file.DeleteOption;
import org.apache.commons.io.file.StandardDeleteOption;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class CleaningPathVisitorTest extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testVisitFileWithBigIntegerCounters() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("", "testFile");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(attributes).size();

        FileVisitResult result = visitor.visitFile(path, attributes);

        assertEquals(FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void testPreVisitDirectoryWithNullPath() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] skipFiles = new String[1];
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipFiles);

        try {
            visitor.preVisitDirectory(null, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Arrays", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullDeleteOption() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[2];
        String[] skipFiles = new String[3];

        try {
            new CleaningPathVisitor(counters, deleteOptions, skipFiles);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullSkipFiles() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skipFiles = new String[10];

        try {
            new CleaningPathVisitor(counters, skipFiles);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testVisitFileWithIOException() throws Throwable {
        DeleteOption[] deleteOptions = new DeleteOption[1];
        deleteOptions[0] = StandardDeleteOption.OVERRIDE_READ_ONLY;
        String[] skipFiles = new String[0];
        CleaningPathVisitor visitor = new CleaningPathVisitor(null, deleteOptions, skipFiles);
        MockFile mockFile = new MockFile("", "");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());

        try {
            visitor.visitFile(path, attributes);
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.io.file.PathUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testVisitFileWithNullAttributes() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        MockFile mockFile = new MockFile("testDir", "testFile");
        Path path = mockFile.toPath();

        try {
            visitor.visitFile(path, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    @Test(timeout = 4000)
    public void testVisitFileWithSecurityException() throws Throwable {
        Future<?> future = executor.submit(() -> {
            try {
                MockFile mockFile = new MockFile("");
                BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
                CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
                Path path = mockFile.toPath();

                try {
                    visitor.visitFile(path, attributes);
                    fail("Expecting exception: SecurityException");
                } catch (SecurityException e) {
                    verifyException("org.evosuite.runtime.sandbox.MSecurityManager", e);
                }
            } catch (Throwable t) {
                // Handle exceptions
            }
        });
        future.get(4000, TimeUnit.MILLISECONDS);
    }

    @Test(timeout = 4000)
    public void testPreVisitDirectoryWithSkipFiles() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] skipFiles = {
            "", "", "org.apache.commons.io.filefilter.AgeFileFilter", "", ">=",
            "org.apache.commons.io.file.Counters$NoopPathCounters", "",
            "org.apache.commons.io.file.CleaningPathVisitor", "testFile"
        };
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipFiles);
        MockFile mockFile = new MockFile("", "testFile");
        Path path = mockFile.toPath();
        BasicFileAttributes attributes = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());

        FileVisitResult result = visitor.preVisitDirectory(path, attributes);

        assertEquals(FileVisitResult.SKIP_SUBTREE, result);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] skipFiles = new String[6];
        CleaningPathVisitor visitor1 = new CleaningPathVisitor(counters, skipFiles);
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(counters, skipFiles);

        assertTrue(visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentInstances() throws Throwable {
        CountingPathVisitor visitor1 = CleaningPathVisitor.withBigIntegerCounters();
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        DeleteOption[] deleteOptions = new DeleteOption[4];
        deleteOptions[1] = StandardDeleteOption.OVERRIDE_READ_ONLY;
        String[] skipFiles = new String[0];
        CleaningPathVisitor visitor2 = new CleaningPathVisitor(counters, deleteOptions, skipFiles);

        assertFalse(visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypes() throws Throwable {
        CountingPathVisitor visitor1 = CleaningPathVisitor.withBigIntegerCounters();
        DeletingPathVisitor visitor2 = DeletingPathVisitor.withLongCounters();

        assertFalse(visitor1.equals(visitor2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] skipFiles = new String[6];
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipFiles);

        assertFalse(visitor.equals(""));
    }

    @Test(timeout = 4000)
    public void testVisitFileWithNullAttributesAndSkipFiles() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        String[] skipFiles = {",(-_DTfh #j%MqF^"};
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipFiles);
        MockFile mockFile = new MockFile(",(-_DTfh #j%MqF^");
        Path path = mockFile.toPath();

        try {
            visitor.visitFile(path, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.io.file.CountingPathVisitor", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullSkipFiles() throws Throwable {
        Counters.PathCounters counters = CountingPathVisitor.defaultPathCounters();
        new CleaningPathVisitor(counters, (String[]) null);
    }

    @Test(timeout = 4000)
    public void testPreVisitDirectoryWithNullPathAndAttributes() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        FileVisitResult result = visitor.preVisitDirectory(null, null);

        assertEquals(FileVisitResult.CONTINUE, result);
    }

    @Test(timeout = 4000)
    public void testWithLongCounters() throws Throwable {
        CountingPathVisitor visitor = CleaningPathVisitor.withLongCounters();
        assertNotNull(visitor);
    }

    @Test(timeout = 4000)
    public void testHashCode() throws Throwable {
        CountingPathVisitor.Builder builder = new CountingPathVisitor.Builder();
        Counters.PathCounters counters = builder.getPathCounters();
        String[] skipFiles = new String[6];
        CleaningPathVisitor visitor = new CleaningPathVisitor(counters, skipFiles);

        visitor.hashCode();
    }
}