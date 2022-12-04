package ru.artdy.LibraryAccountingBoot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artdy.LibraryAccountingBoot.models.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> searchByTitleStartingWith(String query);
}
