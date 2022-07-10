package com.pather.kafka.common;


import java.io.Serializable;
import lombok.Data;

@Data
public class ClickEvent implements Serializable {

  /**
   * 点击的连接
   */
  String clickUrl;
  /**
   * 目标图谱ID
   */
  String targetGraphId;
  /**
   * 发生点击事件的页面
   */
  String currentPageUrl;
}
