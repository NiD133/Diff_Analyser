package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;

/**
 * This test was improved for understandability.
 * The original was a generated test case for the ShapeCollection class.
 */
public class ShapeCollection_ESTestTest40 { // Retaining original class name for context

    /**
     * Tests that {@link ShapeCollection#removeIf(Predicate)} returns false
     * when the provided predicate does not match any element in the collection.
     *
     * This scenario is created by using a predicate that checks for equality with the
     * collection object itself. Since a collection does not contain itself as an element,
     * the predicate will never be satisfied, and no elements should be removed.
     */
    @Test
    public void removeIfShouldReturnFalseWhenPredicateMatchesNoElements() {
        // Arrange
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create an inner shape collection, which will be the single element
        // in our main collection under test.
        ShapeCollection<JtsPoint> innerCollection = new ShapeCollection<>(
                new ArrayList<>(), spatialContext
        );

        // Create the main ShapeCollection containing the inner collection.
        // The list must implement RandomAccess, so ArrayList is a good choice.
        List<ShapeCollection<JtsPoint>> shapes = new ArrayList<>();
        shapes.add(innerCollection);
        ShapeCollection<ShapeCollection<JtsPoint>> collectionUnderTest =
                new ShapeCollection<>(shapes, spatialContext);

        // Create a predicate that checks for equality with the main collection itself.
        // This predicate will be tested against the elements *within* the collection
        // (i.e., against `innerCollection`), not the collection itself.
        Predicate<Object> nonMatchingPredicate = Predicate.isEqual(collectionUnderTest);

        // Act
        // Attempt to remove elements using the predicate. Since the predicate will not
        // match any element, the collection should remain unchanged.
        boolean wasModified = collectionUnderTest.removeIf(nonMatchingPredicate);

        // Assert
        // The removeIf method should return false, indicating that the collection was not modified.
        assertFalse("The collection should not be modified when the predicate matches no elements.", wasModified);
    }
}