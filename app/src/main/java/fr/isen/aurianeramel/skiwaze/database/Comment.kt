package fr.isen.aurianeramel.skiwaze.database


data class Comment(val user_id: String = "",
                   val piste_id: String = "",
                   val commentary: String = ""
)