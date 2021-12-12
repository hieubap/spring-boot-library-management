package vn.isofh.jpa.entity;//package com.example.final_library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.isofh.jpa.Dao.HeadBookDao;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "head_book")
public class HeadBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private String publisher;
    private String author;
    @Column(name = "totalPage")
    private Long numberOfPages;
    private Long price;

    @JsonInclude(JsonInclude.Include.NON_EMPTY) // nếu rỗng thì không hiển thị
    @OneToMany(mappedBy = "headBook",cascade = CascadeType.ALL)
    private Collection<Book> books;

    public HeadBook() {
    }

    public HeadBook(String name, String publisher) {
        this.name = name;
        this.publisher = publisher;
    }

    public void setHeadBook(HeadBook headBook){
        if(headBook.getName() != null){
            this.setName(headBook.getName());
        }
        if (headBook.getPublisher() != null){
            this.setPublisher(headBook.getPublisher());
        }
        if (headBook.getAuthor() != null){
            this.setAuthor(headBook.getAuthor());
        }
        if (headBook.getNumberOfPages() != null)
            this.setNumberOfPages(headBook.getNumberOfPages());
        if (headBook.getPrice() != null){
            this.setPrice(headBook.getPrice());
        }
    }
    public void setHeadBook(HeadBookDao headBook){
        if(headBook.getName() != null){
            this.setName(headBook.getName());
        }
        if (headBook.getPublisher() != null){
            this.setPublisher(headBook.getPublisher());
        }
        if (headBook.getAuthor() != null){
            this.setAuthor(headBook.getAuthor());
        }
        if (headBook.getNumberOfPages() != null)
            this.setNumberOfPages(headBook.getNumberOfPages());
        if (headBook.getPrice() != null){
            this.setPrice(headBook.getPrice());
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonBackReference
    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }
}
