package org.apache.commons.collections4.map;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NotNullPredicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PredicatedMap_ESTestTest26 extends PredicatedMap_ESTest_scaffolding {

    /**
     * Tests that checkSetValue() throws an IllegalArgumentException when the value
     * is rejected by the value predicate.
     */
    @Test
    public void checkSetValueShouldThrowExceptionWhenValueIsRejectedByPredicate() {
        // Arrange
        final Map<Object, Integer> baseMap = new HashMap<>();
        
        // Use a predicate that accepts any key, as key validation is not the focus of this test.
        final Predicate<Object> keyPredicate = TruePredicate.truePredicate();
        
        // Use a standard NotNullPredicate, which will reject any null value.
        // This makes the test's intent clear.
        final Predicate<Integer> valuePredicate = NotNullPredicate.notNullPredicate();

        final PredicatedMap<Object, Integer> predicatedMap =
                new PredicatedMap<>(baseMap, keyPredicate, valuePredicate);

        // Act & Assert
        // We expect an IllegalArgumentException when trying to validate a null value,
        // as it violates the NotNullPredicate.
        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> predicatedMap.checkSetValue(null)
        );

        // Verify the exception message is as expected.
        assertEquals("Cannot set value - Predicate rejected it", exception.getMessage());
    }
}