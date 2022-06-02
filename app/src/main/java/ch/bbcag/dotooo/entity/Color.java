package ch.bbcag.dotooo.entity;

public enum Color {
    GREY("Grey", "#6b6b6b"),
    RED("Red", "#d1422c"),
    BLUE("Blue", "#0400d4"),
    GREEN("Green", "#20bd4a"),
    YELLOW("Yellow", "#ebe307"),
    PURPLE("Purple", "#9307eb"),
    BLACK("Black", "#000000");

    private final String displayName;
    private final String hex;

    Color(String displayName, String hex) {
        this.displayName = displayName;
        this.hex = hex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getHex() {
        return hex;
    }
}
