package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JacksonInject.Value tests")
public class JacksonInjectValueTest {

    // Fixture: fields annotated in various ways to exercise parsing
    private static final class Fixture {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int field;

        @JacksonInject
        public int vanilla;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalField;
    }

    private static final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    @Test
    @DisplayName("empty() exposes nulls and uses default willUseInput behavior")
    void emptyValue_hasNullsAndDefaults() {
        assertAll(
                () -> assertNull(EMPTY.getId(), "id should be null"),
                () -> assertNull(EMPTY.getUseInput(), "useInput should be null (DEFAULT)"),
                () -> assertNull(EMPTY.getOptional(), "optional should be null (DEFAULT)"),

                () -> assertTrue(EMPTY.willUseInput(true), "DEFAULT should preserve true"),
                () -> assertFalse(EMPTY.willUseInput(false), "DEFAULT should preserve false"),

                () -> assertSame(EMPTY, JacksonInject.Value.construct(null, null, null),
                        "construct(null, null, null) must return EMPTY"),
                () -> assertSame(EMPTY, JacksonInject.Value.construct("", null, null),
                        "empty String id is coerced to null and should return EMPTY")
        );
    }

    @Test
    @DisplayName("from(null) returns EMPTY; parsing of annotation values is correct")
    void fromAnnotation_parsing() throws Exception {
        assertSame(EMPTY, JacksonInject.Value.from(null), "from(null) should be legal and return EMPTY");

        // Fully specified annotation (@JacksonInject(value='inject', useInput=FALSE, optional=FALSE))
        JacksonInject annFull = annotationOf("field");
        JacksonInject.Value vFull = JacksonInject.Value.from(annFull);
        assertAll(
                () -> assertEquals("inject", vFull.getId()),
                () -> assertEquals(Boolean.FALSE, vFull.getUseInput()),
                () -> assertEquals(Boolean.FALSE, vFull.getOptional()),
                () -> assertEquals("JacksonInject.Value(id=inject,useInput=false,optional=false)", vFull.toString()),
                () -> assertNotEquals(EMPTY, vFull),
                () -> assertNotEquals(vFull, EMPTY)
        );

        // Defaults only (@JacksonInject)
        JacksonInject annDefault = annotationOf("vanilla");
        JacksonInject.Value vDefault = JacksonInject.Value.from(annDefault);
        assertEquals(JacksonInject.Value.construct(null, null, null), vDefault,
                "optional should be null (DEFAULT) by default");

        // Only optional set (@JacksonInject(optional=TRUE))
        JacksonInject annOptional = annotationOf("optionalField");
        JacksonInject.Value vOptional = JacksonInject.Value.from(annOptional);
        assertEquals(JacksonInject.Value.construct(null, null, true), vOptional);
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    @DisplayName("toString, hashCode, and equals behave as expected")
    void standardMethods() {
        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)", EMPTY.toString());
        assertNonZeroHashCode(EMPTY);

        assertAll(
                () -> assertEquals(EMPTY, EMPTY),
                () -> assertNotEquals(EMPTY, null),
                () -> assertNotEquals(EMPTY, "xyz")
        );

        JacksonInject.Value equals1 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value equals2 = JacksonInject.Value.construct("value", true, true);
        JacksonInject.Value valueNull = JacksonInject.Value.construct(null, true, true);
        JacksonInject.Value useInputNull = JacksonInject.Value.construct("value", null, true);
        JacksonInject.Value optionalNull = JacksonInject.Value.construct("value", true, null);
        JacksonInject.Value valueNotEqual = JacksonInject.Value.construct("not equal", true, true);
        JacksonInject.Value useInputNotEqual = JacksonInject.Value.construct("value", false, true);
        JacksonInject.Value optionalNotEqual = JacksonInject.Value.construct("value", true, false);
        String string = "string";

        assertAll(
                () -> assertEquals(equals1, equals2),
                () -> assertNotEquals(equals1, valueNull),
                () -> assertNotEquals(equals1, useInputNull),
                () -> assertNotEquals(equals1, optionalNull),
                () -> assertNotEquals(equals1, valueNotEqual),
                () -> assertNotEquals(equals1, useInputNotEqual),
                () -> assertNotEquals(equals1, optionalNotEqual),
                () -> assertNotEquals(equals1, string)
        );
    }

    @Test
    @DisplayName("Mutant factory methods are functional and idempotent")
    void factories_withers() {
        JacksonInject.Value v1 = EMPTY.withId("name");
        assertAll(
                () -> assertNotSame(EMPTY, v1),
                () -> assertEquals("name", v1.getId()),
                () -> assertSame(v1, v1.withId("name"), "withId with same value should return same instance")
        );

        JacksonInject.Value v2 = v1.withUseInput(Boolean.TRUE);
        assertAll(
                () -> assertNotSame(v1, v2),
                () -> assertNotEquals(v1, v2),
                () -> assertSame(v2, v2.withUseInput(Boolean.TRUE), "withUseInput idempotency"),
                () -> assertEquals(Boolean.TRUE, v2.getUseInput())
        );

        JacksonInject.Value v3 = v1.withOptional(Boolean.TRUE);
        assertAll(
                () -> assertNotSame(v1, v3),
                () -> assertNotEquals(v1, v3),
                () -> assertSame(v3, v3.withOptional(Boolean.TRUE), "withOptional idempotency"),
                () -> assertEquals(Boolean.TRUE, v3.getOptional())
        );

        assertNonZeroHashCode(v2);
    }

    // Helpers

    private static JacksonInject annotationOf(String fieldName) throws NoSuchFieldException {
        return Fixture.class.getField(fieldName).getAnnotation(JacksonInject.class);
    }

    private static void assertNonZeroHashCode(Object o) {
        int hash = o.hashCode();
        if (hash == 0) {
            fail("hashCode should not be 0");
        }
    }
}