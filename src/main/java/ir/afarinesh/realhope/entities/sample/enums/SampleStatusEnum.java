package ir.afarinesh.realhope.entities.sample.enums;

import ir.afarinesh.realhope.core.domain.AbstractEnum;
import ir.afarinesh.realhope.core.domain.SelectEnum;

import java.util.ArrayList;
import java.util.List;

public enum SampleStatusEnum implements AbstractEnum {

    Void, Created, Expired;

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
            case Created:
                return "ایجاد شده";
            case Expired:
                return "پایان یافته";
            case Void:
                return "همه";
        }
        return this.name();
    }

    @Override
    public String en() {
        switch (this) {
            case Created:
                return "Created";
            case Expired:
                return "Expired";
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

    public static SampleStatusEnum findByName(String name) {
        if (name == null) {
            return null;
        }
        for (int i = 0; i < values().length; i++) {
            SampleStatusEnum statusEnum = values()[i];
            if (statusEnum.name().equals(name)) {
                return statusEnum;
            }
        }
        return null;
    }
}
