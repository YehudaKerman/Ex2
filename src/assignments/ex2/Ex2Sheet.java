package assignments.ex2;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Ex2Sheet implements the Sheet interface, representing a simple spreadsheet
 * with a 2D array of SCell objects. It provides methods to get and set cell values,
 * evaluate expressions, compute dependency depths, and save/load the sheet to/from a file.
 * This class supports handling numeric values, text, and formulas, including cyclic references.
 */
public class Ex2Sheet implements Sheet {
    private SCell[][] table;

    /**
     * Initializes an Ex2Sheet instance with the specified width (x) and height (y).
     * Fills the sheet with empty SCell objects and evaluates the sheet.
     * @param x the width (number of columns) of the sheet.
     * @param y the height (number of rows) of the sheet.
     */
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell(" ");
            }
        }
        eval();
    }
    /**
     * Initializes an Ex2Sheet instance with default dimensions defined by Ex2Utils.WIDTH and Ex2Utils.HEIGHT.
     */
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    /**
     * Returns the string value that will represen in the GUI of the cell located at coordinates (x, y).
     * Throws IndexOutOfBoundsException if the coordinates are out of bounds.
     *
     * @param x the x-coordinate of the cell.
     * @param y the y-coordinate of the cell.
     * @return the string value of the cell.
     */
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

    /**
     * Returns the SCell object located at coordinates (x, y).
     * Throws IndexOutOfBoundsException if the coordinates are out of bounds.
     *
     * @param x the x-coordinate of the cell.
     * @param y the y-coordinate of the cell.
     * @return the SCell object at the specified coordinates.
     */
    @Override
    public SCell get(int x, int y) {
        if(!isIn(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        else{
            return table[x][y];
        }
    }

    /**
     * Returns the SCell object located at the specified coordinates (cords), given in a string format.
     * Throws IndexOutOfBoundsException if the coordinates are out of bounds.
     *
     * @param cords the string representation of the cell coordinates.
     * @return the SCell object at the specified coordinates.
     */
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

    /**
     * Returns the width of the sheet (number of columns).
     *
     * @return the width of the sheet.
     */

    @Override
    public int width() {
        return table.length;
    }

    /**
     * Returns the height of the sheet (number of rows).
     *
     * @return the height of the sheet.
     */
    @Override
    public int height() {
        return table[0].length;
    }

    /**
     * Sets the value of the cell located at coordinates (x, y) to the given string (s).
     * Throws IndexOutOfBoundsException if the coordinates are out of bounds.
     * @param x the x-coordinate of the cell.
     * @param y the y-coordinate of the cell.
     * @param s the string representation of the cell value.
     */
    @Override
    public void set(int x, int y, String s) {
        if (!isIn(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        else {
            SCell c = new SCell(s);
            table[x][y] = c;
        }
    }

    /**
     * Evaluates the entire sheet, computing values and types for each cell based on their formulas and references.
     */
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

    /**
     * Checks whether the coordinates (xx, yy) are within the bounds of the sheet.
     *
     * @param xx the x-coordinate to check.
     * @param yy the y-coordinate to check.
     * @return true if the coordinates are within bounds, false otherwise.
     */
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

    /**
     * Computes and returns a 2D array representing the depth of each cell in the sheet.
     * The depth indicates the level of dependency among cells.
     *
     * @return a 2D array of integers representing the depth of each cell.
     */
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
                    String[] lastCheck = SCell.getCellName(get(i,j).getData());
                    if(lastCheck.length==0)
                    {
                        ans[i][j]=0;
                        get(i,j).setOrder(0);
                    }else {
                        ans[i][j] = computeDepth(get(i, j), new HashSet<SCell>(), new HashSet<SCell>());
                        get(i, j).setOrder(ans[i][j]);
                        }
                    }
                }
            }
                return ans;
    }

    /**
     * A helper function for depth() that Recursively computes the depth of a given cell (c),
     * while checking for cyclic references.
     *
     * @param c the SCell to compute the depth for.
     * @param checked a set of cells that have been checked.
     * @param inProcess a set of cells that are currently being processed.
     * @return the depth of the cell or Ex2Utils.ERR_CYCLE_FORM if a cycle is detected.
     */
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
        if(references.length==0) {
            return 0;
        }
        else {return 1 + lastMax;}
    }

    /**
     * Loads the sheet from a file specified by fileName. Throws IOException if an error occurs during file reading.
     * @param fileName a String representing the full (an absolute or relative path to the loaded file).
     * @throws IOException
     */
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

    /**
     * Saves the spreadsheet to a file.
     * @param fileName a String representing the full (an absolute or relative path tp the saved file).
     * @throws IOException
     */
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

    /**
     * Evaluates the cell at the given coordinates.
     * @param x - integer, x-coordinate of the cell.
     * @param y - integer, y-coordinate of the cell.
     * @return the evaluated value of the cell as a String.
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
     */
    @Override
    public String eval(int x, int y)
    {
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
                    try
                    {
                        double comp = computeFormula(get(x,y).getData());
                        ans = Double.toString(comp);
                        return ans;
                    }
                    catch (Exception e) {
                        ans = get(x,y).toString();
                    }
                }
            }
            else
            {
                ans = get(x,y).toString();
            }
        } else if (SCell.isForm(ans))
            {
                try {
                    double comp = computeFormula(ans);
                    ans = Double.toString(comp);
                }
                catch (Exception e)
                {
                    ans = Ex2Utils.ERR_FORM;
                }
            }
    /////////////////////
        return ans;
    }

    /**
     * Computes the result of a formula.
     * This method processes the formula by removing the '=' sign,
     * validating the formula, and recursively computing the result.
     * It handles numbers, cell references, and arithmetic operations.
     *
     * @param formula - String, the formula to compute.
     * @return the computed result as a double.
     */

    public double computeFormula(String formula) throws Exception {
        double compans = 0;
        if(formula.equals(" ")) {
            throw new Exception();
        }
        if(formula.charAt(0)=='=') {formula = formula.substring(1);}
        if (SCell.isValidForm(formula)) {
                if (formula.isEmpty() || formula == null||formula.equals(" ")) {
                    throw new Exception();
            } else {
                if (SCell.isNumber(formula)) {
                    compans = Double.parseDouble(formula);
                } else if (SCell.isCell(formula)) {
                    if (formula.charAt(0) == '-') {
                        int depth = computeDepth(get(formula.substring(1)), new HashSet<SCell>(), new HashSet<SCell>());
                        if (depth == -1) {
                            return Ex2Utils.ERR_CYCLE_FORM;
                        } else if (depth != -1) {
                            compans = -1 * (computeFormula(get(formula.substring(1)).toString()));
                        }
                    }
                        else
                            {
                                int depth = computeDepth(get(formula), new HashSet<SCell>(), new HashSet<SCell>());
                                if (depth == -1) {
                                    return Ex2Utils.ERR_CYCLE_FORM;
                                }
                                else if (depth != -1)
                                    {
                                        compans = (computeFormula(get(formula).toString()));
                                    }
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

    /**
     * Identifies the last operation in the formula.
     * This method finds the position of the last operator in the formula and
     * returns its index and type (1: +, 2: -, 3: *, 4: /).
     *
     * @param formula - String, the formula to analyze.
     * @return an array where the first element is the index of the last operation, and the second element is the type of operation.
     */
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
