package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for JacksonInject.Value")
public class JacksonInjectTest {

    /**
     * A helper class with fields annotated with @JacksonInject to test
     * the creation of Value objects from annotations.
     */
    private static class AnnotatedBean {
        @JacksonInject(value = "inject", useInput = OptBoolean.FALSE, optional = OptBoolean.FALSE)
        public int allProperties;

        @JacksonInject
        public int defaultProperties;

        @JacksonInject(optional = OptBoolean.TRUE)
        public int optionalProperty;
    }

    private static final JacksonInject.Value EMPTY = JacksonInject.Value.empty();

    @Nested
    @DisplayName("empty() factory and instance")
    class EmptyInstanceTests {
        @Test
        @DisplayName("should have null properties")
        void emptyValueShouldHaveNullProperties() {
            assertAll("EMPTY instance properties",
                () -> assertNull(EMPTY.getId(), "id should be null"),
                () -> assertNull(EMPTY.getUseInput(), "useInput should be null"),
                () -> assertNull(EMPTY.getOptional(), "optional should be null")
            );
        }

        @Test
        @DisplayName("willUseInput() should return default when not set")
        void willUseInputShouldReturnDefault() {
            assertTrue(EMPTY.willUseInput(true), "Should return true when default is true");
            assertFalse(EMPTY.willUseInput(false), "Should return false when default is false");
        }
    }

    @Nested
    @DisplayName("construct() factory")
    class ConstructFactoryTests {
        @Test
        @DisplayName("should return EMPTY instance for null values")
        void constructWithNullsShouldReturnEmpty() {
            assertSame(EMPTY, JacksonInject.Value.construct(null, null, null),
                "Constructing with all nulls should return the EMPTY singleton");
        }

        @Test
        @DisplayName("should treat empty string id as null")
        void constructWithEmptyIdShouldReturnEmpty() {
            // As per behavior, an empty string for the id is coerced to null.
            assertSame(EMPTY, JacksonInject.Value.construct("", null, null),
                "Constructing with an empty string id should return the EMPTY singleton");
        }
    }

    @Nested
    @DisplayName("from() factory")
    class FromFactoryTests {
        @Test
        @DisplayName("should return EMPTY for null annotation")
        void fromNullAnnotationShouldReturnEmpty() {
            assertSame(EMPTY, JacksonInject.Value.from(null));
        }

        @Test
        @DisplayName("should correctly read all specified properties from annotation")
        void fromAnnotationWithAllProperties() throws NoSuchFieldException {
            JacksonInject ann = AnnotatedBean.class.getField("allProperties").getAnnotation(JacksonInject.class);
            JacksonInject.Value value = JacksonInject.Value.from(ann);

            assertAll("Value from fully configured annotation",
                () -> assertEquals("inject", value.getId()),
                () -> assertEquals(Boolean.FALSE, value.getUseInput()),
                () -> assertEquals(Boolean.FALSE, value.getOptional())
            );
        }

        @Test
        @DisplayName("should create EMPTY value from annotation with default properties")
        void fromAnnotationWithDefaultProperties() throws NoSuchFieldException {
            JacksonInject ann = AnnotatedBean.class.getField("defaultProperties").getAnnotation(JacksonInject.class);
            JacksonInject.Value value = JacksonInject.Value.from(ann);

            // An annotation with default values results in an EMPTY Value object,
            // as defaults (like useInput=true) are handled at a higher level.
            assertEquals(EMPTY, value);
        }

        @Test
        @DisplayName("should correctly read optional property from annotation")
        void fromAnnotationWithOptionalProperty() throws NoSuchFieldException {
            JacksonInject ann = AnnotatedBean.class.getField("optionalProperty").getAnnotation(JacksonInject.class);
            JacksonInject.Value value = JacksonInject.Value.from(ann);

            JacksonInject.Value expected = JacksonInject.Value.construct(null, null, true);
            assertEquals(expected, value);
        }
    }

    @Nested
    @DisplayName("Standard methods (equals, hashCode, toString)")
    class StandardMethodTests {

        @Test
        @DisplayName("toString() should return correct representation")
        void testToString() {
            assertEquals("JacksonInject.Value(id=null,useInput=null,optional=null)", EMPTY.toString());

            JacksonInject.Value value = JacksonInject.Value.construct("id", true, false);
            assertEquals("JacksonInject.Value(id=id,useInput=true,optional=false)", value.toString());
        }

        @Test
        @DisplayName("hashCode() should be consistent with equals()")
        void testHashCode() {
            JacksonInject.Value v1 = JacksonInject.Value.construct("value", true, true);
            JacksonInject.Value v2 = JacksonInject.Value.construct("value", true, true);
            JacksonInject.Value v3 = JacksonInject.Value.construct("other", false, false);

            assertEquals(v1.hashCode(), v2.hashCode(), "Equal objects must have equal hash codes");
            assertNotEquals(v1.hashCode(), EMPTY.hashCode(), "Non-equal objects should ideally have different hash codes");
            assertNotEquals(v1.hashCode(), v3.hashCode(), "Non-equal objects should ideally have different hash codes");
        }

        @SuppressWarnings("unlikely-arg-type")
        @Test
        @DisplayName("equals() should follow its contract")
        void testEquals() {
            JacksonInject.Value v1 = JacksonInject.Value.construct("value", true, true);
            JacksonInject.Value v2 = JacksonInject.Value.construct("value", true, true);

            // Test cases for inequality
            JacksonInject.Value valueNull = JacksonInject.Value.construct(null, true, true);
            JacksonInject.Value useInputNull = JacksonInject.Value.construct("value", null, true);
            JacksonInject.Value optionalNull = JacksonInject.Value.construct("value", true, null);
            JacksonInject.Value valueNotEqual = JacksonInject.Value.construct("not equal", true, true);
            JacksonInject.Value useInputNotEqual = JacksonInject.Value.construct("value", false, true);
            JacksonInject.Value optionalNotEqual = JacksonInject.Value.construct("value", true, false);

            assertAll("Equals contract",
                // Reflexive
                () -> assertEquals(v1, v1),
                // Symmetric
                () -> {
                    assertEquals(v1, v2, "v1 should be equal to v2");
                    assertEquals(v2, v1, "v2 should be equal to v1");
                },
                // Not equal to null
                () -> assertNotEquals(null, v1),
                // Not equal to different type
                () -> assertNotEquals("a string", v1),
                // Inequality checks for each property
                () -> assertNotEquals(v1, EMPTY),
                () -> assertNotEquals(v1, valueNull),
                () -> assertNotEquals(v1, useInputNull),
                () -> assertNotEquals(v1, optionalNull),
                () -> assertNotEquals(v1, valueNotEqual),
                () -> assertNotEquals(v1, useInputNotEqual),
                () -> assertNotEquals(v1, optionalNotEqual)
            );
        }
    }

    @Nested
    @DisplayName("Wither methods")
    class WitherMethodTests {

        @Test
        @DisplayName("withId() should create a new instance with updated id")
        void withId() {
            JacksonInject.Value v1 = EMPTY.withId("name");

            assertAll("withId behavior",
                () -> assertNotSame(EMPTY, v1, "Should create a new instance"),
                () -> assertEquals("name", v1.getId(), "ID should be updated"),
                () -> assertNull(v1.getUseInput(), "Other properties should be unchanged"),
                () -> assertNull(v1.getOptional(), "Other properties should be unchanged")
            );

            // Calling with the same value should return the same instance (optimization)
            assertSame(v1, v1.withId("name"), "Calling with same id should return same instance");
        }

        @Test
        @DisplayName("withUseInput() should create a new instance with updated useInput")
        void withUseInput() {
            JacksonInject.Value v1 = EMPTY.withUseInput(true);

            assertAll("withUseInput behavior",
                () -> assertNotSame(EMPTY, v1, "Should create a new instance"),
                () -> assertEquals(true, v1.getUseInput(), "useInput should be updated"),
                () -> assertNull(v1.getId(), "Other properties should be unchanged"),
                () -> assertNull(v1.getOptional(), "Other properties should be unchanged")
            );

            // Calling with the same value should return the same instance (optimization)
            assertSame(v1, v1.withUseInput(true), "Calling with same useInput should return same instance");
        }

        @Test
        @DisplayName("withOptional() should create a new instance with updated optional")
        void withOptional() {
            JacksonInject.Value v1 = EMPTY.withOptional(true);

            assertAll("withOptional behavior",
                () -> assertNotSame(EMPTY, v1, "Should create a new instance"),
                () -> assertEquals(true, v1.getOptional(), "optional should be updated"),
                () -> assertNull(v1.getId(), "Other properties should be unchanged"),
                () -> assertNull(v1.getUseInput(), "Other properties should be unchanged")
            );

            // Calling with the same value should return the same instance (optimization)
            assertSame(v1, v1.withOptional(true), "Calling with same optional should return same instance");
        }
    }
}