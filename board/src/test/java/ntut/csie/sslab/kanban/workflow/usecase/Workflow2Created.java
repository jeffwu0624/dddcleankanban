package ntut.csie.sslab.kanban.workflow.usecase;

import ntut.csie.sslab.ddd.model.DomainEvent;

import java.util.Date;

public class Workflow2Created extends DomainEvent {
    private final String workflowId;
    private final String workflowName;
    private final String boardId;

    public Workflow2Created(String workflowId, String workflowName, String boardId, Date date) {
        super(date);
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.boardId = boardId;
    }
}
