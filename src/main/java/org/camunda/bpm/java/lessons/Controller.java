package org.camunda.bpm.java.lessons;

import org.camunda.bpm.engine.cdi.BusinessProcess;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;

@Named
@ConversationScoped
public class Controller implements Serializable {

  private static  final long serialVersionUID = 1L;

  // Inject the BusinessProcess to access the process variables
  @Inject
  private BusinessProcess businessProcess;

  // Inject the EntityManager to access the persisted order
  @SuppressWarnings("unused")
@PersistenceContext
  private EntityManager entityManager;

  // Inject the OrderBusinessLogic to update the persisted order
  @Inject
  private Logic Logic;

  // Caches the OrderEntity during the conversation
  private QEntity QEntity;

  public QEntity getQEntity() {
    if (QEntity == null) {
      // Load the order entity from the database if not already cached
      QEntity = Logic.getData((Long) businessProcess.getVariable("questionId"));
    }
    return QEntity;
  }

  public void submitForm() throws IOException {
    // Persist updated order entity and complete task form
    Logic.mergeOrderAndCompleteTask(QEntity);
  }
}
