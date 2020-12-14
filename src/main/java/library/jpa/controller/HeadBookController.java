package library.jpa.controller;//package com.example.final_library.controller;

import library.jpa.entity.HeadBook;
import library.jpa.responceEntity.EntityResponse;
import library.jpa.service.HeadBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/book/head")
public class HeadBookController {
    private final HeadBookService headbookService;

    @Autowired
    public HeadBookController(HeadBookService bookService) {
        this.headbookService = bookService;
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public EntityResponse<HeadBook> update(@RequestBody HeadBook book, @RequestParam Long id) {
        headbookService.updateHeadBook(book, id);
        return new EntityResponse<>(HttpStatus.OK, "Update Successful", book);
    }

    @DeleteMapping(value = "/delete")
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public EntityResponse<HeadBook> delete(@RequestParam Long id) {
        headbookService.deleteHeadBookById(id);
        return new EntityResponse<>(HttpStatus.OK, "Delete successful", headbookService.getHeadBookById(id));
    }
}
