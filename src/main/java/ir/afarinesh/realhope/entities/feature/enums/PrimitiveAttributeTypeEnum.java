package ir.afarinesh.realhope.entities.feature.enums;

public enum PrimitiveAttributeTypeEnum {
    String, Integer, Long, Boolean, Date, Enum;

    public String angular() {
        switch (this) {
            case Date: {
                return "Date";
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
