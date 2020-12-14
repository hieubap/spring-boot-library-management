package library.jpa.DAO;

public class HeadBookDao {
    private String name;
    private String publisher;
    private String author;
    private Long numberOfPages;
    private Long price;
    private Long numberBooks;

    public HeadBookDao() {
    }

    public void autoSet(){
        if (name == null)
            name = "";
        if (publisher == null)
            publisher = "";
        if (author == null)
            author = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Long numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getNumberBooks() {
        return numberBooks;
    }

    public void setNumberBooks(Long numberBooks) {
        this.numberBooks = numberBooks;
    }
}
