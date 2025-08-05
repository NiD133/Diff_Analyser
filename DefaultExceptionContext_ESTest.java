package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DefaultExceptionContext_ESTest extends DefaultExceptionContext_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testAddContextValueAndRetrieveFirstValue() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        exceptionContext.addContextValue("key", "value");
        Object retrievedValue = exceptionContext.getFirstContextValue("key");
        assertEquals("value", retrievedValue);
    }

    @Test(timeout = 4000)
    public void testGetContextLabelsAfterAddingValue() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        exceptionContext.addContextValue("key", "value");
        Set<String> contextLabels = exceptionContext.getContextLabels();
        assertFalse(contextLabels.isEmpty());
    }

    @Test(timeout = 4000)
    public void testGetContextEntriesSizeAfterAddingValue() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        exceptionContext.addContextValue("key", "value");
        List<Pair<String, Object>> contextEntries = exceptionContext.getContextEntries();
        assertEquals(1, contextEntries.size());
    }

    @Test(timeout = 4000)
    public void testSetNullContextValueAndGetFormattedMessage() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        exceptionContext.setContextValue(null, null);
        String formattedMessage = exceptionContext.getFormattedExceptionMessage(null);
        assertEquals("Exception Context:\n\t[1:null=null]\n---------------------------------", formattedMessage);
    }

    @Test(timeout = 4000)
    public void testGetFormattedMessageWithBaseMessage() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        String formattedMessage = exceptionContext.getFormattedExceptionMessage("BaseMessage");
        assertEquals("BaseMessage", formattedMessage);
    }

    @Test(timeout = 4000)
    public void testGetFormattedMessageWithNullBaseMessage() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        String formattedMessage = exceptionContext.getFormattedExceptionMessage(null);
        assertEquals("", formattedMessage);
    }

    @Test(timeout = 4000)
    public void testSetContextValueWithListAndExpectStackOverflowError() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        List<Pair<String, Object>> contextEntries = exceptionContext.getContextEntries();
        exceptionContext.setContextValue("key", contextEntries);
        try {
            exceptionContext.getFormattedExceptionMessage("key");
            fail("Expecting exception: StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetContextValuesForNonExistentKey() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        List<Object> contextValues = exceptionContext.getContextValues("nonExistentKey");
        assertFalse(contextValues.contains("nonExistentKey"));
    }

    @Test(timeout = 4000)
    public void testAddContextValueWithSetAndRetrieveValues() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        Set<String> contextLabels = exceptionContext.getContextLabels();
        exceptionContext.addContextValue("key", contextLabels);
        List<Object> contextValues = exceptionContext.getContextValues("key");
        assertEquals(1, contextValues.size());
    }

    @Test(timeout = 4000)
    public void testGetFirstContextValueForNonExistentKey() throws Throwable {
        DefaultExceptionContext exceptionContext = new DefaultExceptionContext();
        Object retrievedValue = exceptionContext.getFirstContextValue("nonExistentKey");
        assertNull(retrievedValue);
    }
}