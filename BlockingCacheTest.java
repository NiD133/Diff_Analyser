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
 */
class BlockingCacheTest {

  private static SqlSessionFactory sqlSessionFactory;

  /**
   * Sets up the test environment by initializing the SqlSessionFactory and populating the in-memory database.
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
   * Tests that the blocking cache causes threads to wait, ensuring that the total execution time is at least 1000 ms.
   */
  @Test
  void testBlockingCacheDelaysExecution() throws InterruptedException {
    ExecutorService threadPool = Executors.newFixedThreadPool(2);
    long startTime = System.currentTimeMillis();

    for (int i = 0; i < 2; i++) {
      threadPool.execute(this::simulateDatabaseAccess);
    }

    threadPool.shutdown();
    if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
      threadPool.shutdownNow();
    }

    long elapsedTime = System.currentTimeMillis() - startTime;
    Assertions.assertThat(elapsedTime).isGreaterThanOrEqualTo(1000);
  }

  /**
   * Simulates database access by retrieving all persons and introducing a delay.
   */
  private void simulateDatabaseAccess() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper personMapper = sqlSession.getMapper(PersonMapper.class);
      personMapper.findAll();
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Assertions.fail("Thread was interrupted: " + e.getMessage());
    }
  }

  /**
   * Ensures that a lock is acquired before an object is put into the cache.
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
   * Ensures that a lock is released when a transaction is rolled back.
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