package assignments.ex2;
// Add your documentation below:

import java.util.Scanner;

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;
    private String name;

    public SCell(String data, String name) {
        this.name=name;
        setData(data);
        if(isNumber(line)){
            setType(2);
            setOrder(0);
        } else if ((isText(line))){
            setType(1);
            setOrder(0);
        } else if (isForm(line)) {

        }
    }

    public SCell(String data) {
        SCell temp = new SCell(data, "A0");

    }

    @Override
    public int getOrder() {
        order = 0;

        return order;
        // ///////////////////
    }

    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
public void setData(String s) {
        // Add your code here
        line = s;
        /////////////////////
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
        // Add your code here

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
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isText (String s) {
        if (s.charAt(0)== '=' || isNumber(s)) {
            return false;
        }
        else {return true;}
    }
    public static boolean isCell (SCell cell) {
        String s = cell.getName();
        //if(cell.CellEntry.isValid())
            return true;

    }
    public static boolean isForm (String s) {
        boolean ans = true;
        if (s.isEmpty()||s.equals("+")||s.equals("-")||s.equals("*")||s.equals("/")) {
            ans = false;
        }
        else{
            if (s.charAt(0)== '=') {
                String s1 = s.substring(1,s.length());
                if (isNumber(s1)) {
                    ans = true;
                }
                else if (s1.charAt(0)=='('&&s.charAt(s1.length()-1)==')') {
                    ans = isForm(s1.substring(1,s.length()-1));
                } else
                {
                    int i = lastOp(s1);
                    ans = isForm(s1.substring(1,i))&&isForm(s1.substring(i+1,s1.length()));
                }
            }
        }
        return ans;
        }
    public static int lastOp (String s){
        int ans = 0;
        double min = 999999999;
        double val = 0;
        int depth = 0;
        for(int i = 0; i < s.length(); i++){
            try {
                Integer.parseInt(s.substring(i,i+1));{
                    continue;
                }
            }catch (NumberFormatException e) {
                if(s.charAt(i)== '('){
                    depth++;
                    continue;
                }
                else if(s.charAt(i)==')'){
                    depth--;
                    continue;
                }
                else if(s.charAt(i)=='+'||s.charAt(i)=='-'){
                    val=depth+0.25;
                }
                else if (s.charAt(i)=='*'||s.charAt(i)=='/') {
                    val=depth+0.5;
                }
                if (val<=min) {
                    min = val;
                    ans = i;
                }
            }
        }
            return ans;
        }
    }


