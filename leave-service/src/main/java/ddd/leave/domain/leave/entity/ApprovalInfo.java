package ddd.leave.domain.leave.entity;

import ddd.leave.domain.leave.entity.valueobject.ApprovalType;
import ddd.leave.domain.leave.entity.valueobject.Approver;
import lombok.Data;

/**
 * 审批意见实体
 * @author karson
 */
@Data
public class ApprovalInfo {

    private String approvalInfoId;

    // 审批人（值对象）
    private Approver approver;

    // 审批状态类型
    private ApprovalType approvalType;

    // 审批意见
    private String msg;

    private long time;

}
