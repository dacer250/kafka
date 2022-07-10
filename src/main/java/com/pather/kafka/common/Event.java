package com.pather.kafka.common;

import java.io.Serializable;
import java.util.Date;
import lombok.*;

/**
 * @author Eric Tseng
 * @description Event
 * @since 2022/4/16 17:35
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
 public  class Event<T>  implements Serializable {
    private Integer userId;
    private String eventType;
    T eventInfo;
    Date eventTime;
}
