package org.example.decorator;

import org.example.service.CrudService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LoggerConsoleDecorator<T> implements Logger<T> {
    private final CrudService<T> crudService;

    public LoggerConsoleDecorator(CrudService<T> crudService) {
        this.crudService = crudService;
    }

    @Override
    public T create(T entity) throws SQLException {
        logMethodCall("create", entity);
        T result = crudService.create(entity);
        logMethodResult("create", result);
        return result;
    }

    @Override
    public T update(Long id, T entity) throws SQLException {
        logMethodCall("update", id, entity);
        T result = crudService.update(id, entity);
        logMethodResult("update", result);
        return result;
    }

    @Override
    public void delete(Long id) throws SQLException {
        logMethodCall("delete", id);
        crudService.delete(id);
        logMethodResult("delete", null);
    }

    @Override
    public Optional<T> getById(Long id) throws SQLException {
        logMethodCall("getById", id);
        Optional<T> result = crudService.getById(id);
        logMethodResult("getById", result);
        return result;
    }

    @Override
    public List<T> getAll() throws SQLException {
        logMethodCall("getAll");
        List<T> result = crudService.getAll();
        logMethodResult("getAll", result);
        return result;
    }

    private void logMethodCall(String methodName, Object... args) {
        StringBuilder logMessage = new StringBuilder("Calling method: ");
        logMessage.append(methodName);
        for (Object arg : args) {
            logMessage.append(", Argument: ").append(arg);
        }
        logToConsole(logMessage.toString());
    }

    private void logMethodResult(String methodName, Object result) {
        String logMessage = "Method " + methodName + " executed. Result: " + result;
        logToConsole(logMessage);
    }

    private void logToConsole(String message) {
        System.out.println(message);
    }
}
