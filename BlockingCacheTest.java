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
 * Test suite for the BlockingCache functionality.
 * Ensures that the cache behaves correctly under concurrent access.
 */
class BlockingCacheTest {

  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Sets up the test environment by initializing the SqlSessionFactory
   * and populating the in-memory database.
   */
  @BeforeEach
  void setUp() throws Exception {
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
  }

  /**
   * Tests that the BlockingCache blocks concurrent access correctly.
   * Ensures that the total execution time is at least 1000 milliseconds.
   */
  @Test
  void testBlockingCacheUnderConcurrentAccess() throws InterruptedException {
    ExecutorService threadPool = Executors.newFixedThreadPool(2);
    long startTime = System.currentTimeMillis();

    // Execute database access in two separate threads
    for (int i = 0; i < 2; i++) {
      threadPool.execute(this::accessDatabase);
    }

    // Shutdown the thread pool and wait for tasks to complete
    threadPool.shutdown();
    if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
      threadPool.shutdownNow();
    }

    long totalTime = System.currentTimeMillis() - startTime;
    Assertions.assertThat(totalTime).isGreaterThanOrEqualTo(1000);
  }

  /**
   * Simulates database access by retrieving all persons and introducing a delay.
   */
  private void accessDatabase() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      personMapper.findAll();
      // Simulate processing delay
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Assertions.fail("Thread was interrupted: " + e.getMessage());
    }
  }

  /**
   * Tests that the lock is acquired before a put operation in the cache.
   */
  @Test
  void testLockAcquisitionBeforePut() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      personMapper.delete(-1);
      personMapper.findAll();
      sqlSession.commit();
    }
  }

  /**
   * Tests that the lock is released when a transaction is rolled back.
   */
  @Test
  void testLockReleaseOnRollback() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      personMapper.delete(-1);
      personMapper.findAll();
      sqlSession.rollback();
    }
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      personMapper.findAll();
    }
  }
}