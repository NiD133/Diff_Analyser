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
 * Notes on the allocation tests:
 * These tests intentionally use CompactHashSet instead of CompactLinkedHashSet to verify
 * the shared allocation behavior (needsAllocArrays and elements array length) that
 * CompactLinkedHashSet inherits from CompactHashSet.
 */
@GwtIncompatible // java.util.Arrays#copyOf(Object[], int), java.lang.reflect.Array
@NullUnmarked
public class CompactLinkedHashSetTest extends TestCase {

  // Alias for readability within allocation tests.
  private static final int DEFAULT_SIZE = CompactHashing.DEFAULT_SIZE;

  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    // Common features for both the regular and flooding-resistant implementations.
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

    // Standard CompactLinkedHashSet conformance tests.
    suite.addTest(
        SetTestSuiteBuilder.using(newStringSetGenerator(/*floodingProtection=*/ false))
            .named("CompactLinkedHashSet")
            .withFeatures(allFeatures)
            .createTestSuite());

    // CompactLinkedHashSet with hash-flooding protection enabled.
    suite.addTest(
        SetTestSuiteBuilder.using(newStringSetGenerator(/*floodingProtection=*/ true))
            .named("CompactLinkedHashSet with flooding protection")
            .withFeatures(allFeatures)
            .createTestSuite());

    return suite;
  }

  private static TestStringSetGenerator newStringSetGenerator(final boolean floodingProtection) {
    return new TestStringSetGenerator() {
      @Override
      protected Set<String> create(String[] elements) {
        CompactLinkedHashSet<String> set =
            (floodingProtection) ? CompactLinkedHashSet.<String>create() : CompactLinkedHashSet.create(asList(elements));

        if (floodingProtection) {
          set.convertToHashFloodingResistantImplementation();
          Collections.addAll(set, elements);
          return set;
        } else {
          // The non-flooding-protection branch already returned a pre-populated set above.
          return set;
        }
      }
    };
  }

  /**
   * By default construction, the internal arrays are not allocated until the first element is added.
   * After the first add, the backing array length equals DEFAULT_SIZE.
   */
  public void testAllocArrays_defaultConstruction_allocatesOnFirstAdd() {
    CompactHashSet<Integer> set = CompactHashSet.create();

    // Before any element is added, arrays are not allocated.
    assertThat(set.needsAllocArrays()).isTrue();
    assertThat(set.elements).isNull();

    // First add triggers allocation to the default capacity.
    set.add(1);
    assertThat(set.needsAllocArrays()).isFalse();
    assertThat(set.elements).hasLength(DEFAULT_SIZE);
  }

  /**
   * When constructed with an expected size, arrays are still allocated lazily on first add.
   * On allocation, the capacity is max(1, expectedSize).
   */
  public void testAllocArrays_expectedSize_allocatesOnFirstAddWithCapacityAtLeastOne() {
    for (int expectedSize = 0; expectedSize <= DEFAULT_SIZE; expectedSize++) {
      CompactHashSet<Integer> set = CompactHashSet.createWithExpectedSize(expectedSize);

      // Still lazy before first add, regardless of expected size
      assertThat(set.needsAllocArrays()).isTrue();
      assertThat(set.elements).isNull();

      // Allocation happens on first add, with size max(1, expectedSize)
      set.add(1);
      assertThat(set.needsAllocArrays()).isFalse();
      int expectedArrayLength = max(1, expectedSize);
      assertThat(set.elements).hasLength(expectedArrayLength);
    }
  }
}