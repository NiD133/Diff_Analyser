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

public class AccumulatorPathVisitor_ESTestTest8 extends AccumulatorPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        HiddenFileFilter hiddenFileFilter0 = (HiddenFileFilter) HiddenFileFilter.HIDDEN;
        AccumulatorPathVisitor accumulatorPathVisitor0 = AccumulatorPathVisitor.withBigIntegerCounters((PathFilter) hiddenFileFilter0, (PathFilter) hiddenFileFilter0);
        MockFile mockFile0 = new MockFile("org.apache.commons.io.file.AccumulatorPathVisitor$Builder", "9|ng{v.vnRUd<'l0fW");
        Path path0 = mockFile0.toPath();
        BasicFileAttributes basicFileAttributes0 = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
        doReturn(0L).when(basicFileAttributes0).size();
        accumulatorPathVisitor0.updateFileCounters(path0, basicFileAttributes0);
        MockFile mockFile1 = new MockFile("9|ng{v.vnRUd<'l0fW");
        Path path1 = mockFile1.toPath();
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        // Undeclared exception!
        try {
            accumulatorPathVisitor0.relativizeFiles(path1, true, comparator0);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
