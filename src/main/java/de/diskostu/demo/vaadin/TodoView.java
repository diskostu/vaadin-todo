package de.diskostu.demo.vaadin;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class TodoView extends VerticalLayout {

    private final TodoRepo repo;

    private final TextField taskField = new TextField();
    private final Button buttonAdd = new Button("Add");
    private final Button buttonClearCompleted = new Button("Clear completed tasks");
    private final VerticalLayout todosList = new VerticalLayout();


    // every class is a bean, so we can autowire the repo
    // normally, you would have to annotate such a parameter with @Autowire, but that's now optional
    public TodoView(@Autowired TodoRepo repo) {
        this.repo = repo;

        add(
                new H1("Important stuff:"),
                new HorizontalLayout(taskField, buttonAdd),
                todosList,
                buttonClearCompleted
        );

        refreshTodos();

        buttonAdd.addClickListener(e -> {
            repo.save(new Todo(taskField.getValue()));
            taskField.clear();
            taskField.focus();

            refreshTodos();
        });
        buttonAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonAdd.addClickShortcut(Key.ENTER);

        buttonClearCompleted.addClickListener(e -> {
            // short way: convention of Spring data
            repo.deleteByDone(true);

            // manuel way: getting all completed tasks from the repo, then deleting them
            //            final List<Todo> completedTasks = repo.findAll()
            //                                                  .stream()
            //                                                  .filter(Todo::isDone)
            //                                                  .collect(Collectors.toList());
            //            repo.deleteAll(completedTasks);

            // always don't forget to refresh
            refreshTodos();
        });
        buttonClearCompleted.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }


    private void refreshTodos() {
        todosList.removeAll();
        repo.findAll()
            .stream()
            .map(TodoLayout::new)
            .forEach(todosList::add);
    }


    class TodoLayout extends HorizontalLayout {
        final Checkbox done = new Checkbox();
        final TextField task = new TextField();
        final Button deleteButton = new Button("Delete");


        public TodoLayout(Todo todo) {
            add(done, task, deleteButton);
            setDefaultVerticalComponentAlignment(Alignment.CENTER);

            final Binder<Todo> binder = new Binder<>(Todo.class);
            binder.bindInstanceFields(this);
            binder.setBean(todo);

            binder.addValueChangeListener(e -> repo.save(binder.getBean()));

            deleteButton.addClickListener(e -> {
                repo.delete(binder.getBean());
                refreshTodos();
            });
        }
    }
}