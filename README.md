# Kafffe (*K*otlin *A*pplication *F*ramework *f*or *F*rontend)
Kafffe is intended to be a framework/compoent library for writing one page HTML applications using [Kotlin](https://kotlinlang.org/).

Kafffe is inspired by Wicket and WAF (a Wickate Application Framework). It is also intended to work very directly with HTML DOM in a browser. 

Basically a KafffeComponent is a "wrapper" around one HTMLElement, and have the knowledege of how to rerender (or rebuild) the HTMLElement and replace it in the DOM. 

## Main areas 
```mermaid
graph LR;
    Kafffe --> KafffeComponent
    Kafffe --> KafffeHTML --> HTML_DSL[DSL Builder]
    KafffeComponent --> CH[Composition Hierarchy]
    KafffeComponent --> CL[Lifecycle]
    KafffeComponent --> CM[Modifiers/Behaviors]
    KafffeComponent --> Bootstrap
    KafffeComponent --> Forms --> FormsDSL[DSL Builder]
 ```

Kafffe is mainly 2 things:
1. KafffeComponent, which is a component hierarchy that wraps around the HTMLElements of the browser DOM, and give funtinality to mainpulate the DOM.
2. KafffeHTML a DSL that allows to build HTMLElements from Kotlin, it is similar to kotlinx.html, but is smaller and not as restrictive

# Using in a gradle project
Currently Kafffe is only published to GitHub maven repo, so this need to be setup in the gradle.build.kts in a Kotlin/js project or module:

#### gradle.build.kts
```kotlin
// ...    
repositories {
    // add this to your repositories    
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/palfred/kafffe") // Github Package
        credentials {
            val githubUser: String by project
            val githubToken: String by project
            username = githubUser
            password = githubToken

        }
    }
}
// ...    
dependencies {
    // add this to your dependencies (or other version)    
    implementation("dk.rheasoft:kafffe:1.10")
}
```
A githubUser and githubToken is also needed in gradle.properties or by other means. Like:

#### gradle.propertie
```properties
githubUser=XXXXXX@XXXXXXX.X
githubToken=ghp_XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```

# KafffeHTML DSL and builder
KafffeHTML unlike kotlinx.html is less rstirctive allows to build "invalid" HTML, but also makes it easy to extend with custom tags. 

#### Example to build  a div link, depending on the context the start of the build maybe given,`KafffeHtml.start` gives a start without any element (start from scratch, raten than adding to a surrounding element, the element can then later be added as a child to another HTMLElement):
```kotlin
KafffeHtml.start.div {
    h1 {text("Some Heading")}
    a {
        // we are now having an implicit this of type KafffeElement<HTMLAnchorElement>
        addClass("btn btn-default")
        withElement {
            // we are having an implicit element of type HTMLAnchorElement
            href = "#"
            addEventListener("click", {event -> doSometing() })
        }
        text("Click")
        span {
            text("here")
            withStyle {
                // we are now on the style attribute of type CSSStyleDeclaration
                fontWeigth = 800
            }
        }
    }
}
```
This will produce HTMLDIVElement, with a click listener on the anchor (observer no whitespace in elements, as there would often be if it had been written by hand, this may affect the look a bit, if you have several inline elements there are no whitespace separation by "default"):
```html
<div><h1>Some Heading</h1><a href="#" class="btn btn-default">Click<span style="font-weight:800">here</span></a></div>
```

