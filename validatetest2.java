package org.jsoup.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// Renamed from ValidateTestTest2 to follow standard conventions.
public class ValidateTest {

    @Test
    @DisplayName("notNull should throw an exception with a filtered stack trace for null input")
    void notNullThrowsExceptionWithFilteredStackTrace() {
        // The purpose of this test is to verify that when a validation fails, the resulting
        // exception's stack trace is "clean". A clean stack trace does not include the
        // internal implementation frames of the Validate helper class itself. This directs
        // the developer to the actual call site that failed validation, improving debuggability.

        // ACT: Call a validation method that is expected to fail.
        // ASSERT: It throws the correct exception type.
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            Validate.notNull(null);
        });

        // ASSERT: The exception message is as expected.
        assertEquals("Object must not be null", exception.getMessage());

        // ASSERT: The Validate class is not present in the stack trace.
        Stream<StackTraceElement> stackTrace = Arrays.stream(exception.getStackTrace());
        boolean isValidateClassInTrace = stackTrace
            .anyMatch(element -> element.getClassName().equals(Validate.class.getName()));

        assertFalse(isValidateClassInTrace,
            "The stack trace should not contain the Validate class, to point developers to the calling code.");
    }
}