package ntut.csie.sslab.kanban.workflow.usecase;

import com.google.common.eventbus.Subscribe;
import ntut.csie.sslab.ddd.adapter.gateway.GoogleEventBusAdapter;
import ntut.csie.sslab.ddd.usecase.DomainEventBus;
import ntut.csie.sslab.ddd.usecase.cqrs.CqrsOutput;
import ntut.csie.sslab.kanban.workflow.adapter.out.InMemoryWorkflow2Repository;
import ntut.csie.sslab.kanban.workflow.usecase.port.in.CreteWorkflow2UseCase;
import ntut.csie.sslab.kanban.workflow.usecase.service.CreteWorkflow2UseCaseService;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateCreteWorkflow2UseCaseTest {

    @Test
    public void create_workflow_use_case() {
        DomainEventBus domainEventBus = new GoogleEventBusAdapter();
        FakeEventHandler eventHandler = new FakeEventHandler();
        domainEventBus.register(eventHandler);

        CreateWorkflow2Input input = new CreateWorkflow2Input();
        input.setBoardId("boardId");
        input.setWorkflowId("workId");
        input.setWorkflowName("workflowName");

        Workflow2Repository repo = new InMemoryWorkflow2Repository();

        CreteWorkflow2UseCase useCase = new CreteWorkflow2UseCaseService(
                repo,
                domainEventBus);

        CqrsOutput output = useCase.execute(input);

        assertNotNull(output.getId());
        assertTrue(repo.findById(output.getId()).isPresent());
        assertEquals(1,eventHandler.notifyCount);
    }
}

class FakeEventHandler {
    public int notifyCount = 0;

    @Subscribe
    public void whenWorkflowCreated(Workflow2Created event) {
        notifyCount++;
    }
}
