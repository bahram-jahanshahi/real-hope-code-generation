package ir.afarinesh.realhope.entities.feature.enums;

public enum PrimitiveAttributeTypeEnum {
    String, Integer, Long, Boolean, JavaDate, Enum, Entity;

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
            case Enum: {
                return "SelectEnum";
            }
            case Entity: {
                return "SelectEntity";
            }
        }
        return this.toString();
    }
}
