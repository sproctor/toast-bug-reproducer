import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dokar.sonner.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(Modifier.fillMaxSize()) {

            val messages = remember { MutableStateFlow(listOf(1, 2, 3, 4, 5, 6)) }

            val toaster = rememberToasterState { toast ->
                messages.update { it - toast.id as Int }
            }

            LaunchedEffect(Unit) {
                toaster.listenMany(
                    messages.map { list ->
                        list.map { message ->
                            Toast(
                                id = message,
                                message = "Toast: $message",
                                duration = ToasterDefaults.DurationShort,
                                action = TextToastAction(
                                    text = "Dismiss",
                                    onClick = { toaster.dismiss(it.id) }
                                )
                            )
                        }
                    }
                )
            }

            Toaster(state = toaster, dismissPause = ToastDismissPause.OnNotFront)
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
