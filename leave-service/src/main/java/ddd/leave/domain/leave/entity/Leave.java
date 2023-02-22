package ddd.leave.domain.leave.entity;

import ddd.leave.domain.leave.entity.valueobject.Applicant;
import ddd.leave.domain.leave.entity.valueobject.Approver;
import ddd.leave.domain.leave.entity.valueobject.LeaveType;
import ddd.leave.domain.leave.entity.valueobject.Status;
import lombok.Data;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请假单（聚合根）
 */
@Data
public class Leave {

    private String id;
    // 请假申请人(值对象)
    private Applicant applicant;
    // 审批人（值对象）
    private Approver approver;
    // 请假类型
    private LeaveType type;
    // 请假单状态
    private Status status;
    // 审批开始时间
    private Date startTime;
    // 审批结束时间
    private Date endTime;
    // 审批耗时
    private long duration;
    // 审批领导的最大级别
    private int leaderMaxLevel;
    // 当前节点审批信息
    private ApprovalInfo currentApprovalInfo;
    // 审批历史轨迹（值对象）
    private List<ApprovalInfo> historyApprovalInfos;

    /**
     * 获取审批时长
     * @return
     */
    public long getDuration() {
        return endTime.getTime() - startTime.getTime();
    }

    /**
     * 添加审批历史轨迹
     * @param approvalInfo
     * @return
     */
    public Leave addHistoryApprovalInfo(ApprovalInfo approvalInfo) {
        if (null == historyApprovalInfos)
            historyApprovalInfos = new ArrayList<>();
        this.historyApprovalInfos.add(approvalInfo);
        return this;
    }

    /**
     * 创建请假申请
     * @return
     */
    public Leave create(){
        this.setStatus(Status.APPROVING);
        this.setStartTime(new Date());
        return this;
    }

    /**
     * 审批通过
     * @param nextApprover
     * @return
     */
    public Leave agree(Approver nextApprover){
        this.setStatus(Status.APPROVING);
        this.setApprover(nextApprover);
        return this;
    }

    /**
     * 审批拒绝
     * @param approver
     * @return
     */
    public Leave reject(Approver approver){
        this.setApprover(approver);
        this.setStatus(Status.REJECTED);
        this.setApprover(null);
        return this;
    }

    /**
     * 审批结束
     * @return
     */
    public Leave finish(){
        this.setApprover(null);
        this.setStatus(Status.APPROVED);
        this.setEndTime(new Date());
        this.setDuration(this.getEndTime().getTime() - this.getStartTime().getTime());
        return this;
    }
}
