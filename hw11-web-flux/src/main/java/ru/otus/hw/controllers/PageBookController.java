package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageBookController {

    @GetMapping("/list")
    public String listBooksPage(Model model) {
        return "list";
    }

    @GetMapping("/create/book")
    public String createBookPage(Model model) {
        return "create";
    }

    @GetMapping("/edit/book/{id}")
    public String editPage(@PathVariable("id") String id, Model model) {
        return "edit";
    }

    @GetMapping("/delete/book/{id}")
    public String deletePage(@PathVariable("id") String id, Model model) {
        return "delete";
    }
}