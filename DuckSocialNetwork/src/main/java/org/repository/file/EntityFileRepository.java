package org.repository.file;

import org.domain.Entity;
import org.domain.exceptions.RepositoryException;
import org.domain.validators.Validator;
import org.repository.EntityRepository;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public abstract class EntityFileRepository<ID, E extends Entity<ID>> extends EntityRepository<ID, E> {
    String fileName;

    public EntityFileRepository(String fileName, Validator<E> validator,Boolean autoLoad) {
        super(validator);
        this.fileName = fileName;
        if(autoLoad) loadData();
    }

    public EntityFileRepository(String fileName, Validator<E> validator) {
        this(fileName, validator, true);
    }

    public abstract E extractEntity(List<String> attributes);
    protected abstract String createEntityAsString(E entity);


    protected void loadData() { // template pattern
        File file = new File(fileName);
        if (!file.exists()) {
                throw new RepositoryException("File specified does not exist" + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> data = Arrays.asList(line.split(","));
                if (data.isEmpty()) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                try {
                    E entity = extractEntity(data);
                    super.save(entity);
                } catch (Exception e) {
                    throw new RepositoryException("Failed to parse line: " + line);

                }
            }
        } catch (IOException e) {
            throw new RepositoryException("I/O error reading " + fileName, e);
        }
    }


    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if (result == null) {
            writeToFile(entity);
        }
        return result;
    }

    @Override
    public E delete(ID id) {
        E removed = super.delete(id);
        if (removed != null) {
            rewriteFile();
        }
        return removed;
    }

    @Override
    public E update(E entity) {
        E result = super.update(entity);
        if (result == null) {
            rewriteFile();
            return null;
        }
        return result;
    }

    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(createEntityAsString(entity));
            writer.newLine();
        } catch (IOException e) {
            throw new RepositoryException("I/O error reading " + fileName, e);
        }
    }

    private void rewriteFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {
            for (E e : entities.values()) {
                writer.write(createEntityAsString(e));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RepositoryException("I/O error reading " + fileName, e);
        }
    }
    public static <T extends Comparable<T>> T getMaxId(Iterable<? extends Entity<T>> entities) {

        T maxId = null;

        for (Entity<T> e : entities) {
            if (e.getId() != null) {
                if (maxId == null || e.getId().compareTo(maxId) > 0) {
                    maxId = e.getId();
                }
            }
        }
        return maxId;
    }
}