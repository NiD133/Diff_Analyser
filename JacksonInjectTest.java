package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonInjectTest {
    private static final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    // Helper class for annotation examples
    private static final class AnnotationExamples {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int explicitValues;

        @JacksonInject
        public int defaultValues;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    @Test
    public void emptyValue_hasExpectedDefaults() {
        assertNull(EMPTY.getId());
        assertNull(EMPTY.getUseInput());
        assertTrue(EMPTY.willUseInput(true));
        assertFalse(EMPTY.willUseInput(false));
    }

    @Test
    public void emptyValue_constructsSingletonInstance() {
        assertSame(EMPTY, JacksonInject.Value.construct(null, null, null));
        assertSame(EMPTY, JacksonInject.Value.construct("", null, null)); // Empty string coerced to null
    }

    @Test
    public void fromAnnotation_withNull_returnsEmpty() {
        assertSame(EMPTY, JacksonInject.Value.from(null));
    }

    @Test
    public void fromAnnotation_withExplicitValues() throws Exception {
        JacksonInject ann = AnnotationExamples.class
            .getField("explicitValues")
            .getAnnotation(JacksonInject.class);
        JacksonInject.Value value = JacksonInject.Value.from(ann);

        assertEquals("inject", value.getId());
        assertEquals(Boolean.FALSE, value.getUseInput());
        assertEquals(Boolean.FALSE, value.getOptional());
        assertEquals(
            "JacksonInject.Value(id=inject,useInput=false,optional=false)",
            value.toString()
        );
        assertNotEquals(EMPTY, value);
    }

    @Test
    public void fromAnnotation_withDefaultValues() throws Exception {
        JacksonInject ann = AnnotationExamples.class
            .getField("defaultValues")
            .getAnnotation(JacksonInject.class);
        JacksonInject.Value value = JacksonInject.Value.from(ann);

        assertEquals(JacksonInject.Value.construct(null, null, null), value);
    }

    @Test
    public void fromAnnotation_withOptionalField() throws Exception {
        JacksonInject ann = AnnotationExamples.class
            .getField("optionalField")
            .getAnnotation(JacksonInject.class);
        JacksonInject.Value value = JacksonInject.Value.from(ann);

        assertEquals(JacksonInject.Value.construct(null, null, true), value);
    }

    @Test
    public void emptyValue_toString() {
        assertEquals(
            "JacksonInject.Value(id=null,useInput=null,optional=null)",
            EMPTY.toString()
        );
    }

    @Test
    public void emptyValue_hashCodeNonZero() {
        assertNotEquals(0, EMPTY.hashCode());
    }

    @Test
    public void emptyValue_equals() {
        assertEquals(EMPTY, EMPTY);
        assertNotEquals(EMPTY, null);
        assertNotEquals(EMPTY, "differentType");
    }

    @Test
    public void valueEquality() {
        JacksonInject.Value base = JacksonInject.Value.construct("id", true, true);
        JacksonInject.Value same = JacksonInject.Value.construct("id", true, true);
        JacksonInject.Value differentId = JacksonInject.Value.construct("other", true, true);
        JacksonInject.Value differentUseInput = JacksonInject.Value.construct("id", false, true);
        JacksonInject.Value differentOptional = JacksonInject.Value.construct("id", true, false);
        JacksonInject.Value nullId = JacksonInject.Value.construct(null, true, true);
        JacksonInject.Value nullUseInput = JacksonInject.Value.construct("id", null, true);
        JacksonInject.Value nullOptional = JacksonInject.Value.construct("id", true, null);

        assertEquals(base, same);
        assertNotEquals(base, differentId);
        assertNotEquals(base, differentUseInput);
        assertNotEquals(base, differentOptional);
        assertNotEquals(base, nullId);
        assertNotEquals(base, nullUseInput);
        assertNotEquals(base, nullOptional);
        assertNotEquals(base, "string");
    }

    @Test
    public void withMethods_createNewInstances() {
        JacksonInject.Value withId = EMPTY.withId("name");
        assertNotSame(EMPTY, withId);
        assertEquals("name", withId.getId());

        JacksonInject.Value withUseInput = withId.withUseInput(Boolean.TRUE);
        assertNotSame(withId, withUseInput);
        assertEquals(Boolean.TRUE, withUseInput.getUseInput());

        JacksonInject.Value withOptional = withId.withOptional(Boolean.TRUE);
        assertNotSame(withId, withOptional);
        assertEquals(Boolean.TRUE, withOptional.getOptional());
    }

    @Test
    public void withMethods_idempotent() {
        JacksonInject.Value value = JacksonInject.Value.forId("name");
        assertSame(value, value.withId("name"));
        assertSame(value, value.withUseInput(null));
        assertSame(value, value.withOptional(null));
    }

    @Test
    public void nonEmptyValue_hashCodeNonZero() {
        JacksonInject.Value value = EMPTY.withId("name").withUseInput(Boolean.TRUE);
        assertNotEquals(0, value.hashCode());
    }
}