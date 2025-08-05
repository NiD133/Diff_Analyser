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
 * Tests for {@link CompactLinkedHashSet}. This test suite verifies:
 * <ul>
 *   <li>Basic functionality through the common collection test suite</li>
 *   <li>Special case behavior with flood protection enabled</li>
 *   <li>Internal array allocation patterns</li>
 * </ul>
 */
@GwtIncompatible // java.util.Arrays#copyOf(Object[], int), java.lang.reflect.Array
@NullUnmarked
public class CompactLinkedHashSetTest extends TestCase {
  private static final List<Feature<?>> ALL_FEATURES = Arrays.asList(
      CollectionSize.ANY,
      CollectionFeature.ALLOWS_NULL_VALUES,
      CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
      CollectionFeature.GENERAL_PURPOSE,
      CollectionFeature.REMOVE_OPERATIONS,
      CollectionFeature.SERIALIZABLE,
      CollectionFeature.KNOWN_ORDER,
      CollectionFeature.SUPPORTS_ADD,
      CollectionFeature.SUPPORTS_REMOVE
  );

  /**
   * Creates a comprehensive test suite that includes:
   * <ol>
   *   <li>Local tests from this class</li>
   *   <li>Generated tests for standard CompactLinkedHashSet</li>
   *   <li>Generated tests for flood-protected variant</li>
   * </ol>
   */
  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    TestSuite suite = new TestSuite();
    
    // Add local tests from this class
    suite.addTestSuite(CompactLinkedHashSetTest.class);
    
    // Test suite for standard CompactLinkedHashSet
    suite.addTest(
        SetTestSuiteBuilder.using(new StandardCompactLinkedHashSetGenerator())
            .named("CompactLinkedHashSet")
            .withFeatures(ALL_FEATURES)
            .createTestSuite());
    
    // Test suite for CompactLinkedHashSet with flood protection
    suite.addTest(
        SetTestSuiteBuilder.using(new FloodProtectedCompactLinkedHashSetGenerator())
            .named("CompactLinkedHashSet with flooding protection")
            .withFeatures(ALL_FEATURES)
            .createTestSuite());
    
    return suite;
  }

  // ===================================================================
  // Test Generators for SetTestSuiteBuilder
  // ===================================================================
  
  /** Generator for standard CompactLinkedHashSet instances */
  private static class StandardCompactLinkedHashSetGenerator extends TestStringSetGenerator {
    @Override
    protected Set<String> create(String[] elements) {
      return CompactLinkedHashSet.create(asList(elements));
    }
  }
  
  /** Generator for flood-protected CompactLinkedHashSet instances */
  private static class FloodProtectedCompactLinkedHashSetGenerator extends TestStringSetGenerator {
    @Override
    protected Set<String> create(String[] elements) {
      CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
      // Force conversion to flood-resistant implementation before adding elements
      set.convertToHashFloodingResistantImplementation();
      Collections.addAll(set, elements);
      return set;
    }
  }

  // ===================================================================
  // Tests for internal array allocation behavior
  // ===================================================================
  
  /**
   * Verifies internal array allocation behavior for the default constructor.
   * Tests that:
   * <ol>
   *   <li>Arrays are not allocated until first element is added</li>
   *   <li>Initial array size matches default capacity</li>
   * </ol>
   */
  public void testAllocArraysForDefaultConstructor() {
    CompactHashSet<Integer> set = CompactHashSet.create();
    
    // Verify initial state (no arrays allocated)
    assertThat(set.needsAllocArrays()).isTrue();
    assertThat(set.elements).isNull();
    
    // Add element and verify arrays are initialized
    set.add(1);
    assertThat(set.needsAllocArrays()).isFalse();
    assertThat(set.elements).hasLength(CompactHashing.DEFAULT_SIZE);
  }

  /**
   * Verifies internal array allocation behavior for the expected-size constructor.
   * Tests that:
   * <ol>
   *   <li>Arrays are not allocated until first element is added</li>
   *   <li>Initial array size matches requested expected size</li>
   *   <li>Handles edge cases (size=0 and size=default)</li>
   * </ol>
   */
  public void testAllocArraysForExpectedSizeConstructor() {
    // Test across range [0, DEFAULT_SIZE] to cover edge cases
    for (int expectedSize = 0; expectedSize <= CompactHashing.DEFAULT_SIZE; expectedSize++) {
      CompactHashSet<Integer> set = CompactHashSet.createWithExpectedSize(expectedSize);
      
      // Verify initial state (no arrays allocated)
      assertThat(set.needsAllocArrays()).isTrue();
      assertThat(set.elements).isNull();
      
      // Add element and verify arrays are initialized
      set.add(1);
      assertThat(set.needsAllocArrays()).isFalse();
      
      // Verify capacity matches max(1, expectedSize)
      int expectedCapacity = max(1, expectedSize);
      assertThat(set.elements).hasLength(expectedCapacity);
    }
  }
}