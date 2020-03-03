package kafffe.bootstrap

import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kotlin.browser.window

data class Alert(val text: String, val level: BootstrapLevel = BootstrapLevel.error)
class AlertsPanel : KafffeComponent() {

    var alerts: Set<Alert> by rerenderOnChange(setOf<Alert>())
    var dismissible: Boolean by rerenderOnChange(true)

    var levelIcon: Boolean by rerenderOnChange(true)

    fun clearAlerts() {
        alerts = setOf<Alert>()
    }

    fun addAlert(alert: Alert) {
        alerts += alert
    }

    fun setAlertWithDismiss(alert: Alert, dismissAfterSeconds: Int = 5) {
        alerts = setOf(alert)
        window.setTimeout({removeAlert(alert)}, dismissAfterSeconds * 1000)
    }

    fun addAlertWithDismiss(alert: Alert, dismissAfterSeconds: Int = 5) {
        alerts += alert
        window.setTimeout({removeAlert(alert)}, dismissAfterSeconds * 1000)
    }

    fun info(msg: String) {
        addAlert(Alert(msg, BootstrapLevel.info))
    }

    fun warn(msg: String) {
        addAlert(Alert(msg, BootstrapLevel.warning))
    }

    fun error(msg: String) {
        addAlert(Alert(msg, BootstrapLevel.danger))
    }

    fun removeAlert(alert: Alert) {
        alerts -= alert
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