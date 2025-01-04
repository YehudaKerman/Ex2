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
    public void isFormTest (){
        String [] goodForm = {"1+2*(3+6)","((2+60+3)-(2-5)*6)","(1+2)*(3+6)"};
        for (int i = 0; i < goodForm.length; i = i + 1) {
            boolean ok = SCell.isForm(goodForm[i]);
            assertTrue(ok);
        }
        String [] badForm = {"1a","sasdgf","dfas","A2","=1++2.5","=((9+4)"};
    }
}
