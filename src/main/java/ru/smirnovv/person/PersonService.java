package ru.smirnovv.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * A service that manages registered in the system persons.
 */
@SuppressWarnings({"designForExtension", "magicNumber"})
@Service
public class PersonService {
    /**
     * A repository that manages registered in the system persons.
     */
    private final PersonRepository personRepository;

    /**
     * Constructs an instance with injected dependencies.
     *
     * @param personRepository a repository that manages registered in the system persons.
     */
    @Autowired
    public PersonService(final PersonRepository personRepository) {
        Assert.notNull(personRepository, "Argument 'personRepository' can not be null");
        this.personRepository = personRepository;
    }

    /**
     * Checks name for validity.
     *
     * @param name the name of the person.
     * @throws InvalidNameException is thrown when a name does not conform to the naming syntax.
     */
    public static void checkName(final String name) {
        if (name.isEmpty()) {
            throw new InvalidNameException("Invalid name! The name must not be empty!");
        } else if (!name.matches("([A-Z][A-z .-]*)")) {
            throw new InvalidNameException("Invalid name! The name must contain Latin characters, "
                    + "numbers, signs '-', '.' and start with a capital letter!");
        } else if (name.length() > 50) {
            throw new InvalidNameException("Invalid name! The name must be no longer than 50 characters!");
        }
    }

    /**
     * Lists all persons registered in the system.
     *
     * @param pageable a paging information.
     * @return the page of retrieved persons.
     */
    @Transactional(readOnly = true)
    public Page<Person> list(final Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    /**
     * Adds a new person.
     *
     * @param name the name of the person.
     * @return the added person.
     * @throws InvalidNameException is thrown when a name does not conform to the naming syntax.
     */
    @Transactional
    public Person add(final String name) throws InvalidNameException {
        checkName(name);

        return personRepository.save(new Person(name));
    }

    /**
     * Returns the person by id if it exists.
     *
     * @param id the id of the person.
     * @return the found person.
     * @throws PersonNotFoundException is thrown when a person with such id does not exist.
     */
    @Transactional(readOnly = true)
    public Person getPersonById(final long id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(
                () -> new PersonNotFoundException("Person " + id + " not found."));
    }

    /**
     * Updates name of the person by id if name passed the verification.
     *
     * @param id   the id of the person.
     * @param name the new name of the person.
     * @return the updated person.
     * @throws PersonNotFoundException is thrown when a person with such id does not exist.
     * @throws InvalidNameException    is thrown when a name does not conform to the naming syntax.
     */
    @Transactional
    public Person update(final long id, final String name) throws PersonNotFoundException, InvalidNameException {
        checkName(name);

        final Person person = getPersonById(id);
        person.setName(name);
        return personRepository.save(person);
    }

    /**
     * Removes the person by id if it exists.
     *
     * @param id the id of the person.
     */
    @Transactional
    public void remove(final long id) {
        personRepository.deleteById(id);
    }
}
