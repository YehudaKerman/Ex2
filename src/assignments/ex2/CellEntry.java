package assignments.ex2;
// Add your documentation below:

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class represents a cell entry in a spreadsheet with coordinates in the form of "A1", "B2", etc.
 * It implements the Index2D interface and provides methods to validate and retrieve the coordinates.
 */
public class CellEntry implements Index2D {
    private String coordinates;
    private int row;
    private int col;

    /**
     * Constructs a CellEntry object with the given coordinates.
     * @param cords the coordinates in the form of a string (e.g., "A1").
     */
    public CellEntry(String cords) {
        coordinates = cords;
        if (cords == null || cords.length() < 2 || cords.length() > 3) {
            this.row = Ex2Utils.ERR;
            this.col = Ex2Utils.ERR;
            coordinates = Ex2Utils.ERR_FORM;
        } else {
            if (isValid()) {
                col = getX();
                row = getY();
            }
            else {
                this.row = Ex2Utils.ERR;
                this.col = Ex2Utils.ERR;
                coordinates = Ex2Utils.ERR_FORM;
            }
        }
    }

    /**
     * Checks if the coordinates are valid.
     * @return true if the coordinates are valid, false otherwise.
     */
    @Override
    public boolean isValid() {
        if (row == Ex2Utils.ERR || col == Ex2Utils.ERR) {
            return false;
        } else if (coordinates==null||coordinates.substring(1).matches(".*[a-zA-Z].*")) {
            return false;
        } else if (getX() >= 0 && getX() <= 25 && getY() >= 0 && getY() <= 99) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retrieves the X coordinate (column) from the coordinates string.
     * @return the X coordinate as an integer, or Ex2Utils.ERR if invalid.
     */
    @Override
    public int getX() {
        if (!coordinates.substring(0,1).matches(".*[a-zA-Z].*")) {
            return Ex2Utils.ERR;
        } else if (coordinates.substring(1).matches(".*[a-zA-Z].*")||coordinates.substring(1).isEmpty()) {
            return Ex2Utils.ERR;
        }
        else
        {
            String newS = coordinates.substring(0, 1).toUpperCase();
            for (int i = 0; i < 26; i++) {
                if (newS.equals(Ex2Utils.ABC[i])) {
                    return i;
                }
            }
        }
        return Ex2Utils.ERR;
    }

    /**
     * Retrieves the Y coordinate (row) from the coordinates string.
     * @return the Y coordinate as an integer, or Ex2Utils.ERR if invalid.
     */
    @Override
    public int getY() {
        String newS = coordinates.substring(1);
        if (newS.matches(".*[a-zA-Z].*")) {
            return Ex2Utils.ERR;
        } else {
            try {
                int y = Integer.parseInt(newS);
                if (y >= 0 && y <= 99) {
                    return y;
                } else {
                    return Ex2Utils.ERR;
                }
            } catch (NumberFormatException e) {
                return Ex2Utils.ERR;
            }
        }
    }

    /**
     * The method is getting a x & y coordinets and return the cell name.
     * @param row
     * @param col
     * @return the cell name in string
     */
    public static String getName(int row, int col) {
        String ans = "";
        ans = Ex2Utils.ABC[col]+Integer.toString(row);
        return ans;
    }

    /**
     * the method print the coordinates as cell name
     * @return CellEntry as a cell name
     */
    @Override
    public String toString() {
        return getName(row, col);
    }
}
