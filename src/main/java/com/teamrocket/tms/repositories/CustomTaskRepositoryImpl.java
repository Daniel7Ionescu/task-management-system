package com.teamrocket.tms.repositories;

import com.teamrocket.tms.models.entities.Task;
import com.teamrocket.tms.utils.enums.Priority;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomTaskRepositoryImpl implements CustomTaskRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Task> findFilteredTasks(Long userId, LocalDate dueDate, Priority priority) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> cq = cb.createQuery(Task.class);

        Root<Task> task = cq.from(Task.class);
        List<Predicate> predicates = new ArrayList<>();

        if (userId != null) {
            predicates.add(cb.equal(task.get("id"), userId));
        }
        if (dueDate != null) {
            predicates.add(cb.equal(task.get("due_date"), dueDate));
        }
        if (userId != null) {
            predicates.add(cb.equal(task.get("priority"), priority));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}
