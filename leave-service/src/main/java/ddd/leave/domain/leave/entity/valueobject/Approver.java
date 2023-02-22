package ddd.leave.domain.leave.entity.valueobject;

import ddd.leave.domain.person.entity.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审批人
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Approver {

    private String personId;
    private String personName;
    // 管理级别
    private int level;

    public static Approver fromPerson(Person person){
        Approver approver = new Approver();
        approver.setPersonId(person.getPersonId());
        approver.setPersonName(person.getPersonName());
        approver.setLevel(person.getRoleLevel());
        return approver;
    }

}
