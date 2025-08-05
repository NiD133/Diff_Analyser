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
import java.time.Duration;
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
 * Tests for the {@link org.apache.ibatis.cache.decorators.BlockingCache}.
 * This suite verifies that the cache correctly blocks concurrent threads to prevent
 * a "cache stampede" and properly manages lock releases on commit and rollback.
 *
 * Based on issue #524.
 */
class BlockingCacheTest {

  private static final int NUM_THREADS = 2;
  private static final long PER_THREAD_SLEEP_MS = 500L;
  // The total time should be at least the sum of individual sleep times,
  // as the cache lock serializes the execution.
  private static final long EXPECTED_MIN_TOTAL_TIME_MS = PER_THREAD_SLEEP_MS * NUM_THREADS;

  private SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  void setUp() throws Exception {
    // create a SqlSessionFactory
    try (Reader reader = Resources
        .getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }

    // populate in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
  }

  /**
   * Verifies that when multiple threads request the same uncached data, the BlockingCache
   * serializes the requests. This prevents multiple threads from hitting the database
   * simultaneously for the same query.
   */
  @Test
  void shouldBlockConcurrentThreadsOnCacheMiss() throws InterruptedException {
    // Arrange
    ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

    // Act
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < NUM_THREADS; i++) {
      executor.execute(this::queryAllPersonsWithDelay);
    }

    executor.shutdown();
    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
      executor.shutdownNow();
      Assertions.fail("Test threads did not complete in time.");
    }

    long totalTime = System.currentTimeMillis() - startTime;

    // Assert
    /*
     * How timing works:
     * 1. Thread 1 misses the cache, acquires a lock, and queries the database.
     * 2. Thread 2 attempts the same query but blocks on the lock held by Thread 1.
     * 3. Thread 1 finishes the query, then sleeps for 500ms. When its session closes,
     *    the result is cached and the lock is released.
     * 4. Thread 2 acquires the lock, immediately finds the data in the cache, and then
     *    sleeps for 500ms.
     * The total execution time is serialized, so it should be at least 2 * 500ms.
     */
    Assertions.assertThat(totalTime).isGreaterThanOrEqualTo(EXPECTED_MIN_TOTAL_TIME_MS);
  }

  private void queryAllPersonsWithDelay() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper pm = sqlSession.getMapper(PersonMapper.class);
      pm.findAll();
      // Simulate additional work to ensure threads overlap.
      Thread.sleep(PER_THREAD_SLEEP_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Thread was interrupted during sleep", e);
    }
  }

  /**
   * Verifies that a lock acquired on a cache miss is correctly released when the session is committed.
   * A commit triggers a `putObject` into the cache, which should release the lock. This test
   * ensures the sequence does not result in a deadlock or timeout.
   */
  @Test
  void shouldReleaseLockOnCommit() {
    // Arrange: A sequence of operations within a single session.
    // Act & Assert: We expect this to complete without throwing any exceptions (e.g., from a deadlock).
    Assertions.assertDoesNotThrow(() -> {
      try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
        PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
        // This flushes the cache for the namespace, ensuring the next `findAll` is a miss.
        mapper.delete(-1);
        // This should be a cache miss, which acquires a lock.
        mapper.findAll();
        // Committing the session puts the result in the cache and must release the lock.
        sqlSession.commit();
      }
    });
  }

  /**
   * Verifies that a lock acquired on a cache miss is released when the session is rolled back.
   * This prevents other sessions from being permanently blocked if a transaction fails.
   */
  @Test
  void shouldReleaseLockOnRollback() {
    // Arrange: The first session acquires a lock and then rolls back.
    try (SqlSession sqlSession1 = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession1.getMapper(PersonMapper.class);
      // Flush the cache to ensure the next `findAll` is a miss.
      mapper.delete(-1);
      // This is a cache miss and should acquire a lock.
      mapper.findAll();
      // Rolling back should release the lock without caching the result.
      sqlSession1.rollback();
    }

    // Act & Assert: The second session should be able to proceed without getting stuck.
    // We use a timeout to explicitly verify that the second session does not deadlock.
    Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
      try (SqlSession sqlSession2 = sqlSessionFactory.openSession()) {
        PersonMapper mapper = sqlSession2.getMapper(PersonMapper.class);
        // This call should not block, as the lock from the first session should have been released.
        mapper.findAll();
      }
    }, "The second session timed out, indicating the lock was not released on rollback.");
  }
}