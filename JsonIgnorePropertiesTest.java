package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link JsonIgnoreProperties.Value} class, focusing on its construction,
 * property modification, and merging logic.
 */
@DisplayName("JsonIgnoreProperties.Value")
class JsonIgnorePropertiesValueTest {

    @JsonIgnoreProperties(value = {"foo", "bar"}, ignoreUnknown = true)
    private static class Bogus {
    }

    private final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    private Set<String> asSet(String... args) {
        return new LinkedHashSet<>(Arrays.asList(args));
    }

    @Nested
    @DisplayName("Core Functionality")
    class CoreFunctionality {

        @Test
        @DisplayName("should have correct default properties")
        void emptyValueShouldHaveDefaultProperties() {
            assertAll("Default 'empty' value properties",
                    () -> assertEquals(Collections.emptySet(), EMPTY.getIgnored(), "ignored properties should be empty"),
                    () -> assertFalse(EMPTY.getIgnoreUnknown(), "ignoreUnknown should be false"),
                    () -> assertFalse(EMPTY.getAllowGetters(), "allowGetters should be false"),
                    () -> assertFalse(EMPTY.getAllowSetters(), "allowSetters should be false"),
                    () -> assertTrue(EMPTY.getMerge(), "merge should be true by default")
            );
        }

        @Test
        @DisplayName("should follow equals and hashCode contract")
        void equality() {
            JsonIgnoreProperties.Value v1 = JsonIgnoreProperties.Value.empty();
            JsonIgnoreProperties.Value v2 = JsonIgnoreProperties.Value.empty();
            JsonIgnoreProperties.Value v3WithMergeDisabled = v1.withoutMerge();

            // Should be equal to itself and other equivalent instances
            assertEquals(v1, v1);
            assertEquals(v1, v2);
            assertEquals(v1.hashCode(), v2.hashCode());

            // Should not be equal if properties differ (e.g., merge flag)
            assertNotEquals(v1, v3WithMergeDisabled);
            assertNotEquals(v1.hashCode(), v3WithMergeDisabled.hashCode());
        }

        @Test
        @DisplayName("should produce a descriptive toString()")
        void toStringRepresentation() {
            String expected = "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=true,merge=true)";
            String actual = EMPTY.withAllowSetters().withMerge().toString();
            assertEquals(expected, actual);
        }
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethods {

        @Test
        @DisplayName("should create an empty value from a null annotation")
        void fromNullAnnotation() {
            assertSame(EMPTY, JsonIgnoreProperties.Value.from(null));
        }

        @Test
        @DisplayName("should create a value from an annotation instance")
        void fromAnnotation() {
            JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.from(
                    Bogus.class.getAnnotation(JsonIgnoreProperties.class));

            assertNotNull(value);
            assertAll("Properties from @JsonIgnoreProperties(value={\"foo\", \"bar\"}, ignoreUnknown=true)",
                    () -> assertEquals(asSet("foo", "bar"), value.getIgnored()),
                    () -> assertTrue(value.getIgnoreUnknown()),
                    () -> assertFalse(value.getAllowGetters()),
                    () -> assertFalse(value.getAllowSetters()),
                    () -> assertFalse(value.getMerge(), "merge should be false when created from an annotation")
            );
        }

        @Test
        @DisplayName("should return the EMPTY singleton for default values")
        void factoriesForEmptyValuesShouldReturnSingleton() {
            assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
            assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
            assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));
        }

        @Test
        @DisplayName("should create a value with specified ignored properties")
        void forIgnoredProperties() {
            JsonIgnoreProperties.Value value = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
            assertEquals(asSet("a", "b"), value.getIgnored());
        }
    }

    @Nested
    @DisplayName("Wither Methods")
    class WitherMethods {

        @Test
        @DisplayName("should create new instances with modified properties")
        void witherMethodsShouldModifyProperties() {
            assertAll("Wither methods should create new instances with updated values",
                    () -> assertEquals(asSet("a", "b"), EMPTY.withIgnored("a", "b").getIgnored()),
                    () -> assertTrue(EMPTY.withIgnoreUnknown().getIgnoreUnknown()),
                    () -> assertFalse(EMPTY.withoutIgnoreUnknown().getIgnoreUnknown()),
                    () -> assertTrue(EMPTY.withAllowGetters().getAllowGetters()),
                    () -> assertFalse(EMPTY.withoutAllowGetters().getAllowGetters()),
                    () -> assertTrue(EMPTY.withAllowSetters().getAllowSetters()),
                    () -> assertFalse(EMPTY.withoutAllowSetters().getAllowSetters()),
                    () -> assertTrue(EMPTY.withMerge().getMerge()),
                    () -> assertFalse(EMPTY.withoutMerge().getMerge())
            );
        }

        @Test
        @DisplayName("should ignore properties for serialization if allowGetters is false")
        void allowGettersAffectsSerialization() {
            JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");

            // By default, allowGetters is false, so properties are ignored for serialization
            assertEquals(asSet("a", "b"), base.findIgnoredForSerialization());

            // With allowGetters=true, properties are NOT ignored for serialization
            JsonIgnoreProperties.Value withGettersAllowed = base.withAllowGetters();
            assertTrue(withGettersAllowed.getAllowGetters());
            assertEquals(Collections.emptySet(), withGettersAllowed.findIgnoredForSerialization());
        }

        @Test
        @DisplayName("should ignore properties for deserialization if allowSetters is false")
        void allowSettersAffectsDeserialization() {
            JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");

            // By default, allowSetters is false, so properties are ignored for deserialization
            assertEquals(asSet("a", "b"), base.findIgnoredForDeserialization());

            // With allowSetters=true, properties are NOT ignored for deserialization
            JsonIgnoreProperties.Value withSettersAllowed = base.withAllowSetters();
            assertTrue(withSettersAllowed.getAllowSetters());
            assertEquals(Collections.emptySet(), withSettersAllowed.findIgnoredForDeserialization());
        }
    }

    @Nested
    @DisplayName("Merging Logic")
    class MergingLogic {

        @Test
        @DisplayName("should combine properties when override has merge enabled")
        void mergeWithMergeEnabled() {
            JsonIgnoreProperties.Value base = EMPTY.withIgnored("a").withIgnoreUnknown();
            JsonIgnoreProperties.Value override = EMPTY.withIgnored("b").withAllowGetters().withMerge(); // merge=true

            JsonIgnoreProperties.Value merged = base.withOverrides(override);

            assertAll("Merged result should be a union of properties",
                    () -> assertEquals(asSet("a", "b"), merged.getIgnored()),
                    () -> assertTrue(merged.getIgnoreUnknown()),
                    () -> assertTrue(merged.getAllowGetters()),
                    () -> assertFalse(merged.getAllowSetters())
            );
        }

        @Test
        @DisplayName("should replace properties when override has merge disabled")
        void mergeWithMergeDisabled() {
            JsonIgnoreProperties.Value base = EMPTY.withIgnored("a").withIgnoreUnknown().withAllowGetters();
            JsonIgnoreProperties.Value override = EMPTY.withIgnored("b").withoutMerge(); // merge=false

            JsonIgnoreProperties.Value merged = base.withOverrides(override);

            // When merge is disabled on the override, the override acts as a complete replacement.
            assertEquals(override, merged);
            assertAll("Merged result should be identical to the override value",
                    () -> assertEquals(asSet("b"), merged.getIgnored()),
                    () -> assertFalse(merged.getIgnoreUnknown()),
                    () -> assertFalse(merged.getAllowGetters())
            );
        }

        @Test
        @DisplayName("should return self when merging with null or empty override")
        void mergeWithNullOrEmpty() {
            JsonIgnoreProperties.Value base = EMPTY.withIgnored("a");
            assertSame(base, base.withOverrides(null));
            assertSame(base, base.withOverrides(EMPTY));
        }

        @Test
        @DisplayName("should combine all ignored properties using mergeAll")
        void mergeAll() {
            JsonIgnoreProperties.Value v1 = EMPTY.withIgnored("a");
            JsonIgnoreProperties.Value v2 = EMPTY.withIgnored("b");
            JsonIgnoreProperties.Value v3 = EMPTY.withIgnored("c");

            JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(v1, v2, v3);

            assertEquals(asSet("a", "b", "c"), merged.getIgnored());
        }
    }
}