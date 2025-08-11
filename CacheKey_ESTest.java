/*
 * Test suite for CacheKey class functionality
 * Tests cover construction, updates, equality, and edge cases
 */

package org.apache.ibatis.cache;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.apache.ibatis.cache.CacheKey;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class CacheKeyTest extends CacheKey_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void testEqualityChangesAfterUpdate() throws Throwable {
      // Given: Two identical empty cache keys
      CacheKey firstKey = new CacheKey();
      CacheKey secondKey = new CacheKey();
      assertTrue("Empty cache keys should be equal", secondKey.equals(firstKey));
      
      // When: One key is updated with the other key as content
      secondKey.update(firstKey);
      
      // Then: Keys should no longer be equal
      boolean areEqual = firstKey.equals(secondKey);
      assertFalse("Cache keys should not be equal after one is updated", areEqual);
  }

  @Test(timeout = 4000)
  public void testUpdateCountIncrementsAfterSingleUpdate() throws Throwable {
      // Given: A new cache key
      CacheKey cacheKey = new CacheKey();
      
      // When: An object is added to the cache key
      Object testObject = new Object();
      cacheKey.update(testObject);
      
      // Then: Update count should be 1
      int updateCount = cacheKey.getUpdateCount();
      assertEquals("Update count should be 1 after single update", 1, updateCount);
  }

  @Test(timeout = 4000)
  public void testUpdateAllThrowsExceptionForNullArray() throws Throwable {
      // Given: A cache key
      CacheKey cacheKey = new CacheKey();
      
      // When/Then: Updating with null array should throw NullPointerException
      try { 
        cacheKey.updateAll((Object[]) null);
        fail("Expected NullPointerException when updating with null array");
      } catch(NullPointerException e) {
         verifyException("org.apache.ibatis.cache.CacheKey", e);
      }
  }

  @Test(timeout = 4000)
  public void testConstructorThrowsExceptionForNullArray() throws Throwable {
      // When/Then: Creating cache key with null array should throw NullPointerException
      try {
        CacheKey cacheKey = new CacheKey((Object[]) null);
        fail("Expected NullPointerException when constructing with null array");
      } catch(NullPointerException e) {
         verifyException("org.apache.ibatis.cache.CacheKey", e);
      }
  }

  @Test(timeout = 4000)
  public void testUpdateAllWithMultipleObjects() throws Throwable {
      // Given: A cache key and an array of objects
      CacheKey cacheKey = new CacheKey();
      Object[] objectArray = new Object[7];
      
      // When: All objects are added to the cache key
      cacheKey.updateAll(objectArray);
      
      // Then: Update count should match array length
      assertEquals("Update count should match number of objects added", 7, cacheKey.getUpdateCount());
  }

  @Test(timeout = 4000)
  public void testUpdateWithNullObject() throws Throwable {
      // Given: A cache key
      CacheKey cacheKey = new CacheKey();
      
      // When: Null object is added to the cache key
      cacheKey.update((Object) null);
      
      // Then: Update count should still increment
      assertEquals("Update count should increment even for null objects", 1, cacheKey.getUpdateCount());
  }

  @Test(timeout = 4000)
  public void testEqualityWithCircularReference() throws Throwable {
      // Given: An array and cache key with circular reference
      Object[] objectArray = new Object[5];
      CacheKey firstKey = new CacheKey(objectArray);
      objectArray[2] = firstKey; // Create circular reference
      CacheKey secondKey = new CacheKey(objectArray);
      
      // When/Then: Keys with circular references should not be equal to the referenced object
      boolean areEqual = secondKey.equals(objectArray[2]);
      assertEquals("Cache key should have correct update count", 5, secondKey.getUpdateCount());
      assertFalse("Cache key should not equal object it contains", areEqual);
  }

  @Test(timeout = 4000)
  public void testEqualityChangesAfterSelfUpdate() throws Throwable {
      // Given: Two initially equal cache keys
      CacheKey firstKey = new CacheKey();
      Object[] emptyArray = new Object[0];
      CacheKey secondKey = new CacheKey(emptyArray);
      assertTrue("Keys should initially be equal", secondKey.equals(firstKey));
      
      // When: One key updates itself
      secondKey.update(secondKey);
      
      // Then: Keys should no longer be equal
      boolean areEqual = secondKey.equals(firstKey);
      assertFalse("Keys should not be equal after self-update", areEqual);
  }

  @Test(timeout = 4000)
  public void testEqualityWithNull() throws Throwable {
      // Given: A cache key
      CacheKey cacheKey = new CacheKey();
      
      // When/Then: Cache key should not equal null
      boolean equalsNull = cacheKey.equals(null);
      assertFalse("Cache key should not equal null", equalsNull);
  }

  @Test(timeout = 4000)
  public void testSelfEquality() throws Throwable {
      // Given: A cache key
      CacheKey cacheKey = new CacheKey();
      
      // When/Then: Cache key should equal itself
      boolean equalsSelf = cacheKey.equals(cacheKey);
      assertTrue("Cache key should equal itself", equalsSelf);
  }

  @Test(timeout = 4000)
  public void testEqualityOfEmptyCacheKeys() throws Throwable {
      // Given: Two empty cache keys created differently
      CacheKey firstKey = new CacheKey();
      Object[] emptyArray = new Object[0];
      CacheKey secondKey = new CacheKey(emptyArray);
      
      // When/Then: Empty cache keys should be equal
      boolean areEqual = secondKey.equals(firstKey);
      assertTrue("Empty cache keys should be equal regardless of creation method", areEqual);
  }

  @Test(timeout = 4000)
  public void testNullCacheKeyUpdateAllThrowsException() throws Throwable {
      // Given: An array of objects and access to NULL_CACHE_KEY
      Object[] objectArray = new Object[4];
      CacheKey cacheKey = new CacheKey(objectArray);
      
      // When/Then: Updating NULL_CACHE_KEY should throw RuntimeException
      try { 
        cacheKey.NULL_CACHE_KEY.updateAll(objectArray);
        fail("Expected RuntimeException when updating NULL_CACHE_KEY");
      } catch(RuntimeException e) {
         assertEquals("Not allowed to update a null cache key instance.", e.getMessage());
         verifyException("org.apache.ibatis.cache.CacheKey$1", e);
      }
  }

  @Test(timeout = 4000)
  public void testClonedCacheKeyEquality() throws Throwable {
      // Given: A cache key with content
      CacheKey originalKey = new CacheKey();
      originalKey.update(originalKey);
      
      // When: Cache key is cloned
      CacheKey clonedKey = originalKey.clone();
      
      // Then: Clone should equal original but be different instance
      boolean areEqual = originalKey.equals(clonedKey);
      assertNotSame("Cloned key should be different instance", clonedKey, originalKey);
      assertTrue("Cloned key should equal original", areEqual);
  }

  @Test(timeout = 4000)
  public void testHashCodeGeneration() throws Throwable {
      // Given: A cache key
      CacheKey cacheKey = new CacheKey();
      
      // When/Then: Hash code should be generated without exception
      cacheKey.hashCode(); // Should not throw exception
  }

  @Test(timeout = 4000)
  public void testCloneCreatesNewInstance() throws Throwable {
      // Given: A cache key
      CacheKey originalKey = new CacheKey();
      
      // When: Cache key is cloned
      CacheKey clonedKey = originalKey.clone();
      
      // Then: Clone should be different instance
      assertNotSame("Clone should create new instance", clonedKey, originalKey);
  }

  @Test(timeout = 4000)
  public void testToStringFormat() throws Throwable {
      // Given: An empty cache key
      CacheKey cacheKey = new CacheKey();
      
      // When: String representation is generated
      String stringRepresentation = cacheKey.toString();
      
      // Then: Should follow expected format (hashcode:updatecount)
      assertEquals("String representation should follow expected format", "17:0", stringRepresentation);
  }

  @Test(timeout = 4000)
  public void testInitialUpdateCount() throws Throwable {
      // Given: A new cache key
      CacheKey cacheKey = new CacheKey();
      
      // When/Then: Initial update count should be zero
      int updateCount = cacheKey.getUpdateCount();
      assertEquals("Initial update count should be zero", 0, updateCount);
  }
}