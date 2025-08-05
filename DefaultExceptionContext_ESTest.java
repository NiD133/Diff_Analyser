package org.apache.commons.lang3.exception;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class DefaultExceptionContext_ESTest extends DefaultExceptionContext_ESTest_scaffolding {

    // Constants for context labels/values used across tests
    private static final String TEST_LABEL = "\t[";
    private static final String TEST_VALUE = "\t[";
    private static final String NON_EXISTENT_LABEL = "nonExistentLabel";

    @Test(timeout = 4000)
    public void addContextValueThenGetFirstValue_returnsSameValue() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        context.addContextValue(TEST_LABEL, TEST_VALUE);
        
        Object result = context.getFirstContextValue(TEST_LABEL);
        assertEquals("First context value should match added value", TEST_VALUE, result);
    }

    @Test(timeout = 4000)
    public void addContextValueThenGetLabels_returnsNonEmptySet() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        context.addContextValue(TEST_LABEL, TEST_VALUE);
        
        Set<String> labels = context.getContextLabels();
        assertFalse("Context labels should not be empty after adding value", labels.isEmpty());
    }

    @Test(timeout = 4000)
    public void addContextValueThenGetEntries_returnsSizeOne() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        context.addContextValue(TEST_LABEL, TEST_VALUE);
        
        List<Pair<String, Object>> entries = context.getContextEntries();
        assertEquals("Context entries should have size 1", 1, entries.size());
    }

    @Test(timeout = 4000)
    public void setNullContextValueThenFormatMessage_returnsExpectedFormat() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        context.setContextValue(null, null);
        
        String result = context.getFormattedExceptionMessage(null);
        assertEquals("Formatted message should match expected structure",
                     "Exception Context:\n\t[1:null=null]\n---------------------------------", 
                     result);
    }

    @Test(timeout = 4000)
    public void getFormattedMessageWithoutContext_returnsBaseMessage() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        String baseMessage = "Test message";
        
        String result = context.getFormattedExceptionMessage(baseMessage);
        assertEquals("Should return base message unchanged when no context exists",
                     baseMessage, result);
    }

    @Test(timeout = 4000)
    public void getFormattedMessageWithNullBase_returnsEmptyString() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        
        String result = context.getFormattedExceptionMessage(null);
        assertTrue("Should return empty string for null base message", 
                   result.isEmpty());
    }

    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void setRecursiveContextValueThenFormatMessage_throwsStackOverflow() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        List<Pair<String, Object>> entries = context.getContextEntries();
        
        // Create recursive structure by storing context's own entries
        context.setContextValue(",yx", entries);
        
        // Formatting triggers infinite recursion
        context.getFormattedExceptionMessage(",yx");
    }

    @Test(timeout = 4000)
    public void getContextValuesForNonExistentLabel_returnsEmptyList() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        
        List<Object> values = context.getContextValues(NON_EXISTENT_LABEL);
        assertTrue("Should return empty list for non-existent label", 
                   values.isEmpty());
    }

    @Test(timeout = 4000)
    public void addContextValueThenGetValues_returnsSizeOne() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        context.addContextValue("=", "value");
        
        List<Object> values = context.getContextValues("=");
        assertEquals("Context values should have size 1 after addition", 1, values.size());
    }

    @Test(timeout = 4000)
    public void getFirstContextValueForNonExistentLabel_returnsNull() {
        DefaultExceptionContext context = new DefaultExceptionContext();
        
        Object value = context.getFirstContextValue(NON_EXISTENT_LABEL);
        assertNull("Should return null for non-existent label", value);
    }
}