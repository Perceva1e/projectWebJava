package bsuir.group.projectweb.service.impl;

import bsuir.group.projectweb.model.Author;
import bsuir.group.projectweb.model.Text;
import bsuir.group.projectweb.repository.AuthorRepositoryDAO;
import bsuir.group.projectweb.repository.SalaryRepositoryDAO;
import bsuir.group.projectweb.repository.TextRepositoryDAO;
import bsuir.group.projectweb.service.AuthorService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    /**
     * This logger.
     *
     * @param LOGGER is a server
     */
    static final Logger LOGGER = LogManager.getLogger(AuthorServiceImpl.class);
    /**
     * This is a repository of entity text.
     */
    private final TextRepositoryDAO repositoryText;
    /**
     * This is a repository of entity author.
     */
    private final AuthorRepositoryDAO repositoryAuthor;
    /**
     * This is a repository of entity salary.
     */
    private final SalaryRepositoryDAO repositorySalary;

    /**
     * This savePerson.
     *
     * @param author is an entity of author for save
     * @return restore author after save
     */

    @Override
    public Author savePerson(final Author author) {
        LOGGER.info("save a person");
        repositorySalary.save(author.getSalaries());
        return repositoryAuthor.save(author);
    }

    /**
     * This method delete Author Connection.
     *
     * @param idAuthor is an id of entity for delete
     * @param idText   is an id of entity for delete
     * @return restore true if delete, else false
     */
    @Override
    public boolean deleteAuthorConnection(final Long idAuthor,
                                          final Long idText) {
        if (repositoryAuthor.existsById(idAuthor)) {
            LOGGER.info("delete author connection");
            Text text = repositoryText.findTextById(idText);
            Set<Author> authors = text.getAuthors();
            List<Author> authorsList = new java.util.ArrayList<>(
                    authors.stream().toList());
            for (Author author : authorsList) {
                if (author.getId().equals(idAuthor)) {
                    authorsList.remove(author);
                    break;
                }
            }
            text.setAuthors(new HashSet<>(authorsList));
            repositoryText.save(text);
            return true;
        }
        return false;
    }

    /**
     * This method add Author In Text.
     *
     * @param informationExist is a string exists
     * @param authorAdd        is an entity for add
     * @return restore true if changed, else false
     */
    @Override
    public Boolean addAuthorInText(final String informationExist,
                                   final Author authorAdd) {
        if (repositoryText.existsByInformation(informationExist)) {
            LOGGER.info("add author connection ");
            Text informationChange = repositoryText.
                    findByInformation(informationExist);
            Set<Author> authors = informationChange.getAuthors();
            repositorySalary.save(authorAdd.getSalaries());
            authors.add(authorAdd);
            informationChange.setAuthors(authors);
            repositoryText.save(informationChange);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method find Author By Parameters.
     *
     * @param lastName is a string for find
     * @param nameList is a list of string for find
     * @return restore list author after find
     */
    @Override
    public List<Author> findAuthorByParameters(final String lastName,
                                               final List<String> nameList) {
        LOGGER.info("find author by parameters");
        return repositoryAuthor.findAuthorByParameters(lastName, nameList);
    }
}
