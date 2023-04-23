package ntut.csie.sslab.kanban.workflow.usecase.service;

import ntut.csie.sslab.ddd.usecase.DomainEventBus;
import ntut.csie.sslab.ddd.usecase.cqrs.CqrsOutput;
import ntut.csie.sslab.kanban.workflow.usecase.CreateWorkflow2Input;
import ntut.csie.sslab.kanban.workflow.usecase.port.in.CreteWorkflow2UseCase;
import ntut.csie.sslab.kanban.workflow.usecase.Workflow2;
import ntut.csie.sslab.kanban.workflow.usecase.Workflow2Repository;

public class CreteWorkflow2UseCaseService implements CreteWorkflow2UseCase {
    private final Workflow2Repository repo;
    private final DomainEventBus domainEventBus;

    public CreteWorkflow2UseCaseService(Workflow2Repository repo, DomainEventBus domainEventBus) {
        this.repo = repo;
        this.domainEventBus = domainEventBus;
    }

    @Override
    public CqrsOutput execute(CreateWorkflow2Input input) {
        Workflow2 workflow2 = new Workflow2(input.getWorkflowId(), input.getWorkflowName(), input.getBoardId());
        repo.save(workflow2);

        domainEventBus.postAll(workflow2);

        CqrsOutput<?> output = CqrsOutput.create();
        output.setId(input.getWorkflowId());
        return output;
    }
}
