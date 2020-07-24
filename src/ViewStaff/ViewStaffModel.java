package ViewStaff;

public class ViewStaffModel {
    private String staffID;
    private String staffName;
    private String staffContact;
    private String staffDesignation;

    public ViewStaffModel(String staffID, String staffName, String staffContact, String staffDesignation) {
        this.staffID = staffID;
        this.staffName = staffName;
        this.staffContact = staffContact;
        this.staffDesignation = staffDesignation;
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

    public String getStaffContact() {
        return staffContact;
    }

    public void setStaffContact(String staffContact) {
        this.staffContact = staffContact;
    }

    public String getStaffDesignation() {
        return staffDesignation;
    }

    public void setStaffDesignation(String staffDesignation) {
        this.staffDesignation = staffDesignation;
    }
}
