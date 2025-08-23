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

public class CleaningPathVisitor_ESTestTest7 extends CleaningPathVisitor_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        Future<?> future = executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    MockFile mockFile0 = new MockFile("");
                    BasicFileAttributes basicFileAttributes0 = mock(BasicFileAttributes.class, new ViolatedAssumptionAnswer());
                    CountingPathVisitor countingPathVisitor0 = CleaningPathVisitor.withBigIntegerCounters();
                    Path path0 = mockFile0.toPath();
                    // Undeclared exception!
                    try {
                        countingPathVisitor0.visitFile(path0, basicFileAttributes0);
                        fail("Expecting exception: SecurityException");
                    } catch (SecurityException e) {
                        //
                        // Security manager blocks (\"java.io.FilePermission\" \"\" \"delete\")
                        // java.lang.Thread.getStackTrace(Thread.java:1564)
                        // org.evosuite.runtime.sandbox.MSecurityManager.checkPermission(MSecurityManager.java:424)
                        // java.lang.SecurityManager.checkDelete(SecurityManager.java:1007)
                        // sun.nio.fs.UnixPath.checkDelete(UnixPath.java:807)
                        // sun.nio.fs.UnixFileSystemProvider.implDelete(UnixFileSystemProvider.java:222)
                        // sun.nio.fs.AbstractFileSystemProvider.deleteIfExists(AbstractFileSystemProvider.java:108)
                        // java.nio.file.Files.deleteIfExists(Files.java:1165)
                        // org.apache.commons.io.file.CleaningPathVisitor.visitFile(CleaningPathVisitor.java:132)
                        // sun.reflect.GeneratedMethodAccessor119.invoke(Unknown Source)
                        // sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
                        // java.lang.reflect.Method.invoke(Method.java:498)
                        // org.evosuite.testcase.statements.MethodStatement$1.execute(MethodStatement.java:256)
                        // org.evosuite.testcase.statements.AbstractStatement.exceptionHandler(AbstractStatement.java:165)
                        // org.evosuite.testcase.statements.MethodStatement.execute(MethodStatement.java:219)
                        // org.evosuite.testcase.execution.TestRunnable.executeStatements(TestRunnable.java:286)
                        // org.evosuite.testcase.execution.TestRunnable.call(TestRunnable.java:192)
                        // org.evosuite.testcase.execution.TestRunnable.call(TestRunnable.java:49)
                        // java.util.concurrent.FutureTask.run(FutureTask.java:266)
                        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
                        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
                        // java.lang.Thread.run(Thread.java:750)
                        //
                        verifyException("org.evosuite.runtime.sandbox.MSecurityManager", e);
                    }
                } catch (Throwable t) {
                    // Need to catch declared exceptions
                }
            }
        });
        future.get(4000, TimeUnit.MILLISECONDS);
    }
}
