/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.submitted.blocking_cache;

import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for MyBatis BlockingCache functionality (issue #524).
 * 
 * BlockingCache prevents multiple threads from executing the same query simultaneously
 * by blocking subsequent threads until the first thread completes and populates the cache.
 */
class BlockingCacheTest {

  private static final int THREAD_POOL_SIZE = 2;
  private static final int THREAD_EXECUTION_TIMEOUT_SECONDS = 5;
  private static final int DATABASE_ACCESS_DELAY_MS = 500;
  private static final int EXPECTED_MINIMUM_EXECUTION_TIME_MS = 1000;
  
  private static SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  void setUp() throws Exception {
    initializeSqlSessionFactory();
    populateTestDatabase();
  }

  /**
   * Tests that BlockingCache properly blocks concurrent access to the same cache key.
   * 
   * When two threads try to access the same cached data simultaneously:
   * - The first thread executes the query and populates the cache
   * - The second thread waits (blocks) until the first thread completes
   * - Total execution time should be at least the sum of both operations (sequential execution)
   */
  @Test
  void shouldBlockConcurrentAccessToSameCacheKey() throws InterruptedException {
    // Given: A thread pool with 2 threads
    ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    long startTime = System.currentTimeMillis();

    // When: Both threads try to access the database simultaneously
    for (int i = 0; i < THREAD_POOL_SIZE; i++) {
      threadPool.execute(this::performDatabaseAccessWithDelay);
    }

    // Then: Wait for all threads to complete
    shutdownThreadPoolGracefully(threadPool);
    
    // And: Verify that blocking occurred (execution took longer than parallel execution would)
    long totalExecutionTime = System.currentTimeMillis() - startTime;
    Assertions.assertThat(totalExecutionTime)
        .as("BlockingCache should cause sequential execution, taking at least %d ms", 
            EXPECTED_MINIMUM_EXECUTION_TIME_MS)
        .isGreaterThanOrEqualTo(EXPECTED_MINIMUM_EXECUTION_TIME_MS);
  }

  /**
   * Tests that cache locks are properly acquired before putting data into cache.
   * 
   * This ensures that the locking mechanism works correctly during cache population
   * after a cache-invalidating operation (delete).
   */
  @Test
  void shouldAcquireLockBeforePuttingDataIntoCache() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      
      // Given: Cache is invalidated by a delete operation
      mapper.delete(-1); // Delete non-existent record to trigger cache invalidation
      
      // When: Data is queried (which will populate the cache)
      mapper.findAll();
      
      // Then: Transaction commits successfully (lock was acquired properly)
      sqlSession.commit();
    }
    // Test passes if no exceptions are thrown
  }

  /**
   * Tests that cache locks are properly released when a transaction is rolled back.
   * 
   * This prevents deadlocks by ensuring that locks don't remain held indefinitely
   * when transactions fail or are explicitly rolled back.
   */
  @Test
  void shouldReleaseLockOnTransactionRollback() {
    // Given: A transaction that will be rolled back
    try (SqlSession firstSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = firstSession.getMapper(PersonMapper.class);
      
      mapper.delete(-1); // Invalidate cache
      mapper.findAll();  // Acquire lock for cache population
      
      // When: Transaction is rolled back
      firstSession.rollback(); // This should release the lock
    }
    
    // Then: Another session should be able to access the same data without blocking
    try (SqlSession secondSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = secondSession.getMapper(PersonMapper.class);
      mapper.findAll(); // Should not block indefinitely
    }
    // Test passes if the second session doesn't hang
  }

  // Helper Methods
  
  private void initializeSqlSessionFactory() throws Exception {
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
  }

  private void populateTestDatabase() throws Exception {
    BaseDataTest.runScript(
        sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
  }

  private void performDatabaseAccessWithDelay() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      
      // Execute query that will be cached
      personMapper.findAll();
      
      // Simulate processing time to make blocking behavior observable
      simulateProcessingDelay();
    }
  }

  private void simulateProcessingDelay() {
    try {
      Thread.sleep(DATABASE_ACCESS_DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Assertions.fail("Thread was interrupted during processing delay: " + e.getMessage());
    }
  }

  private void shutdownThreadPoolGracefully(ExecutorService threadPool) throws InterruptedException {
    threadPool.shutdown();
    if (!threadPool.awaitTermination(THREAD_EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
      threadPool.shutdownNow();
      Assertions.fail("Thread pool did not terminate within the expected time");
    }
  }
}