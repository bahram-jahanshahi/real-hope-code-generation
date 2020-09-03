package ir.afarinesh.realhope.entities.feature.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum EntityAttributeCategoryEnum implements AbstractEnum {
    Void, Primitive, DomainEntity, SelectEntity, SelectEnum;


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
            case Primitive:
                return "Primitive";
            case DomainEntity:
                return "DomainEntity";
            case SelectEntity:
                return "SelectEntity";
            case SelectEnum:
                return "SelectEnum";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        if (this == EntityAttributeCategoryEnum.Void) {
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

    public static EntityAttributeCategoryEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            EntityAttributeCategoryEnum value = values()[i];
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }
}
