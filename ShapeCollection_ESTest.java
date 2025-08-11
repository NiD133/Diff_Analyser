package org.locationtech.spatial4j.shape;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.GeodesicSphereDistCalc;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.locationtech.spatial4j.shape.SpatialRelation;
import org.locationtech.spatial4j.shape.impl.PointImpl;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

/**
 * Test suite for ShapeCollection functionality including:
 * - Basic collection operations (size, get, etc.)
 * - Spatial operations (area calculation, buffering, spatial relations)
 * - Bounding box computation
 * - Edge cases and error conditions
 */
public class ShapeCollectionTest {

    private static final SpatialContext GEO_CONTEXT = SpatialContext.GEO;
    private static final double DELTA = 0.01;

    // ========== Area Calculation Tests ==========

    @Test(expected = NullPointerException.class)
    public void testGetArea_WithNullShape_ThrowsException() {
        // Given: A collection containing a null shape
        List<JtsPoint> shapesWithNull = new ArrayList<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        // When: Getting area with different context
        SpatialContext differentContext = new SpatialContext(new SpatialContextFactory());
        
        // Then: Should throw NullPointerException
        collection.getArea(differentContext);
    }

    @Test
    public void testGetArea_EmptyCollection_ReturnsZero() {
        // Given: An empty shape collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Getting the area
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        double area = emptyCollection.getArea(context);
        
        // Then: Area should be zero
        assertEquals("Empty collection should have zero area", 0.0, area, DELTA);
    }

    @Test
    public void testGetArea_NestedCollection_ReturnsZero() {
        // Given: A nested collection (collection containing another empty collection)
        List<JtsPoint> innerShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> innerCollection = new ShapeCollection<>(innerShapes, GEO_CONTEXT);
        
        List<ShapeCollection<JtsPoint>> outerShapes = new ArrayList<>();
        outerShapes.add(innerCollection);
        ShapeCollection<ShapeCollection<JtsPoint>> nestedCollection = 
            new ShapeCollection<>(outerShapes, GEO_CONTEXT);
        
        // When: Getting the area
        double area = nestedCollection.getArea(GEO_CONTEXT);
        
        // Then: Area should be zero
        assertEquals("Nested empty collection should have zero area", 0.0, area, DELTA);
    }

    // ========== Mutual Disjoint Tests ==========

    @Test
    public void testComputeMutualDisjoint_EmptyCollections_ReturnsTrue() {
        // Given: Two empty collections
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> collection1 = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        ShapeCollection<JtsPoint> collection2 = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        List<ShapeCollection<JtsPoint>> collections = new ArrayList<>();
        collections.add(collection1);
        collections.add(collection2);
        
        // When: Computing mutual disjoint
        boolean areMutuallyDisjoint = ShapeCollection.computeMutualDisjoint(collections);
        
        // Then: Should return true (empty collections are disjoint)
        assertTrue("Empty collections should be mutually disjoint", areMutuallyDisjoint);
    }

    @Test
    public void testComputeMutualDisjoint_SingleEmptyCollection_ReturnsTrue() {
        // Given: A single empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> singleCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Computing mutual disjoint for single collection
        boolean isMutuallyDisjoint = ShapeCollection.computeMutualDisjoint(singleCollection);
        
        // Then: Should return true
        assertTrue("Single empty collection should be mutually disjoint", isMutuallyDisjoint);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeMutualDisjoint_NullList_ThrowsException() {
        // When: Passing null list to computeMutualDisjoint
        // Then: Should throw NullPointerException
        ShapeCollection.computeMutualDisjoint(null);
    }

    // ========== Buffering Tests ==========

    @Test(expected = NullPointerException.class)
    public void testGetBuffered_NullContext_ThrowsException() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Getting buffered shape with null context
        // Then: Should throw NullPointerException
        collection.getBuffered(100.0, null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBuffered_CollectionWithNullShape_ThrowsException() {
        // Given: A collection containing a null shape
        List<JtsPoint> shapesWithNull = new ArrayList<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        // When: Getting buffered shape
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        
        // Then: Should throw NullPointerException
        collection.getBuffered(0.0, context);
    }

    @Test
    public void testGetBuffered_EmptyCollection_ReturnsEmptyCollection() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Getting buffered shape
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection bufferedCollection = emptyCollection.getBuffered(0.0, context);
        
        // Then: Should return empty collection
        assertEquals("Buffered empty collection should be empty", 0, bufferedCollection.size());
    }

    @Test
    public void testGetBuffered_NestedCollection_PreservesStructure() {
        // Given: A nested collection structure
        List<JtsPoint> innerShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> innerCollection = new ShapeCollection<>(innerShapes, GEO_CONTEXT);
        
        List<ShapeCollection<JtsPoint>> outerShapes = new ArrayList<>();
        outerShapes.add(innerCollection);
        ShapeCollection<ShapeCollection<JtsPoint>> nestedCollection = 
            new ShapeCollection<>(outerShapes, GEO_CONTEXT);
        
        // When: Getting buffered shape
        ShapeCollection bufferedCollection = nestedCollection.getBuffered(100.0, GEO_CONTEXT);
        
        // Then: Should preserve the structure
        assertEquals("Buffered nested collection should maintain size", 1, bufferedCollection.size());
    }

    // ========== Bounding Box Tests ==========

    @Test(expected = NullPointerException.class)
    public void testComputeBoundingBox_WithNullShape_ThrowsException() {
        // Given: A collection with null shape
        List<JtsPoint> shapesWithNull = new ArrayList<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        // When: Computing bounding box
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        
        // Then: Should throw NullPointerException
        collection.computeBoundingBox(shapesWithNull, context);
    }

    @Test
    public void testComputeBoundingBox_EmptyCollection_ReturnsNaNBounds() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Computing bounding box
        SpatialContext context = new SpatialContextFactory().newSpatialContext();
        Rectangle boundingBox = emptyCollection.computeBoundingBox(emptyShapes, context);
        
        // Then: All bounds should be NaN
        assertTrue("Empty collection bounding box should have NaN bounds", 
                   Double.isNaN(boundingBox.getMaxY()) && 
                   Double.isNaN(boundingBox.getMaxX()) && 
                   Double.isNaN(boundingBox.getMinX()) && 
                   Double.isNaN(boundingBox.getMinY()));
    }

    // ========== Collection Operations Tests ==========

    @Test
    public void testSize_EmptyCollection_ReturnsZero() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Getting size
        int size = emptyCollection.size();
        
        // Then: Size should be zero
        assertEquals("Empty collection should have size 0", 0, size);
    }

    @Test
    public void testSize_CollectionWithNullElement_ReturnsCorrectSize() {
        // Given: A collection with one null element
        List<JtsPoint> shapesWithNull = new Vector<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        // When: Getting size
        int size = collection.size();
        
        // Then: Size should be 1
        assertEquals("Collection with null element should have size 1", 1, size);
    }

    @Test
    public void testGetShapes_EmptyCollection_ReturnsEmptyList() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Getting shapes
        List<JtsPoint> shapes = emptyCollection.getShapes();
        
        // Then: Should return empty list
        assertTrue("Empty collection should return empty shapes list", shapes.isEmpty());
    }

    @Test
    public void testGetShapes_CollectionWithElements_ReturnsNonEmptyList() {
        // Given: A collection with null elements
        List<JtsPoint> shapesWithNulls = new ArrayList<>();
        shapesWithNulls.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNulls, GEO_CONTEXT);
        
        // When: Getting shapes
        List<JtsPoint> shapes = collection.getShapes();
        
        // Then: Should return non-empty list
        assertFalse("Collection with elements should return non-empty shapes list", shapes.isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBounds_ThrowsException() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Accessing element at invalid index
        // Then: Should throw IndexOutOfBoundsException
        emptyCollection.get(1341);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGet_NegativeIndex_ThrowsException() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // When: Accessing element at negative index
        // Then: Should throw ArrayIndexOutOfBoundsException
        emptyCollection.get(-1151);
    }

    @Test
    public void testGet_ValidIndex_ReturnsElement() {
        // Given: A collection with multiple null elements
        List<JtsPoint> shapesWithNulls = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            shapesWithNulls.add(null);
        }
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNulls, GEO_CONTEXT);
        
        // When: Getting element at valid index
        JtsPoint element = collection.get(10);
        
        // Then: Should return the element (null in this case)
        assertNull("Element at index 10 should be null", element);
    }

    // ========== Constructor Tests ==========

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullContext_ThrowsException() {
        // Given: A list of shapes and null context
        List<JtsPoint> shapes = new ArrayList<>();
        
        // When: Creating ShapeCollection with null context
        // Then: Should throw NullPointerException
        new ShapeCollection<>(shapes, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_NonRandomAccessList_ThrowsException() {
        // Given: A non-RandomAccess list (using existing collection as list)
        List<JtsPoint> shapes = new Vector<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> existingCollection = new ShapeCollection<>(shapes, context);
        
        // When: Creating ShapeCollection with non-RandomAccess list
        // Then: Should throw IllegalArgumentException
        new ShapeCollection<>(existingCollection, context);
    }

    // ========== Equality and Hash Tests ==========

    @Test
    public void testEquals_SameCollections_ReturnsTrue() {
        // Given: Two identical collections
        List<JtsPoint> shapes = new Vector<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> collection1 = new ShapeCollection<>(shapes, context);
        ShapeCollection<JtsPoint> collection2 = new ShapeCollection<>(shapes, context);
        
        // When: Comparing for equality
        boolean areEqual = collection1.equals(collection2);
        
        // Then: Should be equal
        assertTrue("Identical collections should be equal", areEqual);
    }

    @Test
    public void testEquals_DifferentObjectType_ReturnsFalse() {
        // Given: A collection and a different object type
        List<JtsPoint> shapes = new ArrayList<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapes, context);
        
        // When: Comparing with different object type
        boolean areEqual = collection.equals(context);
        
        // Then: Should not be equal
        assertFalse("Collection should not equal different object type", areEqual);
    }

    @Test
    public void testEquals_Null_ReturnsFalse() {
        // Given: A collection
        List<JtsPoint> shapes = new ArrayList<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapes, context);
        
        // When: Comparing with null
        boolean areEqual = collection.equals(null);
        
        // Then: Should not be equal
        assertFalse("Collection should not equal null", areEqual);
    }

    @Test
    public void testEquals_SameInstance_ReturnsTrue() {
        // Given: A collection
        List<JtsPoint> shapes = new Vector<>();
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapes, GEO_CONTEXT);
        
        // When: Comparing with itself
        boolean areEqual = collection.equals(collection);
        
        // Then: Should be equal
        assertTrue("Collection should equal itself", areEqual);
    }

    // ========== String Representation Tests ==========

    @Test
    public void testToString_LargeCollection_ShowsEllipsis() {
        // Given: A collection with many null elements (more than display limit)
        List<JtsPoint> manyNulls = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            manyNulls.add(null);
        }
        ShapeCollection<JtsPoint> largeCollection = new ShapeCollection<>(manyNulls, GEO_CONTEXT);
        
        // When: Converting to string
        String stringRepresentation = largeCollection.toString();
        
        // Then: Should show ellipsis for truncated display
        assertTrue("Large collection should show ellipsis", 
                   stringRepresentation.contains("...") && stringRepresentation.contains("25)"));
    }

    @Test
    public void testToString_SmallCollection_ShowsAllElements() {
        // Given: A small collection with 2 null elements
        List<JtsPoint> smallCollection = new ArrayList<>();
        smallCollection.add(null);
        smallCollection.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(smallCollection, GEO_CONTEXT);
        
        // When: Converting to string
        String stringRepresentation = collection.toString();
        
        // Then: Should show all elements without ellipsis
        assertEquals("Small collection should show all elements", 
                     "ShapeCollection(null, null)", stringRepresentation);
    }

    // ========== Spatial Relation Tests ==========

    @Test(expected = NullPointerException.class)
    public void testRelate_CollectionWithNullShape_ThrowsException() {
        // Given: A collection with null shape and a test point
        List<JtsPoint> shapesWithNull = new ArrayList<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        PointImpl testPoint = new PointImpl(624.2, 624.2, GEO_CONTEXT);
        
        // When: Testing spatial relation with a rectangle
        Rectangle testRectangle = createTestRectangle(testPoint, 624.2);
        
        // Then: Should throw NullPointerException
        collection.relate(testRectangle);
    }

    @Test
    public void testRelate_EmptyCollectionWithPoint_ReturnsDisjoint() {
        // Given: An empty collection and a test point
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        PointImpl testPoint = new PointImpl(2180.0, -2963.967974, GEO_CONTEXT);
        
        // When: Testing spatial relation
        SpatialRelation relation = emptyCollection.relate(testPoint);
        
        // Then: Should not intersect (disjoint)
        assertFalse("Empty collection should be disjoint from any point", relation.intersects());
    }

    @Test
    public void testRelate_EmptyCollectionWithWorldBounds_Intersects() {
        // Given: An empty collection with modified bounding box
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // Modify the bounding box by creating a test point and expanding bounds
        PointImpl testPoint = new PointImpl(18.5747965337, 18.5747965337, GEO_CONTEXT);
        createTestRectangle(testPoint, 18.5747965337);
        
        // When: Testing relation with world bounds
        Rectangle worldBounds = GEO_CONTEXT.getWorldBounds();
        SpatialRelation relation = collection.relate(worldBounds);
        
        // Then: Should intersect
        assertTrue("Collection should intersect with world bounds", relation.intersects());
    }

    // ========== Area and Geometry Tests ==========

    @Test(expected = NullPointerException.class)
    public void testHasArea_CollectionWithNullShape_ThrowsException() {
        // Given: A collection containing a null shape
        List<JtsPoint> shapesWithNull = new ArrayList<>();
        shapesWithNull.add(null);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapesWithNull, GEO_CONTEXT);
        
        // When: Checking if collection has area
        // Then: Should throw NullPointerException
        collection.hasArea();
    }

    @Test
    public void testHasArea_NestedEmptyCollection_ReturnsFalse() {
        // Given: A nested collection structure
        List<JtsPoint> innerShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> innerCollection = new ShapeCollection<>(innerShapes, GEO_CONTEXT);
        
        List<ShapeCollection<JtsPoint>> outerShapes = new ArrayList<>();
        outerShapes.add(innerCollection);
        ShapeCollection<ShapeCollection<JtsPoint>> nestedCollection = 
            new ShapeCollection<>(outerShapes, GEO_CONTEXT);
        
        // When: Checking if nested collection has area
        boolean hasArea = nestedCollection.hasArea();
        
        // Then: Should return false
        assertFalse("Nested empty collection should not have area", hasArea);
    }

    @Test
    public void testGetCenter_EmptyCollection_ReturnsEmptyPoint() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new Vector<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> emptyCollection = new ShapeCollection<>(emptyShapes, context);
        
        // When: Getting the center point
        Point center = emptyCollection.getCenter();
        
        // Then: Should return empty point
        assertTrue("Empty collection should have empty center point", center.isEmpty());
    }

    @Test
    public void testGetCenter_CollectionWithBounds_ReturnsValidCenter() {
        // Given: A collection with modified bounding box
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        
        // Modify bounding box through distance calculation
        GeodesicSphereDistCalc.LawOfCosines calculator = new GeodesicSphereDistCalc.LawOfCosines();
        PointImpl testPoint = new PointImpl(2630.69905510879, 2630.69905510879, GEO_CONTEXT);
        Rectangle boundingBox = collection.getBoundingBox();
        calculator.calcBoxByDistFromPt(testPoint, 2630.69905510879, GEO_CONTEXT, boundingBox);
        
        // When: Getting the center
        Point center = collection.getCenter();
        
        // Then: Should have valid Y coordinate
        assertEquals("Center should have correct Y coordinate", 0.0, center.getY(), DELTA);
    }

    // ========== Context and Utility Tests ==========

    @Test
    public void testGetContext_ReturnsCorrectContext() {
        // Given: A collection with specific context
        List<JtsPoint> shapes = new ArrayList<>();
        SpatialContextFactory factory = new SpatialContextFactory();
        factory.normWrapLongitude = true;
        SpatialContext customContext = new SpatialContext(factory);
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapes, customContext);
        
        // When: Getting the context
        SpatialContext retrievedContext = collection.getContext();
        
        // Then: Should return the same context
        assertSame("Should return the same context instance", customContext, retrievedContext);
    }

    @Test
    public void testGetContext_FactoryCreatedContext_HasCorrectProperties() {
        // Given: A collection with factory-created context
        List<JtsPoint> shapes = new ArrayList<>();
        SpatialContext factoryContext = SpatialContextFactory.makeSpatialContext(
            new java.util.HashMap<>(), ClassLoader.getSystemClassLoader());
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(shapes, factoryContext);
        
        // When: Getting the context
        SpatialContext context = collection.getContext();
        
        // Then: Should have expected properties
        assertFalse("Factory context should not normalize wrap longitude", 
                    context.isNormWrapLongitude());
    }

    @Test
    public void testRelateContainsShortCircuits_ReturnsTrue() {
        // Given: An empty collection
        List<JtsPoint> emptyShapes = new ArrayList<>();
        SpatialContext context = new SpatialContext(new SpatialContextFactory());
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(emptyShapes, context);
        
        // When: Checking if relate contains short circuits
        boolean shortCircuits = collection.relateContainsShortCircuits();
        
        // Then: Should return true (default behavior)
        assertTrue("Default implementation should return true for short circuits", shortCircuits);
    }

    // ========== Helper Methods ==========

    /**
     * Creates a test rectangle by calculating a bounding box around a point with given distance
     */
    private Rectangle createTestRectangle(PointImpl point, double distance) {
        GeodesicSphereDistCalc.LawOfCosines calculator = new GeodesicSphereDistCalc.LawOfCosines();
        List<JtsPoint> emptyShapes = new ArrayList<>();
        ShapeCollection<JtsPoint> collection = new ShapeCollection<>(emptyShapes, GEO_CONTEXT);
        Rectangle boundingBox = collection.getBoundingBox();
        return calculator.calcBoxByDistFromPt(point, distance, GEO_CONTEXT, boundingBox);
    }
}