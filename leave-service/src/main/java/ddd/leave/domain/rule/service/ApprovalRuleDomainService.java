package ddd.leave.domain.rule.service;

import ddd.leave.domain.rule.entity.ApprovalRule;
import ddd.leave.domain.rule.repository.facade.ApprovalRuleRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 提供请假审批规则查询
 */
@Service
public class ApprovalRuleDomainService {

    @Autowired
    private ApprovalRuleRepositoryInterface repositoryInterface;

    public int getLeaderMaxLevel(String personType, String leaveType, long duration) {
        ApprovalRule rule = new ApprovalRule();
        rule.setPersonType(personType);
        rule.setLeaveType(leaveType);
        rule.setDuration(duration);
        return repositoryInterface.getLeaderMaxLevel(rule);
    }
}
