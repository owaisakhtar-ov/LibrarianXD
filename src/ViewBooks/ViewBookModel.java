package ViewBooks;

public class ViewBookModel {
    private String bookID;
    private String bookName;
    private String bookLanguage;
    private String bookWriter;
    private int bookQuantity;
    private int bookEdition;
    private String bookGenre;

    public ViewBookModel(String bookID, String bookName, String bookLanguage, String bookWriter, int bookQuantity, int bookEdition, String bookGenre) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookLanguage = bookLanguage;
        this.bookWriter = bookWriter;
        this.bookQuantity = bookQuantity;
        this.bookEdition = bookEdition;
        this.bookGenre = bookGenre;
    }

    public int getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(int bookEdition) {
        this.bookEdition = bookEdition;
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

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getBookWriter() {
        return bookWriter;
    }

    public void setBookWriter(String bookWriter) {
        this.bookWriter = bookWriter;
    }

    public int getBookQuantity() {
        return bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }
}
