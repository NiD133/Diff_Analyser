package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the creation and property defaults of {@link JsonTypeInfo.Value}.
 */
class JsonTypeInfoValueTest {

    // Annotation setup for the test.
    // The 'requireTypeIdForSubtypes' property is intentionally omitted to test its default behavior.
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = As.EXTERNAL_PROPERTY,
        property = "ext",
        defaultImpl = Void.class
    )
    private static class TypeInfoWithDefaultRequireTypeId {
    }

    @Test
    void shouldCreateValueWithNullRequireTypeIdForSubtypesWhenNotSpecified() {
        // Arrange: Get the annotation from our test class
        JsonTypeInfo annotation = TypeInfoWithDefaultRequireTypeId.class.getAnnotation(JsonTypeInfo.class);
        assertNotNull(annotation, "Annotation should be present on the helper class");

        // Act: Create a Value instance from the annotation
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

        // Assert: Verify that all properties of the Value instance are correctly set
        assertAll("Properties of JsonTypeInfo.Value from annotation",
            () -> assertEquals(JsonTypeInfo.Id.NAME, value.getIdType(),
                "idType should match the annotation"),
            () -> assertEquals(As.EXTERNAL_PROPERTY, value.getInclusionType(),
                "includeAs should match the annotation"),
            () -> assertEquals("ext", value.getPropertyName(),
                "propertyName should match the annotation"),
            () -> assertEquals(Void.class, value.getDefaultImpl(),
                "defaultImpl should match the annotation"),
            () -> assertFalse(value.getIdVisible(),
                "'visible' should default to false when not specified"),
            () -> assertNull(value.getRequireTypeIdForSubtypes(),
                "'requireTypeIdForSubtypes' should default to null when not specified")
        );
    }
}