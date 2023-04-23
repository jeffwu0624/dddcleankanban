package ntut.csie.sslab.kanban.workflow.adapter.out;

import ntut.csie.sslab.kanban.workflow.usecase.Workflow2;
import ntut.csie.sslab.kanban.workflow.usecase.Workflow2Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryWorkflow2Repository implements Workflow2Repository {

    private List<Workflow2> store = new ArrayList<>();

    @Override
    public Optional<Workflow2> findById(String id) {
        return store.stream().filter(workflow -> workflow.getId().equals(id)).findFirst();
    }

    @Override
    public void save(Workflow2 workflow2) {
        store.add(workflow2);
    }
}
