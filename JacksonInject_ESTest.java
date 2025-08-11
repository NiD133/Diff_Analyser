package com.fasterxml.jackson.annotation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable tests for JacksonInject.Value focusing on:
 * - empty vs non-empty instances
 * - id handling (including blank String)
 * - immutability of withX(...) methods (return same vs new instances)
 * - equality and toString contract
 * - willUseInput defaulting behavior
 */
public class JacksonInjectValueTest {

    @Test
    public void empty_hasNoId_and_nullFlags() {
        JacksonInject.Value v = JacksonInject.Value.empty();

        assertFalse(v.hasId());
        assertNull(v.getId());
        assertNull(v.getUseInput());
        assertNull(v.getOptional());
    }

    @Test
    public void forId_setsId_and_hasIdIsTrue() {
        Object id = new Object();

        JacksonInject.Value v = JacksonInject.Value.forId(id);

        assertTrue(v.hasId());
        assertSame(id, v.getId());
    }

    @Test
    public void construct_withBlankStringId_isTreatedAsNoId() {
        JacksonInject.Value v = JacksonInject.Value.construct("", Boolean.FALSE, Boolean.FALSE);

        assertFalse(v.hasId());
    }

    @Test
    public void withId_sameId_returnsSameInstance() {
        Object id = new Object();
        JacksonInject.Value v = JacksonInject.Value.forId(id);

        JacksonInject.Value same = v.withId(id);

        assertSame(v, same);
    }

    @Test
    public void withId_null_clearsId_butKeepsOtherFlags() {
        JacksonInject.Value original = JacksonInject.Value.construct(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);

        JacksonInject.Value cleared = original.withId(null);

        assertNotSame(original, cleared);
        assertFalse(cleared.hasId());
        assertEquals(original.getUseInput(), cleared.getUseInput());
        assertEquals(original.getOptional(), cleared.getOptional());
    }

    @Test
    public void withUseInput_sameValue_returnsSameInstance() {
        JacksonInject.Value v = JacksonInject.Value.construct(new Object(), Boolean.FALSE, Boolean.FALSE);

        JacksonInject.Value same = v.withUseInput(Boolean.FALSE);

        assertSame(v, same);
    }

    @Test
    public void withOptional_sameValue_returnsSameInstance() {
        JacksonInject.Value v = JacksonInject.Value.construct(new Object(), Boolean.FALSE, Boolean.FALSE);

        JacksonInject.Value same = v.withOptional(Boolean.FALSE);

        assertSame(v, same);
    }

    @Test
    public void withUseInput_changedValue_returnsNewInstance_andNotEqual() {
        JacksonInject.Value base = JacksonInject.Value.forId(new Object());

        JacksonInject.Value changed = base.withUseInput(Boolean.FALSE);

        assertNotSame(base, changed);
        assertNotEquals(base, changed);
        assertTrue(changed.hasId());
    }

    @Test
    public void chainWithers_canRoundTripBackToEmpty() {
        JacksonInject.Value empty = JacksonInject.Value.empty();

        JacksonInject.Value roundTripped = empty.withUseInput(Boolean.FALSE)
                                               .withUseInput(null);

        assertEquals(empty, roundTripped);
    }

    @Test
    public void willUseInput_prefersExplicitSettingOverDefault() {
        JacksonInject.Value explicitFalse = JacksonInject.Value.construct(null, Boolean.FALSE, null);

        assertFalse(explicitFalse.willUseInput(true));
        assertFalse(explicitFalse.willUseInput(false));
    }

    @Test
    public void willUseInput_usesProvidedDefaultWhenUnset() {
        JacksonInject.Value unset = JacksonInject.Value.construct(null, null, null);

        assertTrue(unset.willUseInput(true));
        assertFalse(unset.willUseInput(false));
    }

    @Test
    public void equality_reflexive_and_sameContentEqual_withSameHashCode() {
        Object id = new Object();
        JacksonInject.Value a = JacksonInject.Value.construct(id, Boolean.TRUE, Boolean.FALSE);
        JacksonInject.Value b = JacksonInject.Value.construct(id, Boolean.TRUE, Boolean.FALSE);

        assertEquals(a, a); // reflexive
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equality_differentIds_notEqual() {
        JacksonInject.Value v1 = JacksonInject.Value.forId(new Object());
        JacksonInject.Value v2 = JacksonInject.Value.forId(new Object());

        assertNotEquals(v1, v2);
    }

    @Test
    public void toString_containsAllFields_inExpectedFormat() {
        JacksonInject.Value v = JacksonInject.Value.forId(null).withOptional(Boolean.FALSE);

        assertEquals("JacksonInject.Value(id=null,useInput=null,optional=false)", v.toString());
    }

    @Test
    public void fromNull_returnsEmpty() {
        JacksonInject.Value fromNull = JacksonInject.Value.from(null);

        assertEquals(JacksonInject.Value.empty(), fromNull);
        assertNull(fromNull.getOptional());
    }

    @Test
    public void valueFor_returnsAnnotationTypeInterface() {
        Class<JacksonInject> type = JacksonInject.Value.empty().valueFor();

        assertTrue(type.isInterface());
    }
}