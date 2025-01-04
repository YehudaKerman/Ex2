package assignments.ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private String name;

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
        name = getName(cell);
        return isValid();
    }

    @Override
    public int getX() {
        String newS = name.substring(0, 1).toUpperCase();
        for (int i=0;i<26;i++){
            if (newS.equals(Ex2Utils.ABC[i])){
                return i;
            }
        }
        return Ex2Utils.ERR;}

    @Override
    public int getY() {
        String newS = name.substring(1, name.length());
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
    public String getName(SCell cell) {
        this.name = cell.getName();
        return name;

    }
    public int getX(SCell cell) {
        name = cell.getName();
        return getX();
    }
    public int getY(SCell cell) {
        name = cell.getName();
        return getY();
    }
}
