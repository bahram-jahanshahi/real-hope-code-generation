package ir.afarinesh.realhope.entities.feature.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum EntityAttributeQuantityEnum implements AbstractEnum {
    Void, Mono, List;

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
        switch (this) {
            case Mono:
                return "Mono";
            case List:
                return "List";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        switch (this) {
            case Mono:
                return "Mono";
            case List:
                return "List";
            case Void:
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

    public static EntityAttributeQuantityEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            EntityAttributeQuantityEnum entityAttributeQuantityEnum = values()[i];
            if (entityAttributeQuantityEnum.name().equals(name)) {
                return entityAttributeQuantityEnum;
            }
        }
        return null;
    }
}
