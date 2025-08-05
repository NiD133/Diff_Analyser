package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections4.comparators.FixedOrderComparator;

/**
 * Test suite for FixedOrderComparator functionality.
 * Tests constructor behavior, comparison logic, locking mechanism, and unknown object handling.
 */
public class FixedOrderComparatorTest {

    // Test Data
    private static final String[] PLANETS = {"Mercury", "Venus", "Earth", "Mars"};
    private static final Integer[] NUMBERS = {1, 2, 3, 4, 5};

    // Constructor Tests
    
    @Test
    public void testDefaultConstructor() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        
        assertFalse("New comparator should not be locked", comparator.isLocked());
        assertEquals("Default behavior should be EXCEPTION", 
                    FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, 
                    comparator.getUnknownObjectBehavior());
    }

    @Test
    public void testConstructorWithArray() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        assertFalse("New comparator should not be locked", comparator.isLocked());
        // Verify order is maintained
        assertTrue("Mercury should come before Venus", 
                  comparator.compare("Mercury", "Venus") < 0);
        assertTrue("Venus should come before Earth", 
                  comparator.compare("Venus", "Earth") < 0);
    }

    @Test
    public void testConstructorWithList() {
        List<String> planetList = Arrays.asList(PLANETS);
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(planetList);
        
        assertFalse("New comparator should not be locked", comparator.isLocked());
        assertTrue("Earth should come before Mars", 
                  comparator.compare("Earth", "Mars") < 0);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullArray() {
        new FixedOrderComparator<>((String[]) null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullList() {
        new FixedOrderComparator<>((List<String>) null);
    }

    // Add Method Tests

    @Test
    public void testAddNewItem() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        
        boolean added = comparator.add("first");
        assertTrue("Adding new item should return true", added);
        
        boolean addedAgain = comparator.add("first");
        assertFalse("Adding existing item should return false", addedAgain);
    }

    @Test
    public void testAddAsEqual() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        boolean added = comparator.addAsEqual("Earth", "Terra");
        assertTrue("Adding new equal item should return true", added);
        
        assertEquals("Equal items should compare as equal", 
                    0, comparator.compare("Earth", "Terra"));
    }

    @Test
    public void testAddAsEqualSameObject() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        boolean added = comparator.addAsEqual("Earth", "Earth");
        assertFalse("Adding same object as equal should return false", added);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAsEqualWithUnknownExistingObject() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.addAsEqual("Unknown", "NewItem");
    }

    // Comparison Tests

    @Test
    public void testBasicComparison() {
        FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>(NUMBERS);
        
        assertTrue("1 should come before 3", comparator.compare(1, 3) < 0);
        assertTrue("5 should come after 2", comparator.compare(5, 2) > 0);
        assertEquals("Same objects should be equal", 0, comparator.compare(3, 3));
    }

    @Test
    public void testComparisonLocksComparator() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        assertFalse("Comparator should not be locked initially", comparator.isLocked());
        
        comparator.compare("Mercury", "Venus");
        
        assertTrue("Comparator should be locked after comparison", comparator.isLocked());
    }

    // Unknown Object Behavior Tests

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownObjectWithExceptionBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        // Default behavior is EXCEPTION
        comparator.compare("Unknown", "Mercury");
    }

    @Test
    public void testUnknownObjectWithAfterBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        
        // Unknown object should come after known objects
        assertTrue("Unknown object should come after known object", 
                  comparator.compare("Unknown", "Mercury") > 0);
        assertTrue("Known object should come before unknown object", 
                  comparator.compare("Venus", "Unknown") < 0);
    }

    @Test
    public void testUnknownObjectWithBeforeBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        
        // Unknown object should come before known objects
        assertTrue("Unknown object should come before known object", 
                  comparator.compare("Unknown", "Mercury") < 0);
        assertTrue("Known object should come after unknown object", 
                  comparator.compare("Venus", "Unknown") > 0);
    }

    @Test
    public void testBothObjectsUnknownWithAfterBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        
        assertEquals("Two unknown objects should be equal", 
                    0, comparator.compare("Unknown1", "Unknown2"));
    }

    @Test
    public void testNullObjectHandling() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        
        assertTrue("Null should come before known object", 
                  comparator.compare(null, "Mercury") < 0);
        assertTrue("Known object should come after null", 
                  comparator.compare("Venus", null) > 0);
    }

    // Locking Mechanism Tests

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAfterLocking() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // Lock the comparator by performing a comparison
        comparator.compare("Mercury", "Venus");
        
        // This should throw an exception
        comparator.add("Jupiter");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAsEqualAfterLocking() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // Lock the comparator
        comparator.compare("Mercury", "Venus");
        
        // This should throw an exception
        comparator.addAsEqual("Earth", "Terra");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetUnknownObjectBehaviorAfterLocking() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // Lock the comparator
        comparator.compare("Mercury", "Venus");
        
        // This should throw an exception
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCheckLockedAfterComparison() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // Lock the comparator
        comparator.compare("Mercury", "Venus");
        
        // This should throw an exception
        comparator.checkLocked();
    }

    @Test
    public void testCheckLockedBeforeComparison() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // This should not throw an exception
        comparator.checkLocked();
    }

    // Configuration Tests

    @Test(expected = NullPointerException.class)
    public void testSetNullUnknownObjectBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        comparator.setUnknownObjectBehavior(null);
    }

    @Test
    public void testGetUnknownObjectBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        
        assertEquals("Default behavior should be EXCEPTION", 
                    FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, 
                    comparator.getUnknownObjectBehavior());
        
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        assertEquals("Behavior should be updated to AFTER", 
                    FixedOrderComparator.UnknownObjectBehavior.AFTER, 
                    comparator.getUnknownObjectBehavior());
    }

    // Equals and HashCode Tests

    @Test
    public void testEquals() {
        FixedOrderComparator<String> comparator1 = new FixedOrderComparator<>();
        FixedOrderComparator<String> comparator2 = new FixedOrderComparator<>();
        
        assertTrue("Empty comparators should be equal", comparator1.equals(comparator2));
        assertTrue("Comparator should equal itself", comparator1.equals(comparator1));
        assertFalse("Comparator should not equal null", comparator1.equals(null));
        assertFalse("Comparator should not equal different type", comparator1.equals("string"));
    }

    @Test
    public void testEqualsWithDifferentItems() {
        FixedOrderComparator<String> comparator1 = new FixedOrderComparator<>(PLANETS);
        FixedOrderComparator<String> comparator2 = new FixedOrderComparator<>();
        
        assertFalse("Comparators with different items should not be equal", 
                   comparator1.equals(comparator2));
    }

    @Test
    public void testEqualsWithDifferentBehavior() {
        FixedOrderComparator<String> comparator1 = new FixedOrderComparator<>();
        FixedOrderComparator<String> comparator2 = new FixedOrderComparator<>();
        
        comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        
        assertFalse("Comparators with different behaviors should not be equal", 
                   comparator1.equals(comparator2));
    }

    @Test
    public void testHashCode() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(PLANETS);
        
        // Just verify hashCode doesn't throw an exception
        int hashCode = comparator.hashCode();
        assertNotNull("HashCode should be calculated", hashCode);
    }

    // Edge Cases and Error Conditions

    @Test(expected = StackOverflowError.class)
    public void testCircularReferenceInConstructor() {
        LinkedList<Object> list = new LinkedList<>();
        Object[] array = new Object[1];
        array[0] = list;
        list.add(array[0]); // Create circular reference
        
        new FixedOrderComparator<>(array);
    }

    @Test
    public void testRealWorldUsageExample() {
        // Demonstrate the intended usage as shown in the class documentation
        String[] planets = {"Mercury", "Venus", "Earth", "Mars"};
        FixedOrderComparator<String> distanceFromSun = new FixedOrderComparator<>(planets);
        
        // Sort to alphabetical order
        Arrays.sort(planets);
        assertEquals("Earth", planets[0]); // Alphabetically first
        
        // Sort back to original order using our comparator
        Arrays.sort(planets, distanceFromSun);
        assertEquals("Mercury", planets[0]); // Should be first by distance from sun
        assertEquals("Venus", planets[1]);
        assertEquals("Earth", planets[2]);
        assertEquals("Mars", planets[3]);
    }
}