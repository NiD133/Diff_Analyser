package org.apache.ibatis.submitted.blocking_cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.ibatis.BaseDataTest;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BlockingCache Concurrency Test")
class BlockingCacheTest {

  private static final int NUM_THREADS = 2;
  private static final int TASK_DELAY_MS = 500;
  private static final long EXPECTED_MINIMUM_DURATION_MS = (long) TASK_DELAY_MS * NUM_THREADS;
  private static final int SHUTDOWN_TIMEOUT_SECONDS = 5;

  private static SqlSessionFactory sqlSessionFactory;

  @BeforeAll
  static void setup() throws Exception {
    // Create a SqlSessionFactory once for all tests
    try (Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml")) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    // Populate the in-memory database
    BaseDataTest.runScript(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql");
  }

  /**
   * Simulates a database access that is subject to caching.
   * Includes a delay to represent processing time.
   */
  private void accessDatabaseWithDelay() {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      // The first call to this method will hit the database. Subsequent calls
      // should hit the cache if caching is working correctly.
      mapper.findAll();

      // Simulate work. This delay occurs *before* the session is closed,
      // which is when MyBatis flushes session caches and puts the result
      // into the second-level (blocking) cache.
      Thread.sleep(TASK_DELAY_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      fail("Test thread was interrupted", e);
    }
  }

  @Test
  @DisplayName("Should block concurrent threads, causing serialized execution")
  void shouldBlockSecondThreadWhileFirstThreadPopulatesCache() throws InterruptedException {
    // Arrange
    ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
    long startTime = System.currentTimeMillis();

    // Act:
    // Execute two tasks concurrently that query the same data.
    // The BlockingCache should ensure the first thread acquires a lock and hits the database.
    // The second thread should block, waiting for the first to populate the cache.
    // Because each task has a 500ms delay, serialized execution will take > 1000ms.
    // If blocking fails, both threads would run in parallel, taking ~500ms.
    for (int i = 0; i < NUM_THREADS; i++) {
      executor.execute(this::accessDatabaseWithDelay);
    }

    executor.shutdown();
    if (!executor.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
      executor.shutdownNow();
    }

    // Assert
    long totalDuration = System.currentTimeMillis() - startTime;
    assertThat(totalDuration)
        .withFailMessage(
            "Total execution time should be at least %dms due to blocking, but was %dms. This suggests blocking did not occur.",
            EXPECTED_MINIMUM_DURATION_MS, totalDuration)
        .isGreaterThanOrEqualTo(EXPECTED_MINIMUM_DURATION_MS);
  }
}