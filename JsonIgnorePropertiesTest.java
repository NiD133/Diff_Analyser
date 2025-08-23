package com.fasterxml.jackson.annotation;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Focused tests for JsonIgnoreProperties.Value.
 * Organized by behavior: construction, equality, factories, mutators, merging, and representation.
 */
public class JsonIgnorePropertiesValueTest {

    @JsonIgnoreProperties(value = { "foo", "bar" }, ignoreUnknown = true)
    private static final class AnnotatedSample { }

    private static final JsonIgnoreProperties.Value EMPTY = JsonIgnoreProperties.Value.empty();

    // ---------- Construction and equality ----------

    @Test
    void emptyValue_defaultsAndSingleton() {
        // from(null) should return the shared EMPTY instance
        assertSame(EMPTY, JsonIgnoreProperties.Value.from(null));

        // All defaults for EMPTY
        assertState(EMPTY,
                Collections.emptySet(),
                false, // ignoreUnknown
                false, // allowGetters
                false, // allowSetters
                true   // merge
        );
    }

    @Test
    void equality_dependsOnMergeFlag() {
        assertEquals(EMPTY, EMPTY);
        // EMPTY carries merge=true; toggling withMerge() is a no-op
        assertSame(EMPTY, EMPTY.withMerge());

        JsonIgnoreProperties.Value noMerge = EMPTY.withoutMerge();
        assertEquals(noMerge, noMerge);

        // Different merge flags should make instances unequal
        assertNotEquals(EMPTY, noMerge);
        assertNotEquals(noMerge, EMPTY);
    }

    // ---------- Building from annotation ----------

    @Test
    void fromAnnotation_parsesAllAttributes() {
        JsonIgnoreProperties.Value v = JsonIgnoreProperties.Value.from(
                AnnotatedSample.class.getAnnotation(JsonIgnoreProperties.class));

        assertNotNull(v);
        assertState(v,
                setOf("foo", "bar"),
                true,   // ignoreUnknown specified on annotation
                false,  // allowGetters default
                false,  // allowSetters default
                false   // merge off when built from annotation
        );
    }

    // ---------- Factories ----------

    @Test
    void factoryShortcuts_returnEmptyWhenNoChange() {
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoreUnknown(false));
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties());
        assertSame(EMPTY, JsonIgnoreProperties.Value.forIgnoredProperties(Collections.emptySet()));
    }

    @Test
    void allowGettersAndSetters_affectFindIgnoredQueries() {
        JsonIgnoreProperties.Value base = JsonIgnoreProperties.Value.forIgnoredProperties("a", "b");
        assertEquals(setOf("a", "b"), base.getIgnored());

        JsonIgnoreProperties.Value allowGetters = base.withAllowGetters();
        assertTrue(allowGetters.getAllowGetters());
        assertFalse(allowGetters.getAllowSetters());
        assertEquals(setOf("a", "b"), allowGetters.getIgnored());
        assertEquals(setOf("a", "b"), allowGetters.findIgnoredForDeserialization());
        assertEquals(setOf(), allowGetters.findIgnoredForSerialization());

        JsonIgnoreProperties.Value allowSetters = base.withAllowSetters();
        assertFalse(allowSetters.getAllowGetters());
        assertTrue(allowSetters.getAllowSetters());
        assertEquals(setOf("a", "b"), allowSetters.getIgnored());
        assertEquals(setOf(), allowSetters.findIgnoredForDeserialization());
        assertEquals(setOf("a", "b"), allowSetters.findIgnoredForSerialization());
    }

    // ---------- Mutator methods (fluent "with/without") ----------

    @Test
    void mutators_toggleFlagsAndCollections() {
        assertEquals(2, EMPTY.withIgnored("a", "b").getIgnored().size());
        assertEquals(1, EMPTY.withIgnored(Collections.singleton("x")).getIgnored().size());
        assertEquals(0, EMPTY.withIgnored((Set<String>) null).getIgnored().size());

        assertTrue(EMPTY.withIgnoreUnknown().getIgnoreUnknown());
        assertFalse(EMPTY.withoutIgnoreUnknown().getIgnoreUnknown());

        assertTrue(EMPTY.withAllowGetters().getAllowGetters());
        assertFalse(EMPTY.withoutAllowGetters().getAllowGetters());
        assertTrue(EMPTY.withAllowSetters().getAllowSetters());
        assertFalse(EMPTY.withoutAllowSetters().getAllowSetters());

        assertTrue(EMPTY.withMerge().getMerge());
        assertFalse(EMPTY.withoutMerge().getMerge());
    }

    // ---------- Merging semantics ----------

    @Test
    void merge_withOverridesVsReplace() {
        JsonIgnoreProperties.Value base = EMPTY.withIgnoreUnknown().withAllowGetters();
        JsonIgnoreProperties.Value overridesMerge = EMPTY.withMerge().withIgnored("a");
        JsonIgnoreProperties.Value overridesReplace = EMPTY.withoutMerge();

        // With merge: union of ignored and OR of flags
        JsonIgnoreProperties.Value merged = base.withOverrides(overridesMerge);
        assertEquals(Collections.singleton("a"), merged.getIgnored());
        assertTrue(merged.getIgnoreUnknown());
        assertTrue(merged.getAllowGetters());
        assertFalse(merged.getAllowSetters());

        // Without merge: overrides replace base entirely
        JsonIgnoreProperties.Value replaced = JsonIgnoreProperties.Value.merge(base, overridesReplace);
        assertEquals(Collections.emptySet(), replaced.getIgnored());
        assertFalse(replaced.getIgnoreUnknown());
        assertFalse(replaced.getAllowGetters());
        assertFalse(replaced.getAllowSetters());
        assertEquals(overridesReplace, replaced);

        // withOverrides(null) and withOverrides(EMPTY) are no-ops
        assertSame(overridesReplace, overridesReplace.withOverrides(null));
        assertSame(overridesReplace, overridesReplace.withOverrides(EMPTY));
    }

    @Test
    void mergeAll_unionsIgnoredSets() {
        JsonIgnoreProperties.Value v1 = EMPTY.withIgnored("a");
        JsonIgnoreProperties.Value v2 = EMPTY.withIgnored("b");
        JsonIgnoreProperties.Value v3 = EMPTY.withIgnored("c");

        JsonIgnoreProperties.Value merged = JsonIgnoreProperties.Value.mergeAll(v1, v2, v3);
        Set<String> all = merged.getIgnored();
        assertEquals(3, all.size());
        assertTrue(all.contains("a"));
        assertTrue(all.contains("b"));
        assertTrue(all.contains("c"));
    }

    // ---------- Representation ----------

    @Test
    void toStringAndHashCode_stable() {
        String expected = "JsonIgnoreProperties.Value(ignored=[],ignoreUnknown=false,allowGetters=false,allowSetters=true,merge=true)";
        assertEquals(expected, EMPTY.withAllowSetters().withMerge().toString());

        // Non-zero hashCode (basic sanity check)
        int hash = EMPTY.hashCode();
        if (hash == 0) {
            fail("hashCode should not be 0");
        }
    }

    // ---------- Helpers ----------

    private static void assertState(JsonIgnoreProperties.Value v,
                                    Set<String> ignored,
                                    boolean ignoreUnknown,
                                    boolean allowGetters,
                                    boolean allowSetters,
                                    boolean merge) {
        assertEquals(ignored, v.getIgnored());
        assertEquals(ignoreUnknown, v.getIgnoreUnknown());
        assertEquals(allowGetters, v.getAllowGetters());
        assertEquals(allowSetters, v.getAllowSetters());
        assertEquals(merge, v.getMerge());
    }

    private static Set<String> setOf(String... values) {
        if (values == null || values.length == 0) {
            return Collections.emptySet();
        }
        return new LinkedHashSet<>(Arrays.asList(values));
    }
}