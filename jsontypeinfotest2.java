package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonTypeInfo.Value} class, focusing on its
 * construction from a {@link JsonTypeInfo} annotation.
 */
class JsonTypeInfoValueTest {

    /**
     * A test fixture with a @JsonTypeInfo annotation where most properties
     * are explicitly defined.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
            include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class,
            requireTypeIdForSubtypes = OptBoolean.FALSE,
            visible = false)
    private static class ConfigFullySpecified {
    }

    /**
     * A test fixture with a @JsonTypeInfo annotation that relies on several
     * default property values.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS,
            visible = true,
            defaultImpl = JsonTypeInfo.class,
            requireTypeIdForSubtypes = OptBoolean.TRUE)
    private static class ConfigWithDefaults {
    }

    @Nested
    @DisplayName("from() factory method")
    class FromAnnotation {

        @Test
        @DisplayName("should capture explicitly defined annotation values")
        void fromAnnotationShouldCaptureExplicitValues() {
            // Arrange
            JsonTypeInfo annotation = ConfigFullySpecified.class.getAnnotation(JsonTypeInfo.class);

            // Act
            JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

            // Assert
            assertAll("Properties from a fully specified annotation",
                    () -> assertEquals(JsonTypeInfo.Id.NAME, value.getIdType(), "Should use Id from annotation"),
                    () -> assertEquals(As.EXTERNAL_PROPERTY, value.getInclusionType(), "Should use 'include' from annotation"),
                    () -> assertEquals("ext", value.getPropertyName(), "Should use 'property' from annotation"),
                    () -> assertEquals(Void.class, value.getDefaultImpl(), "Should use 'defaultImpl' from annotation"),
                    () -> assertFalse(value.getIdVisible(), "Should use 'visible' from annotation"),
                    () -> assertFalse(value.getRequireTypeIdForSubtypes(), "Should use 'requireTypeIdForSubtypes' from annotation")
            );
        }

        @Test
        @DisplayName("should apply correct defaults for unspecified annotation values")
        void fromAnnotationShouldApplyDefaultsForMissingProperties() {
            // Arrange
            JsonTypeInfo annotation = ConfigWithDefaults.class.getAnnotation(JsonTypeInfo.class);

            // Act
            JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

            // Assert
            assertAll("Properties from an annotation that uses defaults",
                    // Explicitly set values
                    () -> assertEquals(JsonTypeInfo.Id.CLASS, value.getIdType()),
                    () -> assertTrue(value.getIdVisible()),
                    () -> assertTrue(value.getRequireTypeIdForSubtypes()),

                    // Defaulted values
                    () -> assertEquals(As.PROPERTY, value.getInclusionType(), "'include' should default to PROPERTY"),
                    () -> assertEquals("@class", value.getPropertyName(), "'property' name should default based on Id type"),
                    () -> assertNull(value.getDefaultImpl(), "'defaultImpl=JsonTypeInfo.class' should be interpreted as null")
            );
        }
    }

    @Test
    @DisplayName("should follow equals and hashCode contract")
    void equalsAndHashCodeShouldFollowContract() {
        // Arrange
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.from(ConfigWithDefaults.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value1Again = JsonTypeInfo.Value.from(ConfigWithDefaults.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value2 = JsonTypeInfo.Value.from(ConfigFullySpecified.class.getAnnotation(JsonTypeInfo.class));

        // Assert
        assertAll("Equals and HashCode Contract",
                () -> assertEquals(value1, value1, "A value must be equal to itself (reflexive)"),
                () -> assertEquals(value1, value1Again, "Two values with the same properties should be equal"),
                () -> assertEquals(value1.hashCode(), value1Again.hashCode(), "Hashcodes of equal objects must be the same"),
                () -> assertNotEquals(value1, value2, "Values with different properties should not be equal"),
                () -> assertNotEquals(value1, null, "A value must not be equal to null"),
                () -> assertNotEquals(value1, new Object(), "A value must not be equal to an object of a different type")
        );
    }

    @Test
    @DisplayName("should provide a clear and descriptive toString representation")
    void toStringShouldProvideClearRepresentation() {
        // Arrange
        JsonTypeInfo.Value valueFromDefaults = JsonTypeInfo.Value.from(ConfigWithDefaults.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value valueFromExplicit = JsonTypeInfo.Value.from(ConfigFullySpecified.class.getAnnotation(JsonTypeInfo.class));

        String expectedDefaultsString = "JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class,defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)";
        String expectedExplicitString = "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)";

        // Assert
        assertEquals(expectedDefaultsString, valueFromDefaults.toString());
        assertEquals(expectedExplicitString, valueFromExplicit.toString());
    }
}