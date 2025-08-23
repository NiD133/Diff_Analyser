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

public class CountingPathVisitor_ESTestTest12 extends CountingPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test11() throws Throwable {
        MockFile mockFile0 = new MockFile("");
        Path path0 = mockFile0.toPath();
        PathEqualsFileFilter pathEqualsFileFilter0 = new PathEqualsFileFilter(path0);
        NotFileFilter notFileFilter0 = (NotFileFilter) HiddenFileFilter.VISIBLE;
        IOFileFilter iOFileFilter0 = pathEqualsFileFilter0.or(notFileFilter0);
        AccumulatorPathVisitor accumulatorPathVisitor0 = AccumulatorPathVisitor.withLongCounters((PathFilter) iOFileFilter0, (PathFilter) notFileFilter0);
        BasicFileAttributes basicFileAttributes0 = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        // Undeclared exception!
        try {
            accumulatorPathVisitor0.preVisitDirectory(path0, basicFileAttributes0);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }
}
