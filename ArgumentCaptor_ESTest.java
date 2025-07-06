package org.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentCaptorTest {

    @Test
    void capture_shouldReturnNullForObject() {
        // Given
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

        // When
        Object capturedObject = argumentCaptor.capture();

        // Then
        assertNull(capturedObject, "Captured object should be null as nothing was passed to the mock.");
    }

    @Test
    void forClass_shouldThrowNullPointerExceptionWhenClassIsNull() {
        // Given & When & Then
        Assertions.assertThrows(NullPointerException.class, () -> {
            ArgumentCaptor.forClass(null);
        }, "Should throw NullPointerException when null class is passed to forClass()");
    }

    @Test
    void captor_shouldThrowIllegalArgumentExceptionWhenArgumentsAreProvided() {
        // Given
        Object[] dummyArguments = new Object[4];

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentCaptor.captor(dummyArguments);
        }, "Should throw IllegalArgumentException when arguments are provided to captor()");

        assertEquals("Do not provide any arguments to the 'captor' call", exception.getMessage());
    }

    @Test
    void captor_shouldThrowIllegalArgumentExceptionWhenNullArrayIsProvided() {
        // Given

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ArgumentCaptor.captor((Integer[]) null);
        }, "Should throw IllegalArgumentException when a null array is provided to captor()");

        assertEquals("Do not provide any arguments to the 'captor' call", exception.getMessage());
    }


    @Test
    void getAllValues_shouldReturnEmptyListWhenNoValuesAreCaptured() {
        // Given
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);

        // When
        List<Object> capturedValues = argumentCaptor.getAllValues();

        // Then
        assertTrue(capturedValues.isEmpty(), "Should return an empty list when no values have been captured.");
    }

    @Test
    void getCaptorType_shouldReturnCorrectClassType() {
        // Given
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

        // When
        Class<? extends Integer> captorType = argumentCaptor.getCaptorType();

        // Then
        assertEquals(Integer.class, captorType, "Should return the correct class type.");
        assertFalse(captorType.isAnnotation());
    }

    @Test
    void capture_shouldReturnDefaultValueForPrimitiveType() {
        // Given
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.captor();

        // When
        Integer capturedInteger = argumentCaptor.capture();

        // Then
        assertEquals(0, capturedInteger, "Should return the default value (0) for Integer.");
    }

    @Test
    void getValue_shouldThrowRuntimeExceptionWhenNoValueIsCaptured() {
        // Given
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.captor();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            argumentCaptor.getValue();
        }, "Should throw a RuntimeException when no value has been captured.");
    }
}