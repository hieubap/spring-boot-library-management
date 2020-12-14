package library.jpa.DAO;

import java.util.List;

public class PayBookDAO {
    private List<Long> listIdBooks;

    public PayBookDAO() {
    }

    public List<Long> getListIdBooks() {
        return listIdBooks;
    }

    public void setListIdBooks(List<Long> listIdBooks) {
        this.listIdBooks = listIdBooks;
    }
}
