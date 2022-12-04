package ru.artdy.LibraryAccountingBoot.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import ru.artdy.LibraryAccountingBoot.models.Person;
import ru.artdy.LibraryAccountingBoot.services.PeopleService;

@Component
public class PeopleValidator implements Validator {
    private final PeopleService peopleService;

    public PeopleValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(errors.hasFieldErrors("dateOfBirth")) {
            FieldError dateOfBirthError = errors.getFieldError("dateOfBirth");
            String rejectedValue = (String) dateOfBirthError.getRejectedValue();
            String DATE_FORMAT_REGEXP = "^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$";
            if (!rejectedValue.matches(DATE_FORMAT_REGEXP)) {
                errors.rejectValue("dateOfBirth", "", "Неверный формат даты (используйте следующий формат — дд.мм.гггг).");
            }
        }

        if(peopleService.getPersonByFullName(person.getFullName()).isPresent()) {
            errors.rejectValue("fullName", "", "Человек с таким именем уже существует.");
        }
    }
}
