package org.camunda.bpm.java.lessons;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.cdi.jsf.TaskForm;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.io.IOException;

@Stateless
@Named
public class Logic {

  // Inject the entity manager
  @PersistenceContext
  private EntityManager entityManager;
  private TaskForm taskForm;

  public void persistQuestion(DelegateExecution delegateExpression) {
    // Create new order instance
    QEntity qEntity = new QEntity();

    // Get all process variables
    Map<String, Object> variables = delegateExpression.getVariables();

    // Set order attributes
    qEntity.setType((String) variables.get("type"));
    qEntity.setQuestion((String) variables.get("question"));
    qEntity.setAnswer((String) variables.get("answer"));

    /*
      Persist order instance and flush. After the flush the
      id of the order instance is set.
    */
    entityManager.persist(qEntity);
    entityManager.flush();

    // Remove no longer needed process variables
    delegateExpression.removeVariables(variables.keySet());

    // Add newly created order id as process variable
    delegateExpression.setVariable("questionId", qEntity.getId());
  }
  public QEntity getData(Long questionId) {
	    // Load order entity from database
	    return entityManager.find(QEntity.class, questionId);
	  }

	  /*
	    Merge updated order entity and complete task form in one transaction. This ensures
	    that both changes will rollback if an error occurs during transaction.
	   */
  public void mergeQuestionAndCompleteTask(QEntity qEntity) {
	    // Merge detached order entity with current persisted state
	    entityManager.merge(qEntity);
	    try {
	      // Complete user task from
	      taskForm.completeTask();
	    } catch (IOException e) {
	      // Rollback both transactions on error
	      throw new RuntimeException("Cannot complete task", e);
	    }
	  }
}