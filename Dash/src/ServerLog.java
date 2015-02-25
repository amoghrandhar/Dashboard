import java.io.Serializable;
import java.util.Date;

/**
 * Created by Amogh on 22-02-2015.
 */
public class ServerLog implements Serializable {

    private Date sdate;
    private Date eDate;
    private double ID;
    private int pagesViewed;
    private boolean conversion;

    ServerLog(Date sd, double id, Date ed, int pages, boolean conv) {
        this.sdate = sd;
        this.ID = id;
        this.eDate = ed;
        this.pagesViewed = pages;
        this.conversion = conv;
    }

    public Date getEndDate() {
        return eDate;
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public boolean isConverted() {
        return conversion;
    }

    public Date getStartDate() {
        return sdate;
    }

    public double getID() {
        return ID;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ServerLog) {
            ServerLog t = (ServerLog) obj;
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
