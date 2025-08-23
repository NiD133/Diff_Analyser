package org.jfree.data;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Readable unit tests for ComparableObjectItem.
 * 
 * These tests avoid mocking and focus on the specified behavior of:
 * - constructor preconditions
 * - accessors and mutators
 * - compareTo ordering
 * - equals/hashCode contract
 * - cloning
 */
public class ComparableObjectItemTest {

    // ----------------------------------------------------------------------
    // Constructor and basic accessors/mutators
    // ----------------------------------------------------------------------

    @Test
    public void constructor_rejectsNullX() {
        try {
            new ComparableObjectItem(null, "y");
            fail("Expected IllegalArgumentException for null x");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Null 'x' argument"));
        }
    }

    @Test
    public void getters_returnValuesPassedToConstructor() {
        ComparableObjectItem item = new ComparableObjectItem(Integer.valueOf(10), "y");
        assertEquals(Integer.valueOf(10), item.getComparable());
        assertEquals("y", item.getObject());
    }

    @Test
    public void setObject_updatesY_and_allowsNull() {
        ComparableObjectItem item = new ComparableObjectItem(1, "a");
        item.setObject("b");
        assertEquals("b", item.getObject());

        item.setObject(null);
        assertNull(item.getObject());
    }

    // ----------------------------------------------------------------------
    // compareTo: ordering and error cases
    // ----------------------------------------------------------------------

    @Test
    public void compareTo_ordersByX_only() {
        ComparableObjectItem a = new ComparableObjectItem(1, "a");
        ComparableObjectItem b = new ComparableObjectItem(2, "b");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }

    @Test
    public void compareTo_equalWhenXEqual_evenIfYDiffers() {
        ComparableObjectItem c1 = new ComparableObjectItem(3, "x");
        ComparableObjectItem c2 = new ComparableObjectItem(3, "y");
        assertEquals(0, c1.compareTo(c2));
        assertEquals(0, c2.compareTo(c1));
    }

    @Test
    public void compareTo_nullOther_throwsNullPointerException() {
        ComparableObjectItem item = new ComparableObjectItem(1, "a");
        try {
            item.compareTo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // expected
        }
    }

    @Test
    public void compareTo_incompatibleXTypes_throwsClassCastException() {
        ComparableObjectItem i = new ComparableObjectItem(1, "a");     // x is Integer
        ComparableObjectItem s = new ComparableObjectItem("1", "b");   // x is String
        try {
            i.compareTo(s); // Integer.compareTo(String) -> ClassCastException
            fail("Expected ClassCastException due to incompatible x types");
        } catch (ClassCastException expected) {
            // expected
        }
    }

    // ----------------------------------------------------------------------
    // equals and hashCode
    // ----------------------------------------------------------------------

    @Test
    public void equals_reflexive_true() {
        ComparableObjectItem item = new ComparableObjectItem(1, "a");
        assertTrue(item.equals(item));
    }

    @Test
    public void equals_null_false() {
        ComparableObjectItem item = new ComparableObjectItem(1, "a");
        assertFalse(item.equals(null));
    }

    @Test
    public void equals_differentType_false() {
        ComparableObjectItem item = new ComparableObjectItem(1, "a");
        assertFalse(item.equals("not a ComparableObjectItem"));
    }

    @Test
    public void equals_sameXAndY_true() {
        ComparableObjectItem a = new ComparableObjectItem(1, "a");
        ComparableObjectItem b = new ComparableObjectItem(1, "a");
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_differentX_false() {
        ComparableObjectItem a = new ComparableObjectItem(1, "a");
        ComparableObjectItem b = new ComparableObjectItem(2, "a");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void equals_differentY_false() {
        ComparableObjectItem a = new ComparableObjectItem(1, "a");
        ComparableObjectItem b = new ComparableObjectItem(1, "b");
        assertFalse(a.equals(b));
        assertFalse(b.equals(a));
    }

    @Test
    public void hashCode_handlesNullY() {
        ComparableObjectItem item = new ComparableObjectItem(1, null);
        // Just ensure no exception and that equals/hashCode are consistent.
        ComparableObjectItem same = new ComparableObjectItem(1, null);
        assertEquals(item, same);
        assertEquals(item.hashCode(), same.hashCode());
    }

    // ----------------------------------------------------------------------
    // clone
    // ----------------------------------------------------------------------

    @Test
    public void clone_returnsEqualButDistinctInstance() throws CloneNotSupportedException {
        ComparableObjectItem original = new ComparableObjectItem(1, "a");
        ComparableObjectItem copy = (ComparableObjectItem) original.clone();

        assertNotSame(original, copy);
        assertEquals(original, copy);

        // Changing original's y should not change the clone (shallow copy of references).
        original.setObject("b");
        assertNotEquals(original, copy);
        assertEquals(Integer.valueOf(1), copy.getComparable());
        assertEquals("a", copy.getObject());
    }
}