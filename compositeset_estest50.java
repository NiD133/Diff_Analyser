package org.apache.commons.collections4.set;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.ExceptionPredicate;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains focused tests for the CompositeSet class, particularly its behavior with empty sets.
 */
public class CompositeSetTest {

    @Test
    public void testRemoveAllFromEmptySetShouldReturnFalse() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();
        // The collection to remove is also empty, so no change should occur.
        CompositeSet<String> setToRemove = new CompositeSet<>();

        // Act
        boolean wasModified = emptySet.removeAll(setToRemove);

        // Assert
        assertFalse("removeAll should return false when the set is not modified", wasModified);
    }

    @Test
    public void testEqualsShouldReturnFalseForNonSetObject() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();
        Object otherObject = new Object();

        // Act
        boolean isEqual = emptySet.equals(otherObject);

        // Assert
        assertFalse("A CompositeSet should not be equal to a non-Set object", isEqual);
    }

    @Test
    public void testEqualsShouldReturnTrueForAnotherEmptySet() {
        // Arrange
        CompositeSet<String> setA = new CompositeSet<>();
        CompositeSet<String> setB = new CompositeSet<>();

        // Act & Assert
        assertTrue("Two empty CompositeSets should be equal", setA.equals(setB));
    }

    @Test
    public void testContainsOnEmptySetShouldReturnFalse() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();
        Object objectToFind = "testItem";

        // Act
        boolean contains = emptySet.contains(objectToFind);

        // Assert
        assertFalse("An empty CompositeSet should not contain any object", contains);
    }

    @Test
    public void testRemoveIfOnEmptySetShouldReturnFalseAndNotExecutePredicate() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();
        // Use a predicate that would throw an exception if its test() method were ever called.
        // This verifies that the predicate is not executed for an empty collection.
        Predicate<String> failingPredicate = ExceptionPredicate.exceptionPredicate();

        // Act
        boolean wasModified = emptySet.removeIf(failingPredicate);

        // Assert
        assertFalse("removeIf on an empty set should return false as nothing was removed", wasModified);
        // An implicit assertion is that no exception was thrown.
    }

    @Test
    public void testContainsNullOnEmptySetShouldReturnFalse() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();

        // Act
        boolean containsNull = emptySet.contains(null);

        // Assert
        assertFalse("An empty CompositeSet should not contain null", containsNull);
    }
}