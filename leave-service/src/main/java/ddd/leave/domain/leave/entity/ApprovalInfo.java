package ddd.leave.domain.leave.entity;

import ddd.leave.domain.leave.entity.valueobject.ApprovalType;
import ddd.leave.domain.leave.entity.valueobject.Approver;
import lombok.Data;

/**
 * 审批意见实体
 */
@Data
public class ApprovalInfo {

    String approvalInfoId;
    // 审批人
    Approver approver;
    // 审批状态类型
    ApprovalType approvalType;
    // 审批意见
    String msg;
    long time;

}
