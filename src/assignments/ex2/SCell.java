package assignments.ex2;
// Add your documentation below:

import java.util.ArrayList;

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;
    private String name;

    public SCell(String data, String name) {
        setName(name);
        setData(data);
        if (isNumber(line)) {
            setType(2);
            setOrder(0);
        } else if ((isText(line))) {
            setType(1);
            setOrder(0);
        } else if (isForm(line)) {
            setType(3);
            setOrder(getOrder());
        }
    }

    public SCell(String data) {
        this(data, "A0");
    }

    @Override
    public int getOrder() {
        if (getType() == 1||getType() == 2) {
            order=0;
        }
        else if (getType() == 3) {
            order = 1;
        }
        return order;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        if (getType() == 1||getType() == 2||getType() == 3) {
            return getData();
        }
        else if (getType() == -1) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isText(String s) {
        if (s.charAt(0) == '=' || isNumber(s)) {
            return false;
        } else {
            return true;
        }
    }
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
    public static boolean isForm (String s){
            if (s.charAt(0) != '=' || s.isEmpty() || s.equals(null) || (s.charAt(0) == '=' && s.length() == 1)) {
                return false;
            } else {
                return isValidForm(s.substring(1, s.length()));
            }
        }
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
                if (Character.isLetter(s.charAt(i)))
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


