package assignments.ex2;
import java.io.IOException;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private SCell[][] table;
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell("-1");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        // Add your code here

        SCell c = get(x,y);
        if(c!=null) {ans = c.toString();}

        /////////////////////
        return ans;
    }

    @Override
    public SCell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        SCell c = new SCell(s);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }
    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here

        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        if (get(x,y)!=null) {
            ans = get(x,y).toString();
        }
        if (SCell.isText(ans)||SCell.isNumber(ans)) {
            return ans;
        }
        else
        {
            double comp = computeFormula(ans);
            ans = Double.toString(comp);
        }
    /////////////////////
        return ans;
    }

        public double computeFormula(String formula) {
        double compans = 0;
        if(formula.charAt(0)=='=') {formula = formula.substring(1);}
        if (SCell.isValidForm(formula)) {
                if (formula.isEmpty() || formula == null) {
                    throw new IllegalArgumentException("formula is empty");
            } else {
                if (SCell.isNumber(formula)) {
                    compans = Double.parseDouble(formula);
                } else if (SCell.isCell(formula)) {
                   compans = (computeFormula(get(formula).toString()));
                } else
                {
                    if (formula.charAt(0) == '(' && formula.charAt(formula.length() - 1) == ')')
                    {
                        int closeIndex = SCell.parentheses(formula);
                        if (closeIndex == -1 || closeIndex == formula.length() - 1)
                        {
                            closeIndex = formula.length() - 1;
                            compans = computeFormula(formula.substring(1, closeIndex));
                        }
                        else if (formula.charAt(0) == '(' && formula.charAt(formula.length() - 1) == ')')
                        {
                            compans = computeFormula(formula.substring(1, closeIndex)) + computeFormula(formula.substring(closeIndex + 2, formula.length()));
                        }
                        else
                        {
                            compans = computeFormula(formula.substring(1, closeIndex)) + computeFormula(formula.substring(closeIndex + 2, formula.length() - 1));
                        }
                    }
                    else {
                        int i = (lastOp(formula)[0]);
                        int j = (lastOp(formula)[1]);
                        if (i==formula.length()-2){
                            if(j==1){compans = computeFormula(formula.substring(0, i)) + computeFormula(formula.substring(formula.length()-1));}
                            else if (j==2) {compans = computeFormula(formula.substring(0, i)) - computeFormula(formula.substring(formula.length()-1));}
                            else if (j==3) {compans = computeFormula(formula.substring(0, i)) * computeFormula(formula.substring(formula.length()-1));}
                            else if (j==4) {compans = computeFormula(formula.substring(0, i)) / computeFormula(formula.substring(formula.length()-1));}
                        } else {
                            if(j==1){compans = computeFormula(formula.substring(0, i)) + computeFormula(formula.substring(i+1,formula.length()));}
                            else if (j==2) {compans = computeFormula(formula.substring(0, i)) - computeFormula(formula.substring(i+1,formula.length()));}
                            else if (j==3) {compans = computeFormula(formula.substring(0, i)) * computeFormula(formula.substring(i+1,formula.length()));}
                            else if (j==4) {compans = computeFormula(formula.substring(0, i)) / computeFormula(formula.substring(i+1,formula.length()));}
                        }
                    }
                }
            }
        }
        return compans;
    }
    private int[] lastOp(String formula) {
        int[] ans = new int[2];
        ans[0] = SCell.lastOp(formula);
        if(formula.charAt(ans[0])=='+')
        {
            ans[1] = 1;
        } else if (formula.charAt(ans[0])=='-') {
            ans[1] = 2;
        } else if (formula.charAt(ans[0])=='*') {
            ans[1] = 3;
        } else if (formula.charAt(ans[0])=='/') {
            ans[1] = 4;
        }
        return ans;
    }

}
