package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class ComparableObjectItem_ESTest extends ComparableObjectItem_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testGetObjectReturnsNull() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, null);
        assertNull(item.getObject());
    }

    @Test(timeout = 4000)
    public void testGetComparableReturnsNotNull() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockComparable).toString();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, mockComparable);
        assertNotNull(item.getComparable());
    }

    @Test(timeout = 4000)
    public void testCompareToReturnsPositive() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(98).when(mockComparable).compareTo(any());
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, mockComparable);
        assertEquals(98, item.compareTo(item));
    }

    @Test(timeout = 4000)
    public void testCompareToReturnsNegative() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(-1236).when(mockComparable).compareTo(any());
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, mockComparable);
        assertEquals(-1236, item.compareTo(item));
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testHashCodeThrowsIllegalArgumentException() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockComparable).toString();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, mockComparable);
        item.getObject().hashCode();
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testEqualsThrowsIllegalArgumentException() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(null).when(mockComparable).toString();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, mockComparable);
        item.getObject().equals(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCompareToWithNullThrowsNullPointerException() {
        ComparableObjectItem item = new ComparableObjectItem("h", "h");
        item.compareTo(null);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testCompareToWithDifferentTypeThrowsClassCastException() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        ComparableObjectItem item1 = new ComparableObjectItem(mockComparable, null);
        ComparableObjectItem item2 = new ComparableObjectItem(item1, null);
        item2.compareTo(item1);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullComparableThrowsIllegalArgumentException() {
        new ComparableObjectItem(null, null);
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNonNullObject() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, null);
        item.setObject(new Object());
        item.hashCode();
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullObject() {
        Comparable<Object> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, null);
        item.hashCode();
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() {
        Object obj = new Object();
        ComparableObjectItem item1 = new ComparableObjectItem("^VA/-a*$;'d", obj);
        ComparableObjectItem item2 = new ComparableObjectItem("^VA/-a*$;'d", item1);
        assertFalse(item1.equals(item2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameObject() {
        Comparable<ComparableObjectItem> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        Object obj = new Object();
        ComparableObjectItem item1 = new ComparableObjectItem(mockComparable, obj);
        ComparableObjectItem item2 = new ComparableObjectItem(item1, null);
        ComparableObjectItem item3 = new ComparableObjectItem(item1, null);
        assertTrue(item2.equals(item3));
        assertFalse(item3.equals(item1));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSelf() {
        Comparable<ComparableObjectItem> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        Object obj = new Object();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, obj);
        assertTrue(item.equals(item));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentType() {
        Comparable<ComparableObjectItem> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        Object obj = new Object();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, obj);
        assertFalse(item.equals(obj));
    }

    @Test(timeout = 4000)
    public void testCompareToReturnsZero() {
        Comparable<ComparableObjectItem> mockComparable = mock(Comparable.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(mockComparable).compareTo(any(ComparableObjectItem.class));
        Object obj = new Object();
        ComparableObjectItem item = new ComparableObjectItem(mockComparable, obj);
        assertEquals(0, item.compareTo(item));
    }

    @Test(timeout = 4000)
    public void testCloneCreatesNewInstance() {
        ComparableObjectItem item = new ComparableObjectItem("^VA/-a*$;'d", "^VA/-a*$;'d");
        Object clone = item.clone();
        assertNotSame(item, clone);
    }
}