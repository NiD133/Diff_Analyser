package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.function.IOBiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.runner.RunWith;

public class AccumulatorPathVisitor_ESTestTest13 extends AccumulatorPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        AccumulatorPathVisitor accumulatorPathVisitor0 = new AccumulatorPathVisitor();
        MockFile mockFile0 = new MockFile("", "");
        Path path0 = mockFile0.toPath();
        BasicFileAttributes basicFileAttributes0 = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(basicFileAttributes0).size();
        accumulatorPathVisitor0.visitFile(path0, basicFileAttributes0);
        AccumulatorPathVisitor accumulatorPathVisitor1 = new AccumulatorPathVisitor();
        boolean boolean0 = accumulatorPathVisitor1.equals(accumulatorPathVisitor0);
        assertFalse(boolean0);
    }
}
