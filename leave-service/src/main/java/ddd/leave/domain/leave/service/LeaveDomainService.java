package ddd.leave.domain.leave.service;

import ddd.leave.domain.leave.entity.Leave;
import ddd.leave.domain.leave.entity.valueobject.ApprovalType;
import ddd.leave.domain.leave.entity.valueobject.Approver;
import ddd.leave.domain.leave.event.LeaveEvent;
import ddd.leave.domain.leave.event.LeaveEventType;
import ddd.leave.domain.leave.repository.facade.LeaveRepositoryInterface;
import ddd.leave.domain.leave.repository.po.LeavePO;
import ddd.leave.infrastructure.common.event.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 完成请假申请和审核核心逻辑
 */
@Service
@Slf4j
public class LeaveDomainService {

    // 用领域事件实现数据的最终一致性
    @Autowired
    EventPublisher eventPublisher;

    // 用仓储模式实现领域层与基础层的依赖倒置
    @Autowired
    LeaveRepositoryInterface leaveRepositoryInterface;

    // 用工厂模式实现复杂聚合的实体数据初始化
    @Autowired
    LeaveFactory leaveFactory;

    /**
     * 创建请假单
     * @param leave
     * @param leaderMaxLevel
     * @param approver
     */
    @Transactional
    public void createLeave(Leave leave, int leaderMaxLevel, Approver approver) {
        leave.setLeaderMaxLevel(leaderMaxLevel);
        leave.setApprover(approver);
        leave.create();
        // 1. 保存 leave
        leaveRepositoryInterface.save(leaveFactory.createLeavePO(leave));
        LeaveEvent event = LeaveEvent.create(LeaveEventType.CREATE_EVENT, leave);
        // 2. 保存事件
        leaveRepositoryInterface.saveEvent(leaveFactory.createLeaveEventPO(event));
        // 3. 发布事件
        eventPublisher.publish(event);
    }

    /**
     * 更新请假信息
     * @param leave
     */
    @Transactional
    public void updateLeaveInfo(Leave leave) {
        LeavePO po = leaveRepositoryInterface.findById(leave.getId());
        if (null == po) {
            throw new RuntimeException("leave does not exist");
        }
        leaveRepositoryInterface.save(leaveFactory.createLeavePO(leave));
    }

    /**
     * 请假提交审批事件
     * @param leave
     * @param approver
     */
    @Transactional
    public void submitApproval(Leave leave, Approver approver) {
        LeaveEvent event;
        // 1. 执行业务逻辑，产生领域事件
        if ( ApprovalType.REJECT == leave.getCurrentApprovalInfo().getApprovalType()) {
            // reject, then the leave is finished with REJECTED status
            leave.reject(approver);
            event = LeaveEvent.create(LeaveEventType.REJECT_EVENT, leave);
        } else {
            if (approver != null) {
                // agree and has next approver
                leave.agree(approver);
                event = LeaveEvent.create(LeaveEventType.AGREE_EVENT, leave);
            } else {
                // agree and hasn't next approver, then the leave is finished with APPROVED status
                leave.finish();
                event = LeaveEvent.create(LeaveEventType.APPROVED_EVENT, leave);
            }
        }
        leave.addHistoryApprovalInfo(leave.getCurrentApprovalInfo());

        // 2. 完成业务数据持久化
        leaveRepositoryInterface.save(leaveFactory.createLeavePO(leave));
        // 3. 完成事件数据持久化
        leaveRepositoryInterface.saveEvent(leaveFactory.createLeaveEventPO(event));
        // 4. 完成领域事件发布
        eventPublisher.publish(event);
    }

    /**
     * 获取请假信息
     * @param leaveId
     * @return
     */
    public Leave getLeaveInfo(String leaveId) {
        LeavePO leavePO = leaveRepositoryInterface.findById(leaveId);
        return leaveFactory.getLeave(leavePO);
    }

    /**
     * 根据申请人 id 获取请假申请列表
     * @param applicantId
     * @return
     */
    public List<Leave> queryLeaveInfosByApplicant(String applicantId) {
        List<LeavePO> leavePOList = leaveRepositoryInterface.queryByApplicantId(applicantId);
        return leavePOList.stream()
                .map(leavePO -> leaveFactory.getLeave(leavePO))
                .collect(Collectors.toList());
    }

    /**
     * 根据审批人 id 获取请假申请列表
     * @param approverId
     * @return
     */
    public List<Leave> queryLeaveInfosByApprover(String approverId) {
        List<LeavePO> leavePOList = leaveRepositoryInterface.queryByApproverId(approverId);
        return leavePOList.stream()
                .map(leavePO -> leaveFactory.getLeave(leavePO))
                .collect(Collectors.toList());
    }
}