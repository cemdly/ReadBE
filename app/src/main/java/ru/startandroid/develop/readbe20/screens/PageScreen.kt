package ru.startandroid.develop.readbe20.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import ru.startandroid.develop.readbe20.R
import java.io.InputStream
import java.util.zip.ZipInputStream
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.documentfile.provider.DocumentFile
import java.io.FileInputStream


sealed class BookUiState {
    object Loading : BookUiState()
    data class Error(val message: String) : BookUiState()
    data class Success(val pages: List<String>) : BookUiState()
}

class BookViewModel(
    private val context: Context,
    private val encodedBookUri: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookUiState>(BookUiState.Loading)
    val uiState = _uiState.asStateFlow()

    fun loadBook() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val uri = Uri.decode(encodedBookUri)
                val parsedUri = Uri.parse(uri)

                context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                    val epubText = parseEpub(inputStream)
                    val pages = splitTextToPages(epubText)
                    _uiState.update { BookUiState.Success(pages) }
                } ?: run {
                    _uiState.update { BookUiState.Error("Не удалось открыть файл") }
                }

            } catch (e: Exception) {
                Log.e("BookViewModel", "Ошибка загрузки книги", e)
                _uiState.update { BookUiState.Error(e.localizedMessage ?: "Ошибка") }
            }
        }
    }

    private fun parseEpub(inputStream: InputStream): String {
        Log.d("BookViewModel", "Начинаем парсинг EPUB")
        val zip = ZipInputStream(inputStream)
        var entry = zip.nextEntry
        val fullText = StringBuilder()

        while (entry != null) {
            if (entry.name.endsWith(".html")) {
                val html = zip.readBytes().toString(Charsets.UTF_8)
                val doc = Jsoup.parse(html)
                fullText.append(doc.text()).append("\n\n")
            }
            entry = zip.nextEntry
        }

        Log.d("BookViewModel", "Парсинг завершён, длина текста: ${fullText.length}")
        return fullText.toString()
    }

    private fun splitTextToPages(text: String, wordsPerPage: Int = 200): List<String> {
        Log.d("BookViewModel", "Разбиваем текст на страницы (по $wordsPerPage слов)")
        return text.split(" ").chunked(wordsPerPage) { chunk ->
            chunk.joinToString(" ")
        }
    }
}

class BookViewModelFactory(
    private val context: Context,
    private val encodedBookUri: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(context.applicationContext, encodedBookUri) as T
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageScreen(bookUri: String) {

    val context = LocalContext.current
    val viewModel: BookViewModel = viewModel(
        factory = BookViewModelFactory(context.applicationContext, bookUri)
    )

    val uiState by viewModel.uiState.collectAsState()
    val currentPage = remember { mutableIntStateOf(0) }
    val pages = remember(uiState) {
        if (uiState is BookUiState.Success) (uiState as BookUiState.Success).pages else emptyList()
    }

    LaunchedEffect(Unit) {
        Log.d("PageScreen", "Запускаем загрузку книги")
        viewModel.loadBook()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffE5D9D9))
                .padding(end = 16.dp, bottom = 20.dp, top = 32.dp)
        ) {
            // Заголовок
            InfoTop(
                currentPage = currentPage.intValue + 1,
                totalPages = pages.size
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Основной текст
            when (val state = uiState) {
                BookUiState.Loading -> LoadingIndicator()
                is BookUiState.Error -> ErrorScreen(state.message)
                is BookUiState.Success -> {
                    MainText(
                        text = pages.getOrNull(currentPage.intValue) ?: "Конец книги"
                    )
                }
            }

            // Навигация
            Spacer(modifier = Modifier.weight(1f))

            NavigationButtons(
                currentPage = currentPage.intValue,
                totalPages = pages.size,
                onNext = {
                    if (currentPage.intValue < pages.size - 1) {
                        currentPage.intValue++
                        Log.d("PageScreen", "Текущая страница: ${currentPage.intValue + 1}")
                    }
                },
                onPrev = {
                    if (currentPage.intValue > 0) {
                        currentPage.intValue--
                        Log.d("PageScreen", "Текущая страница: ${currentPage.intValue + 1}")
                    }
                }
            )
        }
    }
}


@Composable
private fun InfoTop(currentPage: Int, totalPages: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "О дивный новый мир",
            fontSize = 13.sp,
            color = Color(0xffADADAD),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify
        )

        Text(
            text = "$currentPage из $totalPages",
            fontSize = 13.sp,
            color = Color(0xffADADAD),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
private fun MainText(text: String) {
    val scrollState = rememberScrollState()

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .verticalScroll(scrollState),
        text = text,
        fontSize = 16.sp,
        color = Color.Black,
        textAlign = TextAlign.Justify
    )
}

@Composable
private fun NavigationButtons(
    currentPage: Int,
    totalPages: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = onPrev,
            enabled = currentPage > 0
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Назад",
                tint = if (currentPage > 0) Color.Black else Color.Gray
            )
        }

        IconButton(
            onClick = onNext,
            enabled = currentPage < totalPages - 1
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Вперёд",
                tint = if (currentPage < totalPages - 1) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun ErrorScreen(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Red
        )
    }
}