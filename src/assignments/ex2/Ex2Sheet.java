package assignments.ex2;
import java.io.IOException;
import java.util.ArrayList;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private SCell[][] table;
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(" ",x,y);
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
        SCell c = get(x,y);
        if(c!=null) {ans = c.toString();}
        switch(c.getType()) {
            case 1, 2:
                ans = c.toString();
                break;
            case 3:
                ans = eval(x,y);
                break;
            case -2:
                ans = Ex2Utils.ERR_FORM;
                break;
            case -1:
                ans = Ex2Utils.ERR_CYCLE;
        }
        return ans;
    }

    @Override
    public SCell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public SCell get(String cords) {
        SCell ans = null;
        CellEntry newCords = new CellEntry(cords);
        int x = newCords.getX();
        int y = newCords.getY();
        if (x<=width()&&y<=height())
        {
            if(x!=-1 && y!=-1) {
                ans = get(x,y);
            }
        }
        else
        {
            throw new IndexOutOfBoundsException();
        }
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
        for(int i=0;i<width();i=i+1) {
            for(int j=0;j<height();j=j+1) {
                SCell c = get(i,j);
                if(c!=null&&dd[i][j]!=-1) {
                    String res = eval(i,j);
                    c.setValue(res);
                } else if (dd[i][j] == -1){
                    c.setType(Ex2Utils.ERR_CYCLE_FORM);
                }
            }
        }
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
        int change=-1;
        {
            for(int i=0;i<width();i++) {
                for(int j=0;j<height();j++) {
                    ans [i][j] = -1;
                }
            }
            for(int i=0;i<width();i++) {
                for(int j=0;j<height();j++) {
                    try {
                        ans[i][j] = computeDepth(get(i,j));
                        get(i,j).setOrder(ans[i][j]);
                    }catch(Exception e) {
                        ans[i][j] = -1;
                    }
                }
            }
        }
        return ans;
    }

    public int computeDepth(SCell c)
    {
        int depth = 0;
        if(c!=null) {
            if(SCell.getCellName(c.toString()).length==0) {
                depth = 0;
            }
            else {
                String[] temp = SCell.getCellName(c.toString());
                int lastMax = 0;
                for(int i=0;i<temp.length;i++) {
                    if (!cycled(get(temp[i]))) {
                        int tempDepth = computeDepth(get(temp[i]));
                        lastMax = Math.max(lastMax, tempDepth);

                    }
                    else
                    {
                       throw new RuntimeException("cycled");
                    }
                }
                depth = 1+lastMax;
            }
        }
        return depth;
    }

    private boolean cycled(SCell c)
    {
        ArrayList<SCell> cycle = new ArrayList<SCell>();
        String[] temp = SCell.getCellName(c.toString());
        boolean ans=false;
        if (cycle.contains(c)) {
            ans=true;
        }
        for(int i=0;i<temp.length;i++) {
            if (cycled(get(temp[i])))
            {
                ans=true;
                break;
            }
        }
        cycle.add(c);
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
