package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTypeInfoTest {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, visible = true,
            defaultImpl = JsonTypeInfo.class, requireTypeIdForSubtypes = OptBoolean.TRUE)
    private static final class Anno1 { }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class, requireTypeIdForSubtypes = OptBoolean.FALSE)
    private static final class Anno2 { }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class)
    private static final class Anno3 { }

    // Helper to get Value from annotation class
    private JsonTypeInfo.Value getValueFromAnnotation(Class<?> clazz) {
        return JsonTypeInfo.Value.from(clazz.getAnnotation(JsonTypeInfo.class));
    }

    @Test
    void valueFromNullShouldReturnNull() {
        assertNull(JsonTypeInfo.Value.from(null));
    }

    @Test
    void fromAnnotationAnno1ShouldHaveCorrectValues() {
        JsonTypeInfo.Value value = getValueFromAnnotation(Anno1.class);

        assertEquals(JsonTypeInfo.Id.CLASS, value.getIdType());
        assertEquals(JsonTypeInfo.As.PROPERTY, value.getInclusionType());
        assertEquals("@class", value.getPropertyName());
        assertTrue(value.getIdVisible());
        assertNull(value.getDefaultImpl());
        assertTrue(value.getRequireTypeIdForSubtypes());
    }

    @Test
    void fromAnnotationAnno2ShouldHaveCorrectValues() {
        JsonTypeInfo.Value value = getValueFromAnnotation(Anno2.class);

        assertEquals(JsonTypeInfo.Id.NAME, value.getIdType());
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, value.getInclusionType());
        assertEquals("ext", value.getPropertyName());
        assertFalse(value.getIdVisible());
        assertEquals(Void.class, value.getDefaultImpl());
        assertFalse(value.getRequireTypeIdForSubtypes());
    }

    @Test
    void fromAnnotationAnno3ShouldHaveNullRequireTypeId() {
        JsonTypeInfo.Value value = getValueFromAnnotation(Anno3.class);
        assertNull(value.getRequireTypeIdForSubtypes());
    }

    @Test
    void valueObjectsShouldFollowEqualsContract() {
        JsonTypeInfo.Value v1 = getValueFromAnnotation(Anno1.class);
        JsonTypeInfo.Value v2 = getValueFromAnnotation(Anno2.class);

        // Reflexivity
        assertEquals(v1, v1);
        assertEquals(v2, v2);

        // Symmetry
        assertNotEquals(v1, v2);
        assertNotEquals(v2, v1);
    }

    @Test
    void toStringShouldReflectObjectState() {
        JsonTypeInfo.Value v1 = getValueFromAnnotation(Anno1.class);
        JsonTypeInfo.Value v2 = getValueFromAnnotation(Anno2.class);
        JsonTypeInfo.Value v3 = getValueFromAnnotation(Anno3.class);

        assertEquals(
            "JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class," +
            "defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)",
            v1.toString()
        );

        assertEquals(
            "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," +
            "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)",
            v2.toString()
        );

        assertEquals(
            "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext," +
            "defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)",
            v3.toString()
        );
    }

    @Test
    void withIdTypeShouldCreateNewInstanceWhenChanged() {
        JsonTypeInfo.Value base = getValueFromAnnotation(Anno1.class);

        // No change should return same instance
        assertSame(base, base.withIdType(JsonTypeInfo.Id.CLASS));

        // Changes should return new instances
        JsonTypeInfo.Value minimal = base.withIdType(JsonTypeInfo.Id.MINIMAL_CLASS);
        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, minimal.getIdType());

        JsonTypeInfo.Value simple = base.withIdType(JsonTypeInfo.Id.SIMPLE_NAME);
        assertEquals(JsonTypeInfo.Id.SIMPLE_NAME, simple.getIdType());
    }

    @Test
    void withInclusionTypeShouldCreateNewInstanceWhenChanged() {
        JsonTypeInfo.Value base = getValueFromAnnotation(Anno1.class);

        // No change should return same instance
        assertSame(base, base.withInclusionType(JsonTypeInfo.As.PROPERTY));

        // Change should return new instance
        JsonTypeInfo.Value changed = base.withInclusionType(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, changed.getInclusionType());
    }

    @Test
    void withDefaultImplShouldCreateNewInstanceWhenChanged() {
        JsonTypeInfo.Value base = getValueFromAnnotation(Anno1.class);

        // No change should return same instance
        assertSame(base, base.withDefaultImpl(null));

        // Change should return new instance
        JsonTypeInfo.Value changed = base.withDefaultImpl(String.class);
        assertEquals(String.class, changed.getDefaultImpl());
    }

    @Test
    void withIdVisibleShouldChangeVisibility() {
        JsonTypeInfo.Value base = getValueFromAnnotation(Anno1.class);

        // No change should return same instance
        assertSame(base, base.withIdVisible(true));

        // Change should return new instance
        JsonTypeInfo.Value changed = base.withIdVisible(false);
        assertFalse(changed.getIdVisible());
    }

    @Test
    void withPropertyNameShouldChangeName() {
        JsonTypeInfo.Value base = getValueFromAnnotation(Anno1.class);
        JsonTypeInfo.Value changed = base.withPropertyName("foobar");
        assertEquals("foobar", changed.getPropertyName());
    }

    @Test
    void withRequireTypeIdForSubtypesShouldHandleAllCases() {
        JsonTypeInfo.Value empty = JsonTypeInfo.Value.EMPTY;

        // Initial state
        assertNull(empty.getRequireTypeIdForSubtypes());

        // True value
        JsonTypeInfo.Value trueValue = empty.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertEquals(Boolean.TRUE, trueValue.getRequireTypeIdForSubtypes());

        // False value
        JsonTypeInfo.Value falseValue = empty.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertEquals(Boolean.FALSE, falseValue.getRequireTypeIdForSubtypes());

        // Null value
        JsonTypeInfo.Value nullValue = empty.withRequireTypeIdForSubtypes(null);
        assertNull(nullValue.getRequireTypeIdForSubtypes());
    }
}