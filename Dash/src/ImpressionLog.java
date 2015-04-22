import java.io.Serializable;
import java.util.Date;

/**
 * Created by Amogh on 20-02-2015.
 */
public class ImpressionLog implements Serializable {

    private Date date;
    private double ID;
    private double impression;
    private boolean gender;
    private int ageGroup;
    private int incomeGroup;
    private String context;

    ImpressionLog(Date d, double id, boolean sex, int age, int inc, String con, double imp) {
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

    public Double getID() {
        return ID;
    }

    public Double getImpression() {
        return impression;
    }

    public boolean getGender() {
        return gender;
    }

    public Integer getAgeGroup() {
        return ageGroup;
    }

    public Integer getIncomeGroup() {
        return incomeGroup;
    }

    public String getContext() {
        return context;
    }

    public Boolean isSameID ( Double id ){
        return this.ID == id.doubleValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ImpressionLog) {
            ImpressionLog t = (ImpressionLog) obj;
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
