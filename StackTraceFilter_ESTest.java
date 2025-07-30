package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StackTraceFilter_ESTest extends StackTraceFilter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testFindSourceFileWithEmptyStackTrace() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = new StackTraceElement[0];
        String result = stackTraceFilter.findSourceFile(stackTraceElements, "");
        assertEquals("", result);
    }

    @Test(timeout = 4000)
    public void testFilterFirstWithEmptyMethodName() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = { new StackTraceElement("", "", "", 0) };
        MockThrowable mockThrowable = new MockThrowable();
        mockThrowable.setStackTrace(stackTraceElements);
        StackTraceElement result = stackTraceFilter.filterFirst(mockThrowable, false);
        assertEquals("", result.getMethodName());
    }

    @Test(timeout = 4000)
    public void testFilterFirstWithNonNativeMethod() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        MockThrowable mockThrowable = new MockThrowable();
        StackTraceElement stackTraceElement = new StackTraceElement("A", "methodName", "fileName", 6);
        StackTraceElement[] stackTraceElements = { stackTraceElement, stackTraceElement, stackTraceElement, stackTraceElement, stackTraceElement, stackTraceElement, stackTraceElement, stackTraceElement };
        mockThrowable.setStackTrace(stackTraceElements);
        StackTraceElement result = stackTraceFilter.filterFirst(mockThrowable, true);
        assertFalse(result.isNativeMethod());
    }

    @Test(timeout = 4000)
    public void testFilterFirstWithNullOriginThrowsException() throws Throwable {
        MockThrowable mockThrowable = new MockThrowable();
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        mockThrowable.setOriginForDelegate(null);
        try {
            stackTraceFilter.filterFirst(mockThrowable, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner", e);
        }
    }

    @Test(timeout = 4000)
    public void testFilterWithNullElementThrowsException() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = new StackTraceElement[1];
        try {
            stackTraceFilter.filter(stackTraceElements, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner", e);
        }
    }

    @Test(timeout = 4000)
    public void testFindSourceFileWithNonNullStackTrace() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        MockThrowable mockThrowable = new MockThrowable();
        StackTraceElement stackTraceElement = stackTraceFilter.filterFirst(mockThrowable, false);
        StackTraceElement[] stackTraceElements = { stackTraceElement };
        String result = stackTraceFilter.findSourceFile(stackTraceElements, "org.mockito.internal.configuration.plugins.DefaultMockitoPlugins");
        // Note: Unstable assertion removed
    }

    @Test(timeout = 4000)
    public void testFindSourceFileWithNullValues() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = { new StackTraceElement(null, null, null, 2) };
        String result = stackTraceFilter.findSourceFile(stackTraceElements, null);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testFindSourceFileWithNullElementThrowsException() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = new StackTraceElement[1];
        try {
            stackTraceFilter.findSourceFile(stackTraceElements, "org.mockito.internal.configuration.plugins.DefaultMockitoPlugins");
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.exceptions.stacktrace.DefaultStackTraceCleaner", e);
        }
    }

    @Test(timeout = 4000)
    public void testFilterWithEmptyStackTrace() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = new StackTraceElement[0];
        StackTraceElement[] result = stackTraceFilter.filter(stackTraceElements, false);
        stackTraceFilter.findSourceFile(result, "S-pOJA?<SA5#B9J&^;");
        assertNotSame(result, stackTraceElements);
    }

    @Test(timeout = 4000)
    public void testFilterFirstWithOriginSet() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        MockThrowable mockThrowable = new MockThrowable();
        StackTraceElement stackTraceElement = new StackTraceElement("/34KVo", "org.mockito.internal.configuration.plugins.DefaultPluginSwitch", "/34KVo", 1);
        mockThrowable.setOriginForDelegate(stackTraceElement);
        StackTraceElement result = stackTraceFilter.filterFirst(mockThrowable, true);
        assertEquals("<evosuite>", result.getFileName());
    }

    @Test(timeout = 4000)
    public void testFilterFirstWithEmptyStackTrace() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement[] stackTraceElements = new StackTraceElement[0];
        MockThrowable mockThrowable = new MockThrowable();
        mockThrowable.setStackTrace(stackTraceElements);
        StackTraceElement result = stackTraceFilter.filterFirst(mockThrowable, false);
        assertNull(result);
    }

    @Test(timeout = 4000)
    public void testFilterWithNegativeLineNumber() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        StackTraceElement stackTraceElement = new StackTraceElement("org.mockito.internal.PremainAttach", "methodName", "fileName", -1512);
        StackTraceElement[] stackTraceElements = { stackTraceElement };
        StackTraceElement[] result = stackTraceFilter.filter(stackTraceElements, false);
        assertEquals(0, result.length);
    }

    @Test(timeout = 4000)
    public void testFilterWithMockThrowable() throws Throwable {
        StackTraceFilter stackTraceFilter = new StackTraceFilter();
        MockThrowable mockThrowable = new MockThrowable();
        StackTraceElement stackTraceElement = stackTraceFilter.filterFirst(mockThrowable, false);
        StackTraceElement[] stackTraceElements = { stackTraceElement };
        StackTraceElement[] result = stackTraceFilter.filter(stackTraceElements, false);
        // Note: Unstable assertions removed
    }
}