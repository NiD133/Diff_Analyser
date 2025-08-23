package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.UnaryOperator;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.PathEqualsFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

public class CountingPathVisitor_ESTestTest13 extends CountingPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        CountingPathVisitor.Builder countingPathVisitor_Builder0 = new CountingPathVisitor.Builder();
        Counters.PathCounters counters_PathCounters0 = countingPathVisitor_Builder0.getPathCounters();
        String[] stringArray0 = new String[3];
        stringArray0[0] = "hCUJ&KIcnhn";
        stringArray0[1] = "*Qus;#HzV)I)";
        stringArray0[2] = "&V8coOO1NH$";
        DeletingPathVisitor deletingPathVisitor0 = new DeletingPathVisitor(counters_PathCounters0, stringArray0);
        File file0 = MockFile.createTempFile("-i8e'2;3AOg#yY!", "fileFilter");
        Path path0 = file0.toPath();
        MockIOException mockIOException0 = new MockIOException();
        try {
            deletingPathVisitor0.postVisitDirectory(path0, (IOException) mockIOException0);
            fail("Expecting exception: NoSuchFileException");
        } catch (NoSuchFileException e) {
        }
    }
}
