package kafffe.bootstrap

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kotlinx.browser.window

data class Alert(val text: String, val level: BootstrapLevel = BootstrapLevel.error)
class AlertsPanel : KafffeComponent() {

    var alerts: Set<Alert> by rerenderOnChange(setOf<Alert>())
    var dismissible: Boolean by rerenderOnChange(true)

    var levelIcon: Boolean by rerenderOnChange(true)

    fun clearAlerts() {
        alerts = setOf<Alert>()
    }

    fun addAlert(alert: Alert) {
        alerts = alerts + alert
    }

    fun setAlertWithDismiss(alert: Alert, dismissAfterSeconds: Int = 5) {
        alerts = setOf(alert)
        window.setTimeout({removeAlert(alert)}, dismissAfterSeconds * 1000)
    }

    fun addAlertWithDismiss(alert: Alert, dismissAfterSeconds: Int = 5) {
        alerts = alerts + alert
        window.setTimeout({removeAlert(alert)}, dismissAfterSeconds * 1000)
    }

    var infoDismissSeconds = 5
    var warnDismissSeconds = 8
    var errorDismissSeconds = 12
    /**
     * Shorthand for default info message
     */
    fun infoAdd(message: String) = addAlertWithDismiss(Alert(message, BootstrapLevel.info), infoDismissSeconds)
    fun infoSet(message: String) = setAlertWithDismiss(Alert(message, BootstrapLevel.info), infoDismissSeconds)

    /**
     * Shorthand for default warning message
     */
    fun warnAdd(message: String) = addAlertWithDismiss(Alert(message, BootstrapLevel.warning), warnDismissSeconds)
    fun warnSet(message: String) = setAlertWithDismiss(Alert(message, BootstrapLevel.warning), warnDismissSeconds)

    /**
     * Shorthand for default error message
     */
    fun errorAdd(message: String) = addAlertWithDismiss(Alert(message, BootstrapLevel.error), errorDismissSeconds)
    fun errorSet(message: String) = setAlertWithDismiss(Alert(message, BootstrapLevel.error), errorDismissSeconds)


    fun removeAlert(alert: Alert) {
        alerts = alerts - alert
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                for (alert in alerts) {
                    div {
                        withElement {
                            addClass("alert")
                            addClass(alert.level.alertClass)
                            setAttribute("role", "alert")
                            if (dismissible) {
                                addClass("alert-dismissible")
                                addClass("fade")
                                addClass("show")
                            }
                        }
                        if (levelIcon) {
                            i {
                                withElement {
                                    addClass(alert.level.iconClasses)
                                }
                            }
                            text(" ")
                        }
                        text(alert.text)
                        if (dismissible) {
                            button {
                                withElement {
                                    onclick = { removeAlert(alert) }
                                    type = "button"
                                    addClass("close")
                                    setAttribute("data-dismiss", "close")
                                    setAttribute("aria-label", "Close")
                                }
                                span {
                                    withElement {
                                        setAttribute("aria-hidden", "true")
                                        innerHTML = "&times;"
                                    }
                                }
                            }
                        }
                    }
                }
            }

}