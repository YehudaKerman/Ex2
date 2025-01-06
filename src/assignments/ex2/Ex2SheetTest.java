package assignments.ex2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class Ex2SheetTest {

    @Test
    void value() {
    }

    @Test
    void get() {
    }

    @Test
    void testGet() {
    }

    @Test
    void width() {
    }

    @Test
    void height() {
    }

    @Test
    void set() {
    }

    @Test
    void eval() {
    }

    @Test
    void isIn() {
    }

    @Test
    void depth() {
    }

    @Test
    void load() {
    }

    @Test
    void save() {
    }

    @Test
    void testEval() {
    }

    @Test
    void computeFormulaTest() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(1,1);
        String exp = "3.0";
        ex2Sheet.set(0,0,"=1+2");
        String res =ex2Sheet.eval(0,0);
            assertEquals(exp,res);
        }
}