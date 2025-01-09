package assignments.ex2;
// Add your documentation below:

import java.util.ArrayList;

/**
 * This class represents a cell in a spreadsheet.
 * It can contain a number, text, or a formula.
 */
public class SCell implements Cell {
    private String line;
    private int type;
    private int order;
    private static String value;

    /**
     * Constructs an SCell object with the given data.
     * @param data the data to initialize the cell with.
     */
    public SCell(String data) {
        setData(data);
    }

    /**
     * Constructs an SCell object by copying another SCell object.
     * @param cell the SCell object to copy.
     */
    public SCell(SCell cell) {
        this(cell.line);
    }

    /**
     * Sets the value of the cell.
     * @param newValue the new value to set.
     */
    public static void setValue(String newValue) {
        value = newValue;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        if (getType() == 1||getType() == 3) {
            return getData();
        } else if (getType() == 2) {
            return Double.toString(Double.parseDouble(getData()));
        } else if (getType() == -1) {
            return Ex2Utils.ERR_CYCLE;
        }
        else if (getType() == -2) {
            return Ex2Utils.ERR_FORM;
        }
        else
        {
            return "ERROR";
        }
    }

    @Override
    public void setData(String s) {
        if (!s.equals("")&&s!=null) {
            line = s;
            if (isNumber(line)) {
                setType(Ex2Utils.NUMBER);
                setOrder(0);
            } else if ((isText(line))) {
                setType(Ex2Utils.TEXT);
                setOrder(0);
            } else if (isForm(line)) {
                setType(Ex2Utils.FORM);
                setOrder(getOrder());
            }
            else
            {
                setType(Ex2Utils.ERR_FORM_FORMAT);
            }
        }
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        order = t;
    }

    /**
     * Checks if the given string is a valid number.
     * @param s the string to check.
     * @return true if the string is a valid number, false otherwise.
     */
    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the given string is valid text.
     * @param s the string to check.
     * @return true if the string is valid text, false otherwise.
     */
    public static boolean isText(String s) {
        if (s.charAt(0) == '=' || isNumber(s)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Extracts cell names from the given formula string.
     * @param s the formula string.
     * @return an array of cell names.
     */
    public static String[] getCellName(String s) {
        ArrayList <String> ans = new ArrayList<>();
        if (isForm(s)) {
            for (int i=0;i<s.length();i++) {
                if (Character.isLetter(s.charAt(i))) {
                    int lastDigit = 0;
                    for (int j=i+1;j<s.length();j++) {
                        if (Character.isDigit(s.charAt(j))) {
                            lastDigit = j;
                        }
                        else
                        {
                            break;
                        }
                    }
                    if (isCell(s.substring(i,lastDigit+1))) {
                        ans.add(s.substring(i,lastDigit+1));
                    }
                }
            }
        }
        String[] temp = new String[ans.size()];
        ans.toArray(temp);
        return temp;
    }

    /**
     * Checks if the given string is a valid cell reference.
     * @param s the string to check.
     * @return true if the string is a valid cell reference, false otherwise.
     */
    public static boolean isCell(String s) {
        boolean ans = false;
        if (s.charAt(0) == '=') {
            return isCell(s.substring(1));
        } else {
            int a =parentheses(s);
            if (s.charAt(0)=='('&&s.charAt(s.length()-1)==')') {
                if (a == -1 || a == s.length() - 1) {
                    a = s.length() - 1;
                    ans = isCell(s.substring(1, a));
                } else if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
                    ans = isCell(s.substring(1, a)) && isCell(s.substring(a + 2, s.length()));
                } else {
                    ans = isCell(s.substring(1, a)) && isCell(s.substring(a + 2, s.length() - 1));
                }
            }
            else if (s.length() > 3||!Character.isLetter(s.charAt(0))) {
                ans = false;
            } else {
                String sx = s.substring(0, 1);
                String sy = s.substring(1, s.length());
                int x = -1;
                for (int i = 0; i < 26; i++) {
                    if (sx.toUpperCase().equals(Ex2Utils.ABC[i])) {
                        x = i;
                        break;
                    }
                }
                if ((sy.length() <= 2)&&Character.isDigit(sy.charAt(0)) && x >= 0 && x <= 25)
                    ans = true;
            }
            return ans;
        }
    }

    /**
     * Checks if the given string is a valid formula.
     * @param s the string to check.
     * @return true if the string is a valid formula, false otherwise.
     */
    public static boolean isForm (String s){
            if (s.charAt(0) != '=' || s.isEmpty() || s.equals(null) || (s.charAt(0) == '=' && s.length() == 1)) {
                return false;
            } else {
                return isValidForm(s.substring(1, s.length()));
            }
        }

    /**
     * Checks if the given string is a valid formula (without the leading '=').
     * @param s the string to check.
     * @return true if the string is a valid formula, false otherwise.
     */
    public static boolean isValidForm (String s){
        boolean ans = false;
        if (s.isEmpty() || s == null) {
            ans = false;
        } else {
            if (isNumber(s)) {
                ans = true;
            } else if (isCell(s)) {
                ans = true;
            } else if (s.matches("[a-zA-Z]+")) {
                ans = false;
            } else
                {
                    if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
                    {
                        int closeIndex = parentheses(s);
                        if (closeIndex == -1 || closeIndex == s.length() - 1)
                        {
                            closeIndex = s.length() - 1;
                            ans = isValidForm(s.substring(1, closeIndex));
                        }
                        else if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
                        {
                        ans = isValidForm(s.substring(1, closeIndex)) && isValidForm(s.substring(closeIndex + 2, s.length()));
                        }
                        else
                        {
                            ans = isValidForm(s.substring(1, closeIndex)) && isValidForm(s.substring(closeIndex + 2, s.length() - 1));
                        }
                    }
                    else {
                        int i = lastOp(s);
                        if (i == s.length() - 1) {
                            ans = false;
                        }
                        else if (i==s.length()-2){
                            ans = isValidForm(s.substring(0, i)) && isValidForm(s.substring(s.length()-1));
                        }
                        else {
                            ans = isValidForm(s.substring(0, i)) && isValidForm(s.substring(i + 1, s.length()));
                        }
                    }
                }
            }
        return ans;
    }

    /**
     * Finds the position of the matching closing parenthesis for the first opening parenthesis in the string.
     * @param s the string to check.
     * @return the index of the matching closing parenthesis, or -1 if not found.
     */
    public static int parentheses (String s) {
        int openCount = 1;
        int closeIndex = -1;
        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')') {
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    openCount++;
                }
                if (s.charAt(i) == ')') {
                    if (openCount == 1) {
                        closeIndex = i;
                        break;
                    } else {
                        openCount--;
                    }
                }
            }

        }
        return closeIndex;
    }

    /**
     * Finds the position of the last operator in the string.
     * @param s the string to check.
     * @return the index of the last operator.
     */
    public static int lastOp (String s){
        int ans = 0;
        double min = 999999999;
        double val = 0;
        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            try {
                Integer.parseInt(s.substring(i, i + 1));
                    continue;
                }
            catch (NumberFormatException e)
            {
                if (Character.isLetter(s.charAt(i))||s.charAt(i)=='.'||s.charAt(i)==',')
                {
                    continue;
                }
                else if (s.charAt(i) == '(') {
                    depth++;
                    continue;
                } else if (s.charAt(i) == ')') {
                    depth--;
                    continue;
                }else if(s.charAt(i)=='=') {
                    continue;
                }
                else if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                    val = depth + 0.25;
                } else if (s.charAt(i) == '*' || s.charAt(i) == '/') {
                    val = depth + 0.5;
                }
                if (val <= min) {
                    min = val;
                    ans = i;
                }
            }
        }
        return ans;
    }
}


