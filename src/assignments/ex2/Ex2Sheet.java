package assignments.ex2;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private SCell[][] table;
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(" ");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        if(!isIn(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        String ans = Ex2Utils.EMPTY_CELL;
            SCell c = get(x, y);
            if (c != null) {
                ans = c.toString();
            }
            switch (c.getType()) {
                case 1, 2:
                    ans = c.toString();
                    break;
                case 3:
                    ans = eval(x, y);
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
        if(!isIn(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        else{
            return table[x][y];
        }
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
        if (!isIn(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        else {
            SCell c = new SCell(s);
            table[x][y] = c;
            eval();
        }
    }

    @Override
    public void eval() {
        int[][] dd = depth();
        for (int i = 0; i < width(); i = i + 1) {
            for (int j = 0; j < height(); j = j + 1) {
                SCell c = get(i, j);
                if (c != null && dd[i][j] != -1) {
                    String res = eval(i, j);
                    c.setValue(res);
                    if (SCell.isNumber(c.getData()))
                    {
                        c.setType(2);
                    } else if (SCell.isText(c.getData())) {
                        c.setType(1);
                    } else if (SCell.isForm(c.getData())) {
                        c.setType(3);
                    }
                } else if (dd[i][j] == -1) {
                    int newDepth = computeDepth(get(i, j), new HashSet<SCell>(), new HashSet<SCell>());
                    if (newDepth == -1) {
                        c.setType(Ex2Utils.ERR_CYCLE_FORM);
                        c.setValue(Ex2Utils.ERR_CYCLE);
                    } else {
                        c.setValue(eval(i, j));
                        if (SCell.isForm(c.getData())) {
                            c.setType(Ex2Utils.FORM);
                        } else if (SCell.isNumber(c.getData())) {
                            c.setType(Ex2Utils.TEXT);
                        } else if (SCell.isNumber(c.getData())) {
                            c.setType(Ex2Utils.NUMBER);
                        }
                    }
                } else if (c == null || eval(i, j) == Ex2Utils.ERR_CYCLE) {
                    c.setType(Ex2Utils.ERR_FORM_FORMAT);
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        if(xx>=width()||yy>=height()||xx<0||yy<0) {
            ans = false;
        }
        else {
            ans = true;
        }
        /////////////////////
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        {
            for(int i=0;i<width();i++) {
                for(int j=0;j<height();j++) {
                    ans [i][j] = -1;
                }
            }
            for(int i=0;i<width();i++) {
                for(int j=0;j<height();j++) {
                        ans[i][j] = computeDepth(get(i, j),new HashSet<SCell>(),new HashSet<SCell>());
                        get(i,j).setOrder(ans[i][j]);
                    }
                }
            }
                return ans;
    }

    public int computeDepth(SCell c, Set<SCell> checked, Set<SCell> inProcess) {
        if (c == null) {
            return 0;
        }

        if (inProcess.contains(c)) {
            c.setType(Ex2Utils.ERR_CYCLE_FORM);
            return Ex2Utils.ERR_CYCLE_FORM;
        }

        if (checked.contains(c)) {
            return 0;
        }

        inProcess.add(c);
        String[] references = SCell.getCellName(c.getData());
        int lastMax = 0;

        for (int i = 0; i < references.length; i++) {
            SCell referencedCell = get(references[i]);
            int tempDepth = computeDepth(referencedCell, checked, inProcess);
            if (tempDepth == Ex2Utils.ERR_CYCLE_FORM) {
                c.setType(Ex2Utils.ERR_CYCLE_FORM);
                inProcess.remove(c);
                return Ex2Utils.ERR_CYCLE_FORM;
            }
            lastMax = Math.max(lastMax, tempDepth);
        }

        inProcess.remove(c);
        checked.add(c);
        return 1 + lastMax;
    }

    @Override
    public void load(String fileName) throws IOException {
        try(BufferedReader loadedFile = new BufferedReader(new FileReader(fileName))) {
            String line = loadedFile.readLine();
            while ((line = loadedFile.readLine())!=null) {
                String[] splitLine = line.split(",",3);
                int x = Integer.parseInt(splitLine[0]);
                int y = Integer.parseInt(splitLine[1]);
                table[x][y] = new SCell(splitLine[2]);
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String fileName) throws IOException {
        try(BufferedWriter saveFile = new BufferedWriter(new FileWriter(fileName))){
            saveFile.write("I2CS ArielU: SpreadSheet (Ex2) assignment - my solution, the file name is: "+fileName+"\n");
            for(int i=0;i<width();i++) {
                for(int j=0;j<height();j++) {
                    if(!(get(i,j).getData().equals(" "))) {
                        saveFile.write(String.format("%d,%d,%s\n",i,j,get(i,j).getData()));
                    }

                }
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String eval(int x, int y) {
        if(!isIn(x, y)) {throw new IndexOutOfBoundsException(); }
        String ans = null;
        if (get(x,y)!=null) {
            ans = get(x,y).getData();
        }
        if (SCell.isNumber(ans)) {
            return ans;
        } else if (SCell.isText(ans)) {
            if(get(x,y).getType()==Ex2Utils.ERR_CYCLE_FORM) {
                int depth =computeDepth(get(x,y),new HashSet<SCell>(), new HashSet<SCell>());
                if (depth==Ex2Utils.ERR_CYCLE_FORM) {
                    get(x, y).setType(Ex2Utils.ERR_CYCLE_FORM);
                    get(x, y).setValue(Ex2Utils.ERR_CYCLE);
                    return Ex2Utils.ERR_CYCLE;
                }
                    else if (depth == Ex2Utils.ERR_FORM_FORMAT) {
                    get(x, y).setType(Ex2Utils.ERR_FORM_FORMAT);
                    get(x, y).setValue(Ex2Utils.ERR_FORM);
                    return Ex2Utils.ERR_FORM;
                }
                    else if (depth >=0)
                {
                    double comp = computeFormula(get(x,y).getData());
                    if (comp==Ex2Utils.ERR_CYCLE_FORM) {
                        get(x,y).setValue(Ex2Utils.ERR_CYCLE);
                        get(x,y).setType(Ex2Utils.ERR_CYCLE_FORM);
                        ans = Ex2Utils.ERR_CYCLE;
                        return ans;
                    }
                }
            }
            else
            {
                ans = get(x,y).toString();
            }
        } else if (SCell.isForm(ans))
            {
                double comp = computeFormula(ans);
                if(comp==-1) {
                    ans = Ex2Utils.ERR_CYCLE;
                    get(x, y).setType(Ex2Utils.ERR_CYCLE_FORM);
                } else if (comp==-2) {
                    get(x,y).setType(Ex2Utils.ERR_FORM_FORMAT);
                    ans=Ex2Utils.ERR_FORM;
                }
                else {
                    ans = Double.toString(comp);
                }
            }
    /////////////////////
        return ans;
    }

    public double computeFormula(String formula) {
        double compans = 0;
        if(formula.equals(" ")) {
            compans = Ex2Utils.ERR_FORM_FORMAT;
        }
        if(formula.charAt(0)=='=') {formula = formula.substring(1);}
        if (SCell.isValidForm(formula)) {
                if (formula.isEmpty() || formula == null||formula.equals(" ")) {
                    return Ex2Utils.ERR_FORM_FORMAT;
            } else {
                if (SCell.isNumber(formula)) {
                    compans = Double.parseDouble(formula);
                } else if (SCell.isCell(formula)) {
                    int depth =computeDepth(get(formula), new HashSet<SCell>(), new HashSet<SCell>());
                    if ( depth == -1) {
                        return Ex2Utils.ERR_CYCLE_FORM;
                    } else if (depth != -1) {
                        compans = (computeFormula(get(formula).toString()));
                    }
                }
                else
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
