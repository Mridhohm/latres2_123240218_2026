package com.pbo.latres.controller;

import com.pbo.latres.dto.InsertTodoDTO;
import com.pbo.latres.model.TodoRepository;
import com.pbo.latres.model.TodoTask;
import com.pbo.latres.view.TodoView;

public class TodoController {
    private final TodoView view;
    private final TodoRepository repository;

    public TodoController(TodoView view, TodoRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void initController() {
        view.onAdd(e -> handleAdd());
        view.onUpdate(e -> handleUpdate());
        view.onDelete(e -> handleDelete());
        view.onClear(e -> view.clearForm());
        view.onTableSelect(e -> handleTableSelect());
        refreshTable();
    }

    private void refreshTable() {
        view.showTodos(repository.getAll());
    }

    private void handleAdd() {
        String title = view.getTitleInput();
        String status = view.getStatusInput();

        if (title.isEmpty()) {
            view.showMessage("Task tidak boleh kosong");
            return;
        }

        repository.insert(new InsertTodoDTO(title, status));
        refreshTable();
        view.clearForm();
    }

    private void handleUpdate() {
        int selectedId = view.getSelectedTodoId();

        if (selectedId == -1) {
            view.showMessage("Pilih data terlebih dahulu");
            return;
        }

        TodoTask task = new TodoTask(
            selectedId,
            view.getTitleInput(),
            view.getStatusInput()
        );

        repository.update(task);
        refreshTable();
        view.clearForm();
    }

    private void handleDelete() {
        int selectedId = view.getSelectedTodoId();

        if (selectedId == -1) {
            view.showMessage("Pilih data terlebih dahulu");
            return;
        }

        repository.deleteById(selectedId);
        refreshTable();
        view.clearForm();
    }

    private void handleTableSelect() {
        int selectedId = view.getSelectedTodoId();

        if (selectedId == -1) {
            return;
        }

        TodoTask task = repository.getById(selectedId);

        if (task != null) {
            view.setForm(task);
        }
    }
}