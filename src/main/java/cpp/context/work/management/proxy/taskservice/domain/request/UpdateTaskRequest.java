package cpp.context.work.management.proxy.taskservice.domain.request;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author bu
 */
public class UpdateTaskRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String id;
    
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id);
        hash = 19 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UpdateTaskRequest other = (UpdateTaskRequest) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.status, other.status);
    }
    
    
}
