package ir.afarinesh.realhope.entities.feature.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum UseCaseUsageEnum implements AbstractEnum {
    Void, ViewId, ViewEntity,
    GridListEntity, GridListSearchField,
    UpdateId, UpdateField, UpdateResult,
    AddNewId, AddNewField, AddNewResult,
    DeleteId, DeleteEntity, DeleteResult;


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
            case ViewId:
                return "ViewId";
            case ViewEntity:
                return "ViewEntity";
            case GridListEntity:
                return "GridListEntity";
            case GridListSearchField:
                return "GridListSearchField";
            case UpdateId:
                return "UpdateId";
            case UpdateField:
                return "UpdateField";
            case UpdateResult:
                return "UpdateResult";
            case AddNewId:
                return "AddNewId";
            case AddNewField:
                return "AddNewField";
            case AddNewResult:
                return "AddNewResult";
            case DeleteId:
                return "DeleteId";
            case DeleteEntity:
                return "DeleteEntity";
            case DeleteResult:
                return "DeleteResult";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        if (this == UseCaseUsageEnum.Void) {
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

    public static UseCaseUsageEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            UseCaseUsageEnum useCaseUsageEnum = values()[i];
            if (useCaseUsageEnum.name().equals(name)) {
                return useCaseUsageEnum;
            }
        }
        return null;
    }

}
