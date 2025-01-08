package assignments.ex2;
// Add your documentation below:

public class CellEntry  implements Index2D {
    private  String coordinates;
    private int row;
    private int col;

    public CellEntry(String cords) {
        if (cords == null || cords.length() < 2) {
            this.row = -1;
            this.col = -1;
        }
        else {
            coordinates = cords;
            col = getX();
            row = getY();
        }
    }
    @Override
    public boolean isValid() {
        if(getX()>0&&getX()<26&&getY()>0&&getY()<99){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public int getX() {
        String newS = coordinates.substring(0, 1).toUpperCase();
        for (int i=0;i<26;i++){
            if (newS.equals(Ex2Utils.ABC[i])){
                return i;
            }
        }
        return Ex2Utils.ERR;}

    @Override
    public int getY() {
        String newS = coordinates.substring(1);
        try {
            if (Integer.parseInt(newS)<99&&Integer.parseInt(newS)>=0){
                return Integer.parseInt(newS);
            }
            else {
                return Ex2Utils.ERR;
            }
        }
        catch (NumberFormatException e){
            return Ex2Utils.ERR;}
        }
}
