package samples

import kafffe.bootstrap.*
import kafffe.bootstrap.pagination.BootstrapPagination
import kafffe.bootstrap.pagination.ListModelPager
import kafffe.core.KafffeComponent
import kafffe.core.KafffeHtmlBase
import kafffe.core.Label
import kafffe.core.Model

class TableSample : KafffeComponent() {

    data class Person(val firstName: String, val lastName: String)

    fun createDataList(count: Int = 85): List<Person> {
        val persons = mutableListOf<Person>()
        for (i in 1..count) {
            persons.add(rndPerson(i))
        }
        return persons.toList()
    }

    private fun rndPerson(ix: Int): Person {
        val firstNames = listOf("Peter", "Alfred", "Christian", "Simon", "Karsten", "Thomas", "Svend", "Kim", "Per", "Henning")
        val lastNames = listOf("Petersen", "Østergard", "Christensen", "Simonsen", "Karstensen", "Thomasen", "Svendsen", "Kimbermann", "Skov", "Henningsen")
        return Person(ix.toString() + " " + rndName(firstNames), rndName(lastNames))
    }

    private fun rndName(names: List<String>): String = names[(0..names.size - 1).random()]

    val personsModel: Model<List<Person>> = Model.of(createDataList())
    private fun newPersons() {
        personsModel.data = createDataList((10..120).random())
    }

    val pager = ListModelPager(personsModel, 10)
    val table = createTable().also { addChild(it) }
    val pagination = addChild(BootstrapPagination(pager).apply {
        includePageInfo = true
        includePageSizeSelector = true
    })
    val topPagination = BootstrapPagination(pager).also { addChild(it) }
    val newPersonsButton = BootstrapButton(Model.of("Generate Persons"), { newPersons() }).also { addChild(it) }

    fun createTable(): BootstrapTable<Person> {
        val table = BootstrapTable<Person>(pager.pageModel)
        with(table) {
            col(Model.of("First Name"), { row -> Label(row.firstName) })
            col(Model.of("Last Name"), { row -> Label(row.lastName) })
            applyDefaultStyle()
            with(modifiersHeader) {
                add(BasicColor.primary.backgroundModifer)
                add(BasicColor.black.textModifer)
            }
        }

        return table
    }

    override fun KafffeHtmlBase.kafffeHtml() =
            div {
                add(topPagination.html)
                add(table.html)
                add(pagination.html)
                bootstrapRow {
                    bootstrapCol(ColWidth(ResponsiveSize.md, 6)) {
                        addClass("row mt-4")
                        bootstrapColAuto() {
                            add(newPersonsButton.html)
                        }
                    }
                }
            }

}