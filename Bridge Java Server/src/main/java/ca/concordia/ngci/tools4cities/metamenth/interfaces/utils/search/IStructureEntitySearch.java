package ca.concordia.ngci.tools4cities.metamenth.interfaces.utils.search;

import java.util.HashMap;
import java.util.List;

public interface IStructureEntitySearch {
    Object searchByUid(List<Object> entities, String uid);
    Object searchbyName(List<Object> entities, String name);
    List<Object> search(List<Object> entities, HashMap<String, Object> searchTerms);
    List<Object> dateRangeSearch(List<Object> entities, String fromDateStr, String toDateStr);
    Object searchStructureEntity(List<Object> entities, String searchField, Object searchValue);
}
