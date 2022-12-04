package ru.artdy.LibraryAccountingBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.artdy.LibraryAccountingBoot.models.Book;
import ru.artdy.LibraryAccountingBoot.models.Person;
import ru.artdy.LibraryAccountingBoot.services.BookService;
import ru.artdy.LibraryAccountingBoot.services.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public BookController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }


    @GetMapping()
    public String index(Model model, @RequestParam(value="sort_by_year", required = false) boolean sortByYear,
                        @RequestParam(value="sort_by_title", required = false) boolean sortByTitle) {
        model.addAttribute("books", bookService.findAll(sortByYear));
        return "/books/index";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "/books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = bookService.findOne(id);
        model.addAttribute("book", book);
        model.addAttribute("bookOwner", book.getOwner());
        model.addAttribute("person", new Person());
        model.addAttribute("people", peopleService.findAll());

        return "/books/show";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));
        return "/books/edit";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "/books/search";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.searchByTitle(query));
        return "/books/search";
    }

    @PatchMapping("/{id}/edit")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
