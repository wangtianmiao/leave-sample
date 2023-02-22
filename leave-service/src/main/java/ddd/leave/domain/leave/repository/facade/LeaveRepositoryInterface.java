package ddd.leave.domain.leave.repository.facade;

import ddd.leave.domain.leave.repository.po.LeaveEventPO;
import ddd.leave.domain.leave.repository.po.LeavePO;

import java.util.List;

/**
 * 仓储接口
 * 面向领域服务提供接口，解耦业务逻辑和基础资源（实现依赖倒置）
 */
public interface LeaveRepositoryInterface {

    void save(LeavePO leavePO);

    void saveEvent(LeaveEventPO leaveEventPO);

    LeavePO findById(String id);

    List<LeavePO> queryByApplicantId(String applicantId);

    List<LeavePO> queryByApproverId(String approverId);

}