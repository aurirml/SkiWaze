package fr.isen.aurianeramel.skiwaze.database


data class Comment(val user_id: String = "",
                   val piste_id: Int = 0,
                   val content: String = "",
                   val date: String = "",
)