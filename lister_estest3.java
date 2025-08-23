package org.apache.commons.compress.archivers;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class Lister_ESTestTest3 extends Lister_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        String[] stringArray0 = new String[5];
        stringArray0[0] = "actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it cannot be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile";
        stringArray0[1] = "actual and claimed size don't match while reading a stored entry using data descriptor. Either the archive is broken or it cannot be read using ZipArchiveInputStream and you must use ZipFile. A common cause for this is a ZIP archive containing a ZIP archive. See https://commons.apache.org/proper/commons-compress/zip.html#ZipArchiveInputStream_vs_ZipFile";
        Lister lister0 = new Lister(true, stringArray0);
        try {
            lister0.go();
            fail("Expecting exception: FileSystemException");
        } catch (FileSystemException e) {
        }
    }
}
