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

public class AccumulatorPathVisitor_ESTestTest4 extends AccumulatorPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        Pattern pattern0 = Pattern.compile(">", (-1));
        RegexFileFilter regexFileFilter0 = new RegexFileFilter(pattern0);
        AccumulatorPathVisitor accumulatorPathVisitor0 = AccumulatorPathVisitor.withLongCounters((PathFilter) null, (PathFilter) regexFileFilter0);
        MockFile mockFile0 = new MockFile("org.apache.commons.io.file.AccumulatorPathVisitor$1");
        Path path0 = mockFile0.toPath();
        Comparator<Object> comparator0 = (Comparator<Object>) mock(Comparator.class, new ViolatedAssumptionAnswer());
        List<Path> list0 = accumulatorPathVisitor0.relativizeDirectories(path0, false, comparator0);
        assertFalse(list0.contains(path0));
    }
}
