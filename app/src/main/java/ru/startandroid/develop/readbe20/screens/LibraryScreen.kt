package ru.startandroid.develop.readbe20.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.startandroid.develop.readbe20.R
import ru.startandroid.develop.readbe20.ui.theme.ReadBe20Theme
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    fun copyUriToFile(context: Context, uri: Uri): File? {
        return try {
            val cacheFile = File.createTempFile("epub_", ".tmp", context.cacheDir)
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(cacheFile).use { output ->
                    input.copyTo(output)
                }
            }
            cacheFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@SuppressLint("WrongConstant")
@Composable
fun LibraryScreen(onOpenBook: (String) -> Unit, onNavigateToHome: () -> Unit) {
    var anyBook: Boolean = false

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                val contentResolver = context.contentResolver
                try {

                    Log.d("Launcher", "Получен URI: $uri")
                } catch (e: Exception) {
                    Log.e("Launcher", "Ошибка получения долгосрочного доступа", e)
                }

                val safeUri = Uri.encode(uri.toString())
                onOpenBook(safeUri)
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xffF4FBF8))
    ) {
        // ОСНОВНОЙ КОНТЕНТ
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp)
        ) {
            // ШАПКА С ПОИСКОМ
            Header(launcher)

            // РАЗДЕЛИТЕЛЬ ПОСЛЕ ШАПКИ
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.99f)
                    .padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color(0xff725B5B)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!anyBook) {

                Text(
                    text = "Здесь пока ничего нет (",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray,
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                    textAlign = TextAlign.Center
                )

            }
            else {

                // СПИСОК КНИГ
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                ) {}

            }
        }

        // НИЖНЯЯ ПАНЕЛЬ НАВИГАЦИИ (ПОВЕРХ ВСЕГО)
        Nav2Panel(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomCenter),
            onNavigateToHome = onNavigateToHome
        )
    }
}

// ШАПКА С ПОИСКОМ
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(launcher: ManagedActivityResultLauncher<Array<String>, Uri?>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "Поиск",
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Поиск",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff828282)
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Добавить книгу",
            modifier = Modifier.size(24.dp).clickable { launcher.launch(arrayOf("application/epub+zip")) },
        )
    }
}


// КАРТОЧКА КНИГИ
@Composable
fun BookCard(coverResId: Int, title: String, author: String, year: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Обложка книги
            Image(
                painter = painterResource(id = coverResId),
                contentDescription = "Обложка книги",
                modifier = Modifier
                    .height(184.dp).width(116.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о книге
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = author,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = year.toString(),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                //Текст "Читать"
                Card(
                    modifier = Modifier
                        .height(50.dp)
                        .width(208.dp),
                    shape = RoundedCornerShape(35.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xffC5BBBB))
                ) {
                    Text(
                        text = "Читать",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff424242),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize() // Занимает всё пространство внутри Card
                            .wrapContentSize(Alignment.Center) // Центрирует содержимое
                    )
                }
            }
        }
    }
}

@Composable
fun Nav2Panel(modifier: Modifier = Modifier, onNavigateToHome: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .width(80.dp),
        shape = RoundedCornerShape(35.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xff725B5B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_home),
                contentDescription = "Домой",
                modifier = Modifier.size(24.dp).clickable { onNavigateToHome() }
            )

            Image(
                painter = painterResource(id = R.drawable.ic_library),
                contentDescription = "Библиотека",
                modifier = Modifier.size(24.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Настройки",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun LibraryPreview() {
    ReadBe20Theme {
        LibraryScreen(
            onOpenBook ={}, {}
        )
    }
}
