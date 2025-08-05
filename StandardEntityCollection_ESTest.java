package org.jfree.chart.entity;

import org.junit.Before;
import org.junit.Test;

import java.awt.Rectangle;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * A set of clear, maintainable tests for the {@link StandardEntityCollection} class.
 */
public class StandardEntityCollectionTest {

    private StandardEntityCollection collection;

    @Before
    public void setUp() {
        collection = new StandardEntityCollection();
    }

    /**
     * Helper method to create a simple ChartEntity for tests.
     */
    private ChartEntity createEntity(int x, int y, int width, int height, String toolTip) {
        return new ChartEntity(new Rectangle(x, y, width, height), toolTip);
    }

    // =========================================================================
    // Constructor and Initial State Tests
    // =========================================================================

    @Test
    public void newCollection_shouldBeEmpty() {
        // Assert
        assertEquals("A new collection should have 0 entities.", 0, collection.getEntityCount());
        assertTrue("A new collection's entity list should be empty.", collection.getEntities().isEmpty());
    }

    @Test
    public void getEntities_onNewCollection_shouldReturnEmptyCollection() {
        // Act
        Collection<ChartEntity> entities = collection.getEntities();

        // Assert
        assertNotNull("getEntities should never return null.", entities);
        assertTrue("The returned collection should be empty for a new StandardEntityCollection.", entities.isEmpty());
    }

    @Test
    public void iterator_onNewCollection_shouldNotHaveNext() {
        // Act & Assert
        assertNotNull("Iterator should not be null.", collection.iterator());
        assertFalse("Iterator for an empty collection should not have a next element.", collection.iterator().hasNext());
    }

    // =========================================================================
    // add() and getEntityCount() Tests
    // =========================================================================

    @Test
    public void add_singleEntity_shouldIncreaseCount() {
        // Arrange
        ChartEntity entity = createEntity(0, 0, 10, 10, "Entity 1");

        // Act
        collection.add(entity);

        // Assert
        assertEquals("After adding one entity, the count should be 1.", 1, collection.getEntityCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_nullEntity_shouldThrowException() {
        // Act
        collection.add(null);
    }

    // =========================================================================
    // getEntity(int index) Tests
    // =========================================================================

    @Test
    public void getEntityByIndex_withValidIndex_shouldReturnCorrectEntity() {
        // Arrange
        ChartEntity entity1 = createEntity(0, 0, 10, 10, "Entity 1");
        ChartEntity entity2 = createEntity(20, 20, 10, 10, "Entity 2");
        collection.add(entity1);
        collection.add(entity2);

        // Act
        ChartEntity retrievedEntity = collection.getEntity(1);

        // Assert
        assertSame("getEntity(1) should return the second entity that was added.", entity2, retrievedEntity);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getEntityByIndex_onEmptyCollection_shouldThrowException() {
        // Act
        collection.getEntity(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getEntityByIndex_withIndexEqualToSize_shouldThrowException() {
        // Arrange
        collection.add(createEntity(0, 0, 10, 10, "Entity 1"));

        // Act
        collection.getEntity(1); // Index 1 is out of bounds for a collection of size 1
    }

    // =========================================================================
    // getEntity(double x, double y) Tests
    // =========================================================================

    @Test
    public void getEntityByCoordinates_whenPointIsInsideEntity_shouldReturnThatEntity() {
        // Arrange
        ChartEntity entity = createEntity(10, 10, 20, 20, "Target Entity");
        collection.add(entity);

        // Act
        ChartEntity foundEntity = collection.getEntity(15, 15);

        // Assert
        assertSame("Should return the entity containing the coordinates.", entity, foundEntity);
    }

    @Test
    public void getEntityByCoordinates_whenPointIsOutsideAllEntities_shouldReturnNull() {
        // Arrange
        ChartEntity entity = createEntity(10, 10, 20, 20, "Some Entity");
        collection.add(entity);

        // Act
        ChartEntity foundEntity = collection.getEntity(100, 100);

        // Assert
        assertNull("Should return null if no entity contains the coordinates.", foundEntity);
    }

    @Test
    public void getEntityByCoordinates_whenPointIsInMultipleEntities_shouldReturnLastAddedEntity() {
        // Arrange
        ChartEntity entity1 = createEntity(0, 0, 50, 50, "First Entity"); // Overlaps with entity2
        ChartEntity entity2 = createEntity(10, 10, 20, 20, "Second Entity"); // Last added
        collection.add(entity1);
        collection.add(entity2);

        // Act
        ChartEntity foundEntity = collection.getEntity(15, 15); // Point is inside both

        // Assert
        assertSame("Should return the last added entity that contains the point.", entity2, foundEntity);
    }

    // =========================================================================
    // addAll() Tests
    // =========================================================================

    @Test
    public void addAll_withNonEmptyCollection_shouldAddAllEntities() {
        // Arrange
        StandardEntityCollection otherCollection = new StandardEntityCollection();
        ChartEntity entity1 = createEntity(0, 0, 10, 10, "Entity 1");
        ChartEntity entity2 = createEntity(20, 20, 10, 10, "Entity 2");
        otherCollection.add(entity1);
        otherCollection.add(entity2);

        // Act
        collection.addAll(otherCollection);

        // Assert
        assertEquals("The count should be 2 after adding the other collection.", 2, collection.getEntityCount());
        assertTrue("The collection should contain entity1.", collection.getEntities().contains(entity1));
        assertTrue("The collection should contain entity2.", collection.getEntities().contains(entity2));
    }

    @Test(expected = NullPointerException.class)
    public void addAll_withNullCollection_shouldThrowException() {
        // Act
        collection.addAll(null);
    }

    // =========================================================================
    // clear() Tests
    // =========================================================================

    @Test
    public void clear_onNonEmptyCollection_shouldRemoveAllEntities() {
        // Arrange
        collection.add(createEntity(0, 0, 10, 10, "Entity 1"));
        collection.add(createEntity(20, 20, 10, 10, "Entity 2"));

        // Act
        collection.clear();

        // Assert
        assertEquals("The count should be 0 after clearing.", 0, collection.getEntityCount());
        assertTrue("The entity list should be empty after clearing.", collection.getEntities().isEmpty());
    }

    // =========================================================================
    // clone() Tests
    // =========================================================================

    @Test
    public void clone_shouldReturnIndependentCopy() throws CloneNotSupportedException {
        // Arrange
        collection.add(createEntity(0, 0, 10, 10, "Entity 1"));

        // Act
        StandardEntityCollection clonedCollection = (StandardEntityCollection) collection.clone();

        // Assert
        assertNotSame("Clone should be a different object instance.", collection, clonedCollection);
        assertEquals("Clone should be equal to the original.", collection, clonedCollection);

        // Verify independence by modifying the original collection
        collection.add(createEntity(20, 20, 10, 10, "Entity 2"));
        assertEquals("Original collection count should be 2.", 2, collection.getEntityCount());
        assertEquals("Cloned collection count should remain 1.", 1, clonedCollection.getEntityCount());
    }

    // =========================================================================
    // equals() and hashCode() Tests
    // =========================================================================

    @Test
    public void equals_and_hashCode_contract() {
        // Arrange
        StandardEntityCollection collection1 = new StandardEntityCollection();
        collection1.add(createEntity(10, 10, 20, 20, "A"));

        StandardEntityCollection collection2 = new StandardEntityCollection();
        collection2.add(createEntity(10, 10, 20, 20, "A"));

        StandardEntityCollection collection3 = new StandardEntityCollection();
        collection3.add(createEntity(99, 99, 1, 1, "B"));

        // Assert - equals
        assertTrue("A collection should be equal to itself.", collection1.equals(collection1));
        assertTrue("Two collections with the same entities should be equal.", collection1.equals(collection2));
        assertFalse("Two collections with different entities should not be equal.", collection1.equals(collection3));
        assertFalse("A collection should not be equal to an object of a different type.", collection1.equals(new Object()));
        assertFalse("A collection should not be equal to null.", collection1.equals(null));

        // Assert - hashCode
        assertEquals("Hashcodes of equal collections should be the same.", collection1.hashCode(), collection2.hashCode());
    }
}