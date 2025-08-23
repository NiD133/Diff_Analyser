package org.apache.ibatis.cache;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for CacheKey that focus on readability and intent.
 */
public class CacheKeyTest {

  // ---------------------------------------------------------------------------
  // Equality basics
  // ---------------------------------------------------------------------------

  @Test
  public void twoNewCacheKeysAreEqual() {
    CacheKey a = new CacheKey();
    CacheKey b = new CacheKey();

    assertTrue("Two brand new keys should be equal", a.equals(b));
    assertTrue("Equality should be symmetric", b.equals(a));
  }

  @Test
  public void keyIsEqualToItself() {
    CacheKey key = new CacheKey();
    assertTrue("A key must be equal to itself", key.equals(key));
  }

  @Test
  public void equalsReturnsFalseForNullOrDifferentType() {
    CacheKey key = new CacheKey();

    assertFalse("A key is never equal to null", key.equals(null));
    assertFalse("A key is not equal to a non-CacheKey instance", key.equals(new Object()));
  }

  @Test
  public void emptyKeyEqualsKeyConstructedWithEmptyArray() {
    CacheKey empty = new CacheKey();
    CacheKey fromEmptyArray = new CacheKey(new Object[0]);

    assertTrue("Empty key should equal key constructed with empty array", fromEmptyArray.equals(empty));
    assertTrue("Equality should be symmetric", empty.equals(fromEmptyArray));
  }

  // ---------------------------------------------------------------------------
  // Mutations affect equality
  // ---------------------------------------------------------------------------

  @Test
  public void updatingKeyMakesItUnequalToFreshKey() {
    CacheKey fresh = new CacheKey();
    CacheKey mutated = new CacheKey();

    // Initially equal
    assertTrue(mutated.equals(fresh));

    // After an update, equality should break
    mutated.update("anything");
    assertFalse(mutated.equals(fresh));
    assertFalse(fresh.equals(mutated));
  }

  @Test
  public void updatingWithSelfBreaksEqualityWithFreshKey() {
    CacheKey fresh = new CacheKey();
    CacheKey other = new CacheKey();

    // Initially equal
    assertTrue(other.equals(fresh));

    // Update using itself as the value
    other.update(other);
    assertFalse("Self-update must change equality", other.equals(fresh));
  }

  // ---------------------------------------------------------------------------
  // Update count
  // ---------------------------------------------------------------------------

  @Test
  public void updateCountStartsAtZero() {
    CacheKey key = new CacheKey();
    assertEquals(0, key.getUpdateCount());
  }

  @Test
  public void updateIncrementsCountEvenForNull() {
    CacheKey key = new CacheKey();

    key.update(null);
    assertEquals(1, key.getUpdateCount());
  }

  @Test
  public void updateAllIncrementsCountByArrayLength() {
    CacheKey key = new CacheKey();

    key.updateAll(new Object[7]);
    assertEquals(7, key.getUpdateCount());
  }

  // ---------------------------------------------------------------------------
  // Null handling
  // ---------------------------------------------------------------------------

  @Test(expected = NullPointerException.class)
  public void updateAllWithNullArrayThrowsNPE() {
    new CacheKey().updateAll(null);
  }

  @Test(expected = NullPointerException.class)
  public void constructorWithNullArrayThrowsNPE() {
    new CacheKey((Object[]) null);
  }

  // ---------------------------------------------------------------------------
  // NULL_CACHE_KEY guard
  // ---------------------------------------------------------------------------

  @Test
  public void nullCacheKeyRejectsUpdateAndUpdateAll() {
    CacheKey nullKey = CacheKey.NULL_CACHE_KEY;

    try {
      nullKey.update("x");
      fail("Expected RuntimeException from NULL_CACHE_KEY.update");
    } catch (RuntimeException ex) {
      assertEquals("Not allowed to update a null cache key instance.", ex.getMessage());
    }

    try {
      nullKey.updateAll(new Object[] { "x", "y" });
      fail("Expected RuntimeException from NULL_CACHE_KEY.updateAll");
    } catch (RuntimeException ex) {
      assertEquals("Not allowed to update a null cache key instance.", ex.getMessage());
    }
  }

  // ---------------------------------------------------------------------------
  // Clone and hash/toString
  // ---------------------------------------------------------------------------

  @Test
  public void cloneProducesEqualButIndependentInstance() {
    CacheKey original = new CacheKey();
    original.update("seed");

    CacheKey copy = original.clone();

    assertNotSame("Clone must be a different instance", original, copy);
    assertTrue("Clone must be equal to original", original.equals(copy));

    // Mutate the clone and ensure they diverge
    copy.update("extra");
    assertFalse("After mutation, clone should no longer equal original", original.equals(copy));
  }

  @Test
  public void hashCodeIsCallable() {
    // Intentionally not asserting the exact value to avoid coupling to implementation
    new CacheKey().hashCode();
  }

  @Test
  public void defaultToStringShowsHashAndChecksum() {
    CacheKey key = new CacheKey();
    assertEquals("17:0", key.toString());
  }

  // ---------------------------------------------------------------------------
  // Misc: equals vs. object inside an array used to build a key
  // ---------------------------------------------------------------------------

  @Test
  public void keyBuiltFromArrayIsNotEqualToAnElementOfThatArray() {
    Object[] values = new Object[5];
    CacheKey viaArray = new CacheKey(values);

    // Put the key into the original array and build another key from it
    values[2] = viaArray;
    CacheKey withKeyInside = new CacheKey(values);

    assertEquals(5, withKeyInside.getUpdateCount());
    assertFalse("A CacheKey is not equal to one of the array elements it was built from",
        withKeyInside.equals(values[2]));
  }
}