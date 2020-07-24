package Issuance;

import java.util.Date;

public class ViewIssueModel {
    private String issueID;
    private Date issueDate;
    private Date issueTill;
    private String stdID;
    private String stdName;
    private String bookID;
    private String bookName;
    private String staffID;
    private String staffName;

    public ViewIssueModel(String issueID, Date issueDate, Date issueTill, String stdID, String stdName, String bookID, String bookName, String staffID, String staffName) {
        this.issueID = issueID;
        this.issueDate = issueDate;
        this.issueTill = issueTill;
        this.stdID = stdID;
        this.stdName = stdName;
        this.bookID = bookID;
        this.bookName = bookName;
        this.staffID = staffID;
        this.staffName = staffName;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueID(String issueID) {
        this.issueID = issueID;
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

    public String getStdID() {
        return stdID;
    }

    public void setStdID(String stdID) {
        this.stdID = stdID;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
