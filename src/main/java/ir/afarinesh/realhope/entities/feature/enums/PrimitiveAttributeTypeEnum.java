package ir.afarinesh.realhope.entities.feature.enums;

public enum PrimitiveAttributeTypeEnum {
    String, Integer, Long, Boolean, JavaDate, Enum;

    public String angular() {
        switch (this) {
            case JavaDate: {
                return "JavaDate";
            }
            case Long:
            case Integer: {
                return "number";
            }
            case String: {
                return "string";
            }
            case Boolean: {
                return "boolean";
            }
        }
        return this.toString();
    }
}
