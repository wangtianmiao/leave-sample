package ddd.leave.infrastructure.common.event;

import lombok.Data;

import java.util.Date;

@Data
public class DomainEvent {

    // 事件 ID
    private String id;
    // 事件时间戳
    private Date timestamp;
    // 事件源
    private String source;
    // 事件相关的业务数据
    private String data;
}
