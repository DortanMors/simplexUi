package `in`.fom.simplexui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import `in`.fom.simplexui.ui.theme.SimplexUiTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimplexUiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Conversation(listOf("Vasek", "Bober", "Slava"))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimplexUiTheme {
        Sign()
    }
}

@Composable
fun MessageCard(msg: String) {
    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = msg)
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg)
        }
    }
}

@Composable
fun Conversation(messages: List<String>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}

@Preview
@Composable
fun PreviewConversation() {
    SimplexUiTheme {
        Conversation(listOf("Vasek", "Bober", "Slava"))
    }
}

@Composable
fun Sign() {
    var index = 0
    val signs = listOf("<", "=", ">")
    var signState by remember { mutableStateOf(signs[index]) }
    Button(onClick = {
        index = (index + 1) % signs.size
        signState = signs[index]
    }) {
        Text(text = signState)
    }
}

@Composable
fun InequalitySign() {
}

@Composable
fun NumberField() {
}

@Composable
fun VariableX(index: Int) {
}

@Composable
fun Term(row: Int, index: Int, onEdit: (Double) -> Unit) {
}