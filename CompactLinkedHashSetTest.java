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
    TestSuite suite = new TestSuite();
    
    // Add unit tests from this class
    suite.addTestSuite(CompactLinkedHashSetTest.class);
    
    // Add comprehensive test suite for standard CompactLinkedHashSet
    suite.addTest(createStandardCompactLinkedHashSetTestSuite());
    
    // Add comprehensive test suite for CompactLinkedHashSet with flooding protection
    suite.addTest(createFloodingResistantCompactLinkedHashSetTestSuite());
    
    return suite;
  }

  /**
   * Creates a test suite for the standard CompactLinkedHashSet implementation.
   */
  private static Test createStandardCompactLinkedHashSetTestSuite() {
    return SetTestSuiteBuilder.using(new StandardCompactLinkedHashSetGenerator())
        .named("CompactLinkedHashSet")
        .withFeatures(getAllSupportedFeatures())
        .createTestSuite();
  }

  /**
   * Creates a test suite for CompactLinkedHashSet with flooding protection enabled.
   */
  private static Test createFloodingResistantCompactLinkedHashSetTestSuite() {
    return SetTestSuiteBuilder.using(new FloodingResistantCompactLinkedHashSetGenerator())
        .named("CompactLinkedHashSet with flooding protection")
        .withFeatures(getAllSupportedFeatures())
        .createTestSuite();
  }

  /**
   * Returns all features supported by CompactLinkedHashSet implementations.
   */
  private static List<Feature<?>> getAllSupportedFeatures() {
    return Arrays.<Feature<?>>asList(
        CollectionSize.ANY,
        CollectionFeature.ALLOWS_NULL_VALUES,
        CollectionFeature.FAILS_FAST_ON_CONCURRENT_MODIFICATION,
        CollectionFeature.GENERAL_PURPOSE,
        CollectionFeature.REMOVE_OPERATIONS,
        CollectionFeature.SERIALIZABLE,
        CollectionFeature.KNOWN_ORDER,
        CollectionFeature.SUPPORTS_ADD,
        CollectionFeature.SUPPORTS_REMOVE);
  }

  /**
   * Generator for standard CompactLinkedHashSet instances used in comprehensive testing.
   */
  private static class StandardCompactLinkedHashSetGenerator extends TestStringSetGenerator {
    @Override
    protected Set<String> create(String[] elements) {
      return CompactLinkedHashSet.create(asList(elements));
    }
  }

  /**
   * Generator for CompactLinkedHashSet instances with flooding protection enabled.
   */
  private static class FloodingResistantCompactLinkedHashSetGenerator extends TestStringSetGenerator {
    @Override
    protected Set<String> create(String[] elements) {
      CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
      set.convertToHashFloodingResistantImplementation();
      Collections.addAll(set, elements);
      return set;
    }
  }

  // Unit Tests

  /**
   * Tests that CompactHashSet properly handles lazy array allocation with default sizing.
   * 
   * When created with default constructor:
   * - Arrays should not be allocated until first element is added
   * - After adding first element, arrays should be allocated with default size
   */
  public void testLazyArrayAllocation_DefaultSize() {
    CompactHashSet<Integer> set = CompactHashSet.create();
    
    // Before adding any elements, arrays should not be allocated
    assertArraysNotAllocated(set);

    // After adding first element, arrays should be allocated with default size
    set.add(SAMPLE_ELEMENT);
    assertArraysAllocatedWithSize(set, CompactHashing.DEFAULT_SIZE);
  }

  /**
   * Tests that CompactHashSet properly handles lazy array allocation with expected size.
   * 
   * When created with expected size:
   * - Arrays should not be allocated until first element is added
   * - After adding first element, arrays should be allocated with size >= expected size
   */
  public void testLazyArrayAllocation_ExpectedSize() {
    for (int expectedSize = 0; expectedSize <= CompactHashing.DEFAULT_SIZE; expectedSize++) {
      CompactHashSet<Integer> set = CompactHashSet.createWithExpectedSize(expectedSize);
      
      // Before adding any elements, arrays should not be allocated
      assertArraysNotAllocated(set);

      // After adding first element, arrays should be allocated with appropriate size
      set.add(SAMPLE_ELEMENT);
      int expectedArraySize = max(1, expectedSize);
      assertArraysAllocatedWithSize(set, expectedArraySize);
    }
  }

  // Test Helpers

  private static final Integer SAMPLE_ELEMENT = 1;

  /**
   * Asserts that the given set has not yet allocated its internal arrays.
   */
  private void assertArraysNotAllocated(CompactHashSet<Integer> set) {
    assertThat(set.needsAllocArrays())
        .named("Set should need array allocation")
        .isTrue();
    assertThat(set.elements)
        .named("Elements array should be null before allocation")
        .isNull();
  }

  /**
   * Asserts that the given set has allocated its internal arrays with the expected size.
   */
  private void assertArraysAllocatedWithSize(CompactHashSet<Integer> set, int expectedSize) {
    assertThat(set.needsAllocArrays())
        .named("Set should not need array allocation after first element added")
        .isFalse();
    assertThat(set.elements)
        .named("Elements array should have expected length after allocation")
        .hasLength(expectedSize);
  }
}