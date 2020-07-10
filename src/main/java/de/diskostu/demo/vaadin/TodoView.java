package de.diskostu.demo.vaadin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class TodoView extends VerticalLayout {

    private final TodoRepo repo;

    private final TextField taskField = new TextField();
    private final Button addButton = new Button("Add");
    private final VerticalLayout todosList = new VerticalLayout();


    // every class is a bean, so we can autowire the repo
    // normally, you would have to annotate such a parameter with @Autowire, but that's now optional
    public TodoView(@Autowired TodoRepo repo) {
        this.repo = repo;

        add(
                new H1("Important stuff:"),
                new HorizontalLayout(taskField, addButton),
                todosList
        );

        refreshTodos();

        addButton.addClickListener(e -> {
            repo.save(new Todo(taskField.getValue()));
            taskField.clear();
            taskField.focus();

            refreshTodos();
        });
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


        public TodoLayout(Todo todo) {
            add(done, task);
            setDefaultVerticalComponentAlignment(Alignment.CENTER);

            final Binder<Todo> binder = new Binder<>(Todo.class);
            binder.bindInstanceFields(this);
            binder.setBean(todo);

            binder.addValueChangeListener(e -> repo.save(binder.getBean()));
        }
    }
}