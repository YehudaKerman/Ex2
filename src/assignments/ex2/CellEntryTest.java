package assignments.ex2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellEntryTest {
    @Test
    public void testValidCoordinates() {
        CellEntry cell = new CellEntry("A1");
        assertTrue(cell.isValid());
        assertEquals(0, cell.getX()); // 'A' corresponds to 0
        assertEquals(1, cell.getY());

        cell = new CellEntry("Z98");
        assertTrue(cell.isValid());
        assertEquals(25, cell.getX()); // 'Z' corresponds to 25
        assertEquals(98, cell.getY());
    }

    @Test
    public void testInvalidCoordinates() {
        CellEntry cell = new CellEntry("A100"); // Y-coordinate out of range
        assertFalse(cell.isValid());
        assertEquals(Ex2Utils.ERR, cell.getY());

        cell = new CellEntry("AA1"); // X-coordinate invalid (AA)
        assertFalse(cell.isValid());
        assertEquals(Ex2Utils.ERR, cell.getX());

        cell = new CellEntry("1A"); // Incorrect format
        assertFalse(cell.isValid());
        assertEquals(Ex2Utils.ERR, cell.getX());
        assertEquals(Ex2Utils.ERR, cell.getY());
    }

    @Test
    public void testShortCoordinates() {
        CellEntry cell = new CellEntry("A"); // Too short
        assertFalse(cell.isValid());
        assertEquals(Ex2Utils.ERR, cell.getX());
        assertEquals(Ex2Utils.ERR, cell.getY());
    }

    @Test
    public void testLowercaseCoordinates() {
        CellEntry cell = new CellEntry("a1"); // Lowercase input
        assertTrue(cell.isValid());
        assertEquals(0, cell.getX()); // 'a' corresponds to 0 (case insensitive)
        assertEquals(1, cell.getY());
    }

    @Test
    public void testMixedCaseCoordinates() {
        CellEntry cell = new CellEntry("b2");
        assertTrue(cell.isValid());
        assertEquals(1, cell.getX()); // 'b' corresponds to 1 (case insensitive)
        assertEquals(2, cell.getY());
    }
}
