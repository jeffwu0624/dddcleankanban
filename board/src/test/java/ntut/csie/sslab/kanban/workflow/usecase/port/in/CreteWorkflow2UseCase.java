package ntut.csie.sslab.kanban.workflow.usecase.port.in;

import ntut.csie.sslab.ddd.usecase.cqrs.CqrsOutput;
import ntut.csie.sslab.kanban.workflow.usecase.CreateWorkflow2Input;

public interface CreteWorkflow2UseCase {
    CqrsOutput execute(CreateWorkflow2Input input);
}
