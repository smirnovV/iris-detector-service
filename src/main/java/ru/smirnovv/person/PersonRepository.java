package ru.smirnovv.person;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository that manages registered in the system persons.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
