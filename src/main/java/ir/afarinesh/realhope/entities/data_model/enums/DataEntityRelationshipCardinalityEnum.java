package ir.afarinesh.realhope.entities.data_model.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum DataEntityRelationshipCardinalityEnum implements AbstractEnum {
    Void, ManyToOne, OneToOne, OneToMany;

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
            case ManyToOne:
                return "چند به یک";
            case OneToOne:
                return "یک به یک";
            case OneToMany:
                return "یک به چند";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        switch (this) {
            case ManyToOne:
                return "Many To One";
            case OneToOne:
                return "One To One";
            case OneToMany:
                return "One To Many";
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

    public static DataEntityRelationshipCardinalityEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            DataEntityRelationshipCardinalityEnum status = values()[i];
            if (status.name().equals(name)) {
                return status;
            }
        }
        return null;
    }
}
