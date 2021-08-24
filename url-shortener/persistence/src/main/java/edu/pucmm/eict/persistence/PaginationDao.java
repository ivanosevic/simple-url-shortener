package edu.pucmm.eict.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends from the class Dao. It adds the page functionality to get data.
 * If you don't need this, just extend from the classic Dao class.
 * @param <T> The main type to fetch
 * @param <PK> The type of the primary key
 */
public class PaginationDao<T, PK extends Serializable> extends Dao<T, PK>{

    public PaginationDao(Class<T> entityClass, EntityManagerFactory entityManagerFactory) {
        super(entityClass, entityManagerFactory);
    }

    private long getPageAmount(TypedQuery<Long> count) {
        long amount = 0;
        List<Long> countResult = count.getResultList();

        if(countResult.size() == 1) {
            amount = countResult.get(0);
        }

        // It means it was grouped by with another query...
        if(countResult.size() > 1) {
            amount = countResult.size();
        }

        return amount;
    }

    private boolean isValidPageQuery(int page) {
        return page <= 0;
    }

    private long calculateNumberOfPages(int size, long amount) {
        return (size + amount - 1) / size;
    }

    private boolean isPageFirst(int page) {
        return page == 1;
    }

    private boolean isPageLast(int page, long totalPages) {
        return page == totalPages;
    }

    private int getOffset(int page, int size) {
        return (page - 1) * size;
    }

    private boolean isPageEmpty(long pages) {
        return pages == 0;
    }

    private boolean pageExceedsTotal(int pages, long totalPages) {
        return pages > totalPages;
    }

    private boolean hasNoResults(long amount) {
        return amount == 0;
    }

    /**
     * Retrieves page data that needs to be filtered to avoid hibernate in memory pagination.
     * Use this if you need to use any kind of filter with your data, or the in memory pagination error
     * is popping.
     * @param page The number of page requested
     * @param size The size of page requested
     * @param count The query to count the number of elements in page
     * @param ids The query to filter all the data needed. It's important that returns the ids, and to avoid H104 error
     * @param query The query to fetch the data. Do all your join fetch here. You must pass a parameter called "ids",
     *              this is due to the function passing the ids to get the elements for the page. You can accomplish this
     *              adding this where sentence to your query: query ... where in (:ids) ... rest of the query
     * @return Page of elements requested. It's important to check if it's empty or not empty before consulting the data.
     */
    public Page<T> findPagedFilter(int page, int size, TypedQuery<Long> count, TypedQuery<PK> ids, TypedQuery<T> query) {
        if(isValidPageQuery(page)) {
            throw new PaginationErrorException("The page number can't be less or equal to 0.");
        }

        long amount = getPageAmount(count);
        if(hasNoResults(amount)) {
            return new Page<>(true, new ArrayList<>());
        }

        long totalPages = calculateNumberOfPages(size, amount);
        if(isPageEmpty(totalPages)) {
            return new Page<>(true, new ArrayList<>());
        }

        if(pageExceedsTotal(page, totalPages)) {
            throw new PaginationErrorException("THe number of pages can't exceed total");
        }

        int offset = getOffset(page, size);
        boolean isFirst = isPageFirst(page);
        boolean isLast = isPageLast(page, totalPages);

        List<PK> idsResults = ids.setFirstResult(offset).setMaxResults(size).getResultList();

        List<T> results = query.setParameter("ids", idsResults).getResultList();
        return new Page<>((int) totalPages, page, isFirst, isLast, results);
    }
}
