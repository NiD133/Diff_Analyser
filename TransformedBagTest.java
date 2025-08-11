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

    @Override
    @SuppressWarnings("unchecked")
    public Bag<T> makeObject() {
        return TransformedBag.transformingBag(
            new HashBag<>(),
            (Transformer<T, T>) TransformedCollectionTest.NOOP_TRANSFORMER
        );
    }

    // Test data for transformation scenarios
    private static final String[] STRING_ELEMENTS = {"1", "3", "5", "7", "2", "4", "6"};
    private static final Transformer<Object, Integer> STRING_TO_INTEGER = 
        TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

    /**
     * Tests behavior when using transformingBag() factory method:
     * 1. New elements should be transformed on addition
     * 2. Existing contents should NOT be transformed
     */
    @Test
    void testTransformingBag_TransformsNewElements() {
        // Create empty bag with transformation
        Bag<Object> bag = TransformedBag.transformingBag(
            new HashBag<>(),
            STRING_TO_INTEGER
        );
        
        assertTrue(bag.isEmpty(), "Newly created bag should be empty");
        
        // Verify transformation during element addition
        for (int i = 0; i < STRING_ELEMENTS.length; i++) {
            String element = STRING_ELEMENTS[i];
            Integer transformed = Integer.valueOf(element);
            
            bag.add(element);
            int currentSize = i + 1;
            
            assertEquals(currentSize, bag.size(), "Bag size should increment after add");
            assertTrue(bag.contains(transformed), 
                "Bag should contain transformed element: " + transformed);
            assertFalse(bag.contains(element), 
                "Bag should not contain original element: " + element);
        }
        
        // Verify removal requires transformed element
        String firstElement = STRING_ELEMENTS[0];
        Integer firstTransformed = Integer.valueOf(firstElement);
        
        assertFalse(bag.remove(firstElement), 
            "Should not remove using untransformed element");
        assertTrue(bag.remove(firstTransformed), 
            "Should remove using transformed element");
    }

    /**
     * Tests behavior when using transformedBag() factory method:
     * 1. Existing elements should be transformed during decoration
     * 2. New elements should be transformed on addition
     */
    @Test
    void testTransformedBag_TransformsExistingAndNewElements() {
        // Create bag with existing string elements
        Bag<Object> original = new HashBag<>();
        for (String element : STRING_ELEMENTS) {
            original.add(element);
        }
        
        // Apply transformation to existing contents
        Bag<Object> bag = TransformedBag.transformedBag(
            original, 
            STRING_TO_INTEGER
        );
        
        assertEquals(STRING_ELEMENTS.length, bag.size(), 
            "Bag size should match number of elements added");
        
        // Verify all elements were transformed
        for (String element : STRING_ELEMENTS) {
            Integer transformed = Integer.valueOf(element);
            assertTrue(bag.contains(transformed), 
                "Bag should contain transformed element: " + transformed);
            assertFalse(bag.contains(element), 
                "Bag should not contain original element: " + element);
        }
        
        // Verify removal requires transformed element
        String firstElement = STRING_ELEMENTS[0];
        Integer firstTransformed = Integer.valueOf(firstElement);
        
        assertFalse(bag.remove(firstElement), 
            "Should not remove using untransformed element");
        assertTrue(bag.remove(firstTransformed), 
            "Should remove using transformed element");
    }
}