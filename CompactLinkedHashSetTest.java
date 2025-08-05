/*
 * Copyright (C) 2012 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.truth.Truth.assertThat;
import static java.lang.Math.max;
import static java.util.Arrays.asList;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.collect.testing.SetTestSuiteBuilder;
import com.google.common.collect.testing.TestStringSetGenerator;
import com.google.common.collect.testing.features.CollectionFeature;
import com.google.common.collect.testing.features.CollectionSize;
import com.google.common.collect.testing.features.Feature;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jspecify.annotations.NullUnmarked;

/**
 * Tests for CompactLinkedHashSet.
 *
 * @author Dimitris Andreou
 */
@GwtIncompatible // java.util.Arrays#copyOf(Object[], int), java.lang.reflect.Array
@NullUnmarked
public class CompactLinkedHashSetTest extends TestCase {
  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    List<Feature<?>> allFeatures =
        Arrays.<Feature<?>>asList(
            CollectionSize.ANY,
            CollectionFeature.ALLOWS_NULL_VALUES,
            CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
            CollectionFeature.GENERAL_PURPOSE,
            CollectionFeature.REMOVE_OPERATIONS,
            CollectionFeature.SERIALIZABLE,
            CollectionFeature.KNOWN_ORDER,
            CollectionFeature.SUPPORTS_ADD,
            CollectionFeature.SUPPORTS_REMOVE);

    TestSuite suite = new TestSuite();
    suite.addTestSuite(CompactLinkedHashSetTest.class);
    suite.addTest(
        SetTestSuiteBuilder.using(
                new TestStringSetGenerator() {
                  @Override
                  protected Set<String> create(String[] elements) {
                    return CompactLinkedHashSet.create(asList(elements));
                  }
                })
            .named("CompactLinkedHashSet")
            .withFeatures(allFeatures)
            .createTestSuite());
    suite.addTest(
        SetTestSuiteBuilder.using(
                new TestStringSetGenerator() {
                  @Override
                  protected Set<String> create(String[] elements) {
                    CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
                    set.convertToHashFloodingResistantImplementation();
                    Collections.addAll(set, elements);
                    return set;
                  }
                })
            .named("CompactLinkedHashSet with flooding protection")
            .withFeatures(allFeatures)
            .createTestSuite());
    return suite;
  }

  public void testCreate_initializationIsLazy() {
    // Arrange: Create a set using the default constructor.
    CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.create();

    // Assert: Backing arrays are not allocated upon creation. The set is in a state
    // where it needs to allocate arrays before storing elements.
    assertThat(set.needsAllocArrays()).isTrue();
    assertThat(set.elements).isNull();

    // Act: Add an element to trigger the lazy initialization.
    set.add(1);

    // Assert: After adding an element, the backing arrays are allocated with the default size.
    assertThat(set.needsAllocArrays()).isFalse();
    assertThat(set.elements).hasLength(CompactHashing.DEFAULT_SIZE);
  }

  public void testCreateWithExpectedSize_initializationIsLazy() {
    // Test a range of expected sizes to ensure lazy allocation works consistently.
    for (int expectedSize = 0; expectedSize <= CompactHashing.DEFAULT_SIZE; expectedSize++) {
      String message = "Failure for expectedSize = " + expectedSize;

      // Arrange: Create a set with a specific expected size.
      CompactLinkedHashSet<Integer> set = CompactLinkedHashSet.createWithExpectedSize(expectedSize);

      // Assert: Backing arrays are not allocated upon creation, regardless of the expected size.
      assertThat(set.needsAllocArrays()).withMessage(message).isTrue();
      assertThat(set.elements).withMessage(message).isNull();

      // Act: Add an element to trigger the lazy initialization.
      set.add(1);

      // Assert: After adding an element, the backing arrays are allocated to a size
      // based on the initial expectation (with a minimum size of 1).
      assertThat(set.needsAllocArrays()).withMessage(message).isFalse();
      int allocatedSize = max(1, expectedSize);
      assertThat(set.elements).withMessage(message).hasLength(allocatedSize);
    }
  }
}