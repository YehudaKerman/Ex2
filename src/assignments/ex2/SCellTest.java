package assignments.ex2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SCellTest {
    @Test
    public void lastOpTest() {
        String [] s = {"1+2*(3+6)","((2+60+3)-(2-5)*6)","(1+2)*(3+6)"};
        int [] exp ={1,9,5};
        for (int i = 0; i < s.length; i++)
        {
            assertEquals(exp[i], SCell.lastOp(s[i]));
        }

    }
    @Test
    public void isNumberTest() {
        String [] goodNumbers = {"1","2.5","-3.5","0.25"};
        for (int i = 0; i < goodNumbers.length; i = i + 1) {
            boolean ok = SCell.isNumber(goodNumbers[i]);
            assertTrue(ok);
        }
        String [] badNumbers = {"1a","sasdgf","dfas","A2"};
        for (int i = 0; i < badNumbers.length; i = i + 1) {
            boolean ok = SCell.isNumber(badNumbers[i]);
            assertFalse(ok);
        }
    }
    @Test
    public void isTextTest() {
        String [] badText = {"1","2.5","=-3.5","=1+0.25"};
        for (int i = 0; i < badText.length; i = i + 1) {
            boolean ok = SCell.isText(badText[i]);
            assertFalse(ok);
        }
        String [] goodText = {"1a","sasdgf","dfas","A2"};
        for (int i = 0; i < goodText.length; i = i + 1) {
            boolean ok = SCell.isText(goodText[i]);
            assertTrue(ok);
        }
    }
    @Test
    public void isValidFormTest(){
        String [] goodForm = {"1+2*(3+6)","((2+60+3)-(2-5)*6)","(1+2)*(3+6)","A2"};
        for (int i = 0; i < goodForm.length; i = i + 1) {
            boolean ok = SCell.isValidForm(goodForm[i]);
            assertTrue(ok);
        }
        String [] badForm = {"1a","sasdgf","dfas","=1++2.5","=((9+4)","=asds1+2"};
        for (int i = 0; i < badForm.length; i = i + 1) {
            boolean ok = SCell.isValidForm(badForm[i]);
            assertFalse(ok);
        }
    }
    @Test
    public void isFormTest() {
        String[] goodForm = {"=1+2*(3+6)", "=((2+60+3)-(2-5)*6)", "=(1+2)*(3+6)","=A2"};
        for (int i = 0; i < goodForm.length; i = i + 1) {
            boolean ok = SCell.isForm(goodForm[i]);
            assertTrue(ok);
        }
        String[] badForm = {"1a", "sasdgf", "dfas", "A2", "=1++2.5", "=((9+4)","=asds1+2","=A100","=@56"};
        for (int i = 0; i < badForm.length; i = i + 1) {
            boolean ok = SCell.isForm(badForm[i]);
            assertFalse(ok);

        }
    }
    @Test
    public void isCellTest() {
        String[] goodCell = {"=a3", "=B6", "=(G16)","=y2","A5"};
        for (int i = 0; i < goodCell.length; i = i + 1) {
            boolean ok = SCell.isCell(goodCell[i]);
            assertTrue(ok);
        }
        String[] badCell = {"1a", "k566", "R989", "(A2","&66"};
        for (int i = 0; i < badCell.length; i = i + 1) {
            boolean ok = SCell.isCell(badCell[i]);
            assertFalse(ok);

        }
    }
    @Test
    public void getCellNameTest() {
        String s = "=A2+36+b98+r65+85*2/30";
        String [] expected = {"A2","b98","r65"};
        String [] actual = SCell.getCellName(s);
        for (int i = 0; i < expected.length; i = i + 1) {
            assertEquals(expected[i], actual[i]);
        }
    }

    @Test
    void testToString() {
        SCell textCell = new SCell("test");
        assertEquals("test", textCell.toString());

        SCell numberCell = new SCell("123");
        assertEquals("123.0", numberCell.toString());

        SCell formulaCell = new SCell("=A1+B2");
        assertEquals("=A1+B2", formulaCell.toString());

        SCell errorCell = new SCell("=A1+");
        assertEquals("ERR_FORM!", errorCell.toString());
    }

    @Test
    void setData() {
        String[] data = {"1","=3+6","adfs","=A2","=+"};
        SCell[] test = new SCell[5];
        int[] res = {2,3,1,3,-2};
        for (int i = 0; i < test.length; i++) {
            test[i] = new SCell(data[i]);
            int actual = test[i].getType();
            assertEquals(res[i], actual);
        }
    }

    @Test
    void getData() {
        SCell cell = new SCell("test");
        assertEquals("test", cell.getData());

        SCell numberCell = new SCell("123");
        assertEquals("123", numberCell.getData());

        SCell formulaCell = new SCell("=A1+B2");
        assertEquals("=A1+B2", formulaCell.getData());
    }

    @Test
    void getType() {
        SCell textCell = new SCell("test");
        assertEquals(1, textCell.getType());

        SCell numberCell = new SCell("1");
        assertEquals(2, numberCell.getType());

        SCell formulaCell = new SCell("=A1+B2");
        assertEquals(3, formulaCell.getType());
    }

    @Test
    void setType() {
        SCell cell = new SCell("1");
        cell.setType(3);
        assertEquals(3, cell.getType());

        cell.setType(1);
        assertEquals(1, cell.getType());
    }

    @Test
    void setOrder() {
        SCell cell = new SCell("1");
        cell.setOrder(1);
        assertEquals(1, cell.getOrder());

        cell.setOrder(2);
        assertEquals(2, cell.getOrder());
    }

    @Test
    void parentheses() {
        assertEquals(6, SCell.parentheses("(A1+B2)"));
        assertEquals(-1, SCell.parentheses("(A1+B2"));
    }

    @Test
    public void testSetValue() {
        SCell cell = new SCell("=3+2");
        Ex2Sheet sheet = new Ex2Sheet(1,1);
        sheet.set(0,0,cell.getData());
        // Assuming there's a way to get the static value, otherwise this test needs to be adjusted
        assertEquals("5.0", sheet.value(0,0));
    }

    @Test
    public void testIsNumber() {
        assertTrue(SCell.isNumber("123"));
        assertFalse(SCell.isNumber("abc"));

        assertTrue(SCell.isNumber("456.78"));
        assertFalse(SCell.isNumber("12a34"));
    }

    @Test
    public void testIsText() {
        assertTrue(SCell.isText("hello"));
        assertFalse(SCell.isText("=A1"));
        assertFalse(SCell.isText("123"));

        assertTrue(SCell.isText("world"));
        assertFalse(SCell.isText("=B2"));
    }

    @Test
    public void testGetCellName() {
        String[] cellNames = SCell.getCellName("=A1+B2");
        assertArrayEquals(new String[]{"A1", "B2"}, cellNames);

        cellNames = SCell.getCellName("=C3*D4");
        assertArrayEquals(new String[]{"C3", "D4"}, cellNames);
    }

    @Test
    public void testIsCell() {
        assertTrue(SCell.isCell("A1"));
        assertFalse(SCell.isCell("1A"));
        assertTrue(SCell.isCell("=A1"));

        assertTrue(SCell.isCell("B2"));
        assertFalse(SCell.isCell("2B"));
    }

    @Test
    public void testIsForm() {
        assertTrue(SCell.isForm("=A1+B2"));
        assertFalse(SCell.isForm("A1+B2"));

        assertTrue(SCell.isForm("=C3*D4"));
        assertFalse(SCell.isForm("C3*D4"));
    }

    @Test
    public void testIsValidForm() {
        assertTrue(SCell.isValidForm("A1+B2"));
        assertFalse(SCell.isValidForm("A1+"));

        assertTrue(SCell.isValidForm("C3*D4"));
        assertFalse(SCell.isValidForm("C3*"));
    }

    @Test
    public void testParentheses() {
        assertEquals(6, SCell.parentheses("(A1+B2)"));
        assertEquals(-1, SCell.parentheses("(A1+B2"));

        assertEquals(6, SCell.parentheses("(C3*D4)"));
        assertEquals(-1, SCell.parentheses("(C3*D4"));
    }

    @Test
    public void testLastOp() {
        assertEquals(2, SCell.lastOp("A1+B2"));
        assertEquals(2, SCell.lastOp("A1*B2"));

        assertEquals(2, SCell.lastOp("C3-D4"));
        assertEquals(2, SCell.lastOp("C3/D4"));
    }
}
