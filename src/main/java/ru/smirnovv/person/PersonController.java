package ru.smirnovv.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.smirnovv.ErrorType;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.util.Assert.notNull;
import static ru.smirnovv.person.PersonService.checkName;

/**
 * A REST controller that serves registered in the system persons.
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    /**
     * A service that manages registered in the system persons.
     */
    private final PersonService personService;

    /**
     * Constructs an instance with injected dependencies.
     *
     * @param personService a service that manages registered in the system persons.
     */
    @Autowired
    public PersonController(final PersonService personService) {
        notNull(personService, "Argument 'personService' can not be null");

        this.personService = personService;
    }

    /**
     * Lists all persons registered in the system.
     *
     * @param pageable a paging information.
     * @return the page of retrieved persons.
     */
    @GetMapping
    public final Page<Person> list(@PageableDefault(sort = "id") final Pageable pageable) {
        return personService.list(pageable);
    }

    /**
     * Adds a new person.
     *
     * @param name the name of the person.
     * @return the added person.
     */
    @PutMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public final Person add(@RequestParam final String name) {
        checkName(name);
        return personService.add(name);
    }

    /**
     * Returns the person by id if it exists.
     *
     * @param id the id of the person.
     * @return the found person.
     */
    @GetMapping("/{id}")
    public final Person getPersonById(@PathVariable final long id) {
        return personService.getPersonById(id);
    }

    /**
     * Updates name of the person by id if name passed the verification.
     *
     * @param id   the id of the person.
     * @param name the new name of the person.
     * @return the updated person.
     */
    @PostMapping(path = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public final Person update(@PathVariable final long id, @RequestParam final String name) {
        checkName(name);
        return personService.update(id, name);
    }

    /**
     * Removes the person by id if it exists.
     *
     * @param id the id of the person.
     */
    @DeleteMapping("/{id}")
    public final void remove(@PathVariable final long id) {
        personService.remove(id);
    }

    /**
     * Handles {@link InvalidNameException} and returns the response with error information.
     *
     * @param request   the request where the exception was thrown.
     * @param exception the thrown exception.
     * @return a response entity with error details.
     * @see ErrorType
     */
    @ExceptionHandler(InvalidNameException.class)
    public final ResponseEntity<ErrorType> handleInvalidNameException(
            final HttpServletRequest request, final InvalidNameException exception) {
        return new ResponseEntity<>(
                new ErrorType(request.getRequestURI(), BAD_REQUEST.value(), exception.getMessage()),
                BAD_REQUEST);
    }
}
