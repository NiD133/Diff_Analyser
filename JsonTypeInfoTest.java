package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link JsonTypeInfo} annotation and its value-object representation, {@link JsonTypeInfo.Value}.
 */
@DisplayName("JsonTypeInfo.Value")
class JsonTypeInfoTest {

    //~ Test Fixture: Annotated classes for providing JsonTypeInfo instances
    // ========================================================================

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true,
            defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private static class SampleConfigWithClassId {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class, requireTypeIdForSubtypes = OptBoolean.FALSE)
    private static class SampleConfigWithNameAndExternalProperty {
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class)
    private static class SampleConfigWithDefaultRequireFlag {
    }

    //~ Test cases for JsonTypeInfo.Value.from()
    // ========================================================================

    @Test
    @DisplayName("from(null) should return null")
    void from_withNullAnnotation_shouldReturnNull() {
        // Act
        JsonTypeInfo.Value result = JsonTypeInfo.Value.from(null);

        // Assert
        assertNull(result, "Value.from(null) should return null, not an empty instance.");
    }

    @Test
    @DisplayName("from() should correctly read an annotation with mixed explicit and default properties")
    void from_withMixedExplicitAndDefaultProperties_shouldCreateCorrectValue() {
        // Arrange
        JsonTypeInfo annotation = SampleConfigWithClassId.class.getAnnotation(JsonTypeInfo.class);

        // Act
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

        // Assert
        assertNotNull(value);
        assertEquals(JsonTypeInfo.Id.CLASS, value.getIdType());
        assertTrue(value.getIdVisible());
        // defaultImpl is JsonTypeInfo.class, which signifies "no default" and is represented as null
        assertNull(value.getDefaultImpl());
        assertTrue(value.getRequireTypeIdForSubtypes());

        // Verify properties that fall back to their defaults from the annotation definition
        assertEquals(JsonTypeInfo.As.PROPERTY, value.getInclusionType(), "Inclusion type should default to PROPERTY");
        assertEquals("@class", value.getPropertyName(), "Property name should default based on Id type (CLASS -> @class)");
    }

    @Test
    @DisplayName("from() should correctly read an annotation with all properties explicitly set")
    void from_withAllExplicitProperties_shouldCreateCorrectValue() {
        // Arrange
        JsonTypeInfo annotation = SampleConfigWithNameAndExternalProperty.class.getAnnotation(JsonTypeInfo.class);

        // Act
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

        // Assert
        assertNotNull(value);
        assertEquals(JsonTypeInfo.Id.NAME, value.getIdType());
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, value.getInclusionType());
        assertEquals("ext", value.getPropertyName());
        // defaultImpl is Void.class, which signifies mapping to null
        assertEquals(Void.class, value.getDefaultImpl());
        assertFalse(value.getIdVisible(), "Visibility should default to false if not specified");
        assertFalse(value.getRequireTypeIdForSubtypes());
    }

    @Test
    @DisplayName("from() should use null for requireTypeIdForSubtypes when the annotation property is absent")
    void from_whenRequireTypeIdForSubtypesIsDefault_shouldBeNull() {
        // Arrange
        JsonTypeInfo annotation = SampleConfigWithDefaultRequireFlag.class.getAnnotation(JsonTypeInfo.class);

        // Act
        JsonTypeInfo.Value value = JsonTypeInfo.Value.from(annotation);

        // Assert
        assertNotNull(value);
        assertNull(value.getRequireTypeIdForSubtypes(),
                "When 'requireTypeIdForSubtypes' is not set, the Value property should be null (default).");
    }

    //~ Test cases for JsonTypeInfo.Value mutators ("with..." methods)
    // ========================================================================

    private JsonTypeInfo.Value createBaseValue() {
        return JsonTypeInfo.Value.from(SampleConfigWithClassId.class.getAnnotation(JsonTypeInfo.class));
    }

    @Test
    @DisplayName("withIdType() should create a new Value with the updated IdType")
    void withIdType_shouldCreateNewValueWithUpdatedIdType() {
        // Arrange
        JsonTypeInfo.Value baseValue = createBaseValue();

        // Act: Change the IdType
        JsonTypeInfo.Value updatedValue = baseValue.withIdType(JsonTypeInfo.Id.MINIMAL_CLASS);

        // Assert: A new instance is created with the new value
        assertNotSame(baseValue, updatedValue);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, updatedValue.getIdType());

        // Act: Call with the same IdType
        JsonTypeInfo.Value sameValue = baseValue.withIdType(JsonTypeInfo.Id.CLASS);

        // Assert: The same instance is returned (immutability)
        assertSame(baseValue, sameValue);
    }

    @Test
    @DisplayName("withInclusionType() should create a new Value with the updated InclusionType")
    void withInclusionType_shouldCreateNewValueWithUpdatedInclusionType() {
        // Arrange
        JsonTypeInfo.Value baseValue = createBaseValue();

        // Act: Change the InclusionType
        JsonTypeInfo.Value updatedValue = baseValue.withInclusionType(As.EXTERNAL_PROPERTY);

        // Assert: A new instance is created with the new value
        assertNotSame(baseValue, updatedValue);
        assertEquals(As.EXTERNAL_PROPERTY, updatedValue.getInclusionType());

        // Act: Call with the same InclusionType
        JsonTypeInfo.Value sameValue = baseValue.withInclusionType(As.PROPERTY);

        // Assert: The same instance is returned (immutability)
        assertSame(baseValue, sameValue);
    }

    @Test
    @DisplayName("withDefaultImpl() should create a new Value with the updated default implementation")
    void withDefaultImpl_shouldCreateNewValueWithUpdatedDefaultImpl() {
        // Arrange
        JsonTypeInfo.Value baseValue = createBaseValue();

        // Act: Change the default implementation
        JsonTypeInfo.Value updatedValue = baseValue.withDefaultImpl(String.class);

        // Assert: A new instance is created with the new value
        assertNotSame(baseValue, updatedValue);
        assertEquals(String.class, updatedValue.getDefaultImpl());

        // Act: Call with the same default implementation (null)
        JsonTypeInfo.Value sameValue = baseValue.withDefaultImpl(null);

        // Assert: The same instance is returned (immutability)
        assertSame(baseValue, sameValue);
    }

    @Test
    @DisplayName("withIdVisible() should create a new Value with the updated visibility")
    void withIdVisible_shouldCreateNewValueWithUpdatedVisibility() {
        // Arrange
        JsonTypeInfo.Value baseValue = createBaseValue();

        // Act: Change the visibility
        JsonTypeInfo.Value updatedValue = baseValue.withIdVisible(false);

        // Assert: A new instance is created with the new value
        assertNotSame(baseValue, updatedValue);
        assertFalse(updatedValue.getIdVisible());

        // Act: Call with the same visibility
        JsonTypeInfo.Value sameValue = baseValue.withIdVisible(true);

        // Assert: The same instance is returned (immutability)
        assertSame(baseValue, sameValue);
    }

    @Test
    @DisplayName("withPropertyName() should create a new Value with the updated property name")
    void withPropertyName_shouldCreateNewValueWithUpdatedPropertyName() {
        // Arrange
        JsonTypeInfo.Value baseValue = createBaseValue();

        // Act: Change the property name
        JsonTypeInfo.Value updatedValue = baseValue.withPropertyName("foobar");

        // Assert: A new instance is created with the new value
        assertNotSame(baseValue, updatedValue);
        assertEquals("foobar", updatedValue.getPropertyName());

        // Act: Call with the same property name
        JsonTypeInfo.Value sameValue = baseValue.withPropertyName("@class");

        // Assert: The same instance is returned (immutability)
        assertSame(baseValue, sameValue);
    }

    @Test
    @DisplayName("withRequireTypeIdForSubtypes() should create a new Value with the updated flag")
    void withRequireTypeIdForSubtypes_shouldCreateNewValueWithUpdatedFlag() {
        // Arrange: Start with EMPTY, which has a null 'requireTypeIdForSubtypes'
        JsonTypeInfo.Value baseValue = JsonTypeInfo.Value.EMPTY;
        assertNull(baseValue.getRequireTypeIdForSubtypes(), "Initial value should be null");

        // Act & Assert for TRUE
        JsonTypeInfo.Value updatedToTrue = baseValue.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertNotSame(baseValue, updatedToTrue);
        assertEquals(Boolean.TRUE, updatedToTrue.getRequireTypeIdForSubtypes());

        // Act & Assert for FALSE
        JsonTypeInfo.Value updatedToFalse = baseValue.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertNotSame(baseValue, updatedToFalse);
        assertEquals(Boolean.FALSE, updatedToFalse.getRequireTypeIdForSubtypes());

        // Act & Assert for null (reverting to default)
        JsonTypeInfo.Value revertedToNull = updatedToTrue.withRequireTypeIdForSubtypes(null);
        assertNotSame(updatedToTrue, revertedToNull);
        assertNull(revertedToNull.getRequireTypeIdForSubtypes());

        // Act & Assert for no-op change
        JsonTypeInfo.Value sameValue = baseValue.withRequireTypeIdForSubtypes(null);
        assertSame(baseValue, sameValue, "Calling with same value should return same instance");
    }

    //~ Test cases for standard object methods (equals, hashCode, toString)
    // ========================================================================

    @Test
    @DisplayName("equals() and hashCode() should follow the contract")
    void value_equalsAndHashCode_shouldFollowContract() {
        // Arrange
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.from(SampleConfigWithClassId.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value1Equivalent = JsonTypeInfo.Value.from(SampleConfigWithClassId.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value2 = JsonTypeInfo.Value.from(SampleConfigWithNameAndExternalProperty.class.getAnnotation(JsonTypeInfo.class));

        // Assert equals
        assertEquals(value1, value1, "An object must be equal to itself.");
        assertEquals(value1, value1Equivalent, "Equivalent objects must be equal.");
        assertNotEquals(value1, value2, "Different objects must not be equal.");
        assertNotEquals(value1, null, "An object must not be equal to null.");
        assertNotEquals(value1, new Object(), "An object must not be equal to an object of a different type.");

        // Assert hashCode
        assertEquals(value1.hashCode(), value1Equivalent.hashCode(), "Hash codes must be equal for equal objects.");
        assertNotEquals(value1.hashCode(), value2.hashCode(), "Hash codes should differ for unequal objects.");
    }

    @Test
    @DisplayName("toString() should provide a clear, descriptive representation")
    void value_toString_shouldProvideClearRepresentation() {
        // Arrange
        JsonTypeInfo.Value value1 = JsonTypeInfo.Value.from(SampleConfigWithClassId.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value2 = JsonTypeInfo.Value.from(SampleConfigWithNameAndExternalProperty.class.getAnnotation(JsonTypeInfo.class));
        JsonTypeInfo.Value value3 = JsonTypeInfo.Value.from(SampleConfigWithDefaultRequireFlag.class.getAnnotation(JsonTypeInfo.class));

        // Act & Assert
        String expected1 = "JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class,defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)";
        assertEquals(expected1, value1.toString());

        String expected2 = "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)";
        assertEquals(expected2, value2.toString());

        String expected3 = "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)";
        assertEquals(expected3, value3.toString());
    }
}