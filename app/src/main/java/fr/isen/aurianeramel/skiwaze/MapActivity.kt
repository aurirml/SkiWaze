package fr.isen.aurianeramel.skiwaze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import fr.isen.aurianeramel.skiwaze.database.Pistes
import fr.isen.aurianeramel.skiwaze.database.Remontees
import java.util.Stack
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownhillSkiing
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import fr.isen.aurianeramel.skiwaze.ui.theme.SkiWazeTheme
import fr.isen.aurianeramel.skiwaze.ui.theme.comic_sans
import fr.isen.aurianeramel.skiwaze.ui.theme.stg

//import androidx.compose.material.Text


class MapActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkiWazeTheme {
                // A surface container using the 'background' color from the theme
                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(),
                    // color = MaterialTheme.colorScheme.background
                ) {
                    Background()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(R.color.bright_gray))
                                .border(
                                    width = 2.dp,
                                    color = colorResource(R.color.bright_gray)
                                ),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                text = "Itinéraire",
                                fontFamily = stg,
                                fontSize = 30.sp,
                                color = colorResource(R.color.blue_gray),
                                modifier = Modifier
                            )
                        }
                        Divider(color = Color.Gray, thickness = 1.dp)
                        AutoComplete2()
                    }

                }
            }
            /*Column {
                Divider(color = Color.Gray, thickness = 1.dp)
                AutoComplete2()
            }*/
            //DisplayPaths(start = 1, end =10 )

        }
    }
}

@Composable
fun IdToName(depart : Int): String{
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    GetData(pistes)
    for (piste in pistes) {
        if (piste.id == depart) {
            return piste.name
        }
    }
    val remonteees = remember {
        mutableStateListOf<Remontees>()
    }
    GetData2(remonteees)
    for (rem in remonteees) {
        if (rem.id == depart) {
            return rem.name
        }
    }
    return "Recherche en cours"
}
@Composable
fun getIdFromPisteName(name: String): Int {
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    GetData(pistes)
    val rem = remember {
        mutableStateListOf<Remontees>()
    }
    GetData2(rem)
    val piste = pistes.find { it.name == name }
    if (piste != null) {
        return piste.id
    } else {
        val remonter = rem.find { it.name == name }
        return remonter?.id ?: -1
    }
}

@Composable
fun IdPisteToInfo(depart : Int): Boolean{
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    GetData(pistes)
    for (piste in pistes) {
        if (piste.id == depart) {
            return piste.state
        }
    }
    return true
}

@Composable
fun IdRemonteToInfo(id :Int):Boolean{
    val remonteees = remember {
        mutableStateListOf<Remontees>()
    }
    GetData2(remonteees)
    for (rem in remonteees) {
        if (rem.id == id) {
            return rem.state
        }
    }
    return true
}



@Composable
fun ListVoisin(depart : Int): List<Int>{
    //val voisinage =mutableListOf<Int>()
    val voisinage = remember(depart) {
        mutableStateListOf<Int>()
    }

    if (voisinage.isEmpty()) {
        val pistes = remember {
            mutableStateListOf<Pistes>()
        }
        val remonteeees = remember {
            mutableStateListOf<Remontees>()
        }
        GetData2(remonteeees)
        GetData(pistes)
        for (piste in pistes) {
            if (piste.id == depart) {
                for (id in piste.pistefutur) {
                    if (IdPisteToInfo(id)) {
                        voisinage.add(id)
                    }
                }
                for (id in piste.remontefutur) {
                    if (IdRemonteToInfo(id)) {
                        voisinage.add(id)
                    }
                }
            }
        }
        for (rem in remonteeees) {
            if (rem.id == depart) {
                for (id in rem.pistefutur) {
                    if (IdPisteToInfo(id)) {
                        voisinage.add(id)
                    }
                }
                for (id in rem.remontefutur) {
                    if (IdRemonteToInfo(id)) {
                        voisinage.add(id)
                    }
                }
            }
        }
    }
    Log.d("PATH", "         VOISINAGE $voisinage")
    return voisinage
}

@Composable
fun DisplayPaths(start: Int, end: Int) {
    val paths = removeDuplicateRows( findAllPaths(start, end))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        //Spacer(Modifier.height(20.dp))
        LazyColumn(    horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center, // Alignement vertical au centre
        ) {

            items(paths.toList()){liste ->
                Divider(color = Color.Gray, thickness = 1.dp)
                Text(text = "Itinéraire",
                        fontFamily = comic_sans,

                )
                for(piste in liste){
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp,
                    ),
                    modifier = Modifier
                        .size(
                            width = 250.dp,
                            height = 60.dp
                        )
                        .padding(top = 20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = colorResource(R.color.powder_blue)
                    ),
                    content = {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Row {}
                            Text(
                                text = IdToName(piste),
                                fontFamily = comic_sans

                            )
                        }
                    }
                )
            }
        }
        }
    }
}

@Composable
fun removeDuplicateRows(matrix: List<List<Int>>): List<List<Int>> {
    val uniqueRows = mutableSetOf<List<Int>>()
    val result = mutableListOf<List<Int>>()

    for (row in matrix) {
        if (uniqueRows.add(row)) {
            result.add(row)
        }
    }

    return result
}

@Composable
fun findAllPaths(start: Int, end: Int): List<List<Int>> {
    val allPaths = mutableListOf<List<Int>>()
    val stack = Stack<Pair<Int, MutableList<Int>>>()
    stack.push(start to mutableListOf(start))
    val visited = mutableSetOf<Int>()

    while (stack.isNotEmpty()) {
        val (current, path) = stack.pop()
        if (current == end) {
            Log.d("PATH", "Chemin trouvé : $path")
            allPaths.add(path.toList())
        } else {
            if (current !in visited) {
                visited.add(current)
                Log.d("PATH", "actuellement : $current")
                val neighbors = ListVoisin(current)
                for (neighbor in neighbors) {
                    val newPath = path.toMutableList()
                    newPath.add(neighbor)
                    stack.push(neighbor to newPath)
                }
            }
        }
    }
    Log.d("PATH", "LIBRE")

    return allPaths
}





@Composable
fun CategoryItems(
    title: String,
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect(title)
            }
            .padding(10.dp)
    ) {
        androidx.compose.material.Text(text = title, fontSize = 16.sp)
    }

}

@Composable
fun AutoComplete2() {
    val pistes = remember {
        mutableStateListOf<Pistes>()
    }
    GetData(pistes)
    val rem = remember {
        mutableStateListOf<Remontees>()
    }
    GetData2(rem)
    val nomsPistes: List<String> = (pistes.filter { it.state }.map { it.name } +
            rem.filter { it.state }.map { it.name })


    var category1 by remember {
        mutableStateOf("")
    }

    var category2 by remember {
        mutableStateOf("")
    }

    val heightTextFields by remember {
        mutableStateOf(55.dp)
    }

    var textFieldSize1 by remember {
        mutableStateOf(Size.Zero)
    }

    var textFieldSize2 by remember {
        mutableStateOf(Size.Zero)
    }

    var expanded1 by remember {
        mutableStateOf(false)
    }

    var expanded2 by remember {
        mutableStateOf(false)
    }

    var showResults by remember {
        mutableStateOf(false)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    // First Category Field
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded1 = false
                }
            )
    ) {

        androidx.compose.material.Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = "Depart",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize1 = coordinates.size.toSize()
                        },
                    value = category1,
                    onValueChange = {
                        category1 = it
                        expanded1 = true
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded1 = !expanded1 }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded1) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize1.width.dp),
                    elevation = 15.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (category1.isNotEmpty()) {
                            items(
                                nomsPistes.filter {
                                    it.lowercase()
                                        .contains(category1.lowercase()) || it.lowercase()
                                        .contains("others")
                                }
                                    .sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category1 = title
                                    expanded1 = false
                                }
                            }
                        } else {
                            items(
                                nomsPistes.sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category1 = title
                                    expanded1 = false
                                }
                            }
                        }

                    }

                }
            }

        }
    }

    // Second Category Field
    Column(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    expanded2 = false
                }
            )
    ) {

        androidx.compose.material.Text(
            modifier = Modifier.padding(start = 3.dp, bottom = 2.dp),
            text = "Arrivee",
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )

        Column(modifier = Modifier.fillMaxWidth()) {

            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heightTextFields)
                        .border(
                            width = 1.8.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .onGloballyPositioned { coordinates ->
                            textFieldSize2 = coordinates.size.toSize()
                        },
                    value = category2,
                    onValueChange = {
                        category2 = it
                        expanded2 = true
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    ),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded2 = !expanded2 }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = "arrow",
                                tint = Color.Black
                            )
                        }
                    }
                )
            }

            AnimatedVisibility(visible = expanded2) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .width(textFieldSize2.width.dp),
                    elevation = 15.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 150.dp),
                    ) {

                        if (category2.isNotEmpty()) {
                            items(
                                nomsPistes.filter {
                                    it.lowercase()
                                        .contains(category2.lowercase()) || it.lowercase()
                                        .contains("others")
                                }
                                    .sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category2 = title
                                    expanded2 = false
                                }
                            }
                        } else {
                            items(
                                nomsPistes.sorted()
                            ) {
                                CategoryItems(title = it) { title ->
                                    category2 = title
                                    expanded2 = false
                                }
                            }
                        }

                    }

                }
            }

        }
    }

    Button(
        onClick = { showResults = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Afficher les éléments")
    }
    if (showResults) {
        if (getIdFromPisteName(category1)!=-1 && getIdFromPisteName(category2)!=-1){
            DisplayPaths(start = getIdFromPisteName(category1), end =getIdFromPisteName(category2) )

        }else{
            Text("piste ou remonter non existante: ")
        }
    }
}

