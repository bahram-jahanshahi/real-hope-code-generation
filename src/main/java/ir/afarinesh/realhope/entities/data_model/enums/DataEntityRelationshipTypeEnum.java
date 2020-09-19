package ir.afarinesh.realhope.entities.data_model.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum DataEntityRelationshipTypeEnum implements AbstractEnum {
    Void, HasA, Optional, Mandatory;

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
            case HasA:
                return "مالکیت";
            case Optional:
                return "انتخابی";
            case Mandatory:
                return "اجباری";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        switch (this) {
            case HasA:
                return "HasA";
            case Optional:
                return "Optional";
            case Mandatory:
                return "Mandatory";
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

    public static DataEntityRelationshipTypeEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            DataEntityRelationshipTypeEnum status = values()[i];
            if (status.name().equals(name)) {
                return status;
            }
        }
        return null;
    }
}
