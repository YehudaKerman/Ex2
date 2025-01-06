package assignments.ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private  String cname;

    @Override
    public boolean isValid() {
        if(getX()>0&&getX()<26&&getY()>0&&getY()<99){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isValid(SCell cell) {
        cname = getName(cell);
        return isValid();
    }
    public boolean isValid(String s) {
        cname = s;
        return isValid();
    }

    @Override
    public int getX() {
        String newS = cname.substring(0, 1).toUpperCase();
        for (int i=0;i<26;i++){
            if (newS.equals(Ex2Utils.ABC[i])){
                return i;
            }
        }
        return Ex2Utils.ERR;}

    @Override
    public int getY() {
        String newS = cname.substring(1, cname.length());
        try {
            if (Integer.parseInt(newS)<99&&Integer.parseInt(newS)>0){
                return Integer.parseInt(newS);
            }
            else {
                return Ex2Utils.ERR;
            }
        }
        catch (NumberFormatException e){
            return Ex2Utils.ERR;}
        }
    public  String getName(SCell cell) {
        cname = cell.getName();
        return cname;

    }
    public int getX(SCell cell) {
        cname = cell.getName();
        return getX();
    }
    public int getY(SCell cell) {
        cname = cell.getName();
        return getY();
    }
    public int getX(String s) {
        cname = s;
        return getX();
    }
    public int getY(String s) {
        cname = s;
        return getY();
    }
}
