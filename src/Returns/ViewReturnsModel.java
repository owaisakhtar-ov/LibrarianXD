package Returns;

import java.util.Date;

public class ViewReturnsModel {
    private String retID;
    private Date issueDate;
    private Date issueTill;
    private Date receivedDate;
    private String issueID;
    private String staffName;
    private String staffID;

    public ViewReturnsModel(String retID, Date issueDate, Date issueTill, Date receivedDate, String issueID, String staffName, String staffID) {
        this.retID = retID;
        this.issueDate = issueDate;
        this.issueTill = issueTill;
        this.receivedDate = receivedDate;
        this.issueID = issueID;
        this.staffName = staffName;
        this.staffID = staffID;
    }

    public String getRetID() {
        return retID;
    }

    public void setRetID(String retID) {
        this.retID = retID;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getIssueTill() {
        return issueTill;
    }

    public void setIssueTill(Date issueTill) {
        this.issueTill = issueTill;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }
}
