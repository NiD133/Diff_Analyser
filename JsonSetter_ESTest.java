package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for JsonSetter.Value behavior.
 * Organized by feature area: factories, accessors, modifiers, merging, and std methods.
 */
public class JsonSetterValueTest {

    // --- Helper ---------------------------------------------------------------------

    private static void assertNullPolicies(JsonSetter.Value v, Nulls valueNulls, Nulls contentNulls) {
        assertEquals("valueNulls", valueNulls, v.getValueNulls());
        assertEquals("contentNulls", contentNulls, v.getContentNulls());
    }

    // --- Factory methods -------------------------------------------------------------

    @Test
    public void empty_hasDefaults() {
        JsonSetter.Value v = JsonSetter.Value.empty();

        assertNullPolicies(v, Nulls.DEFAULT, Nulls.DEFAULT);
        assertNull("nonDefaultValueNulls should be null for DEFAULT", v.nonDefaultValueNulls());
        assertNull("nonDefaultContentNulls should be null for DEFAULT", v.nonDefaultContentNulls());
    }

    @Test
    public void construct_setsBothPolicies() {
        JsonSetter.Value v = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);

        assertNullPolicies(v, Nulls.DEFAULT, Nulls.SKIP);
    }

    @Test
    public void forValueNulls_single_setsValueOnly() {
        JsonSetter.Value v = JsonSetter.Value.forValueNulls(Nulls.SET);

        assertNullPolicies(v, Nulls.SET, Nulls.DEFAULT);
    }

    @Test
    public void forValueNulls_both_setsValueAndContent() {
        JsonSetter.Value v = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        assertNullPolicies(v, Nulls.AS_EMPTY, Nulls.AS_EMPTY);
    }

    @Test
    public void forContentNulls_setsContentOnly() {
        JsonSetter.Value v = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        assertNullPolicies(v, Nulls.DEFAULT, Nulls.SKIP);
    }

    @Test
    public void from_nullAnnotation_isEmpty() {
        JsonSetter.Value v = JsonSetter.Value.from(null);

        assertNullPolicies(v, Nulls.DEFAULT, Nulls.DEFAULT);
    }

    // --- Accessors & helpers ---------------------------------------------------------

    @Test
    public void nonDefaultHelpers_returnNullForDefaults_andValuesOtherwise() {
        JsonSetter.Value defaults = JsonSetter.Value.empty();
        assertNull(defaults.nonDefaultValueNulls());
        assertNull(defaults.nonDefaultContentNulls());

        JsonSetter.Value nonDefaults = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.SKIP);
        assertEquals(Nulls.AS_EMPTY, nonDefaults.nonDefaultValueNulls());
        assertEquals(Nulls.SKIP, nonDefaults.nonDefaultContentNulls());
    }

    @Test
    public void valueFor_returnsAnnotationType() {
        JsonSetter.Value v = JsonSetter.Value.empty();

        assertEquals(JsonSetter.class, v.valueFor());
    }

    // --- Mutators (withXxx) ----------------------------------------------------------

    @Test
    public void withValueNulls_single_noChange_returnsSameInstance() {
        JsonSetter.Value v = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);

        JsonSetter.Value out = v.withValueNulls((Nulls) null);

        assertSame("No change => same instance expected", v, out);
    }

    @Test
    public void withContentNulls_null_noChange_returnsSameInstance() {
        JsonSetter.Value v = JsonSetter.Value.empty();

        JsonSetter.Value out = v.withContentNulls(null);

        assertSame("No change => same instance expected", v, out);
    }

    @Test
    public void withContentNulls_changesContent_andReturnsNewInstance() {
        JsonSetter.Value v = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);

        JsonSetter.Value out = v.withContentNulls(Nulls.DEFAULT);

        assertNotSame("Change => new instance expected", v, out);
        assertNullPolicies(out, Nulls.DEFAULT, Nulls.DEFAULT);
        // original left intact
        assertNullPolicies(v, Nulls.DEFAULT, Nulls.SKIP);
    }

    @Test
    public void withValueNulls_both_updatesBoth() {
        JsonSetter.Value v = JsonSetter.Value.empty();

        JsonSetter.Value out = v.withValueNulls(Nulls.DEFAULT, Nulls.SKIP);

        assertNullPolicies(out, Nulls.DEFAULT, Nulls.SKIP);
    }

    // --- Merging / overrides ---------------------------------------------------------

    @Test
    public void merge_prefersOverridesWhenDefined() {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        JsonSetter.Value overrides = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.SKIP);

        JsonSetter.Value merged = JsonSetter.Value.merge(base, overrides);

        assertNullPolicies(merged, Nulls.AS_EMPTY, Nulls.SKIP);
    }

    @Test
    public void merge_handlesNulls() {
        JsonSetter.Value v = JsonSetter.Value.forContentNulls(Nulls.SKIP);

        assertSame(v, JsonSetter.Value.merge(null, v));
        assertSame(v, JsonSetter.Value.merge(v, null));

        JsonSetter.Value e1 = JsonSetter.Value.empty();
        JsonSetter.Value e2 = JsonSetter.Value.empty();
        JsonSetter.Value merged = JsonSetter.Value.merge(e1, e2);
        // Still effectively empty
        assertNullPolicies(merged, Nulls.DEFAULT, Nulls.DEFAULT);
    }

    @Test
    public void withOverrides_appliesOnlyDefinedOverrides() {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);
        // Only valueNulls overridden; contentNulls left DEFAULT => keep base contentNulls
        JsonSetter.Value overrides = JsonSetter.Value.forValueNulls(Nulls.SKIP, Nulls.DEFAULT);

        JsonSetter.Value out = base.withOverrides(overrides);

        assertNullPolicies(out, Nulls.SKIP, Nulls.AS_EMPTY);
    }

    @Test
    public void withOverrides_null_returnsSameInstance() {
        JsonSetter.Value base = JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY, Nulls.AS_EMPTY);

        assertSame(base, base.withOverrides(null));
    }

    // --- readResolve / serialization helpers ----------------------------------------

    @Test
    public void readResolve_onEmptyReturnsCanonicalInstance() {
        JsonSetter.Value empty = JsonSetter.Value.empty();

        JsonSetter.Value resolved = (JsonSetter.Value) empty.readResolve();

        assertSame(JsonSetter.Value.empty(), resolved);
    }

    @Test
    public void readResolve_preservesValuesForNonEmpty() {
        JsonSetter.Value v = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.SKIP);

        JsonSetter.Value resolved = (JsonSetter.Value) v.readResolve();

        assertNullPolicies(resolved, Nulls.DEFAULT, Nulls.SKIP);
    }

    // --- Std methods ----------------------------------------------------------------

    @Test
    public void equals_hashCode_toString() {
        JsonSetter.Value a = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value b = JsonSetter.Value.construct(Nulls.DEFAULT, Nulls.DEFAULT);
        JsonSetter.Value c = JsonSetter.Value.construct(Nulls.SET, Nulls.DEFAULT);

        // equals
        assertEquals(a, a);
        assertEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, new Object());

        // hashCode consistent with equals
        assertEquals(a.hashCode(), b.hashCode());

        // toString format (stable in Jackson)
        assertEquals("JsonSetter.Value(valueNulls=DEFAULT,contentNulls=DEFAULT)", a.toString());
    }
}