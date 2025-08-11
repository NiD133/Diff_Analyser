/*
 * Copyright (c) 2015 Voyager Search and MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 */
package org.locationtech.spatial4j.shape;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A comprehensive and understandable test suite for the {@link ShapeCollection} class.
 * This suite focuses on testing the public API and contract of the class.
 */
class ShapeCollectionTest {

    private SpatialContext ctx;

    @BeforeEach
    void setUp() {
        // Use a standard SpatialContext for all tests to ensure consistency.
        ctx = SpatialContext.GEO;
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorAndBasicProperties {

        @Test
        void constructor_shouldThrowIllegalArgumentException_whenShapeListDoesNotSupportRandomAccess() {
            // Arrange: A List that does not implement RandomAccess
            List<Shape> nonRandomAccessList = new LinkedList<>();

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new ShapeCollection<>(nonRandomAccessList, ctx);
            });
            assertTrue(exception.getMessage().contains("must implement RandomAccess"));
        }

        @Test
        void constructor_shouldThrowNullPointerException_whenContextIsNull() {
            // Arrange
            List<Shape> shapes = new ArrayList<>();

            // Act & Assert
            assertThrows(NullPointerException.class, () -> {
                new ShapeCollection<>(shapes, null);
            });
        }
        
        @Test
        void constructor_shouldThrowNullPointerException_whenShapeListContainsNulls() {
            // Arrange
            List<Shape> shapesWithNull = new ArrayList<>();
            shapesWithNull.add(null);

            // Act & Assert: The constructor computes the bounding box, which will fail on a null shape.
            assertThrows(NullPointerException.class, () -> {
                new ShapeCollection<>(shapesWithNull, ctx);
            });
        }

        @Test
        void basicProperties_shouldBeCorrect_forEmptyCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);

            // Assert
            assertEquals(0, emptyCollection.size());
            assertTrue(emptyCollection.isEmpty());
            assertFalse(emptyCollection.hasArea());
            assertEquals(ctx, emptyCollection.getContext());
            assertTrue(emptyCollection.getShapes().isEmpty());
        }

        @Test
        void basicProperties_shouldBeCorrect_forNonEmptyCollection() {
            // Arrange
            Point point = ctx.makePoint(0, 0);
            List<Shape> shapes = Collections.singletonList(point);
            ShapeCollection<Shape> collection = new ShapeCollection<>(shapes, ctx);

            // Assert
            assertEquals(1, collection.size());
            assertFalse(collection.isEmpty());
            assertFalse(collection.hasArea()); // A collection of points has no area
            assertEquals(shapes, collection.getShapes());
            assertEquals(point, collection.get(0));
        }
    }

    @Nested
    @DisplayName("BoundingBox and Center")
    class BoundingBoxAndCenter {

        @Test
        void getBoundingBox_shouldBeEmpty_forEmptyCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);

            // Act
            Rectangle bbox = emptyCollection.getBoundingBox();

            // Assert
            assertTrue(bbox.isEmpty(), "Bounding box of an empty collection should be empty.");
        }

        @Test
        void getBoundingBox_shouldEncloseAllShapes() {
            // Arrange
            Point p1 = ctx.makePoint(0, 10);
            Point p2 = ctx.makePoint(20, -5);
            List<Shape> shapes = List.of(p1, p2);
            ShapeCollection<Shape> collection = new ShapeCollection<>(shapes, ctx);

            // Act
            Rectangle bbox = collection.getBoundingBox();

            // Assert
            assertEquals(0, bbox.getMinX());
            assertEquals(20, bbox.getMaxX());
            assertEquals(-5, bbox.getMinY());
            assertEquals(10, bbox.getMaxY());
        }

        @Test
        void getCenter_shouldBeEmpty_forEmptyCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);

            // Act
            Point center = emptyCollection.getCenter();

            // Assert
            assertTrue(center.isEmpty(), "Center of an empty collection should be an empty point.");
        }

        @Test
        void getCenter_shouldBeCenterOfBoundingBox() {
            // Arrange
            Point p1 = ctx.makePoint(0, 0);
            Point p2 = ctx.makePoint(10, 10);
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(p1, p2), ctx);

            // Act
            Point center = collection.getCenter();

            // Assert
            assertEquals(5.0, center.getX());
            assertEquals(5.0, center.getY());
        }
    }

    @Nested
    @DisplayName("Relate Method")
    class RelateMethod {

        @Test
        void relate_shouldReturnDisjoint_forEmptyCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);
            Point somePoint = ctx.makePoint(0, 0);

            // Act
            SpatialRelation relation = emptyCollection.relate(somePoint);

            // Assert
            assertEquals(SpatialRelation.DISJOINT, relation);
        }

        @Test
        void relate_shouldReturnContains_whenPointIsOneOfTheShapes() {
            // Arrange
            Point p1 = ctx.makePoint(0, 0);
            Point p2 = ctx.makePoint(10, 10);
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(p1, p2), ctx);

            // Act
            SpatialRelation relation = collection.relate(p1);

            // Assert
            assertEquals(SpatialRelation.CONTAINS, relation);
        }

        @Test
        void relate_shouldReturnIntersects_whenShapeOverlapsOneElement() {
            // Arrange
            Rectangle r1 = ctx.makeRectangle(0, 5, 0, 5);
            Rectangle r2 = ctx.makeRectangle(10, 15, 10, 15);
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(r1, r2), ctx);
            Rectangle intersectingRect = ctx.makeRectangle(12, 18, 12, 18);

            // Act
            SpatialRelation relation = collection.relate(intersectingRect);

            // Assert
            assertEquals(SpatialRelation.INTERSECTS, relation);
        }

        @Test
        void relate_shouldReturnWithin_whenCollectionIsInsideOtherShape() {
            // Arrange
            Point p1 = ctx.makePoint(1, 1);
            Point p2 = ctx.makePoint(2, 2);
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(p1, p2), ctx);
            Rectangle biggerRect = ctx.makeRectangle(0, 5, 0, 5);

            // Act
            SpatialRelation relation = collection.relate(biggerRect);

            // Assert
            assertEquals(SpatialRelation.WITHIN, relation);
        }
    }

    @Nested
    @DisplayName("Area and Buffer")
    class AreaAndBuffer {

        @Test
        void getArea_shouldReturnZero_forEmptyOrPointBasedCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);
            ShapeCollection<Shape> pointCollection = new ShapeCollection<>(List.of(ctx.makePoint(0, 0)), ctx);

            // Act & Assert
            assertEquals(0.0, emptyCollection.getArea(ctx));
            assertEquals(0.0, pointCollection.getArea(ctx));
        }

        @Test
        void getArea_shouldReturnSumOfShapeAreas() {
            // Arrange
            // Note: For GEO context, area is in square degrees.
            Rectangle r1 = ctx.makeRectangle(0, 10, 0, 10); // Area = 100
            Rectangle r2 = ctx.makeRectangle(20, 25, 20, 25); // Area = 25
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(r1, r2), ctx);

            // Act
            double totalArea = collection.getArea(ctx);

            // Assert
            assertEquals(r1.getArea(ctx) + r2.getArea(ctx), totalArea);
        }

        @Test
        void getBuffered_shouldReturnEmptyCollection_whenBufferingAnEmptyCollection() {
            // Arrange
            ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(new ArrayList<>(), ctx);

            // Act
            Shape buffered = emptyCollection.getBuffered(10, ctx);

            // Assert
            assertInstanceOf(ShapeCollection.class, buffered);
            assertTrue(((ShapeCollection<?>) buffered).isEmpty());
        }
    }

    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsAndHashCode {

        @Test
        void equals_shouldBeTrue_forTwoIdenticalCollections() {
            // Arrange
            List<Shape> shapes1 = List.of(ctx.makePoint(0, 0));
            ShapeCollection<Shape> collection1 = new ShapeCollection<>(shapes1, ctx);

            List<Shape> shapes2 = List.of(ctx.makePoint(0, 0));
            ShapeCollection<Shape> collection2 = new ShapeCollection<>(shapes2, ctx);

            // Assert
            assertEquals(collection1, collection2);
        }

        @Test
        void equals_shouldBeFalse_forDifferentShapes() {
            // Arrange
            ShapeCollection<Shape> collection1 = new ShapeCollection<>(List.of(ctx.makePoint(0, 0)), ctx);
            ShapeCollection<Shape> collection2 = new ShapeCollection<>(List.of(ctx.makePoint(1, 1)), ctx);

            // Assert
            assertNotEquals(collection1, collection2);
        }

        @Test
        void equals_shouldBeFalse_forDifferentContext() {
            // Arrange
            List<Shape> shapes = List.of(ctx.makePoint(0, 0));
            ShapeCollection<Shape> collection1 = new ShapeCollection<>(shapes, ctx);
            ShapeCollection<Shape> collection2 = new ShapeCollection<>(shapes, SpatialContext.GEO_KM); // Different context

            // Assert
            assertNotEquals(collection1, collection2);
        }

        @Test
        void equals_shouldAdhereToContract() {
            // Arrange
            ShapeCollection<Shape> collection = new ShapeCollection<>(List.of(ctx.makePoint(0, 0)), ctx);

            // Assert
            assertNotEquals(null, collection);
            assertNotEquals(collection, new Object());
            assertEquals(collection, collection); // Reflexive
        }

        @Test
        void hashCode_shouldBeSame_forEqualObjects() {
            // Arrange
            ShapeCollection<Shape> collection1 = new ShapeCollection<>(List.of(ctx.makePoint(0, 0)), ctx);
            ShapeCollection<Shape> collection2 = new ShapeCollection<>(List.of(ctx.makePoint(0, 0)), ctx);

            // Assert
            assertEquals(collection1, collection2);
            assertEquals(collection1.hashCode(), collection2.hashCode());
        }
    }

    @Nested
    @DisplayName("Static Utility Methods")
    class StaticUtils {

        @Test
        void computeMutualDisjoint_shouldReturnTrue_forEmptyList() {
            assertTrue(ShapeCollection.computeMutualDisjoint(Collections.emptyList()));
        }

        @Test
        void computeMutualDisjoint_shouldReturnTrue_forDisjointShapes() {
            // Arrange
            Rectangle r1 = ctx.makeRectangle(0, 1, 0, 1);
            Rectangle r2 = ctx.makeRectangle(5, 6, 5, 6);

            // Act & Assert
            assertTrue(ShapeCollection.computeMutualDisjoint(List.of(r1, r2)));
        }

        @Test
        void computeMutualDisjoint_shouldReturnFalse_forIntersectingShapes() {
            // Arrange
            Rectangle r1 = ctx.makeRectangle(0, 5, 0, 5);
            Rectangle r2 = ctx.makeRectangle(3, 8, 3, 8);

            // Act & Assert
            assertFalse(ShapeCollection.computeMutualDisjoint(List.of(r1, r2)));
        }
    }
}