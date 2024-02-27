package fr.isen.aurianeramel.skiwaze.database

data class Pistes(val id:Int,
    val name: String = "",
                  val color: Int,
                  val state: Boolean = true,
                  val frequence: Int,
                  val snow: Int,
                  val avalanche: Boolean=false,
                  val damme: Boolean=true,
)

