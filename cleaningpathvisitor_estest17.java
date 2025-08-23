package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.runner.RunWith;

public class CleaningPathVisitor_ESTestTest17 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        CountingPathVisitor countingPathVisitor0 = CleaningPathVisitor.withBigIntegerCounters();
        FileVisitResult fileVisitResult0 = countingPathVisitor0.preVisitDirectory((Path) null, (BasicFileAttributes) null);
        assertEquals(FileVisitResult.CONTINUE, fileVisitResult0);
    }
}
