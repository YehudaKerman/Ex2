package assignments.ex2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
class Ex2SheetTest {

    @Test
    void value() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=1+2*A1+B0");
        ex2Sheet.set(0,1,"=1");
        ex2Sheet.set(1,0,"=1");
        ex2Sheet.set(1,1,"=A0");
        assertEquals("4.0",ex2Sheet.value(0,0));
        assertEquals("1.0",ex2Sheet.value(0,1));
        assertEquals("1.0",ex2Sheet.value(1,0));
        assertEquals("4.0",ex2Sheet.value(1,1));
    }

    @Test
    void getTest() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=1+2*A1+B0");
        ex2Sheet.set(0,1,"=1");
        ex2Sheet.set(1,0,"=1");
        ex2Sheet.set(1,1,"=A0");
        String s = ex2Sheet.get(1,1).getData();
        String a = ex2Sheet.get("a0").getData();
        String expS = "=A0";
        String expA = "=1+2*A1+B0";
        assertEquals(expS,s);
        assertEquals(expA,a);
    }

    @Test
    void width() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        int x = ex2Sheet.width();
        assertEquals(2,x);
    }

    @Test
    void height() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        int y = ex2Sheet.height();
        assertEquals(2,y);
    }

    @Test
    void set() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=1+2*A1+B0");
        ex2Sheet.set(0,1,"=1");
        ex2Sheet.set(1,0,"=1");
        ex2Sheet.set(1,1,"=A0");
        String s = ex2Sheet.get(1,1).getData();
        String a = ex2Sheet.get("a0").getData();
        String expS = "=A0";
        String expA = "=1+2*A1+B0";
        assertEquals(expS,s);
        assertEquals(expA,a);
    }
    @Test
    void isIn() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        try{
            ex2Sheet.isIn(2,2);
        }
        catch(Exception e){
            boolean b = true;
            assertTrue(b);
        }
        assertTrue(ex2Sheet.isIn(0,0));
    }

    @Timeout(1)
    @Test
    void testEval() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=1");
        ex2Sheet.set(0,1,"=1");
        ex2Sheet.set(1,0,"=1");
        ex2Sheet.set(1,1,"=A0");
        ex2Sheet.set(0,0,"=b1");
        String exp = Ex2Utils.ERR_CYCLE;
        String res = ex2Sheet.eval(0,0);
        assertEquals(exp,res);
        ex2Sheet.set(0,0,"=1");
        ex2Sheet.eval();
        String exp2 = "1.0";
        String res2 = ex2Sheet.value(1,1);
        assertEquals(exp2,res2);
    }

    @Test
    void computeFormulaTest() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(1,1);
        String exp = "18.6";
        ex2Sheet.set(0,0,"=((1+2)*6.2)");
        String res =ex2Sheet.eval(0,0);
            assertEquals(exp,res);
        }
    @Test
    void computeDepthTest()
    {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=((1+2)*6.2)");
        ex2Sheet.set(0,1,"=A0");
        ex2Sheet.set(1,0,"=A1+A0");
        ex2Sheet.set(1,1,"=B0");
        int x = ex2Sheet.computeDepth(ex2Sheet.get(1,0),new HashSet<SCell>(),new HashSet<SCell>());
        int exp = 2;
        assertEquals(exp,x);
    }
    @Test
    void depthTest()
    {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=((1+2)*6.2)");
        ex2Sheet.set(0,1,"=A0");
        ex2Sheet.set(1,0,"=A1+A0");
        ex2Sheet.set(1,1,"=A0");
        int[][] exp ={{0,1},{2,1}};
        int[][] res =ex2Sheet.depth();
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                assertEquals(exp[i][j],res[i][j]);
            }
        }
    }


    @Test
    void testDepth() {
        Ex2Sheet ex2Sheet = new Ex2Sheet(2,2);
        ex2Sheet.set(0,0,"=((1+2)*6.2)");
        ex2Sheet.set(0,1,"=b0");
        ex2Sheet.set(1,0,"=A1+A0");
        ex2Sheet.set(1,1,"=A0");
        int[][] exp ={{0,-1},{-1,1}};
        int[][] res =ex2Sheet.depth();
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[i].length; j++) {
                assertEquals(exp[i][j],res[i][j]);
            }
        }
    }
}