package ru.artdy.LibraryAccountingBoot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artdy.LibraryAccountingBoot.models.Book;
import ru.artdy.LibraryAccountingBoot.models.Person;
import ru.artdy.LibraryAccountingBoot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private static final long FIVE_DAYS = 432_000_000;

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get();
        }
        return null;
    }

    public Optional<Person> getPersonByFullName(String fullName) {
        return peopleRepository.getPersonByFullName(fullName);
    }


    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().forEach(book -> {
                        long diffInMillis = Math.abs(new Date().getTime() - book.getTakenAt().getTime());
                        if (diffInMillis > FIVE_DAYS) {
                            book.setExpired(true);
                        }
                    }
            );
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }
}
