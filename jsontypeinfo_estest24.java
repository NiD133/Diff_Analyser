package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Test suite for the {@link JsonTypeInfo.Value} class, focusing on its factory methods.
 */
public class JsonTypeInfoValueTest {

    /**
     * Tests that {@link JsonTypeInfo.Value#from(JsonTypeInfo)} throws a
     * NullPointerException if the provided annotation has a null 'use' id.
     * The 'use' property is fundamental for determining the type identification strategy,
     * and its absence should lead to a failure.
     */
    @Test(expected = NullPointerException.class)
    public void fromAnnotationWithNullUseIdShouldThrowException() {
        // Arrange: Create a mock JsonTypeInfo annotation where the 'use()' method,
        // which specifies the type metadata, returns null. Other properties are
        // set to valid defaults to isolate the cause of the exception.
        JsonTypeInfo mockAnnotation = mock(JsonTypeInfo.class);

        // This is the specific condition under test
        doReturn(null).when(mockAnnotation).use();

        // Stub other methods with valid, non-null values
        doReturn(JsonTypeInfo.As.PROPERTY).when(mockAnnotation).include();
        doReturn("").when(mockAnnotation).property();
        doReturn(JsonTypeInfo.class).when(mockAnnotation).defaultImpl();
        doReturn(false).when(mockAnnotation).visible();
        doReturn(OptBoolean.DEFAULT).when(mockAnnotation).requireTypeIdForSubtypes();

        // Act: Attempt to construct a JsonTypeInfo.Value from the mock annotation.
        // This call is expected to fail with a NullPointerException.
        JsonTypeInfo.Value.from(mockAnnotation);

        // Assert: The exception is caught and verified by the 'expected'
        // parameter of the @Test annotation.
    }
}