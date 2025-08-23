package com.fasterxml.jackson.annotation;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for JsonIgnoreProperties.Value.
 * These tests group related behavior and avoid redundant assertions.
 */
public class JsonIgnorePropertiesValueTest {

    // Helpers

    private static Set<String> setOf(String... names) {
        return new LinkedHashSet<>(Arrays.asList(names));
    }

    private static void assertState(JsonIgnoreProperties.Value v,
                                    Set<String> ignored,
                                    boolean ignoreUnknown,
                                    boolean allowGetters,
                                    boolean allowSetters,
                                    boolean merge) {
        assertEquals("ignored", ignored, v.getIgnored());
        assertEquals("ignoreUnknown", ignoreUnknown, v.getIgnoreUnknown());
        assertEquals("allowGetters", allowGetters, v.getAllowGetters());
        assertEquals("allowSetters", allowSetters, v.getAllowSetters());
        assertEquals("merge", merge, v.getMerge());
    }

    // Fixtures to validate Value.from(annotation)

    @JsonIgnoreProperties
    private static class DefaultsAnnotated {
    }

    @JsonIgnoreProperties(
            value = { "id", "secret" },
            ignoreUnknown = true,
            allowGetters = true,
            allowSetters = false
    )
    private static class CustomAnnotated {
    }

    // Tests

    @Test
    public void emptyHasExpectedDefaults() {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.empty();

        assertState(v, setOf(), false, false, false, true);
        // With no allowers, ignored-for-(de)serialization equals ignored
        assertEquals(setOf(), v.findIgnoredForSerialization());
        assertEquals(setOf(), v.findIgnoredForDeserialization());
        assertEquals(JsonIgnoreProperties.class, v.valueFor());
        assertNotNull(v.toString()); // smoke-check formatting
    }

    @Test
    public void ignoredNamesAndAllowersAffectSerializationAndDeserialization() {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");

        assertState(v, setOf("a", "b"), false, false, false, true);
        // Initially: both sides ignore
        assertEquals(setOf("a", "b"), v.findIgnoredForSerialization());
        assertEquals(setOf("a", "b"), v.findIgnoredForDeserialization());

        // Allow getters -> do NOT ignore for serialization
        JsonIgnoreProperties.Value allowGetters = v.withAllowGetters();
        assertTrue(allowGetters.getAllowGetters());
        assertEquals(setOf(), allowGetters.findIgnoredForSerialization());
        assertEquals(setOf("a", "b"), allowGetters.findIgnoredForDeserialization());

        // Allow setters -> do NOT ignore for deserialization
        JsonIgnoreProperties.Value allowBoth = allowGetters.withAllowSetters();
        assertTrue(allowBoth.getAllowSetters());
        assertEquals(setOf(), allowBoth.findIgnoredForSerialization());
        assertEquals(setOf(), allowBoth.findIgnoredForDeserialization());
    }

    @Test
    public void togglesAreIdempotentAndReversible() {
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.empty();

        JsonIgnoreProperties.Value g1 = base.withAllowGetters();
        JsonIgnoreProperties.Value g2 = g1.withAllowGetters(); // idempotent
        assertEquals(g1, g2);
        assertTrue(g1.getAllowGetters());

        JsonIgnoreProperties.Value g0 = g1.withoutAllowGetters(); // reversible
        assertEquals(base.getAllowGetters(), g0.getAllowGetters());

        JsonIgnoreProperties.Value s1 = base.withAllowSetters();
        JsonIgnoreProperties.Value s2 = s1.withAllowSetters();
        assertEquals(s1, s2);
        assertTrue(s1.getAllowSetters());
        assertEquals(base.getAllowSetters(), s1.withoutAllowSetters().getAllowSetters());

        JsonIgnoreProperties.Value u1 = base.withIgnoreUnknown();
        JsonIgnoreProperties.Value u2 = u1.withIgnoreUnknown();
        assertEquals(u1, u2);
        assertTrue(u1.getIgnoreUnknown());
        assertEquals(base.getIgnoreUnknown(), u1.withoutIgnoreUnknown().getIgnoreUnknown());

        JsonIgnoreProperties.Value m0 = base.withoutMerge();
        assertFalse(m0.getMerge());
        assertTrue(m0.withMerge().getMerge());
    }

    @Test
    public void withoutIgnoredClearsOnlyTheIgnoredNames() {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.forIgnoredProperties("x");
        assertEquals(setOf("x"), v.getIgnored());

        JsonIgnoreProperties.Value cleared = v.withoutIgnored();
        assertEquals(setOf(), cleared.getIgnored());
        // Flags remain unchanged
        assertEquals(v.getIgnoreUnknown(), cleared.getIgnoreUnknown());
        assertEquals(v.getAllowGetters(), cleared.getAllowGetters());
        assertEquals(v.getAllowSetters(), cleared.getAllowSetters());
        assertEquals(v.getMerge(), cleared.getMerge());
    }

    @Test
    public void mergeCombinesFlagsWithLogicalOrAndUnionsIgnoredNames() {
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value
                .forIgnoredProperties("a")
                .withAllowGetters();          // allow getters only

        JsonIgnoreProperties.Value overrides = JsonIgnoreProperties.Value
                .forIgnoredProperties("b")
                .withAllowSetters()           // allow setters only
                .withIgnoreUnknown();         // ignore unknowns

        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.merge(base, overrides);

        assertEquals(setOf("a", "b"), merged.getIgnored());
        assertTrue("ignoreUnknown should be true if any side is true", merged.getIgnoreUnknown());
        assertTrue("allowGetters should be true if any side is true", merged.getAllowGetters());
        assertTrue("allowSetters should be true if any side is true", merged.getAllowSetters());
        assertTrue("merge flag should remain true by default", merged.getMerge());

        // With both allowers true, nothing is ignored for either direction
        assertEquals(setOf(), merged.findIgnoredForSerialization());
        assertEquals(setOf(), merged.findIgnoredForDeserialization());
    }

    @Test
    public void mergeAllHandlesNullsAndMultipleValues() {
        JsonIgnoreProperties.Value v1 = JsonIgnoreProperties.Value.forIgnoredProperties("a");
        JsonIgnoreProperties.Value v2 = JsonIgnoreProperties.Value.forIgnoreUnknown(true);
        JsonIgnoreProperties.Value v3 = JsonIgnoreProperties.Value.forIgnoredProperties("b").withAllowSetters();

        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(null, v1, null, v2, v3, null);

        assertTrue(merged.getIgnoreUnknown());
        assertTrue(merged.getAllowSetters());
        assertEquals(setOf("a", "b"), merged.getIgnored());
    }

    @Test
    public void fromAnnotationReadsDefaults() {
        JsonIgnoreProperties ann = DefaultsAnnotated.class.getAnnotation(JsonIgnoreProperties.class);
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.from(ann);

        assertState(v, setOf(), false, false, false, false); // from(annotation) does not enable merge by default
    }

    @Test
    public void fromAnnotationReadsExplicitValues() {
        JsonIgnoreProperties ann = CustomAnnotated.class.getAnnotation(JsonIgnoreProperties.class);
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.from(ann);

        assertEquals(setOf("id", "secret"), v.getIgnored());
        assertTrue(v.getIgnoreUnknown());
        assertTrue(v.getAllowGetters());
        assertFalse(v.getAllowSetters());
        // With allowGetters=true, nothing ignored for serialization
        assertEquals(setOf(), v.findIgnoredForSerialization());
        // With allowSetters=false, still ignored for deserialization
        assertEquals(setOf("id", "secret"), v.findIgnoredForDeserialization());
    }

    @Test
    public void equalityAndHashCodeConsiderAllFlagsAndIgnoredNames() {
        JsonIgnoreProperties.Value a = JsonIgnoreProperties.Value
                .forIgnoredProperties("x", "y")
                .withIgnoreUnknown()
                .withAllowGetters();

        JsonIgnoreProperties.Value b = JsonIgnoreProperties.Value
                .forIgnoredProperties("x", "y")
                .withIgnoreUnknown()
                .withAllowGetters();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        JsonIgnoreProperties.Value c = b.withoutIgnoreUnknown();
        assertNotEquals(a, c);
    }
}