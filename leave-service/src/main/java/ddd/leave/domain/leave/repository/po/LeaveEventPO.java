package ddd.leave.domain.leave.repository.po;

import ddd.leave.domain.leave.event.LeaveEventType;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class LeaveEventPO {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "uuid") //这个是hibernate的注解/生成32位UUID
    @GeneratedValue(generator = "idGenerator")
    private int id;
    @Enumerated(EnumType.STRING)
    private LeaveEventType leaveEventType;
    private Date timestamp;
    private String source;
    private String data;
}
