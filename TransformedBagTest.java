/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractBagTest} for exercising the {@link TransformedBag}
 * implementation.
 */
public class TransformedBagTest<T> extends AbstractBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Creates a new {@link TransformedBag} with a no-op transformer.
     * This method is used by the superclass tests.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(new HashBag<>(),
                (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    /**
     * Tests that elements added to a bag created with {@code transformingBag}
     * are transformed on insertion.
     */
    @Test
    void testTransformingBag_add() {
        // Arrange
        final Transformer<Object, Object> transformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;
        final Bag<Object> bag = TransformedBag.transformingBag(new HashBag<>(), transformer);
        final String[] elementsToAdd = {"1", "2", "3", "3"};

        // Act
        for (final String element : elementsToAdd) {
            bag.add(element);
        }

        // Assert
        assertEquals(elementsToAdd.length, bag.size(), "Bag size should match the number of added elements");

        // Verify that only transformed elements (Integers) are present
        assertTrue(bag.contains(1));
        assertTrue(bag.contains(2));
        assertEquals(2, bag.getCount(3));

        // Verify that original elements (Strings) are not present
        assertFalse(bag.contains("1"));
        assertFalse(bag.contains("2"));
        assertFalse(bag.contains("3"));
    }

    /**
     * Tests that removing elements from a {@code transformingBag} must be done
     * using the transformed object.
     */
    @Test
    void testTransformingBag_remove() {
        // Arrange
        final Transformer<Object, Object> transformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;
        final Bag<Object> bag = TransformedBag.transformingBag(new HashBag<>(), transformer);
        bag.add("1");

        // Act & Assert
        assertFalse(bag.remove("1"), "Removing the original object should fail");
        assertTrue(bag.remove(1), "Removing the transformed object should succeed");
        assertTrue(bag.isEmpty(), "Bag should be empty after removal");
    }

    /**
     * Tests that {@code transformedBag} factory method correctly transforms
     * the elements of a pre-populated bag.
     */
    @Test
    void testTransformedBag_withPreexistingElements() {
        // Arrange
        final Bag<Object> originalBag = new HashBag<>();
        final String[] initialElements = {"1", "2", "3", "3"};
        originalBag.addAll(Arrays.asList(initialElements));

        final Transformer<Object, Object> transformer = TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // Act
        final Bag<Object> transformedBag = TransformedBag.transformedBag(originalBag, transformer);

        // Assert
        assertEquals(initialElements.length, transformedBag.size(), "Bag size should be unchanged");

        // Verify that only transformed elements (Integers) are present
        assertTrue(transformedBag.contains(1));
        assertTrue(transformedBag.contains(2));
        assertEquals(2, transformedBag.getCount(3));

        // Verify that original elements (Strings) are not present
        assertFalse(transformedBag.contains("1"));
        assertFalse(transformedBag.contains("2"));
        assertFalse(transformedBag.contains("3"));

        // Verify removal behavior
        assertFalse(transformedBag.remove("1"), "Removing the original object should fail");
        assertTrue(transformedBag.remove(1), "Removing the transformed object should succeed");
    }

}