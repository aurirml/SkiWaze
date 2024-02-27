package fr.isen.aurianeramel.skiwaze.database

data class Pistes(val id:Int=0,
                  val pistefutur: List<Int> = emptyList(),
                  val remontefutur: List<Int> = emptyList(),
                  val name: String = "",
                  val color: Int=0,
                  val state: Boolean = true,
                  val frequence: Int=0,
                  val snow: Int=0,
                  val avalanche: Boolean=false,
                  val damne: Boolean=true,
)
