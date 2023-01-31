# Kafffe (*K*otlin *A*pplication *F*ramework *f*or *F*rontend)
Kafffe is intended to be a framework/compoent library for writing one page HTML applications using [Kotlin](https://kotlinlang.org/).

Kafffe is inspired by Wicket and WAF (a Wickate Application Framework). It is also intended to work very directly with HTML DOM in a browser. 

Basically a KafffeComponent is a "wrapper" around one HTMLElement, and have the knowledege of how to rerender (or rebuild) the HTMLElement and replace it in the DOM. 

## Main areas 
```mermaid
graph TD
    subgraph KafffeHTML 
        HTML_DSL[DSL Builder]
    end

    subgraph KafffeComponents
        Component[KafffeComponent]
         
        subgraph Modifiers
           Modifier --> HTMLElementModifier
           HTMLElementModifier --> StyleModifier
           HTMLElementModifier --> CssClassModifier
           Modifier --> AttachAwareModifier
        end
        Component --> Modifier
        Component --> HTML_DSL
        Component --> CH[Composition Hierarchy]
        CH --> Layout
        CH --> Rerender
        CH --> Traversal

        Component --> CL[Lifecycle]
        CL --> Attach/Detach
        AttachAwareModifier --> CL
        Component --> Bootstrap
        Bootstrap --> Forms
        Forms --> FormsDSL[DSL Builder]
    end
    
 ```

Kafffe is mainly 2 things:
1. KafffeComponent, which is a component hierarchy that wraps around the HTMLElements of the browser DOM, and give functionality to mainpulate the DOM.
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
githubUser=XXXXXX@XXXXXXX.XXX
githubToken=ghp_XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```

# KafffeHTML DSL and builder
KafffeHTML unlike kotlinx.html is less rstirctive allows to build "invalid" HTML, but also makes it easy to extend with custom tags. 

#### Example to build  a div link, depending on the context the start of the build maybe given,`KafffeHtml.start` gives a start without any element (start from scratch, raten than adding to a surrounding element, the element can then later be added as a child to another HTMLElement):
```kotlin
KafffeHtml.start.div {// we are now having an implicit this of type KafffeElement<HTMLDivElement>
    h1 {text("Some Heading")}
    a {// we are now having an implicit this of type KafffeElement<HTMLAnchorElement>
        addClass("btn btn-default") // add CSS classes to the class attribute of the current element
        if (largeButtons) {
            addClass("btn-lg") // conditional add a css class 
        }
        withElement { // withElement gives a this of the current HTMLElement in this case HTMLAnchorElement
            href = "#"
            addEventListener("click", {event -> doSometing() })
        }
        text("Click")
        span {
            text("here")
            withStyle { // withStyle gives this the style attribute of the current element, type CSSStyleDeclaration
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

Ordinary Kotlin can of course be used inside the DSL builder, like if and loops, but is execution as part of the build, so if for example largeButtons changes value, the code need to be rerun to build a new HTMLElement with the effect of the value change, this can be automated by the **KafffeComponent**s

# KafffeComponent
This is the base for components in the framework. A KafffeComponent main purpose is to be able to build and rebuild (= rerender) a HTMLElement representing the "look" of the component. The KafffeComponent is part of composition hierarchy in order to this, it have a parent and can have child KafffeComponents. The parent knows where to place the child (layout). 

When a KafffeComponent is rerender in rebuilds the HTMLElement and replaces the old old HTMLELement with the new.

## Bootstrap
The focus during development was to support [Twitter Bootstrap CSS](https://getbootstrap.com). Many of the components and utitity classes/functions is defulting to using CSS classes and HTML like expected for Bootstrap.  

## Forms

## Modifiers / Behaviors

# Samples / Examples
The main sourceset holds a sample application that can be loaded by opening samples.html in the distribution output. The source code for this is located in `src/main/kotlin/samples` and in `src/main/resources/samples.html`
The idea is to provide more dedicated examples for the different aspects, these may be integrated in this sample or maybe be isolated in order to have them as simpleas possible. 
