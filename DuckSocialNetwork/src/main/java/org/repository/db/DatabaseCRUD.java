package org.repository.db;

public interface DatabaseCRUD<ID, E> {
     void saveToDatabase(E entity) ;
     void deleteFromDatabase(ID id) ;
     void updateFromDatabase(E entity) ;
}
