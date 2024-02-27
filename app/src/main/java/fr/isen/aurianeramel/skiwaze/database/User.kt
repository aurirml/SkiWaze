package fr.isen.aurianeramel.skiwaze.database

data class User(val id: Int,
    val name: String = "",
                val passord: String = "",
                val rank: Boolean=false,
    val connection: Boolean=false)
