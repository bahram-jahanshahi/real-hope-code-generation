package ir.afarinesh.realhope.entities.feature.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum UserInterfaceTypeEnum implements AbstractEnum {
    Void, GridList, List, View, Delete, AddNew, Update;


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
            case GridList:
                return "فهرست";
            case List:
                return "لیست";
            case View:
                return "نمایش";
            case Delete:
                return "حذف";
            case AddNew:
                return "افزودن";
            case Update:
                return "ویرایش";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        switch (this) {
            case GridList:
                return "GridList";
            case List:
                return "List";
            case View:
                return "View";
            case Delete:
                return "Delete";
            case AddNew:
                return "AddNew";
            case Update:
                return "Update";
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

    public static UserInterfaceTypeEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            UserInterfaceTypeEnum chequeStatusEnum = values()[i];
            if (chequeStatusEnum.name().equals(name)) {
                return chequeStatusEnum;
            }
        }
        return null;
    }
}
