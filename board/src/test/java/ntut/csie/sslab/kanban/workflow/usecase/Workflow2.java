package ntut.csie.sslab.kanban.workflow.usecase;

import ntut.csie.sslab.ddd.model.AggregateRoot;

import java.util.Date;

public class Workflow2 extends AggregateRoot<String>  {

    private final String workflowId;
    private final String workflowName;
    private final String boardId;

    public Workflow2(String workflowId, String workflowName, String boardId) {
        super(workflowId);

        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.boardId = boardId;

        addDomainEvent(new Workflow2Created(workflowId, workflowName, boardId,new Date() ));
    }


    public String getWorkflowId() {
        return workflowId;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getBoardId() {
        return boardId;
    }
}
