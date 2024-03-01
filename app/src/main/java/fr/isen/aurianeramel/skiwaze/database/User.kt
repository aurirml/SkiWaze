package fr.isen.aurianeramel.skiwaze.database

data class User(val id: Int,
                val lastname: String = "",
                val firstname: String= "",
                val password: String = "",
                val rank: Boolean=false,
                val connection: Boolean=false
)
