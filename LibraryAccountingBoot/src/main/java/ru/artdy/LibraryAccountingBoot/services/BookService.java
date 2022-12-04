package ru.artdy.LibraryAccountingBoot.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artdy.LibraryAccountingBoot.models.Book;
import ru.artdy.LibraryAccountingBoot.models.Person;
import ru.artdy.LibraryAccountingBoot.repositories.BookRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear) {
            return bookRepository.findAll(Sort.by("year"));
        } else {
            return bookRepository.findAll(Sort.by("title"));
        }
    }

    public Book findOne(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> searchByTitle(String query) {
        return bookRepository.searchByTitleStartingWith(query);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = findOne(id);
        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void release(int id){
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
            book.setTakenAt(null);
        });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        bookRepository.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );

    }
}
