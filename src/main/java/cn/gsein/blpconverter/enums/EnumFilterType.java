package cn.gsein.blpconverter.enums;

/**
 * 使用滤镜的方式
 *
 * @author G. Seinfeld
 * @date 2019/08/27
 */
public enum EnumFilterType {
    /**
     * 不使用滤镜
     */
    NONE(0),
    /**
     * 默认滤镜
     */
    DEFAULT(1),
    /**
     * 自定义滤镜
     */
    SELF_DEFINED(2);

    private final int value;

    EnumFilterType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
