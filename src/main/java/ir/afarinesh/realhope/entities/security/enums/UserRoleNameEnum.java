package ir.afarinesh.realhope.entities.security.enums;

public enum UserRoleNameEnum {
    Admin, ProjectManager;


    public String fa() {
        switch (this) {
            case Admin:
                return "مدیر نرم‌افزار";
            case ProjectManager:
                return "مدیر پروژه";
            default:
                return "";
        }
    }

    public String en() {
        switch (this) {
            case Admin:
                return "Software Admin";
            case ProjectManager:
                return "ProjectManager";
            default:
                return "";
        }
    }

    public String title(String locale) {
        switch (locale) {
            case "fa":
                return this.fa();
            case "en":
                return this.en();
        }
        return this.name();
    }
}
