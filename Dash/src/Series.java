import java.util.Calendar;
import java.util.Date;


public class Series {

	Date startDate, endDate;
	
	Boolean gender;
	int ageGroup;
    int income;
    String context;
    int pages, time;
    
    public Series(){
    	
    	this.startDate = null;
    	this.endDate = null;
    	this.gender = null;
    	this.ageGroup = -1;
    	this.income = -1;
    	this.context = null;
    	this.pages = -1;
    	this.time = -1;
    	
    }
    
    public Date getStartDate(){
    	return this.startDate;
    }
    
    public Date getEndDate(){
    	return this.endDate;
    }
    
    public Boolean getGender(){
    	return this.gender;
    }
    
    public int getAgeGroup(){
    	return this.ageGroup;
    }
    
    public int getIncome(){
    	return this.income;
    }
    
    public String getContext(){
    	return this.context;
    }
    
    public int getBouncePages(){
    	return this.pages;
    }
    
    public int getBounceTime(){
    	return this.time;
    }
    
    public void setStartDate(Date startDate){
    	this.startDate = startDate;
    }
    
    public void setEndDate(Date endDate){
    	this.endDate = endDate;
    }
    
    public void setGender(Boolean gender){
    	this.gender = gender;
    }
    
    public void setAgeGroup(int ageGroup){
    	this.ageGroup = ageGroup;
    }
    
    public void setIncome(int income){
    	this.income = income;
    }
    
    public void setContext(String context){
    	this.context = context;
    }
    
    public void setBouncePages(int pages){
    	this.pages = pages;
    }
    
    public void setBounceTime(int time){
    	this.time = time;
    }
	
}
