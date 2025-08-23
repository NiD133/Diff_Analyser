package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JsonIncludeProperties_ESTestTest12_Improved extends JsonIncludeProperties_ESTest_scaffolding {

    /**
     * Tests that {@link JsonIncludeProperties.Value#withOverrides(JsonIncludeProperties.Value)}
     * correctly computes the intersection of properties.
     *
     * <p>This test verifies that when a base Value has an empty set of included properties,
     * overriding it with a Value that has one or more properties results in a new Value
     * that is also empty. The resulting set should be the intersection of the base and
     * the override sets.
     */
    @Test(timeout = 4000)
    public void withOverridesWithEmptyBaseShouldReturnEmptyIntersection() {
        // Arrange

        // 1. Create a base Value representing an empty set of properties,
        //    simulating an annotation like @JsonIncludeProperties({}).
        JsonIncludeProperties emptyAnnotation = mock(JsonIncludeProperties.class);
        doReturn(new String[0]).when(emptyAnnotation).value();
        JsonIncludeProperties.Value baseValue = JsonIncludeProperties.Value.from(emptyAnnotation);

        // 2. Create an overriding Value with a single property.
        Set<String> overrideProperties = Collections.singleton("name");
        JsonIncludeProperties.Value overridesValue = new JsonIncludeProperties.Value(overrideProperties);

        // Act
        JsonIncludeProperties.Value resultValue = baseValue.withOverrides(overridesValue);

        // Assert
        assertNotNull(resultValue);
        assertNotSame("withOverrides should produce a new instance", baseValue, resultValue);

        // The result should be the intersection of the two sets ({} ∩ {"name"}), which is empty.
        // Therefore, the result should be equal to the original empty base value.
        assertEquals("Resulting value should be equal to the empty base value", baseValue, resultValue);
        assertTrue("The resulting set of included properties should be empty", resultValue.getIncluded().isEmpty());
    }
}