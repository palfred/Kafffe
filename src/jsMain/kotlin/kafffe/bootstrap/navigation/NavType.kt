package kafffe.bootstrap.navigation

enum class NavType(val cssClass: String) {
    bar("navbar"),
    tabs("nav-tabs"),
    pills("nav-pills"),
    vertical("flex-column"),
    vertical_pills("nav-pills flex-column"),
    none("");

    val isVertical: Boolean
        get() = this == vertical_pills || this == vertical

    val isPills: Boolean
        get() = this == vertical_pills || this == pills

}