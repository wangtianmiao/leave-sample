package ddd.leave.domain.leave.repository.po;

import ddd.leave.domain.leave.entity.Leave;
import ddd.leave.domain.leave.entity.valueobject.LeaveType;
import ddd.leave.domain.leave.entity.valueobject.Status;
import ddd.leave.domain.person.entity.valueobject.PersonType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Leave")
@Data
public class LeavePO {

    @Id
    @GenericGenerator(name="idGenerator", strategy="uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator="idGenerator")
    private String id;
    private String applicantId;
    private String applicantName;
    @Enumerated(EnumType.STRING)
    private PersonType applicantType;
    private String approverId;
    private String approverName;
    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Date startTime;
    private Date endTime;
    private long duration;
    @Transient
    private List<ApprovalInfoPO> historyApprovalInfoPOList;

    public Leave toLeave() {
        return new Leave();
    }

}
