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

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link TransformedBag} implementation.
 * 
 * Tests the behavior of bags that automatically transform elements when they are added.
 * For example, a bag that converts strings to integers when elements are added.
 */
public class TransformedBagTest<T> extends AbstractBagTest<T> {

    private static final String[] TEST_STRINGS = {"1", "3", "5", "7", "2", "4", "6"};

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Creates a TransformedBag with a no-op transformer for standard bag tests.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(new HashBag<>(),
                (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER);
    }

    /**
     * Tests that TransformedBag.transformingBag() applies transformation to newly added elements.
     * 
     * This test uses a string-to-integer transformer and verifies that:
     * - String elements are transformed to integers when added
     * - The bag contains the transformed values, not the original strings
     * - Removal works with transformed values, not original values
     */
    @Test
    @SuppressWarnings("unchecked")
    void testTransformingBag_appliesTransformationToNewElements() {
        // Given: A bag that transforms strings to integers when elements are added
        final Bag<T> transformingBag = TransformedBag.transformingBag(new HashBag<>(),
                (Transformer<T, T>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        
        // Initially the bag should be empty
        assertTrue(transformingBag.isEmpty());

        // When: Adding string elements to the bag
        for (int i = 0; i < TEST_STRINGS.length; i++) {
            String stringElement = TEST_STRINGS[i];
            transformingBag.add((T) stringElement);
            
            // Then: The bag size increases and contains the transformed integer value
            assertEquals(i + 1, transformingBag.size());
            
            Integer expectedTransformedValue = Integer.valueOf(stringElement);
            assertTrue(transformingBag.contains(expectedTransformedValue), 
                "Bag should contain transformed integer value: " + expectedTransformedValue);
            assertFalse(transformingBag.contains(stringElement), 
                "Bag should not contain original string value: " + stringElement);
        }

        // When: Attempting to remove elements
        String firstString = TEST_STRINGS[0];
        Integer firstTransformedInteger = Integer.valueOf(firstString);
        
        // Then: Removal fails with original string but succeeds with transformed integer
        assertFalse(transformingBag.remove(firstString), 
            "Should not be able to remove original string value");
        assertTrue(transformingBag.remove(firstTransformedInteger), 
            "Should be able to remove transformed integer value");
    }

    /**
     * Tests that TransformedBag.transformedBag() transforms existing elements in the decorated bag.
     * 
     * This test verifies that:
     * - Existing elements in the original bag are transformed when the bag is decorated
     * - The decorated bag contains transformed values, not original values
     * - Removal works with transformed values
     */
    @Test
    @SuppressWarnings("unchecked")
    void testTransformedBag_transformsExistingElements() {
        // Given: An original bag with string elements
        final Bag<T> originalBag = new HashBag<>();
        for (final String stringElement : TEST_STRINGS) {
            originalBag.add((T) stringElement);
        }

        // When: Creating a transformed bag that decorates the original bag
        final Bag<T> transformedBag = TransformedBag.transformedBag(originalBag,
                (Transformer<T, T>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);

        // Then: The transformed bag should have the same size as the original
        assertEquals(TEST_STRINGS.length, transformedBag.size());
        
        // And: The transformed bag should contain transformed integer values, not original strings
        for (final String originalString : TEST_STRINGS) {
            Integer expectedTransformedValue = Integer.valueOf(originalString);
            assertTrue(transformedBag.contains(expectedTransformedValue), 
                "Transformed bag should contain integer value: " + expectedTransformedValue);
            assertFalse(transformedBag.contains(originalString), 
                "Transformed bag should not contain original string: " + originalString);
        }

        // When: Attempting to remove elements
        String firstString = TEST_STRINGS[0];
        Integer firstTransformedInteger = Integer.valueOf(firstString);
        
        // Then: Removal fails with original string but succeeds with transformed integer
        assertFalse(transformedBag.remove(firstString), 
            "Should not be able to remove original string value");
        assertTrue(transformedBag.remove(firstTransformedInteger), 
            "Should be able to remove transformed integer value");
    }

    // Commented out utility method for generating test data files
    // void testCreate() throws Exception {
    //     Bag<T> bag = makeObject();
    //     writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TransformedBag.emptyCollection.version4.obj");
    //     bag = makeFullCollection();
    //     writeExternalFormToDisk((java.io.Serializable) bag, "src/test/resources/data/test/TransformedBag.fullCollection.version4.obj");
    // }
}