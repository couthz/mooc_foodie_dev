package com.zhc.enums;

/**
 * @Desc: 性别 枚举
 */
public enum CommentLevel {
    positive(1, "好评"),
    middle(2, "中评"),
    negative(3, "差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
