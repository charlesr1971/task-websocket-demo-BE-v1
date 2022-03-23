package cpp.context.work.management.proxy.taskservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cpp.context.work.management.proxy.taskservice.domain.repository.TaskDataStore;
import cpp.context.work.management.proxy.taskservice.domain.request.CreateTaskRequest;
import cpp.context.work.management.proxy.taskservice.domain.request.UpdateTaskRequest;
import cpp.context.work.management.proxy.taskservice.domain.response.CreateTaskResponse;
import cpp.context.work.management.proxy.taskservice.domain.response.DeleteTaskResponse;
import cpp.context.work.management.proxy.taskservice.domain.response.UpdateTaskResponse;
import cpp.context.work.management.proxy.taskservice.events.EventTrigger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author betrand.ugorji1
 */
@Stateless
public class TaskService {

    @Inject
    private EventTrigger eventTrigger;

    @Inject
    private TaskDataStore taskDataStore;

    public List<CreateTaskResponse> getTasks() {

        List<CreateTaskResponse> response = taskDataStore.getTasks();

        if (response == null || response.isEmpty()) {
            return new ArrayList<>();
        }

        return response;
    }

    public CreateTaskResponse getTaskById(String id) {
        return taskDataStore.getTaskById(id);
    }

    public DeleteTaskResponse deleteTasks() {

        taskDataStore.clear();
        triggerTaskEvent("Deleted");
        return new DeleteTaskResponse(null, "Deleted");
    }

    public DeleteTaskResponse deleteTask(String id) {

        CreateTaskResponse task = taskDataStore.getTaskById(id);

        if (task != null) {
            taskDataStore.deleteTask(task.getId());

            DeleteTaskResponse response = new DeleteTaskResponse(task.getId(), "Deleted");
            triggerTaskEvent(response);

            return response;
        } else {
            return null;
        }
    }

    public UpdateTaskResponse updateTask(CreateTaskRequest request) {

        CreateTaskResponse task;

        if (request.getId() == null) {
            task = createTask(request);
        } else {
            task = taskDataStore.getTaskById(request.getId());
            if (task != null) {
                task.setStatus(request.getStatus());
                task.setName(request.getName());
                task.setAssignee(request.getAssignee());
                task.setCreated(request.getCreated());
                task.setDue(request.getDue());
                task.setFollowUp(request.getFollowUp());
                task.setDelegationState(request.getDelegationState());
                task.setDescription(request.getDescription());
                task.setExecutionId(request.getExecutionId());
                task.setOwner(request.getOwner());
                task.setParentTaskId(request.getParentTaskId());
                task.setPriority(request.getPriority());
                task.setProcessDefinitionId(request.getProcessDefinitionId());
                task.setProcessInstanceId(request.getProcessInstanceId());
                task.setTaskDefinitionKey(request.getTaskDefinitionKey());
                task.setCaseExecutionId(request.getCaseExecutionId());
                task.setCaseInstanceId(request.getCaseInstanceId());
                task.setCaseDefinitionId(request.getCaseDefinitionId());
                task.setSuspended(request.isSuspended());
                task.setFormKey(request.getFormKey());
                task.setTenantId(request.getTenantId());
                taskDataStore.updateTask(task);
            } else {
                task = createTask(request);
            }
        }

        UpdateTaskResponse response = new UpdateTaskResponse(task.getId(), task.getStatus());
        triggerTaskEvent(response); // send update to listerners

        return response;

    }

    public UpdateTaskResponse updateTaskById(String taskId, UpdateTaskRequest request) {

        CreateTaskResponse task = taskDataStore.getTaskById(taskId);
        if (task != null) {
            task.setStatus(request.getStatus());
            task.setName(request.getName());
            task.setAssignee(request.getAssignee());
            task.setCreated(request.getCreated());
            task.setDue(request.getDue());
            task.setFollowUp(request.getFollowUp());
            task.setDelegationState(request.getDelegationState());
            task.setDescription(request.getDescription());
            task.setExecutionId(request.getExecutionId());
            task.setOwner(request.getOwner());
            task.setParentTaskId(request.getParentTaskId());
            task.setPriority(request.getPriority());
            task.setProcessDefinitionId(request.getProcessDefinitionId());
            task.setProcessInstanceId(request.getProcessInstanceId());
            task.setTaskDefinitionKey(request.getTaskDefinitionKey());
            task.setCaseExecutionId(request.getCaseExecutionId());
            task.setCaseInstanceId(request.getCaseInstanceId());
            task.setCaseDefinitionId(request.getCaseDefinitionId());
            task.setSuspended(request.isSuspended());
            task.setFormKey(request.getFormKey());
            task.setTenantId(request.getTenantId());
            taskDataStore.updateTask(task);

            UpdateTaskResponse response = new UpdateTaskResponse(request.getId(), request.getStatus());
            triggerTaskEvent(response); // send update to listerners

            return response;
        } else {
            return null;
        }
    }

    public CreateTaskResponse createTask(CreateTaskRequest request) {

        CreateTaskResponse response = buildTask(request);

        taskDataStore.createTask(response);
        triggerTaskEvent(response); // send update to listerners

        return response;
    }

    private void triggerTaskEvent(Object response) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

            String task;

            if (response instanceof DeleteTaskResponse) {
                task = objectWriter.writeValueAsString((DeleteTaskResponse) response);
            } else if (response instanceof UpdateTaskResponse) {
                task = objectWriter.writeValueAsString((UpdateTaskResponse) response);
            } else if (response instanceof CreateTaskResponse) {
                task = objectWriter.writeValueAsString((CreateTaskResponse) response);
            } else {
                task = (String) response;
            }

            eventTrigger.fireTaskEvent(task);

        } catch (JsonProcessingException jpe) {
            final String errorMessage = "@TaskService triggerTaskEvent: Could not convert task response to Json String";
            java.util.logging.Logger.getLogger(TaskService.class.getName()).log(Level.SEVERE, null, errorMessage);
        }
    }

    private CreateTaskResponse buildTask(CreateTaskRequest request) {
        CreateTaskResponse ctr = new CreateTaskResponse();
        ctr.setId(UUID.randomUUID().toString());
        ctr.setName(request.getName());
        ctr.setAssignee(request.getAssignee());
        ctr.setCreated(request.getCreated());
        ctr.setDue(request.getDue());
        ctr.setFollowUp(request.getFollowUp());
        ctr.setDelegationState(request.getDelegationState());
        ctr.setDescription(request.getDescription());
        ctr.setExecutionId(request.getExecutionId());
        ctr.setOwner(request.getOwner());
        ctr.setParentTaskId(request.getParentTaskId());
        ctr.setPriority(request.getPriority());
        ctr.setProcessDefinitionId(request.getProcessDefinitionId());
        ctr.setProcessInstanceId(request.getProcessInstanceId());
        ctr.setTaskDefinitionKey(request.getTaskDefinitionKey());
        ctr.setCaseExecutionId(request.getCaseExecutionId());
        ctr.setCaseInstanceId(request.getCaseInstanceId());
        ctr.setCaseDefinitionId(request.getCaseDefinitionId());
        ctr.setSuspended(request.isSuspended());
        ctr.setFormKey(request.getFormKey());
        ctr.setTenantId(request.getTenantId());
        return ctr;
    }

}
