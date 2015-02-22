import java.io.Serializable;
import java.util.Date;

/**
 * Created by Amogh on 20-02-2015.
 */
public class Impression implements Serializable {

    private Date date;
    private double ID;
    private double impression;
    private boolean gender;
    private int ageGroup;
    private int incomeGroup;
    private String context;

    Impression(Date d, double id, boolean sex, int age, int inc, String con, double imp) {
        this.date = d;
        this.ID = id;
        this.impression = imp;
        this.gender = sex;
        this.ageGroup = age;
        this.incomeGroup = inc;
        this.context = con;
    }

    public Date getDate() {
        return date;
    }

    public double getID() {
        return ID;
    }

    public double getImpression() {
        return impression;
    }

    public boolean getGender() {
        return gender;
    }

    public int getAgeGroup() {
        return ageGroup;
    }

    public int getIncomeGroup() {
        return incomeGroup;
    }

    public String getContext() {
        return context;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Impression) {
            Impression t = (Impression) obj;
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
