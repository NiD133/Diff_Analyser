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
 * Contains test cases and a test suite for {@link CompactLinkedHashSet}.
 *
 * <p>This class uses the Guava Testlib to generate a comprehensive suite of tests covering the
 * {@link java.util.Set} interface contract.
 */
@NullUnmarked
public class CompactLinkedHashSetTestSuite extends TestCase {

  /**
   * Creates a JUnit 3 test suite for {@link CompactLinkedHashSet}.
   *
   * <p>This suite combines tests from {@link CompactLinkedHashSetTest} with generated test suites
   * from Guava Testlib for both the standard and hash-flooding-resistant implementations.
   */
  @AndroidIncompatible // TODO(b/65483407): Banning GwtIncompatible from Android.
  public static Test suite() {
    TestSuite suite = new TestSuite("CompactLinkedHashSet");

    // Add existing tests for CompactLinkedHashSet.
    suite.addTestSuite(CompactLinkedHashSetTest.class);

    // Add Guava Testlib suites for comprehensive contract testing.
    suite.addTest(createGuavaTestlibSuite());
    suite.addTest(createGuavaTestlibSuiteForFloodingResistantImplementation());

    return suite;
  }

  /** Returns a Guava Testlib suite for the standard {@link CompactLinkedHashSet}. */
  private static Test createGuavaTestlibSuite() {
    return SetTestSuiteBuilder.using(newStandardSetGenerator())
        .named("CompactLinkedHashSet")
        .withFeatures(getRequiredFeatures())
        .createTestSuite();
  }

  /**
   * Returns a Guava Testlib suite for the hash-flooding-resistant implementation of {@link
   * CompactLinkedHashSet}.
   */
  private static Test createGuavaTestlibSuiteForFloodingResistantImplementation() {
    return SetTestSuiteBuilder.using(newFloodingResistantSetGenerator())
        .named("CompactLinkedHashSet with flooding protection")
        .withFeatures(getRequiredFeatures())
        .createTestSuite();
  }

  /** Defines the set of features supported by {@link CompactLinkedHashSet}. */
  private static List<Feature<?>> getRequiredFeatures() {
    return Arrays.asList(
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

  /** Generator for a standard {@link CompactLinkedHashSet}. */
  private static TestStringSetGenerator newStandardSetGenerator() {
    return new TestStringSetGenerator() {
      @Override
      protected Set<String> create(String[] elements) {
        return CompactLinkedHashSet.create(asList(elements));
      }
    };
  }

  /** Generator for a {@link CompactLinkedHashSet} with hash-flooding protection enabled. */
  private static TestStringSetGenerator newFloodingResistantSetGenerator() {
    return new TestStringSetGenerator() {
      @Override
      protected Set<String> create(String[] elements) {
        CompactLinkedHashSet<String> set = CompactLinkedHashSet.create();
        set.convertToHashFloodingResistantImplementation();
        Collections.addAll(set, elements);
        return set;
      }
    };
  }

  /**
   * Verifies that creating a set with an expected size defers internal array allocation until the
   * first element is added. This test validates behavior inherited from the {@link
   * CompactHashSet} superclass.
   */
  public void testCreateWithExpectedSize_lazilyAllocatesInternalArrays() {
    // Test with various initial expected sizes, from 0 up to the default capacity.
    for (int expectedSizeInput = 0; expectedSizeInput <= CompactHashing.DEFAULT_SIZE; expectedSizeInput++) {
      // Arrange: Create a set with an expected size, but do not add any elements yet.
      CompactHashSet<Integer> set = CompactHashSet.createWithExpectedSize(expectedSizeInput);

      // Assert: Internal arrays should not be allocated yet (lazy initialization).
      assertThat(set.needsAllocArrays())
          .withMessage(
              "needsAllocArrays() should be true for expectedSize %s before adding elements",
              expectedSizeInput)
          .isTrue();
      assertThat(set.elements)
          .withMessage(
              "elements should be null for expectedSize %s before adding elements",
              expectedSizeInput)
          .isNull();

      // Act: Add the first element, which should trigger the array allocation.
      set.add(1);

      // Assert: Internal arrays should now be allocated.
      assertThat(set.needsAllocArrays())
          .withMessage(
              "needsAllocArrays() should be false for expectedSize %s after adding an element",
              expectedSizeInput)
          .isFalse();

      // The allocated array size should be the maximum of 1 and the expected size.
      int expectedArrayLength = max(1, expectedSizeInput);
      assertThat(set.elements)
          .withMessage(
              "elements array for expectedSize %s should have been allocated", expectedSizeInput)
          .isNotNull();
      assertThat(set.elements).hasLength(expectedArrayLength);
    }
  }
}