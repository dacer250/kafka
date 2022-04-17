package com.vi.kafka.common;

import lombok.*;

/**
 * @author Eric Tseng
 * @description Book
 * @since 2022/4/16 17:35
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Book {
    private String tag;
    private String author;
    private String name;
}
