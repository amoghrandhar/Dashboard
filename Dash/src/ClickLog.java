import java.io.Serializable;
import java.util.Date;

/**
* Created by Amogh on 22-02-2015 in Project ${PROJECT_NAME}
 */

public class ClickLog implements Serializable {

    private Date date;
    private double ID;
    private double clickCost;

    ClickLog(Date d, double id, double click) {
        this.date = d;
        this.ID = id;
        this.clickCost = click;
    }

    public Date getDate() {
        return date;
    }

    public double getID() {
        return ID;
    }

    public double getClickCost() {
        return clickCost;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ClickLog) {
            ClickLog t = (ClickLog) obj;
            if (t.getID() == this.ID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(ID);
    }

}