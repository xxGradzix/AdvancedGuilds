package me.xxgradzix.advancedclans.data.database.repositories;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public abstract class EntityRepository<T, ID> {

    private Dao<T, ID> entityDao;

    public EntityRepository(ConnectionSource connectionSource, Class<T> entityClass) {
        try {
            entityDao = DaoManager.createDao(connectionSource, entityClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createOrUpdateEntity(T entity) throws SQLException {
        entityDao.createOrUpdate(entity);
    }

    public void createEntityIfNotExists(T entity) throws SQLException {
        entityDao.createIfNotExists(entity);
    }

    public void deleteEntity(T entity) throws SQLException {
        entityDao.delete(entity);
    }
    public void deleteEntityById(ID id) throws SQLException {
        entityDao.deleteById(id);
    }

    public T getEntityById(ID id) throws SQLException {
        return entityDao.queryForId(id);
    }

    public List<T> getAllEntities() throws SQLException {
        return entityDao.queryForAll();
    }

    public List<T> getAllEntitiesByField(String fieldName, Object value) throws SQLException {
        return entityDao.queryForEq(fieldName, value);
    }

    public List<T> getAllEntitiesByFields(HashMap<String, Object> fields) throws SQLException {
        return entityDao.queryForFieldValues(fields);
    }
}