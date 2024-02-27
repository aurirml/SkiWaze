package fr.isen.aurianeramel.skiwaze.database

data class Remontees (val id:Int=0,
                      val pistefutur: List<Int> = emptyList(),
                      val remontefutur: List<Int> = emptyList(),
                      val name: String = "",
                      val type: Boolean = true,
                      val difficulty: Boolean = true,
                      val frequence: Int=0,
                      val state: Boolean=true,
    )