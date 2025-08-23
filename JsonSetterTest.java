package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.fasterxml.jackson.annotation.Nulls.DEFAULT;
import static com.fasterxml.jackson.annotation.Nulls.FAIL;
import static com.fasterxml.jackson.annotation.Nulls.SET;
import static com.fasterxml.jackson.annotation.Nulls.SKIP;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JsonSetter.Value behavior")
public class JsonSetterTest {

    // Fixture: a field with explicit JsonSetter configuration for "from annotation" tests
    private static final class AnnotatedTarget {
        @JsonSetter(nulls = FAIL, contentNulls = SKIP)
        public int configured;
    }

    private static final JsonSetter.Value EMPTY = JsonSetter.Value.empty();

    // ----- Small helpers to keep assertions concise and intention-revealing -----

    private static void assertDefaults(JsonSetter.Value v) {
        assertAll(
                () -> assertEquals(DEFAULT, v.getValueNulls(), "valueNulls should default"),
                () -> assertEquals(DEFAULT, v.getContentNulls(), "contentNulls should default"),
                () -> assertNull(v.nonDefaultValueNulls(), "nonDefaultValueNulls() must be null when DEFAULT"),
                () -> assertNull(v.nonDefaultContentNulls(), "nonDefaultContentNulls() must be null when DEFAULT")
        );
    }

    private static void assertSettings(JsonSetter.Value v, Nulls valueNulls, Nulls contentNulls) {
        assertAll(
                () -> assertEquals(valueNulls, v.getValueNulls(), "valueNulls"),
                () -> assertEquals(contentNulls, v.getContentNulls(), "contentNulls")
        );
    }

    private static JsonSetter getJsonSetterAnnotation() {
        try {
            return AnnotatedTarget.class.getField("configured").getAnnotation(JsonSetter.class);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Test fixture misconfigured: field not found", e);
        }
    }

    // ----- Tests -----

    @Test
    @DisplayName("Empty instance exposes defaults and annotation type")
    void emptyValue_defaultsAndType() {
        assertDefaults(EMPTY);
        assertEquals(JsonSetter.class, EMPTY.valueFor(), "valueFor() should return annotation type");
    }

    @Test
    @DisplayName("Std method overrides (toString/hashCode/equals) behave as expected")
    void stdMethods() {
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", EMPTY.toString());

        int hash = EMPTY.hashCode();
        assertNotEquals(0, hash, "hashCode should not be 0");

        assertEquals(EMPTY, EMPTY);
        assertNotEquals(EMPTY, null);
        assertNotEquals(EMPTY, "xyz");
    }

    @Test
    @DisplayName("Value.from(null) returns EMPTY; Value.from(annotation) reads both null-handling options")
    void fromAnnotation() {
        assertSame(EMPTY, JsonSetter.Value.from(null), "from(null) must return EMPTY");

        JsonSetter.Value fromAnn = JsonSetter.Value.from(getJsonSetterAnnotation());
        assertSettings(fromAnn, FAIL, SKIP);
    }

    @Test
    @DisplayName("construct(null, null) returns EMPTY (preferred API is empty()/withXxx)")
    void constructReturnsEmptyForNulls() {
        assertSame(EMPTY, JsonSetter.Value.construct(null, null));
    }

    @Test
    @DisplayName("Factories affect only their respective dimension (value vs content)")
    void factories() {
        JsonSetter.Value contentOnly = JsonSetter.Value.forContentNulls(SET);
        assertSettings(contentOnly, DEFAULT, SET);
        assertEquals(SET, contentOnly.nonDefaultContentNulls());

        JsonSetter.Value valueOnly = JsonSetter.Value.forValueNulls(SKIP);
        assertSettings(valueOnly, SKIP, DEFAULT);
        assertEquals(SKIP, valueOnly.nonDefaultValueNulls());
    }

    @Test
    @DisplayName("Simple withXxx merging: apply content first then value")
    void simpleMerge() {
        JsonSetter.Value withContentSkip = EMPTY.withContentNulls(SKIP);
        assertEquals(SKIP, withContentSkip.getContentNulls());

        JsonSetter.Value withValueFail = withContentSkip.withValueNulls(FAIL);
        assertEquals(FAIL, withValueFail.getValueNulls());
    }

    @Test
    @DisplayName("withXxx methods: idempotency, null-as-default, and overrides merging")
    void withMethodsAndOverrides() {
        // withContentNulls(null) should act as "no change" and return same instance
        JsonSetter.Value v = EMPTY.withContentNulls(null);
        assertSame(EMPTY, v, "withContentNulls(null) should return same instance");

        // Change content null policy and verify idempotency on same value
        v = v.withContentNulls(FAIL);
        assertEquals(FAIL, v.getContentNulls());
        assertSame(v, v.withContentNulls(FAIL), "setting to same value should be idempotent");

        // Change value null policy -> should produce a different instance
        JsonSetter.Value vWithValueSkip = v.withValueNulls(SKIP);
        assertEquals(SKIP, vWithValueSkip.getValueNulls());
        assertNotEquals(v, vWithValueSkip);

        // Explicitly set both value+content nulls via two-arg withValueNulls; nulls mean DEFAULT
        JsonSetter.Value resetToDefaults = vWithValueSkip.withValueNulls(null, null);
        assertDefaults(resetToDefaults);
        assertSame(resetToDefaults, resetToDefaults.withValueNulls(null, null),
                "Two-arg withValueNulls(null, null) should be idempotent");

        // Merge overrides: non-default settings from overrides should win
        JsonSetter.Value merged = resetToDefaults.withOverrides(vWithValueSkip);
        assertNotSame(vWithValueSkip, merged, "withOverrides should create a new instance when overrides exist");
        assertEquals(vWithValueSkip, merged);
        assertEquals(merged, vWithValueSkip);
    }
}