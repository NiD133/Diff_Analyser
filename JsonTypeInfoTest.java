package com.fasterxml.jackson.annotation;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.junit.jupiter.api.Test;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.CLASS;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.MINIMAL_CLASS;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.SIMPLE_NAME;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused tests for JsonTypeInfo.Value:
 * - reading values from @JsonTypeInfo annotations
 * - immutability and "withXxx" mutators
 * - equality and string representation
 * - handling of requireTypeIdForSubtypes
 */
public class JsonTypeInfoTest {

    /**
     * Annotation holder for:
     *  use = CLASS (default property name "@class")
     *  include = PROPERTY (default)
     *  visible = true
     *  defaultImpl = JsonTypeInfo.class (placeholder => Value should expose null)
     *  requireTypeIdForSubtypes = TRUE
     */
    @JsonTypeInfo(
            use = Id.CLASS,
            visible = true,
            defaultImpl = JsonTypeInfo.class,
            requireTypeIdForSubtypes = OptBoolean.TRUE
    )
    private static final class ClassIdWithVisibleAndRequire { }

    /**
     * Annotation holder for:
     *  use = NAME
     *  include = EXTERNAL_PROPERTY
     *  property = "ext"
     *  defaultImpl = Void.class
     *  requireTypeIdForSubtypes = FALSE
     */
    @JsonTypeInfo(
            use = Id.NAME,
            include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class,
            requireTypeIdForSubtypes = OptBoolean.FALSE
    )
    private static final class NameIdExternalWithDefaults { }

    /**
     * Annotation holder for:
     *  use = NAME
     *  include = EXTERNAL_PROPERTY
     *  property = "ext"
     *  defaultImpl = Void.class
     *  requireTypeIdForSubtypes = DEFAULT (not specified)
     */
    @JsonTypeInfo(
            use = Id.NAME,
            include = As.EXTERNAL_PROPERTY,
            property = "ext",
            defaultImpl = Void.class
    )
    private static final class NameIdExternalWithDefaultRequire { }

    @Test
    void returnsNullForEmptySource() {
        // Distinguish "none" (no instance) from "empty" value constant
        assertNull(JsonTypeInfo.Value.from(null), "Value.from(null) must return null");
    }

    @Test
    void readsValuesFromAnnotation_ClassIdVariant() {
        JsonTypeInfo.Value v = fromAnnotation(ClassIdWithVisibleAndRequire.class);

        // Defaults come from Id.CLASS and As.PROPERTY
        assertValueProperties(
                v,
                CLASS,           // id type
                PROPERTY,        // inclusion
                "@class",        // default property for CLASS id
                true,            // id visible
                null,            // placeholder defaultImpl => Value exposes null
                Boolean.TRUE     // require type id for subtypes
        );

        assertEquals(
                "JsonTypeInfo.Value(idType=CLASS,includeAs=PROPERTY,propertyName=@class,defaultImpl=NULL,idVisible=true,requireTypeIdForSubtypes=true)",
                v.toString(),
                "toString mismatch for CLASS-id variant"
        );
    }

    @Test
    void readsValuesFromAnnotation_NameIdExternalVariant() {
        JsonTypeInfo.Value v = fromAnnotation(NameIdExternalWithDefaults.class);

        assertValueProperties(
                v,
                NAME,
                EXTERNAL_PROPERTY,
                "ext",
                false,
                Void.class,
                Boolean.FALSE
        );

        assertEquals(
                "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=false)",
                v.toString(),
                "toString mismatch for NAME/EXTERNAL_PROPERTY variant"
        );
    }

    @Test
    void equalityAndInequality() {
        JsonTypeInfo.Value v1 = fromAnnotation(ClassIdWithVisibleAndRequire.class);
        JsonTypeInfo.Value v2 = fromAnnotation(NameIdExternalWithDefaults.class);

        assertEquals(v1, v1, "Value must be equal to itself");
        assertEquals(v2, v2, "Value must be equal to itself");

        assertNotEquals(v1, v2, "Different annotations should yield different Value instances");
        assertNotEquals(v2, v1, "Inequality should be symmetric");
    }

    @Test
    void mutatorsReturnSameInstanceWhenNoChangeAndNewInstanceWhenChanged() {
        JsonTypeInfo.Value original = fromAnnotation(ClassIdWithVisibleAndRequire.class);
        assertEquals(CLASS, original.getIdType(), "Precondition: idType must be CLASS");
        assertEquals(PROPERTY, original.getInclusionType(), "Precondition: inclusion must be PROPERTY");
        assertTrue(original.getIdVisible(), "Precondition: idVisible must be true");

        // withIdType: same
        assertSame(original, original.withIdType(CLASS), "withIdType(no change) must return same instance");
        // withIdType: changed
        JsonTypeInfo.Value changedId = original.withIdType(MINIMAL_CLASS);
        assertEquals(MINIMAL_CLASS, changedId.getIdType(), "withIdType must reflect new id type");

        // another change
        JsonTypeInfo.Value changedId2 = original.withIdType(SIMPLE_NAME);
        assertEquals(SIMPLE_NAME, changedId2.getIdType(), "withIdType must reflect new id type");

        // withInclusionType: same
        assertSame(original, original.withInclusionType(PROPERTY), "withInclusionType(no change) must return same instance");
        // withInclusionType: changed
        JsonTypeInfo.Value changedIncl = original.withInclusionType(EXTERNAL_PROPERTY);
        assertEquals(EXTERNAL_PROPERTY, changedIncl.getInclusionType(), "withInclusionType must reflect new inclusion");

        // withDefaultImpl: same (no-op)
        assertSame(original, original.withDefaultImpl(null), "withDefaultImpl(null) should return same when already null");
        // withDefaultImpl: changed
        JsonTypeInfo.Value changedDefault = original.withDefaultImpl(String.class);
        assertEquals(String.class, changedDefault.getDefaultImpl(), "withDefaultImpl must reflect new default impl");

        // withIdVisible: same vs changed
        assertSame(original, original.withIdVisible(true), "withIdVisible(no change) must return same instance");
        assertFalse(original.withIdVisible(false).getIdVisible(), "withIdVisible(false) must reflect new visibility");

        // withPropertyName: changed
        assertEquals("foobar", original.withPropertyName("foobar").getPropertyName(),
                "withPropertyName must set the new name");
    }

    @Test
    void withRequireTypeIdForSubtypes_usesBoxedBooleanAndSupportsNull() {
        JsonTypeInfo.Value empty = JsonTypeInfo.Value.EMPTY;
        assertNull(empty.getRequireTypeIdForSubtypes(), "EMPTY must have null requireTypeIdForSubtypes");

        JsonTypeInfo.Value requireTrue = empty.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertEquals(Boolean.TRUE, requireTrue.getRequireTypeIdForSubtypes());

        JsonTypeInfo.Value requireFalse = empty.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertEquals(Boolean.FALSE, requireFalse.getRequireTypeIdForSubtypes());

        JsonTypeInfo.Value requireDefault = empty.withRequireTypeIdForSubtypes(null);
        assertNull(requireDefault.getRequireTypeIdForSubtypes());
    }

    @Test
    void defaultValueForRequireTypeIdForSubtypes_isNullWhenNotSpecified() {
        JsonTypeInfo.Value v = fromAnnotation(NameIdExternalWithDefaultRequire.class);

        assertNull(v.getRequireTypeIdForSubtypes(),
                "When not specified on annotation, requireTypeIdForSubtypes should be null");

        assertEquals(
                "JsonTypeInfo.Value(idType=NAME,includeAs=EXTERNAL_PROPERTY,propertyName=ext,defaultImpl=java.lang.Void,idVisible=false,requireTypeIdForSubtypes=null)",
                v.toString(),
                "toString must include 'requireTypeIdForSubtypes=null' when defaulted"
        );
    }

    // Helper: extract JsonTypeInfo.Value from a class annotated with @JsonTypeInfo
    private static JsonTypeInfo.Value fromAnnotation(Class<?> annotatedClass) {
        return JsonTypeInfo.Value.from(annotatedClass.getAnnotation(JsonTypeInfo.class));
    }

    // Helper: assert all key properties in one place to reduce repetition and improve readability
    private static void assertValueProperties(
            JsonTypeInfo.Value v,
            Id expectedId,
            As expectedInclusion,
            String expectedProperty,
            boolean expectedVisible,
            Class<?> expectedDefaultImpl,
            Boolean expectedRequireTypeIdForSubtypes
    ) {
        assertAll(
                () -> assertEquals(expectedId, v.getIdType(), "idType"),
                () -> assertEquals(expectedInclusion, v.getInclusionType(), "inclusionType"),
                () -> assertEquals(expectedProperty, v.getPropertyName(), "propertyName"),
                () -> assertEquals(expectedVisible, v.getIdVisible(), "idVisible"),
                () -> assertEquals(expectedDefaultImpl, v.getDefaultImpl(), "defaultImpl"),
                () -> assertEquals(expectedRequireTypeIdForSubtypes, v.getRequireTypeIdForSubtypes(),
                        "requireTypeIdForSubtypes")
        );
    }
}