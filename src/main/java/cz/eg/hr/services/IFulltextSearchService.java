package cz.eg.hr.services;

import cz.eg.hr.data.Version;

import java.util.List;

public interface IFulltextSearchService<T> {

    List<T> fulltextSearch(String[] fields, String text, Class<T> types);
}
