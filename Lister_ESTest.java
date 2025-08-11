package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.evosuite.runtime.EvoAssertions.verifyException;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import org.apache.commons.compress.archivers.Lister;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class Lister_ESTest extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMainMethodThrowsNoClassDefFoundError() throws Throwable {
        String[] args = new String[3];
        try {
            Lister.main(args);
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsInvalidPathException() throws Throwable {
        String[] args = {"ustar\u0000"};
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: InvalidPathException");
        } catch (InvalidPathException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsFileSystemException() throws Throwable {
        String[] args = {
            "actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it cannot be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile",
            "actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it cannot be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile"
        };
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: FileSystemException");
        } catch (FileSystemException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNullPointerException() throws Throwable {
        String[] args = {"bf,eR=!9:b8u`J"};
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerConstructorThrowsNullPointerException() throws Throwable {
        String[] args = new String[3];
        try {
            new Lister(false, args);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerConstructorThrowsArrayIndexOutOfBoundsException() throws Throwable {
        String[] args = new String[0];
        try {
            new Lister(true, args);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNoClassDefFoundErrorForZip() throws Throwable {
        String[] args = {"sh;T]Ld", "zip"};
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNoClassDefFoundErrorForSevenZ() throws Throwable {
        String[] args = {"bf,eR=!9:b8u`J", "7z"};
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsIOException() throws Throwable {
        String[] args = {"", "not encodeable"};
        Lister lister = new Lister(false, args);
        try {
            lister.go();
            fail("Expecting exception: IOException");
        } catch (IOException e) {
            verifyException("org.apache.commons.compress.archivers.ArchiveStreamFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNoClassDefFoundErrorForIOUtils() throws Throwable {
        Lister lister = new Lister();
        try {
            lister.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.ArchiveStreamFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNoClassDefFoundErrorForTar() throws Throwable {
        String[] args = {"", "tar"};
        Lister lister = new Lister(false, args);
        try {
            lister.go();
            fail("Expecting exception: NoClassDefFoundError");
        } catch (NoClassDefFoundError e) {
            verifyException("org.apache.commons.compress.archivers.tar.TarFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerGoThrowsNoSuchFileException() throws Throwable {
        String[] args = {"A("};
        Lister lister = new Lister(true, args);
        try {
            lister.go();
            fail("Expecting exception: NoSuchFileException");
        } catch (NoSuchFileException e) {
            // Expected exception
        }
    }
}