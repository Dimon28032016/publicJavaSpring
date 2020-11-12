package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.FilterForm;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private FilterForm filter = null;

    @Override
    public List<Book> retreiveAll() {
        if(filter != null) {
            Pattern pattern = Pattern.compile(filter.getRegex());
            int countBooks = (new ArrayList<>(repo)).size();

            return getFilterBooks(countBooks, pattern);
        }
        return new ArrayList<>(repo);
    }

    private List<Book> getFilterBooks(int countBooks, Pattern pattern) {
        List<Book> response = new ArrayList<>();
        String column = "";
        for(int i = 0; i < countBooks; i++) {
            switch (filter.getTypeFilter()){
                case "bookAuthorToFilter":
                    column = repo.get(i).getAuthor();
                    break;
                case "bookTitleToFilter":
                    column = repo.get(i).getTitle();
                    break;
                case "bookSizeToFilter":
                    column = repo.get(i).getSize().toString();
                    break;
            }
            Matcher matcher = pattern.matcher(column);
            if(matcher.lookingAt()) {
                response.add(repo.get(i));
            }
        }
        return response;
    }

    @Override
    public void store(Book book) {
        book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
    }

    @Override
    public boolean remove(String typeRemove, Object valueRemove) throws NumberFormatException {
        boolean flagRemove = false;
        Pattern pattern;
        switch (typeRemove) {
            case "bookIdToRemove":
                int idRemove = Integer.parseInt(valueRemove.toString());
                flagRemove = false;
                for (Book book : retreiveAll()) {
                    if (book.getId().equals(idRemove)) {
                        logger.info("remove book completed: " + book);
                        repo.remove(book);
                        flagRemove = true;
                    }
                }
                return flagRemove;
            case "bookAuthorToRemove":
                String authorRemove = valueRemove.toString();
                flagRemove = false;
                pattern = Pattern.compile(authorRemove);
                for (Book book : retreiveAll()) {
                    Matcher matcher = pattern.matcher(book.getAuthor());
                    if(matcher.lookingAt()) {
                        logger.info("remove book completed: " + book);
                        repo.remove(book);
                        flagRemove = true;
                    }
                }
                return flagRemove;
            case "bookTitleToRemove":
                String titleRemove = valueRemove.toString();
                flagRemove = false;
                pattern = Pattern.compile(titleRemove);
                for (Book book : retreiveAll()) {
                    Matcher matcher = pattern.matcher(book.getTitle());
                    if(matcher.lookingAt()) {
                        logger.info("remove book completed: " + book);
                        repo.remove(book);
                        flagRemove = true;
                    }
                }
                return flagRemove;
            case "bookSizeToRemove":
                int sizeRemove = Integer.parseInt(valueRemove.toString());
                flagRemove = false;
                pattern = Pattern.compile("" + sizeRemove);
                for (Book book : retreiveAll()) {
                    Matcher matcher = pattern.matcher(book.getSize().toString());
                    if(matcher.lookingAt()) {
                        logger.info("remove book completed: " + book);
                        repo.remove(book);
                        flagRemove = true;
                    }
                }
                return flagRemove;
        }
        return false;
    }

    public void initFilter(FilterForm filterForm) { this.filter = filterForm; }
}
