package fr.isen.aurianeramel.skiwaze.database

data class Remontees (val id:Int,
                      val name: String = "",
                      val type: Boolean,
                      val difficulty: Boolean = true,
                      val frequence: Int,
                      val state: Boolean=true, )