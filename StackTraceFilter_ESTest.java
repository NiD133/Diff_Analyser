package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.mock.java.lang.MockThrowable;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;

/**
 * Tests for StackTraceFilter functionality including filtering stack traces,
 * finding first non-filtered elements, and locating source files.
 */
public class StackTraceFilterTest {

    private static final String MOCKITO_INTERNAL_CLASS = "org.mockito.internal.PremainAttach";
    private static final String TEST_CLASS_NAME = "TestClass";
    private static final String TEST_METHOD_NAME = "testMethod";
    private static final String TEST_FILE_NAME = "TestClass.java";
    private static final int TEST_LINE_NUMBER = 42;

    @Test
    public void findSourceFile_WithEmptyStackTrace_ReturnsDefaultValue() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];
        String defaultValue = "default.java";

        // When
        String result = filter.findSourceFile(emptyStackTrace, defaultValue);

        // Then
        assertEquals("Should return default value for empty stack trace", defaultValue, result);
    }

    @Test
    public void filterFirst_WithSingleStackTraceElement_ReturnsElement() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        MockThrowable throwable = createThrowableWithSingleElement();

        // When
        StackTraceElement result = filter.filterFirst(throwable, false);

        // Then
        assertNotNull("Should return a stack trace element", result);
        assertEquals("Should preserve method name", "", result.getMethodName());
    }

    @Test
    public void filterFirst_WithMultipleIdenticalElements_ReturnsFirstElement() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        MockThrowable throwable = createThrowableWithMultipleIdenticalElements();

        // When
        StackTraceElement result = filter.filterFirst(throwable, true);

        // Then
        assertNotNull("Should return a stack trace element", result);
        assertFalse("Should not be a native method", result.isNativeMethod());
    }

    @Test(expected = NullPointerException.class)
    public void filterFirst_WithNullStackTrace_ThrowsNullPointerException() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        MockThrowable throwable = new MockThrowable();
        throwable.setOriginForDelegate(null);

        // When
        filter.filterFirst(throwable, true);

        // Then - exception expected
    }

    @Test(expected = NullPointerException.class)
    public void filter_WithNullElementInArray_ThrowsNullPointerException() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] stackTraceWithNull = new StackTraceElement[1]; // contains null

        // When
        filter.filter(stackTraceWithNull, false);

        // Then - exception expected
    }

    @Test
    public void findSourceFile_WithNullElements_ReturnsNull() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] stackTrace = createStackTraceWithNullValues();

        // When
        String result = filter.findSourceFile(stackTrace, null);

        // Then
        assertNull("Should return null when elements have null values", result);
    }

    @Test(expected = NullPointerException.class)
    public void findSourceFile_WithNullElementInArray_ThrowsNullPointerException() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] stackTraceWithNull = new StackTraceElement[1]; // contains null

        // When
        filter.findSourceFile(stackTraceWithNull, "default");

        // Then - exception expected
    }

    @Test
    public void filter_WithEmptyStackTrace_ReturnsNewEmptyArray() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // When
        StackTraceElement[] result = filter.filter(emptyStackTrace, false);

        // Then
        assertNotNull("Should return non-null array", result);
        assertEquals("Should return empty array", 0, result.length);
        assertNotSame("Should return new array instance", emptyStackTrace, result);
    }

    @Test
    public void filterFirst_WithCustomStackTraceElement_FiltersCorrectly() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        MockThrowable throwable = createThrowableWithCustomElement();

        // When
        StackTraceElement result = filter.filterFirst(throwable, true);

        // Then
        assertNotNull("Should return filtered element", result);
        assertEquals("Should have expected file name", "<evosuite>", result.getFileName());
    }

    @Test
    public void filterFirst_WithEmptyThrowableStackTrace_ReturnsNull() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        MockThrowable throwable = new MockThrowable();
        throwable.setStackTrace(new StackTraceElement[0]);

        // When
        StackTraceElement result = filter.filterFirst(throwable, false);

        // Then
        assertNull("Should return null for empty stack trace", result);
    }

    @Test
    public void filter_WithMockitoInternalClass_FiltersOutElement() {
        // Given
        StackTraceFilter filter = new StackTraceFilter();
        StackTraceElement[] stackTrace = createStackTraceWithMockitoInternalClass();

        // When
        StackTraceElement[] result = filter.filter(stackTrace, false);

        // Then
        assertEquals("Should filter out Mockito internal classes", 0, result.length);
    }

    // Helper methods for creating test data

    private MockThrowable createThrowableWithSingleElement() {
        MockThrowable throwable = new MockThrowable();
        StackTraceElement[] stackTrace = new StackTraceElement[1];
        stackTrace[0] = new StackTraceElement("", "", "", 0);
        throwable.setStackTrace(stackTrace);
        return throwable;
    }

    private MockThrowable createThrowableWithMultipleIdenticalElements() {
        MockThrowable throwable = new MockThrowable();
        StackTraceElement[] stackTrace = new StackTraceElement[8];
        StackTraceElement element = new StackTraceElement("TestClass", "testMethod", "TestClass.java", 6);
        
        // Fill array with identical elements
        for (int i = 0; i < stackTrace.length; i++) {
            stackTrace[i] = element;
        }
        
        throwable.setStackTrace(stackTrace);
        return throwable;
    }

    private StackTraceElement[] createStackTraceWithNullValues() {
        StackTraceElement[] stackTrace = new StackTraceElement[1];
        stackTrace[0] = new StackTraceElement(
            null, null, null, 
            "org.mockito.internal.exceptions.stacktrace.StackTraceFilter",
            "org.mockito.internal.exceptions.stacktrace.StackTraceFilter",
            "org.mockito.internal.exceptions.stacktrace.StackTraceFilter",
            2
        );
        return stackTrace;
    }

    private MockThrowable createThrowableWithCustomElement() {
        MockThrowable throwable = new MockThrowable();
        StackTraceElement customElement = new StackTraceElement(
            "/34KVo",
            "org.mockito.internal.configuration.plugins.DefaultPluginSwitch",
            "/34KVo",
            "org.mockito.internal.configuration.plugins.DefaultPluginSwitch",
            "/34KVo",
            "org.mockito.internal.configuration.plugins.DefaultPluginSwitch",
            1
        );
        throwable.setOriginForDelegate(customElement);
        return throwable;
    }

    private StackTraceElement[] createStackTraceWithMockitoInternalClass() {
        StackTraceElement[] stackTrace = new StackTraceElement[1];
        stackTrace[0] = new StackTraceElement(
            MOCKITO_INTERNAL_CLASS,
            MOCKITO_INTERNAL_CLASS,
            MOCKITO_INTERNAL_CLASS,
            -1512
        );
        return stackTrace;
    }
}