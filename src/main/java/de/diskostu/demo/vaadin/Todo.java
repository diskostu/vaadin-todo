package de.diskostu.demo.vaadin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String task;
    private boolean done;


    public Todo() {
        this.task = "default task";
    }


    public Todo(String taskname) {
        this.task = taskname;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long aId) {
        this.id = aId;
    }


    public String getTask() {
        return task;
    }


    public void setTask(String aTask) {
        this.task = aTask;
    }


    public boolean isDone() {
        return done;
    }


    public void setDone(boolean aDone) {
        this.done = aDone;
    }
}