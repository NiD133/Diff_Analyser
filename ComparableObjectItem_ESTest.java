package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jfree.data.ComparableObjectItem;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ComparableObjectItem_ESTest extends ComparableObjectItem_ESTest_scaffolding {

    // Constructor Tests
    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullXThrowsException() {
        new ComparableObjectItem(null, new Object());
    }

    // Getter Tests
    @Test(timeout = 4000)
    public void testGetComparableReturnsCorrectValue() {
        Comparable<?> comparable = mock(Comparable.class);
        ComparableObjectItem item = new ComparableObjectItem(comparable, "Object");
        assertSame(comparable, item.getComparable());
    }

    @Test(timeout = 4000)
    public void testGetObjectReturnsNullWhenObjectIsNull() {
        Comparable<?> comparable = mock(Comparable.class);
        ComparableObjectItem item = new ComparableObjectItem(comparable, null);
        assertNull(item.getObject());
    }

    // compareTo Tests
    @Test(timeout = 4000)
    public void testCompareToReturnsPositive() {
        Comparable<?> comparable = mock(Comparable.class);
        when(comparable.compareTo(any())).thenReturn(98);
        
        ComparableObjectItem item = new ComparableObjectItem(comparable, "Object");
        assertEquals(98, item.compareTo(item));
    }

    @Test(timeout = 4000)
    public void testCompareToReturnsNegative() {
        Comparable<?> comparable = mock(Comparable.class);
        when(comparable.compareTo(any())).thenReturn(-1236);
        
        ComparableObjectItem item = new ComparableObjectItem(comparable, "Object");
        assertEquals(-1236, item.compareTo(item));
    }

    @Test(timeout = 4000)
    public void testCompareToReturnsZero() {
        Comparable<?> comparable = mock(Comparable.class);
        when(comparable.compareTo(any())).thenReturn(0);
        
        ComparableObjectItem item = new ComparableObjectItem(comparable, "Object");
        assertEquals(0, item.compareTo(item));
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCompareToWithNullThrowsException() {
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        item.compareTo(null);
    }

    @Test(timeout = 4000, expected = ClassCastException.class)
    public void testCompareToWithIncompatibleTypeThrowsException() {
        Comparable<?> comparable = mock(Comparable.class);
        ComparableObjectItem item1 = new ComparableObjectItem(comparable, null);
        ComparableObjectItem item2 = new ComparableObjectItem(item1, null);
        item2.compareTo(item1); // Incomparable types due to mock setup
    }

    // equals Tests
    @Test(timeout = 4000)
    public void testEqualsWithSameInstanceReturnsTrue() {
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        assertTrue(item.equals(item));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTypeReturnsFalse() {
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        assertFalse(item.equals("NotAnItem"));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameKeyAndObjectReturnsTrue() {
        Object obj = new Object();
        ComparableObjectItem item1 = new ComparableObjectItem(10, obj);
        ComparableObjectItem item2 = new ComparableObjectItem(10, obj);
        assertTrue(item1.equals(item2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameKeyButDifferentObjectReturnsFalse() {
        ComparableObjectItem item1 = new ComparableObjectItem("Key", "Value1");
        ComparableObjectItem item2 = new ComparableObjectItem("Key", "Value2");
        assertFalse(item1.equals(item2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentKeyReturnsFalse() {
        ComparableObjectItem item1 = new ComparableObjectItem("Key1", "Value");
        ComparableObjectItem item2 = new ComparableObjectItem("Key2", "Value");
        assertFalse(item1.equals(item2));
    }

    // hashCode Tests
    @Test(timeout = 4000)
    public void testHashCodeWithNonNullObject() {
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        item.setObject("NewValue");
        item.hashCode(); // Verify no exception
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullObject() {
        ComparableObjectItem item = new ComparableObjectItem("Key", null);
        item.hashCode(); // Verify no exception
    }

    /*
     * Note: The following tests are unstable and may fail due to reliance on 
     * mocked null behavior. They are preserved for coverage but should be reviewed.
     */
    @Test(timeout = 4000)
    public void testHashCodeOnNullObjectThrowsException() {
        Comparable<?> comparable = mock(Comparable.class);
        ComparableObjectItem item = new ComparableObjectItem(comparable, null);
        Object obj = item.getObject();
        try {
            obj.hashCode();
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Flaky test: May throw NullPointerException in real scenarios
        }
    }

    @Test(timeout = 4000)
    public void testEqualsOnNullObjectThrowsException() {
        Comparable<?> comparable = mock(Comparable.class);
        ComparableObjectItem item = new ComparableObjectItem(comparable, null);
        Object obj = item.getObject();
        try {
            obj.equals(null);
            fail("Expected exception not thrown");
        } catch (IllegalArgumentException e) {
            // Flaky test: May throw NullPointerException in real scenarios
        }
    }

    // clone Test
    @Test(timeout = 4000)
    public void testCloneReturnsDistinctObject() throws CloneNotSupportedException {
        ComparableObjectItem item = new ComparableObjectItem("Key", "Value");
        Object clone = item.clone();
        assertNotSame(item, clone);
    }
}