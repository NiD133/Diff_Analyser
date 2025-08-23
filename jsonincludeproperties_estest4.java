package com.fasterxml.jackson.annotation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Set;

/**
 * This test class focuses on the behavior of {@link JsonIncludeProperties.Value}.
 * The original name 'JsonIncludeProperties_ESTestTest4' suggests auto-generation;
 * a more descriptive name like 'JsonIncludePropertiesValueTest' would be preferable.
 */
public class JsonIncludeProperties_ESTestTest4 extends JsonIncludeProperties_ESTest_scaffolding {

    /**
     * Tests that two {@link JsonIncludeProperties.Value} instances are considered equal
     * when both represent the inclusion of all properties.
     * <p>
     * This is verified by comparing the static {@code Value.ALL} instance with a new
     * instance created explicitly with a {@code null} set of included properties.
     * The test also confirms that their hash codes are identical, upholding the
     * {@code equals}/{@code hashCode} contract.
     */
    @Test
    public void equals_shouldReturnTrue_whenBothInstancesRepresentAllProperties() {
        // Arrange
        // Value.ALL is a pre-defined instance representing that all properties are included.
        // Internally, this is represented by a null set.
        JsonIncludeProperties.Value allPropertiesValue = JsonIncludeProperties.Value.ALL;

        // Create another instance that also represents "all properties included"
        // by passing a null set to the constructor.
        JsonIncludeProperties.Value anotherAllPropertiesValue = new JsonIncludeProperties.Value((Set<String>) null);

        // Act & Assert
        // 1. Verify that the two instances are considered equal.
        //    Using assertEquals provides a more informative failure message than assertTrue.
        assertEquals(allPropertiesValue, anotherAllPropertiesValue);

        // 2. Verify the equals contract is symmetric.
        assertEquals(anotherAllPropertiesValue, allPropertiesValue);

        // 3. Verify that their hash codes are also equal, as required by the equals/hashCode contract.
        assertEquals(allPropertiesValue.hashCode(), anotherAllPropertiesValue.hashCode());
    }
}