package org.quiz.dao;

import java.util.Optional;

/**
 * A generic interface for Data Access Objects (DAOs) that interact with a database to perform CRUD operations on entities.
 *
 * @param <T> The type of entity.
 * @param <K> The type of the entity's primary key.
 */
public interface GenericDao<T, K> {

  /**
   * Create a new entity in the database.
   *
   * @param entity The entity to create.
   */
  void save(T entity);

  /**
   * Retrieve an entity by its primary key.
   *
   * @param id The primary key of the entity to retrieve.
   * @return An Optional containing the retrieved entity, or an empty Optional if not found.
   */
  Optional<T> getById(K id);

  /**
   * Update an existing entity in the database.
   *
   * @param entity The entity to update.
   */
  void update(T entity);

  /**
   * Delete an entity from the database by its primary key.
   *
   * @param id The primary key of the entity to delete.
   */
  void deleteById(K id);
}