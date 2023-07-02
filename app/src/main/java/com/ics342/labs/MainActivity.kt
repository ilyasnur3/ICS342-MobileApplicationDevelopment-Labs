package com.ics342.labs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.ui.tooling.preview.Preview
import com.ics342.labs.data.DataItem
import com.ics342.labs.ui.theme.LabsTheme


private val dataItems = listOf(
    DataItem(1, "Item 1", "Description 1"),
    DataItem(2, "Item 2", "Description 2"),
    DataItem(3, "Item 3", "Description 3"),
    DataItem(4, "Item 4", "Description 4"),
    DataItem(5, "Item 5", "Description 5"),
    DataItem(6, "Item 6", "Description 6"),
    DataItem(7, "Item 7", "Description 7"),
    DataItem(8, "Item 8", "Description 8"),
    DataItem(9, "Item 9", "Description 9"),
    DataItem(10, "Item 10", "Description 10"),
    DataItem(11, "Item 11", "Description 11"),
    DataItem(12, "Item 12", "Description 12"),
    DataItem(13, "Item 13", "Description 13"),
    DataItem(14, "Item 14", "Description 14"),
    DataItem(15, "Item 15", "Description 15"),
    DataItem(16, "Item 16", "Description 16"),
    DataItem(17, "Item 17", "Description 17"),
    DataItem(18, "Item 18", "Description 18"),
    DataItem(19, "Item 19", "Description 19"),
    DataItem(20, "Item 20", "Description 20"),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabsTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        DataItemList(dataItems, navController)
                    }
                    composable(
                        "details/{dataItemId}",
                        arguments = listOf(navArgument("dataItemId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val dataItemId = backStackEntry.arguments?.getInt("dataItemId")
                        if (dataItemId != null) {
                            val dataItem = dataItems.find { it.id == dataItemId }
                            if (dataItem != null) {
                                DetailsScreen(dataItem)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun DataItemView(dataItem: DataItem, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onItemClick() }
    ) {
        Text(
            text = "ID: ${dataItem.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Greeting(name = dataItem.name) // Use the Greeting function here
            Text(
                text = dataItem.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = dataItem.description,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DataItemList(dataItems: List<DataItem>, navController: NavController) {
    LazyColumn {
        items(dataItems) { dataItem ->
            DataItemView(dataItem, onItemClick = {
                navController.navigate("details/${dataItem.id}")
            })
            Divider(color = MaterialTheme.colorScheme.secondary, thickness = 1.dp)
        }
    }
}

@Composable
fun DetailsScreen(dataItem: DataItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "ID: ${dataItem.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Greeting(name = dataItem.name) // Use the Greeting function here
            Text(
                text = dataItem.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = dataItem.description,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle button click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go Back")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataItemListPreview() {
    LabsTheme {
        DataItemList(dataItems, rememberNavController())
    }
}
