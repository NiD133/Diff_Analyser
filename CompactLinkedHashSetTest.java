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
 * Unit tests for the CompactLinkedHashSet class.
 * This test suite verifies the behavior of CompactLinkedHashSet under various conditions.
 * It includes tests for default initialization, expected size initialization, and hash flooding protection.
 * 
 * Author: Dimitris Andreou
 */
@GwtIncompatible // java.util.Arrays#copyOf(Object[], int), java.lang.reflect.Array
@NullUnmarked
public class CompactLinkedHashSetTest extends TestCase {

  @AndroidIncompatible // test-suite builders
  public static Test suite() {
    // Define all features that the CompactLinkedHashSet should support
    List<Feature<?>> allFeatures = Arrays.asList(
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

    TestSuite suite = new TestSuite();
    suite.addTestSuite(CompactLinkedHashSetTest.class);

    // Add test suite for CompactLinkedHashSet without hash flooding protection
    suite.addTest(
        SetTestSuiteBuilder.using(new TestStringSetGenerator() {
          @Override
          protected Set<String> create(String[] elements) {
            return CompactLinkedHashSet.create(asList(elements));
          }
        })
        .named("CompactLinkedHashSet")
        .withFeatures(allFeatures)
        .createTestSuite()
    );

    // Add test suite for CompactLinkedHashSet with hash flooding protection
    suite.addTest(
        SetTestSuiteBuilder.using(new TestStringSetGenerator() {
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
        .createTestSuite()
    );

    return suite;
  }

  /**
   * Test the default allocation behavior of CompactHashSet.
   * Verifies that arrays are allocated only when needed.
   */
  public void testAllocArraysDefault() {
    CompactHashSet<Integer> set = CompactHashSet.create();
    assertThat(set.needsAllocArrays()).isTrue();
    assertThat(set.elements).isNull();

    set.add(1);
    assertThat(set.needsAllocArrays()).isFalse();
    assertThat(set.elements).hasLength(CompactHashing.DEFAULT_SIZE);
  }

  /**
   * Test the allocation behavior of CompactHashSet with an expected size.
   * Ensures that the set allocates arrays correctly based on the expected size.
   */
  public void testAllocArraysExpectedSize() {
    for (int i = 0; i <= CompactHashing.DEFAULT_SIZE; i++) {
      CompactHashSet<Integer> set = CompactHashSet.createWithExpectedSize(i);
      assertThat(set.needsAllocArrays()).isTrue();
      assertThat(set.elements).isNull();

      set.add(1);
      assertThat(set.needsAllocArrays()).isFalse();
      int expectedSize = max(1, i);
      assertThat(set.elements).hasLength(expectedSize);
    }
  }
}