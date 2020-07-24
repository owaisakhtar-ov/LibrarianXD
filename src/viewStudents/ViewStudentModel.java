package viewStudents;

import java.util.Date;

public class ViewStudentModel {
    private String stdID;
    private String stdName;
    private String stdContact;
    private String stdEmail;
    private String stdAddress;
    private Date stdRegDate;
    private int stdNoOfBooks;
    private String stdBlacklisted;

    public ViewStudentModel(String stdID, String stdName, String stdContact, String stdEmail, String stdAddress, Date stdRegDate, int stdNoOfBooks, String stdBlacklisted) {
        this.stdID = stdID;
        this.stdName = stdName;
        this.stdContact = stdContact;
        this.stdEmail = stdEmail;
        this.stdAddress = stdAddress;
        this.stdRegDate = stdRegDate;
        this.stdNoOfBooks = stdNoOfBooks;
        this.stdBlacklisted = stdBlacklisted;
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

    public String getStdContact() {
        return stdContact;
    }

    public void setStdContact(String stdContact) {
        this.stdContact = stdContact;
    }

    public String getStdEmail() {
        return stdEmail;
    }

    public void setStdEmail(String stdEmail) {
        this.stdEmail = stdEmail;
    }

    public String getStdAddress() {
        return stdAddress;
    }

    public void setStdAddress(String stdAddress) {
        this.stdAddress = stdAddress;
    }

    public Date getStdRegDate() {
        return stdRegDate;
    }

    public void setStdRegDate(Date stdRegDate) {
        this.stdRegDate = stdRegDate;
    }

    public int getStdNoOfBooks() {
        return stdNoOfBooks;
    }

    public void setStdNoOfBooks(int stdNoOfBooks) {
        this.stdNoOfBooks = stdNoOfBooks;
    }

    public String getStdBlacklisted() {
        return stdBlacklisted;
    }

    public void setStdBlacklisted(String stdBlacklisted) {
        this.stdBlacklisted = stdBlacklisted;
    }
}
