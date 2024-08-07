package enums;

public enum Gender {
    MALE,
    FEMALE,
    OTHER,
    UNKNOWN;

    private String description;

    Gender(String description) {
        this.description = description;
    }

    Gender() {
    }

    public String getDescription() {
        return description;
    }

    public static Gender fromDescription(String description) {
        for (Gender gender : values()) {
            if (gender.description != null && gender.description.equalsIgnoreCase(description)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}




