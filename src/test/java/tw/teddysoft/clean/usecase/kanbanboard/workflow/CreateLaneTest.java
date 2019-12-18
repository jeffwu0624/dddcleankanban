package tw.teddysoft.clean.usecase.kanbanboard.workflow;

import org.junit.Before;
import org.junit.Test;
import tw.teddysoft.clean.adapter.gateway.kanbanboard.InMemoryWorkflowRepository;
import tw.teddysoft.clean.adapter.presenter.kanbanboard.lane.SingleStagePresenter;
import tw.teddysoft.clean.adapter.presenter.kanbanboard.workflow.SingleWorkflowPresenter;
import tw.teddysoft.clean.domain.model.kanbanboard.workflow.Lane;
import tw.teddysoft.clean.domain.model.kanbanboard.workflow.LaneOrientation;
import tw.teddysoft.clean.domain.model.kanbanboard.workflow.Workflow;
import tw.teddysoft.clean.usecase.lane.stage.create.CreateStageUseCase;
import tw.teddysoft.clean.usecase.kanbanboard.workflow.create.CreateWorkflowInput;
import tw.teddysoft.clean.usecase.kanbanboard.workflow.create.CreateWorkflowOutput;
import tw.teddysoft.clean.usecase.kanbanboard.workflow.create.CreateWorkflowUseCase;
import tw.teddysoft.clean.usecase.kanbanboard.workflow.create.impl.CreateWorkflowUseCaseImpl;
import tw.teddysoft.clean.usecase.lane.stage.create.CreateStageInput;
import tw.teddysoft.clean.usecase.lane.stage.create.CreateStageOutput;
import tw.teddysoft.clean.usecase.lane.stage.create.impl.CreateStageUseCaseImpl;
import tw.teddysoft.clean.usecase.lane.ministage.create.CreateMinistageInput;
import tw.teddysoft.clean.usecase.lane.ministage.create.CreateMiniStageLaneOutput;
import tw.teddysoft.clean.usecase.lane.ministage.create.CreateMiniStageLaneUseCase;
import tw.teddysoft.clean.usecase.lane.ministage.create.impl.CreateMiniStageLaneUseCaseImpl;
import tw.teddysoft.clean.usecase.lane.swimlane.create.CreateSwimlaneInput;
import tw.teddysoft.clean.usecase.lane.swimlane.create.CreateSwimlaneOutput;
import tw.teddysoft.clean.usecase.lane.swimlane.create.CreateSwimlaneUseCase;
import tw.teddysoft.clean.usecase.lane.swimlane.create.impl.CreateSwimlaneUseCaseImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateLaneTest {

    private WorkflowRepository repository;
    private Workflow workflow;

    @Before
    public void setUp(){
        repository = new InMemoryWorkflowRepository();
        create_default_workflow();
        workflow = repository.findFirstByName("Default");
        assertNotNull(workflow);
    }

    @Test
    public void create_stagelane(){
        create_stagelane(workflow.getId(), "Backlog");
        assertEquals(1, workflow.getStages().size());
        assertEquals(LaneOrientation.VERTICAL, workflow.getStages().get(0).getOrientation());
        assertEquals("Backlog", workflow.getStages().get(0).getTitle());
    }


    @Test
    public void create_two_ministagelanes_under_the_stagelane_Backlog(){
        create_stagelane(workflow.getId(), "Backlog");
        Lane backlog = workflow.getStages().get(0);

        create_ministagelane(workflow.getId(), "Legend", backlog.getId());
        assertEquals(1, backlog.getSubLanes().size());
        assertEquals(LaneOrientation.VERTICAL, backlog.getSubLanes().get(0).getOrientation());
        assertEquals("Legend", backlog.getSubLanes().get(0).getTitle());

        create_ministagelane(workflow.getId(), "Ready", backlog.getId());
        assertEquals(LaneOrientation.VERTICAL, backlog.getSubLanes().get(1).getOrientation());
        assertEquals("Ready", backlog.getSubLanes().get(1).getTitle());
    }

    @Test
    public void create_two_swimlanes_under_the_stagelane_Backlog(){
        create_stagelane(workflow.getId(), "Backlog");
        Lane backlog = workflow.getStages().get(0);

        create_swimlane(workflow.getId(), "Top5", backlog.getId());
        assertEquals(1, backlog.getSubLanes().size());
        assertEquals(LaneOrientation.HORIZONTAL, backlog.getSubLanes().get(0).getOrientation());
        assertEquals("Top5", backlog.getSubLanes().get(0).getTitle());


        create_swimlane(workflow.getId(), "Idea", backlog.getId());
        assertEquals(2, backlog.getSubLanes().size());
        assertEquals(LaneOrientation.HORIZONTAL, backlog.getSubLanes().get(1).getOrientation());
        assertEquals("Idea", backlog.getSubLanes().get(1).getTitle());
    }



    @Test
    public void create_business_process_maintenance_stagelane(){
        // create a stage like : https://reurl.cc/1QEryG

        create_stagelane(workflow.getId(), "Operations - Business Process Maintenance");
        Lane operation = workflow.getStages().get(0);

        String productionProblemId = create_swimlane(workflow.getId(), "Production Problem", operation.getId());
            create_ministagelane(workflow.getId(), "New", productionProblemId);
            String workingID = create_ministagelane(workflow.getId(), "Working", productionProblemId);
                create_ministagelane(workflow.getId(), "Find Cause", workingID);
                create_ministagelane(workflow.getId(), "Fix Cause", workingID);
            create_ministagelane(workflow.getId(), "Done", productionProblemId);

        String plannedBusinessNeedId = create_swimlane(workflow.getId(), "Planned Business Need", operation.getId());
            String due2Months = create_ministagelane(workflow.getId(), "Due 2 months", plannedBusinessNeedId);
                create_swimlane(workflow.getId(), "High Impact", due2Months);
                create_swimlane(workflow.getId(), "Low Impact", due2Months);
            String due1Month = create_ministagelane(workflow.getId(), "Due 1 month", plannedBusinessNeedId);
                create_swimlane(workflow.getId(), "High Impact", due1Month);
                create_swimlane(workflow.getId(), "Low Impact", due1Month);
            String due1week = create_ministagelane(workflow.getId(), "Due 1 week", plannedBusinessNeedId);
                create_swimlane(workflow.getId(), "High Impact", due1week);
                create_swimlane(workflow.getId(), "Low Impact", due1week);
            String inWork = create_ministagelane(workflow.getId(), "In Work", plannedBusinessNeedId);
                create_swimlane(workflow.getId(), "High Impact", inWork);
                create_swimlane(workflow.getId(), "Low Impact", inWork);
            String done = create_ministagelane(workflow.getId(), "Done", plannedBusinessNeedId);
                create_swimlane(workflow.getId(), "High Impact", done);
                create_swimlane(workflow.getId(), "Low Impact", done);

        String routineId = create_swimlane(workflow.getId(), "Routine", operation.getId());

        String unplannedId = create_swimlane(workflow.getId(), "Unplanned", operation.getId());
            create_ministagelane(workflow.getId(), "New", unplannedId);
            create_ministagelane(workflow.getId(), "Committed", unplannedId);
            create_ministagelane(workflow.getId(), "In Work", unplannedId);
            create_ministagelane(workflow.getId(), "Test", unplannedId);
            create_ministagelane(workflow.getId(), "Done", unplannedId);

        String platformImprovementsId = create_swimlane(workflow.getId(), "Platform Improvements", operation.getId());
            create_ministagelane(workflow.getId(), "New", platformImprovementsId);
            create_ministagelane(workflow.getId(), "Committed", platformImprovementsId);
            create_ministagelane(workflow.getId(), "In Work", platformImprovementsId);
            create_ministagelane(workflow.getId(), "Test", platformImprovementsId);
            create_ministagelane(workflow.getId(), "Done", platformImprovementsId);

        workflow.dumpLane();
        assertEquals(36, workflow.getTotalLaneSize());
    }

    @Test
    public void create_devops_workflow(){
        // create a stage like : https://reurl.cc/31W81L ,
        //                       https://reurl.cc/L1gvWL , and
        //                       https://reurl.cc/algpzG

        String devQueueId = create_stagelane(workflow.getId(), "Dev Queue");
            String helpId = create_ministagelane(workflow.getId(), "Help", devQueueId);
                create_swimlane(workflow.getId(), "Legend", helpId);
                create_swimlane(workflow.getId(), "Templates", helpId);
            create_ministagelane(workflow.getId(), "Backlog", devQueueId);
            create_ministagelane(workflow.getId(), "Up Next", devQueueId);

        String devInFightId = create_stagelane(workflow.getId(), "Dev - In Flight");
            String epicInFlightId = create_swimlane(workflow.getId(), "Epic - In Flight", devInFightId);
                create_ministagelane(workflow.getId(), "Breakdown", epicInFlightId);
                create_ministagelane(workflow.getId(), "Doing", epicInFlightId);
                create_ministagelane(workflow.getId(), "Pending Ops", epicInFlightId);

            String expeditedId = create_swimlane(workflow.getId(), "Expedited", devInFightId);
                create_ministagelane(workflow.getId(), "Doing", expeditedId);
                create_ministagelane(workflow.getId(), "Testing", expeditedId);
                create_ministagelane(workflow.getId(), "Review", expeditedId);

            String standardId = create_swimlane(workflow.getId(), "Standard", devInFightId);
                create_ministagelane(workflow.getId(), "Doing", standardId);
                create_ministagelane(workflow.getId(), "Testing", standardId);
                create_ministagelane(workflow.getId(), "Review", standardId);

        String operationsQueue = create_stagelane(workflow.getId(), "Operations Queue");
            String help = create_ministagelane(workflow.getId(), "Help", operationsQueue);
                create_swimlane(workflow.getId(), "Legend", help);
                 create_swimlane(workflow.getId(), "Templates", help);
            create_ministagelane(workflow.getId(), "Ops Backlog", operationsQueue);

        String opsInFlight = create_stagelane(workflow.getId(), "Ops - In Flight");
            create_ministagelane(workflow.getId(), "Up Next", opsInFlight);
        String doingId = create_ministagelane(workflow.getId(), "Doing", opsInFlight);
            String keepTheLightsOnId = create_swimlane(workflow.getId(), "Keep The Lights On", doingId);
                create_ministagelane(workflow.getId(), "Doing", keepTheLightsOnId);
                create_ministagelane(workflow.getId(), "Testing", keepTheLightsOnId);
        String expeditedId2 = create_swimlane(workflow.getId(), "Expedited", doingId);
            create_ministagelane(workflow.getId(), "Doing", expeditedId2);
            create_ministagelane(workflow.getId(), "Testing", expeditedId2);
        String standardId2 = create_swimlane(workflow.getId(), "Standard", doingId);
            create_ministagelane(workflow.getId(), "Doing", standardId2);
            create_ministagelane(workflow.getId(), "Testing", standardId2);

        create_stagelane(workflow.getId(), "Completed");

        String finishedId = create_stagelane(workflow.getId(), "Finished - Ready to Archive");
            create_swimlane(workflow.getId(), "Finished As Planned", finishedId);
            create_swimlane(workflow.getId(), "Started bu not Finished", finishedId);
            create_swimlane(workflow.getId(), "Discarded Requests / Ideas", finishedId);

        workflow.dumpLane();
        assertEquals(41, workflow.getTotalLaneSize());
    }




    private void create_default_workflow() {
        CreateWorkflowUseCase createWorkflowUC = new CreateWorkflowUseCaseImpl(repository);

        CreateWorkflowInput input = CreateWorkflowUseCaseImpl.createInput();
        CreateWorkflowOutput output = new SingleWorkflowPresenter();
        input.setBoardId("000-1234");
        input.setWorkflowName("Default");

        createWorkflowUC.execute(input, output);
        workflow = repository.findFirstByName("Default");
        assertNotNull(workflow);
    }

    private String create_stagelane(String workflowId, String stageName){
        CreateStageUseCase createStageLaneUC = new CreateStageUseCaseImpl(repository);
        CreateStageInput input = CreateStageUseCaseImpl.createInput();
        CreateStageOutput output = new SingleStagePresenter();
        input.setWorkflowId(workflowId);
        input.setTitle(stageName);

        createStageLaneUC.execute(input, output);

        return output.getId();
    }

    private String create_ministagelane(String workflowId, String LaneName, String parentId){

        CreateMiniStageLaneUseCase createMiniStageLaneUC = new CreateMiniStageLaneUseCaseImpl(repository);
        CreateMinistageInput input = CreateMiniStageLaneUseCaseImpl.createInput();
        CreateMiniStageLaneOutput output = new SingleStagePresenter();
        input.setWorkflowId(workflowId);
        input.setParentId(parentId);
        input.setTitle(LaneName);

        createMiniStageLaneUC.execute(input, output);

        return output.getId();
    }

    private String create_swimlane(String workflowId, String LaneName, String parentId){

        CreateSwimlaneUseCase createSwimLaneUC = new CreateSwimlaneUseCaseImpl(repository);

        CreateSwimlaneInput input = CreateSwimlaneUseCaseImpl.createInput();
        CreateSwimlaneOutput output = new SingleStagePresenter();

        input.setWorkflowId(workflowId);
        input.setParentId(parentId);
        input.setTitle(LaneName);

        createSwimLaneUC.execute(input, output);

        return output.getId();
    }

}