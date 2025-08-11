package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused, readable tests for JsonTypeInfo and its nested Value class.
 *
 * Goals:
 * - Use descriptive test names that explain behavior.
 * - Avoid brittle assertions (e.g., exact toString() matches).
 * - Prefer meaningful, deterministic values.
 * - Test "wither" semantics (return same instance if no change; new instance if changed).
 * - Verify defaults and simple invariants.
 */
class JsonTypeInfoValueTest {

    // Helper to make construction concise in tests
    private static JsonTypeInfo.Value newValue(JsonTypeInfo.Id id,
                                               JsonTypeInfo.As include,
                                               String property,
                                               Class<?> defaultImpl,
                                               boolean idVisible,
                                               Boolean requireTypeIdForSubtypes) {
        return JsonTypeInfo.Value.construct(id, include, property, defaultImpl, idVisible, requireTypeIdForSubtypes);
    }

    // ---------------------------------------------------------------------
    // JsonTypeInfo.Id default property names
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Id.CUSTOM has no default property name")
    void idCustom_hasNoDefaultPropertyName() {
        assertNull(JsonTypeInfo.Id.CUSTOM.getDefaultPropertyName());
    }

    @Test
    @DisplayName("Id.CLASS uses '@class' as default property name")
    void idClass_defaultPropertyName() {
        assertEquals("@class", JsonTypeInfo.Id.CLASS.getDefaultPropertyName());
    }

    @Test
    @DisplayName("Id.MINIMAL_CLASS uses '@c' as default property name")
    void idMinimalClass_defaultPropertyName() {
        assertEquals("@c", JsonTypeInfo.Id.MINIMAL_CLASS.getDefaultPropertyName());
    }

    @Test
    @DisplayName("Id.NAME uses '@type' as default property name")
    void idName_defaultPropertyName() {
        assertEquals("@type", JsonTypeInfo.Id.NAME.getDefaultPropertyName());
    }

    @Test
    @DisplayName("Id.SIMPLE_NAME uses '@type' as default property name")
    void idSimpleName_defaultPropertyName() {
        assertEquals("@type", JsonTypeInfo.Id.SIMPLE_NAME.getDefaultPropertyName());
    }

    @Test
    @DisplayName("Id.DEDUCTION has no default property name")
    void idDeduction_hasNoDefaultPropertyName() {
        assertNull(JsonTypeInfo.Id.DEDUCTION.getDefaultPropertyName());
    }

    // ---------------------------------------------------------------------
    // Value.EMPTY defaults
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("EMPTY contains consistent, safe defaults")
    void empty_defaults() {
        JsonTypeInfo.Value v = JsonTypeInfo.Value.EMPTY;

        assertEquals(JsonTypeInfo.Id.NONE, v.getIdType());
        assertEquals(JsonTypeInfo.As.PROPERTY, v.getInclusionType());
        assertNull(v.getPropertyName());
        assertNull(v.getDefaultImpl());
        assertFalse(v.getIdVisible());
        assertNull(v.getRequireTypeIdForSubtypes());
    }

    @Test
    @DisplayName("valueFor() returns the annotation type")
    void valueFor_returnsAnnotationType() {
        assertSame(JsonTypeInfo.class, JsonTypeInfo.Value.EMPTY.valueFor());
    }

    // ---------------------------------------------------------------------
    // Construct factory
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("construct() populates fields as given")
    void construct_populatesFields() {
        JsonTypeInfo.Value v = newValue(
                JsonTypeInfo.Id.MINIMAL_CLASS,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                "type",
                Object.class,
                true,
                Boolean.FALSE
        );

        assertEquals(JsonTypeInfo.Id.MINIMAL_CLASS, v.getIdType());
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, v.getInclusionType());
        assertEquals("type", v.getPropertyName());
        assertEquals(Object.class, v.getDefaultImpl());
        assertTrue(v.getIdVisible());
        assertEquals(Boolean.FALSE, v.getRequireTypeIdForSubtypes());
    }

    @Test
    @DisplayName("construct(): null property name for Id.NAME falls back to default '@type'")
    void construct_nullPropertyNameForIdName_usesDefault() {
        JsonTypeInfo.Value v = newValue(
                JsonTypeInfo.Id.NAME,
                JsonTypeInfo.As.PROPERTY,
                null,
                Integer.class,
                false,
                Boolean.FALSE
        );

        assertEquals("@type", v.getPropertyName());
        assertEquals(JsonTypeInfo.Id.NAME, v.getIdType());
        assertFalse(v.getIdVisible());
    }

    // ---------------------------------------------------------------------
    // Withers: return same instance when no change; new instance when changed
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("withPropertyName(null) returns same instance")
    void withPropertyName_null_noChange_returnsSame() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        assertSame(base, base.withPropertyName(null));
    }

    @Test
    @DisplayName("withPropertyName(new) returns a different instance with updated property")
    void withPropertyName_change_returnsNew() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value changed = base.withPropertyName("typeId");

        assertNotSame(base, changed);
        assertEquals("typeId", changed.getPropertyName());
        assertFalse(changed.getIdVisible()); // unchanged flag
    }

    @Test
    @DisplayName("withIdVisible(false) on already-false returns same instance")
    void withIdVisible_false_noChange_returnsSame() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        assertSame(base, base.withIdVisible(false));
    }

    @Test
    @DisplayName("withIdVisible(true) returns a different instance with visible flag")
    void withIdVisible_true_returnsNew() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value changed = base.withIdVisible(true);

        assertNotSame(base, changed);
        assertTrue(changed.getIdVisible());
    }

    @Test
    @DisplayName("withInclusionType(no change) returns same instance")
    void withInclusionType_noChange_returnsSame() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY; // PROPERTY by default
        assertSame(base, base.withInclusionType(JsonTypeInfo.As.PROPERTY));
    }

    @Test
    @DisplayName("withInclusionType(change) returns new instance")
    void withInclusionType_change_returnsNew() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value changed = base.withInclusionType(JsonTypeInfo.As.WRAPPER_OBJECT);

        assertNotSame(base, changed);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, changed.getInclusionType());
        assertFalse(changed.getIdVisible());
    }

    @Test
    @DisplayName("withIdType(no change) returns same instance")
    void withIdType_noChange_returnsSame() {
        JsonTypeInfo.Value base = newValue(JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.PROPERTY, "x", Object.class, false, null);
        assertSame(base, base.withIdType(JsonTypeInfo.Id.CLASS));
    }

    @Test
    @DisplayName("withIdType(change) returns new instance")
    void withIdType_change_returnsNew() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;
        JsonTypeInfo.Value changed = base.withIdType(JsonTypeInfo.Id.CLASS);

        assertNotSame(base, changed);
        assertEquals(JsonTypeInfo.Id.CLASS, changed.getIdType());
        assertFalse(changed.getIdVisible());
    }

    @Test
    @DisplayName("withDefaultImpl: change returns new; setting same returns same")
    void withDefaultImpl_change_and_noChange() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;

        JsonTypeInfo.Value withObject = base.withDefaultImpl(Object.class);
        assertNotSame(base, withObject);
        assertEquals(Object.class, withObject.getDefaultImpl());

        // Now setting same defaultImpl should return the same instance
        assertSame(withObject, withObject.withDefaultImpl(Object.class));
    }

    @Test
    @DisplayName("withRequireTypeIdForSubtypes: null means no change; toggling updates value")
    void withRequireTypeIdForSubtypes_behaviour() {
        JsonTypeInfo.Value base = JsonTypeInfo.Value.EMPTY;

        // null => same
        assertSame(base, base.withRequireTypeIdForSubtypes(null));

        // set to TRUE => new instance
        JsonTypeInfo.Value t = base.withRequireTypeIdForSubtypes(Boolean.TRUE);
        assertNotSame(base, t);
        assertEquals(Boolean.TRUE, t.getRequireTypeIdForSubtypes());

        // set to FALSE => new instance distinct from the TRUE one
        JsonTypeInfo.Value f = t.withRequireTypeIdForSubtypes(Boolean.FALSE);
        assertNotSame(t, f);
        assertEquals(Boolean.FALSE, f.getRequireTypeIdForSubtypes());
    }

    // ---------------------------------------------------------------------
    // isEnabled helper
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("isEnabled(null) is false")
    void isEnabled_null_isFalse() {
        assertFalse(JsonTypeInfo.Value.isEnabled(null));
    }

    @Test
    @DisplayName("isEnabled(EMPTY) is false")
    void isEnabled_empty_isFalse() {
        assertFalse(JsonTypeInfo.Value.isEnabled(JsonTypeInfo.Value.EMPTY));
    }

    @Test
    @DisplayName("isEnabled is true when idType != NONE")
    void isEnabled_nonNoneId_isTrue() {
        JsonTypeInfo.Value v = newValue(
                JsonTypeInfo.Id.MINIMAL_CLASS,
                JsonTypeInfo.As.WRAPPER_OBJECT,
                "type",
                Object.class,
                false,
                Boolean.FALSE
        );
        assertTrue(JsonTypeInfo.Value.isEnabled(v));
    }

    // ---------------------------------------------------------------------
    // Equality and hashCode basics
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("Two Values with identical state are equal and have same hashCode")
    void equals_and_hashCode_sameState() {
        JsonTypeInfo.Value a = newValue(
                JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.PROPERTY, "t", Object.class, true, Boolean.TRUE);
        JsonTypeInfo.Value b = newValue(
                JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.PROPERTY, "t", Object.class, true, Boolean.TRUE);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    @DisplayName("Values with different state are not equal")
    void equals_differentState_notEqual() {
        JsonTypeInfo.Value a = newValue(
                JsonTypeInfo.Id.CLASS, JsonTypeInfo.As.PROPERTY, "t", Object.class, false, null);
        JsonTypeInfo.Value b = a.withPropertyName("other");

        assertNotEquals(a, b);
    }
}