package org.apache.ibatis.submitted.blocking_cache;

import java.io.Reader;
import java.util.concurrent.CountDownLatch;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests for the BlockingCache decorator (issue #524).
 *
 * Notes
 * - The cache is expected to serialize concurrent cache-miss loaders.
 * - When two threads request the same missing key, one should load from DB while the other waits for the value.
 * - Some tests have no explicit assertions; they pass if they complete within a timeout (i.e., no deadlock).
 */
class BlockingCacheTest {

  private static final String MYBATIS_CONFIG = "org/apache/ibatis/submitted/blocking_cache/mybatis-config.xml";
  private static final String CREATE_DB_SQL = "org/apache/ibatis/submitted/blocking_cache/CreateDB.sql";

  // Concurrency configuration for the main blocking test
  private static final int CONCURRENT_CALLERS = 2;
  private static final long SIMULATED_WORK_MS = 500L; // Simulated "work" to keep the first caller busy
  private static final long EXPECTED_MIN_TOTAL_MS = SIMULATED_WORK_MS * CONCURRENT_CALLERS;
  private static final long AWAIT_SEC = 5L;

  private SqlSessionFactory sqlSessionFactory;

  @BeforeEach
  void setUp() throws Exception {
    try (Reader reader = Resources.getResourceAsReader(MYBATIS_CONFIG)) {
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }
    BaseDataTest.runScript(
        sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(),
        CREATE_DB_SQL
    );
  }

  @Test
  @DisplayName("Blocks concurrent cache-miss loaders so total time >= 2x single load")
  @Timeout(value = 10, unit = TimeUnit.SECONDS)
  void blocksConcurrentCacheMisses() throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(CONCURRENT_CALLERS);
    CountDownLatch startGate = new CountDownLatch(1);
    CountDownLatch doneGate = new CountDownLatch(CONCURRENT_CALLERS);

    long startNanos = System.nanoTime();

    for (int i = 0; i < CONCURRENT_CALLERS; i++) {
      pool.execute(() -> {
        try {
          queryAllPeopleWithSimulatedWork(startGate);
        } finally {
          doneGate.countDown();
        }
      });
    }

    // Start both tasks at (almost) the same time to increase the chance of contending on the same cache key.
    startGate.countDown();

    // Wait for both tasks to complete
    boolean finished = doneGate.await(AWAIT_SEC, TimeUnit.SECONDS);
    pool.shutdownNow();

    Assertions.assertThat(finished)
        .as("Workers did not finish within the timeout; possible deadlock or excessive delay")
        .isTrue();

    long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);

    // If the second thread is properly blocked while the first loads, total time should be >= 1000 ms.
    Assertions.assertThat(elapsedMs)
        .as("Expected serialized loading due to BlockingCache")
        .isGreaterThanOrEqualTo(EXPECTED_MIN_TOTAL_MS);
  }

  /**
   * Opens a session, waits on the start gate, performs the query and sleeps to simulate work, then closes the session.
   */
  private void queryAllPeopleWithSimulatedWork(CountDownLatch startGate) {
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);

      // Ensure both threads call the query at the same time to contend on the same cache key.
      startGate.await();

      // First thread should load from DB and populate the cache; others should block until the value is available.
      mapper.findAll();

      sleepQuietly(SIMULATED_WORK_MS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Assertions.fail("Test thread was interrupted", e);
    }
  }

  @Test
  @DisplayName("Acquires lock before put on commit path (should not deadlock)")
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void acquiresLockBeforePutOnCommit() {
    // This passes if it completes without deadlock.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.delete(-1); // touch cache with a 'delete' to exercise put/remove paths
      mapper.findAll();  // load data and interact with the cache
      sqlSession.commit();
    }
  }

  @Test
  @DisplayName("Releases lock on rollback path (should not deadlock and should allow subsequent access)")
  @Timeout(value = 5, unit = TimeUnit.SECONDS)
  void releasesLockOnRollback() {
    // First, perform operations and roll back; lock should be released.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.delete(-1);
      mapper.findAll();
      sqlSession.rollback();
    }
    // Then, ensure we can access the cache/query again without issues.
    try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
      PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
      mapper.findAll();
    }
  }

  private static void sleepQuietly(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      Assertions.fail("Sleep was interrupted", e);
    }
  }
}