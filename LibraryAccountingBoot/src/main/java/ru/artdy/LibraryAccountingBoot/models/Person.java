package ru.artdy.LibraryAccountingBoot.models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "person")
@SuppressWarnings("com.haulmont.jpb.LombokDataInspection")
public class Person {
    @Transient
    private final String DATE_FORMAT = "dd.MM.yyyy";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Поле не должно быть пустым!")
    @Size(min = 2 , max = 100, message = "Имя должно быть длиной от 2 до 100 символов.")
    @Column(name = "full_name")
    private String fullName;

    @Past(message = "Дата рождения должна быть не позднее сегодняшней даты.")
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = DATE_FORMAT)
    private Date dateOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public String getFormattedDateOfBirth() {
        if (dateOfBirth == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(dateOfBirth);
    }

    public String getYearOfBirth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        return sdf.format(dateOfBirth);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Person{" +
                "fullName='" + fullName + '\'' +
                ", yearOfBirth=" + dateOfBirth +
                '}';
    }
}
