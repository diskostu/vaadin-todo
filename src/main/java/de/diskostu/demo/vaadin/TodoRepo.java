package de.diskostu.demo.vaadin;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface TodoRepo extends JpaRepository<Todo, Long> {

    // Spring data convention: "deleteBy<fieldname>"
    @Transactional
    void deleteByDone(boolean b);
}