package org.jfree.data;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.lang.Comparable;

/**
 * Unit tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest {

    /**
     * Verifies that the getComparable() method correctly returns the
     * 'comparable' object that was provided in the constructor.
     */
    @Test
    public void getComparable_shouldReturnObjectProvidedInConstructor() {
        // Arrange: Create a mock Comparable object to pass to the constructor.
        // The second argument (the 'object' part of the item) is not relevant for this test.
        Comparable<String> expectedComparable = mock(Comparable.class);
        Object dummyObject = new Object();
        ComparableObjectItem item = new ComparableObjectItem(expectedComparable, dummyObject);

        // Act: Call the method under test.
        Comparable<?> actualComparable = item.getComparable();

        // Assert: The returned object should be the exact same instance
        // that was passed to the constructor.
        assertSame("The getter should return the same instance provided to the constructor.",
                expectedComparable, actualComparable);
    }
}