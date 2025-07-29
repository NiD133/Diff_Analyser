package org.jfree.chart.entity;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.time.Clock;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.JapaneseDate;
import javax.swing.JLayeredPane;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultMultiValueCategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CategoryItemEntity_ESTest extends CategoryItemEntity_ESTest_scaffolding {

    private static final String TOOLTIP_TEXT = "N`";
    private static final String URL_TEXT = "N`";

    @Test(timeout = 4000)
    public void testSetRowKeyToNull() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, TOOLTIP_TEXT, URL_TEXT, dataset, 0, 0);

        entity.setRowKey(null);
        assertNull(entity.getRowKey());
    }

    @Test(timeout = 4000)
    public void testGetRowKey() {
        Polygon polygon = new Polygon();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        LocalDate localDate = MockLocalDate.now();
        CategoryItemEntity<ChronoLocalDate, Integer> entity = new CategoryItemEntity<>(polygon, "nO,\"", TOOLTIP_TEXT, dataset, localDate, polygon.npoints);

        assertSame(localDate, entity.getRowKey());
    }

    @Test(timeout = 4000)
    public void testSetColumnKeyToNull() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, TOOLTIP_TEXT, URL_TEXT, dataset, 0, 0);

        entity.setColumnKey(null);
        assertNull(entity.getColumnKey());
    }

    @Test(timeout = 4000)
    public void testGetColumnKey() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        Clock clock = MockClock.systemUTC();
        JapaneseDate japaneseDate = MockJapaneseDate.now(clock);
        CategoryItemEntity<Integer, ChronoLocalDate> entity = new CategoryItemEntity<>(caret, TOOLTIP_TEXT, "", dataset, 0, japaneseDate);

        assertSame(japaneseDate, entity.getColumnKey());
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testSetDatasetToNullThrowsException() {
        Polygon polygon = new Polygon();
        Rectangle rectangle = polygon.getBounds();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(rectangle, TOOLTIP_TEXT, URL_TEXT, dataset, 0, 0);

        entity.setDataset(null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testCloneAndEqualsThrowsException() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, "lK", "lK", dataset, null, null);

        Object clonedEntity = entity.clone();
        entity.equals(clonedEntity);
    }

    @Test(timeout = 4000, expected = IllegalArgumentException.class)
    public void testConstructorWithNullDatasetThrowsException() {
        DefaultCaret caret = new DefaultCaret();
        new CategoryItemEntity<>(caret, "r\"s!dma&B8-JXkb$0-", "r\"s!dma&B8-JXkb$0-", null, 0, 0);
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentColumnKey() {
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        DefaultStatisticalCategoryDataset<Integer, Integer> dataset = new DefaultStatisticalCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(rectangle, "'2zliUz|", "'2zliUz|", dataset, 0, 0);
        Integer columnKey = JLayeredPane.POPUP_LAYER;
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(rectangle, "t.s&<g6o`/(5QNh", "'2zliUz|", dataset, 0, columnKey);

        Object clonedEntity = entity1.clone();
        assertFalse(entity2.equals(clonedEntity));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentRowKey() {
        Polygon polygon = new Polygon();
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(caret, TOOLTIP_TEXT, URL_TEXT, dataset, 0, 0);
        Integer rowKey = JLayeredPane.POPUP_LAYER;
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(polygon, TOOLTIP_TEXT, "", dataset, rowKey, 0);

        assertFalse(entity1.equals(entity2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObject() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, "", "", dataset, 0, 0);

        assertFalse(entity.equals(""));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameObject() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, TOOLTIP_TEXT, URL_TEXT, dataset, 0, 0);

        assertTrue(entity.equals(entity));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameAttributes() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(caret, null, null, dataset, 0, 0);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(caret, null, null, dataset, caret.height, 0);

        assertTrue(entity1.equals(entity2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentDatasets() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset1 = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity1 = new CategoryItemEntity<>(caret, null, null, dataset1, 0, 0);
        DefaultBoxAndWhiskerCategoryDataset<Integer, Integer> dataset2 = new DefaultBoxAndWhiskerCategoryDataset<>();
        entity1.setDataset(dataset2);
        CategoryItemEntity<Integer, Integer> entity2 = new CategoryItemEntity<>(caret, null, null, dataset1, caret.y, 0);

        assertFalse(entity1.equals(entity2));
    }

    @Test(timeout = 4000)
    public void testGetDataset() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, "A!=WtVt`q", "A!=WtVt`q", dataset, 0, 0);

        assertNotNull(entity.getDataset());
    }

    @Test(timeout = 4000)
    public void testToString() {
        DefaultCaret caret = new DefaultCaret();
        DefaultMultiValueCategoryDataset<Integer, Integer> dataset = new DefaultMultiValueCategoryDataset<>();
        CategoryItemEntity<Integer, Integer> entity = new CategoryItemEntity<>(caret, "", "", dataset, 0, 0);

        assertNotNull(entity.toString());
    }
}