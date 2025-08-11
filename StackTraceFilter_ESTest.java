package org.mockito.internal.exceptions.stacktrace;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for {@link StackTraceFilter}.
 *
 * This class verifies that the StackTraceFilter correctly cleans stack traces
 * by removing internal Mockito and other framework elements, while retaining
 * user code elements.
 */
public class StackTraceFilterTest {

    private final StackTraceFilter stackTraceFilter = new StackTraceFilter();

    // A sample stack trace element representing user's code.
    private static final StackTraceElement USER_CODE_ELEMENT =
            new StackTraceElement("com.mycompany.MyClass", "myMethod", "MyClass.java", 123);

    // A sample stack trace element representing Mockito's internal code.
    private static final StackTraceElement MOCKITO_INTERNAL_ELEMENT =
            new StackTraceElement("org.mockito.internal.handler.InvocationNotifierHandler", "handle", "InvocationNotifierHandler.java", 50);

    // A sample stack trace element from another framework that should be filtered.
    private static final StackTraceElement OTHER_FRAMEWORK_ELEMENT =
            new StackTraceElement("org.junit.internal.runners.TestMethodRunner", "run", "TestMethodRunner.java", 80);


    /*
     * Tests for filter(StackTraceElement[], boolean)
     */

    @Test
    public void filter_shouldRemoveMockitoInternalElements() {
        // Given a stack trace with only a Mockito-internal element
        StackTraceElement[] stackTrace = {MOCKITO_INTERNAL_ELEMENT};

        // When the stack trace is filtered
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(stackTrace, false);

        // Then the resulting stack trace should be empty
        assertEquals(0, filteredStackTrace.length);
    }

    @Test
    public void filter_shouldKeepUserCodeElements() {
        // Given a stack trace with only a user code element
        StackTraceElement[] stackTrace = {USER_CODE_ELEMENT};

        // When the stack trace is filtered
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(stackTrace, false);

        // Then the user code element should be retained
        assertArrayEquals(new StackTraceElement[]{USER_CODE_ELEMENT}, filteredStackTrace);
    }

    @Test
    public void filter_shouldCorrectlyFilterMixedStackTrace() {
        // Given a stack trace with a mix of user and internal elements
        StackTraceElement[] stackTrace = {
                USER_CODE_ELEMENT,
                MOCKITO_INTERNAL_ELEMENT,
                OTHER_FRAMEWORK_ELEMENT,
                USER_CODE_ELEMENT
        };

        // When the stack trace is filtered
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(stackTrace, false);

        // Then only the user code elements should remain
        assertArrayEquals(new StackTraceElement[]{USER_CODE_ELEMENT, USER_CODE_ELEMENT}, filteredStackTrace);
    }

    @Test
    public void filter_shouldReturnNewArray_whenFilteringEmptyStackTrace() {
        // Given an empty stack trace
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // When it is filtered
        StackTraceElement[] filteredStackTrace = stackTraceFilter.filter(emptyStackTrace, false);

        // Then the result is a new empty array
        assertEquals(0, filteredStackTrace.length);
        assertNotSame("Filter should return a new array instance", emptyStackTrace, filteredStackTrace);
    }

    @Test(expected = NullPointerException.class)
    public void filter_shouldThrowException_whenStackTraceContainsNullElement() {
        // Given a stack trace containing a null element
        StackTraceElement[] stackTraceWithNull = {USER_CODE_ELEMENT, null};

        // When filtering, it should throw a NullPointerException
        stackTraceFilter.filter(stackTraceWithNull, false);
    }


    /*
     * Tests for filterFirst(Throwable, boolean)
     */

    @Test
    public void filterFirst_shouldReturnFirstUserCodeElement() {
        // Given a throwable with a stack trace starting with internal elements
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[]{MOCKITO_INTERNAL_ELEMENT, OTHER_FRAMEWORK_ELEMENT, USER_CODE_ELEMENT});

        // When finding the first non-filtered element
        StackTraceElement firstElement = stackTraceFilter.filterFirst(throwable, false);

        // Then it should be the user code element
        assertEquals(USER_CODE_ELEMENT, firstElement);
    }

    @Test
    public void filterFirst_shouldReturnTopElement_whenKeepTopIsTrue_evenIfItIsInternal() {
        // Given a throwable with a stack trace starting with an internal element
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[]{MOCKITO_INTERNAL_ELEMENT, USER_CODE_ELEMENT});

        // When finding the first element with keepTop=true
        StackTraceElement firstElement = stackTraceFilter.filterFirst(throwable, true);

        // Then the top element should be returned, even if it's internal
        assertEquals(MOCKITO_INTERNAL_ELEMENT, firstElement);
    }

    @Test
    public void filterFirst_shouldReturnNull_whenStackTraceIsEmpty() {
        // Given a throwable with an empty stack trace
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[0]);

        // When finding the first element
        StackTraceElement firstElement = stackTraceFilter.filterFirst(throwable, false);

        // Then the result should be null
        assertNull(firstElement);
    }

    @Test
    public void filterFirst_shouldReturnNull_whenStackTraceContainsOnlyInternalElements() {
        // Given a throwable with only internal elements in its stack trace
        Throwable throwable = new Throwable();
        throwable.setStackTrace(new StackTraceElement[]{MOCKITO_INTERNAL_ELEMENT, OTHER_FRAMEWORK_ELEMENT});

        // When finding the first non-filtered element
        StackTraceElement firstElement = stackTraceFilter.filterFirst(throwable, false);

        // Then the result should be null as all elements are filtered out
        assertNull(firstElement);
    }


    /*
     * Tests for findSourceFile(StackTraceElement[], String)
     */

    @Test
    public void findSourceFile_shouldReturnFileNameFromFirstUserCodeElement() {
        // Given a stack trace with internal elements followed by a user element
        StackTraceElement[] stackTrace = {MOCKITO_INTERNAL_ELEMENT, USER_CODE_ELEMENT};

        // When finding the source file
        String sourceFile = stackTraceFilter.findSourceFile(stackTrace, "default.java");

        // Then it should return the source file of the user code element
        assertEquals("MyClass.java", sourceFile);
    }

    @Test
    public void findSourceFile_shouldReturnDefaultValue_whenStackTraceIsEmpty() {
        // Given an empty stack trace
        StackTraceElement[] emptyStackTrace = new StackTraceElement[0];

        // When finding the source file
        String sourceFile = stackTraceFilter.findSourceFile(emptyStackTrace, "default.java");

        // Then it should return the provided default value
        assertEquals("default.java", sourceFile);
    }

    @Test
    public void findSourceFile_shouldReturnDefaultValue_whenNoElementHasSourceFile() {
        // Given a stack trace where the user element has no source file info
        StackTraceElement userElementWithNoSource = new StackTraceElement("com.my.Clazz", "work", null, 42);
        StackTraceElement[] stackTrace = {MOCKITO_INTERNAL_ELEMENT, userElementWithNoSource};

        // When finding the source file
        String sourceFile = stackTraceFilter.findSourceFile(stackTrace, "default.java");

        // Then it should return the provided default value
        assertEquals("default.java", sourceFile);
    }

    @Test(expected = NullPointerException.class)
    public void findSourceFile_shouldThrowException_whenStackTraceContainsNullElement() {
        // Given a stack trace containing a null element
        StackTraceElement[] stackTraceWithNull = {null};

        // When finding the source file, it should throw a NullPointerException
        stackTraceFilter.findSourceFile(stackTraceWithNull, "default.java");
    }
}