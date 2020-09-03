package ir.afarinesh.realhope.entities.feature.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum PrimitiveAttributeTypeEnum implements AbstractEnum {
    Void, String, Integer, Long, Boolean, JavaDate, Enum, Entity;

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

    @Override
    public String title(String locale) {
        switch (locale) {
            case "fa":
                return this.fa();
            case "en":
                return this.en();
        }
        return this.name();
    }

    @Override
    public String fa() {
        if (this == PrimitiveAttributeTypeEnum.Void) {
            return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        if (this == PrimitiveAttributeTypeEnum.Void) {
            return "All";
        }
        return this.name();
    }

    @Override
    public SelectEnum getSelectEnum(String locale) {
        return new SelectEnum(title(locale), name());
    }

    @Override
    public List<SelectEnum> getSelectEnumList(String locale) {
        List<SelectEnum> list = new ArrayList<>();
        for (int i = 0; i < values().length; i++) {
            list.add(
                    new SelectEnum(
                            values()[i].title(locale),
                            values()[i].name()
                    )
            );
        }
        return list;
    }

    public static PrimitiveAttributeTypeEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            PrimitiveAttributeTypeEnum value = values()[i];
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
