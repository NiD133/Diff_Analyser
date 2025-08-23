package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

public class ArArchiveOutputStream_ESTestTest14 extends ArArchiveOutputStream_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream0 = new ByteArrayOutputStream();
        ArArchiveOutputStream arArchiveOutputStream0 = new ArArchiveOutputStream(byteArrayOutputStream0);
        MockFile mockFile0 = new MockFile("org.apache.commons.compress.archivers.zip.JarMarker", "org.apache.commons.compress.archivers.zip.JarMarker");
        Path path0 = mockFile0.toPath();
        LinkOption[] linkOptionArray0 = new LinkOption[0];
        try {
            arArchiveOutputStream0.createArchiveEntry(path0, "org.apache.commons.compress.archivers.zip.JarMarker", linkOptionArray0);
            fail("Expecting exception: NoSuchFileException");
        } catch (NoSuchFileException e) {
        }
    }
}
