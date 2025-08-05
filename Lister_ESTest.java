/*
 * Test suite for the Lister class - a command line utility for listing archive contents.
 * Tests various scenarios including error conditions and different archive formats.
 */

package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import org.apache.commons.compress.archivers.Lister;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class ListerTest extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testMainWithNullArguments_ShouldThrowNoClassDefFoundError() throws Throwable {
        // Given: array with null elements
        String[] nullArguments = new String[3];
        
        // When & Then: calling main should throw NoClassDefFoundError due to missing ArrayUtils dependency
        try { 
            Lister.main(nullArguments);
            fail("Expected NoClassDefFoundError for missing ArrayUtils dependency");
        } catch(NoClassDefFoundError e) {
            assertEquals("org/apache/commons/lang3/ArrayUtils", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWithInvalidPath_ShouldThrowInvalidPathException() throws Throwable {
        // Given: invalid path containing null character
        String[] invalidPathArgs = new String[1];
        invalidPathArgs[0] = "ustar\u0000"; // null character makes path invalid
        Lister lister = new Lister(true, invalidPathArgs);
        
        // When & Then: go() should throw InvalidPathException for invalid path
        try { 
            lister.go();
            fail("Expected InvalidPathException for invalid path with null character");
        } catch(InvalidPathException e) {
            // Expected - path with null character is invalid
        }
    }

    @Test(timeout = 4000)
    public void testListerWithVeryLongPath_ShouldThrowFileSystemException() throws Throwable {
        // Given: extremely long path that exceeds filesystem limits
        String veryLongPath = "actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it cannot be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile";
        String[] longPathArgs = new String[5];
        longPathArgs[0] = veryLongPath;
        longPathArgs[1] = veryLongPath;
        Lister lister = new Lister(true, longPathArgs);
        
        // When & Then: go() should throw FileSystemException for path too long
        try { 
            lister.go();
            fail("Expected FileSystemException for path exceeding filesystem limits");
        } catch(FileSystemException e) {
            // Expected - path is too long for filesystem
        }
    }

    @Test(timeout = 4000)
    public void testListerWithRandomString_ShouldThrowNullPointerException() throws Throwable {
        // Given: random string as filename
        String[] randomStringArgs = new String[8];
        randomStringArgs[0] = "bf,eR=!9:b8u`J";
        Lister lister = new Lister(true, randomStringArgs);
        
        // When & Then: go() should throw NullPointerException
        try { 
            lister.go();
            fail("Expected NullPointerException when processing random string as filename");
        } catch(NullPointerException e) {
            assertNull("Expected null message", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerConstructorWithNullFirstArgument_ShouldThrowNullPointerException() throws Throwable {
        // Given: array with null first element
        String[] argsWithNullFirst = new String[3];
        
        // When & Then: constructor should throw NullPointerException for null first argument
        try {
            new Lister(false, argsWithNullFirst);
            fail("Expected NullPointerException for null first argument");
        } catch(NullPointerException e) {
            assertEquals("args[0]", e.getMessage());
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerConstructorWithEmptyArray_ShouldThrowArrayIndexOutOfBoundsException() throws Throwable {
        // Given: empty arguments array
        String[] emptyArgs = new String[0];
        
        // When & Then: constructor should throw ArrayIndexOutOfBoundsException
        try {
            new Lister(true, emptyArgs);
            fail("Expected ArrayIndexOutOfBoundsException for empty arguments array");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("0", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWithZipFormat_ShouldThrowNoClassDefFoundError() throws Throwable {
        // Given: filename and zip format specification
        String[] zipFormatArgs = new String[2];
        zipFormatArgs[0] = "sh;T]Ld";
        zipFormatArgs[1] = "zip";
        Lister lister = new Lister(true, zipFormatArgs);
        
        // When & Then: go() should throw NoClassDefFoundError for missing ZipFile dependency
        try { 
            lister.go();
            fail("Expected NoClassDefFoundError for missing ZipFile dependency");
        } catch(NoClassDefFoundError e) {
            assertEquals("org/apache/commons/compress/archivers/zip/ZipFile$StoredStatisticsStream", 
                        e.getMessage());
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWith7zFormat_ShouldThrowNoClassDefFoundError() throws Throwable {
        // Given: filename and 7z format specification
        String[] sevenZipArgs = new String[8];
        sevenZipArgs[0] = "bf,eR=!9:b8u`J";
        sevenZipArgs[1] = "7z";
        Lister lister = new Lister(true, sevenZipArgs);
        
        // When & Then: go() should throw NoClassDefFoundError for missing SevenZFile dependency
        try { 
            lister.go();
            fail("Expected NoClassDefFoundError for missing SevenZFile dependency");
        } catch(NoClassDefFoundError e) {
            assertEquals("org/apache/commons/compress/archivers/sevenz/SevenZFile", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.Lister", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWithUnsupportedArchiveFormat_ShouldThrowIOException() throws Throwable {
        // Given: empty filename and unsupported archive format
        String[] unsupportedFormatArgs = new String[9];
        unsupportedFormatArgs[0] = "";
        unsupportedFormatArgs[1] = "not encodeable";
        Lister lister = new Lister(false, unsupportedFormatArgs);
        
        // When & Then: go() should throw IOException for unsupported format
        try { 
            lister.go();
            fail("Expected IOException for unsupported archive format");
        } catch(IOException e) {
            assertEquals("Archiver: not encodeable not found.", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.ArchiveStreamFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testDefaultListerConstructor_ShouldThrowNoClassDefFoundError() throws Throwable {
        // Given: default constructor (deprecated)
        Lister lister = new Lister();
        
        // When & Then: go() should throw NoClassDefFoundError for missing IOUtils dependency
        try { 
            lister.go();
            fail("Expected NoClassDefFoundError for missing IOUtils dependency");
        } catch(NoClassDefFoundError e) {
            assertEquals("org/apache/commons/compress/utils/IOUtils", e.getMessage());
            verifyException("org.apache.commons.compress.archivers.ArchiveStreamFactory", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWithTarFormat_ShouldThrowNoClassDefFoundError() throws Throwable {
        // Given: empty filename and tar format
        String[] tarFormatArgs = new String[2];
        tarFormatArgs[0] = "";
        tarFormatArgs[1] = "tar";
        Lister lister = new Lister(false, tarFormatArgs);
        
        // When & Then: go() should throw NoClassDefFoundError for missing ZipEncodingHelper
        try { 
            lister.go();
            fail("Expected NoClassDefFoundError for missing ZipEncodingHelper dependency");
        } catch(NoClassDefFoundError e) {
            assertEquals("Could not initialize class org.apache.commons.compress.archivers.zip.ZipEncodingHelper", 
                        e.getMessage());
            verifyException("org.apache.commons.compress.archivers.tar.TarFile", e);
        }
    }

    @Test(timeout = 4000)
    public void testListerWithNonExistentFile_ShouldThrowNoSuchFileException() throws Throwable {
        // Given: non-existent filename
        String[] nonExistentFileArgs = new String[1];
        nonExistentFileArgs[0] = "A(";
        Lister lister = new Lister(true, nonExistentFileArgs);
        
        // When & Then: go() should throw NoSuchFileException for non-existent file
        try { 
            lister.go();
            fail("Expected NoSuchFileException for non-existent file");
        } catch(NoSuchFileException e) {
            // Expected - file does not exist
        }
    }
}