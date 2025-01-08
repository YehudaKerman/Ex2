package assignments.ex2;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    void getOrder() {
    }

    @Test
    void testToString() {
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
    }

    @Test
    void getType() {
    }

    @Test
    void setType() {
    }

    @Test
    void setOrder() {
    }

    @Test
    void getName() {
    }

    @Test
    void setName() {
    }

    @Test
    void parentheses() {
    }
}
