package fr.isen.aurianeramel.skiwaze

import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database


class DataBaseHelper {
    companion object {
        val database = Firebase.database("https://stationski-f3f87-default-rtdb.europe-west1.firebasedatabase.app/")
        //val database = Firebase.database("https://station-ski-default-rtdb.europe-west1.firebasedatabase.app/")
    }
}