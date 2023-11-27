package cz.eg.hr.services;

import java.util.List;

public interface IFulltextSearchService<T> {

    /**
     * Method for fulltext search on columns in table
     * @param fields Columns in table to be searched
     * @param text Text to be searched in columns
     * @param types Datatype in which will be result returned
     * @return List of objects(row in table) which contain text in field(column)
     */
    List<T> fulltextSearch(String[] fields, String text, Class<T> types);
}
