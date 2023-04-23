package ntut.csie.sslab.kanban.workflow.usecase;

import java.util.Optional;

public interface Workflow2Repository {
    Optional<Workflow2> findById(String id);

    void save(Workflow2 workflow2);
}
