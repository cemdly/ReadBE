package ru.startandroid.develop.readbe20.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.startandroid.develop.readbe20.R
import ru.startandroid.develop.readbe20.ui.theme.ReadBe20Theme


@Composable
fun HomeScreen(onNavigateToLibrary: () -> Unit, onNavigateToPage: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xffF4FBF8)),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 32.dp)
        ) {

            Title()

            Spacer(Modifier.height(30.dp))

            NowReading(onNavigateToPage)

            Spacer(Modifier.fillMaxHeight(0.2f))

            Recent()

            Spacer(Modifier.weight(1f))

            NavPanel(onNavigateToLibrary)



        }

    }

}



@Composable
fun CustomLinearProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 13.dp,
    color: Color = Color(0xff725B5B),
    backgroundColor: Color = Color.LightGray
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val cornerRadius = with(density) { 6.dp.toPx() }

            drawRoundRect(
                color = backgroundColor,
                size = Size(canvasWidth, canvasHeight),
                cornerRadius = CornerRadius(cornerRadius)
            )

            // Заполненная часть с закругленными углами
            if (progress > 0f) {
                drawRoundRect(
                    color = color,
                    size = Size(progress * canvasWidth, canvasHeight),
                    cornerRadius = CornerRadius(cornerRadius),
                    style = Fill
                )
            }
        }
    }
}



@Composable
fun NowReading(onNavigateToPage: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            text = "Сейчас читаю",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().height(215.dp).clickable { onNavigateToPage()},
            shape = RoundedCornerShape(35.dp),
            colors = CardColors(
                contentColor = Color.White,
                containerColor = Color(0xffE9E9E9),
                disabledContentColor = Color.White,
                disabledContainerColor = Color(0xffE9E9E9)
            )
        ) {

            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.cover),
                    contentDescription = "",
                    modifier = Modifier.height(184.dp).width(116.dp),
                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 24.dp)
                )
                {
                    Text(
                        text = "Джоан Роулинг",
                        color = Color(0xff828282),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(Modifier.weight(0.25f))

                    Text(
                        text = "Гарри Поттер и потайная комната",
                        color = Color(0xff424242),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.weight(0.8f))

                    CustomLinearProgressIndicator(
                        progress = 0.57f
                    )

                }

            }

        }
    }
}

//
@Composable
fun Title() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = "ReadBe",
        fontSize = (40).sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center,
        color = Color(0xff725B5B)
    )

    Spacer(Modifier.height(24.dp))

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(0.99f),
        thickness = 1.dp,
        color = Color(0xff725B5B)
    )
}

@Composable
fun Recent(){
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
            text = "Недавние",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(Modifier.height(16.dp))
        HorizontalBookSlider(
            books = listOf(
                Book(R.drawable.book1, "53%"),
                Book(R.drawable.book2, "78%"),
                Book(R.drawable.book1, "22%"),
                Book(R.drawable.book1, "22%"),
                Book(R.drawable.book1, "22%")
            )
        )
    }
}



@Composable
fun HorizontalBookSlider(
    books: List<Book>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            BookItem(book)
        }
    }
}

data class Book(
    val coverResId: Int,
    val progress: String

) {

}

@Composable
fun BookItem(book: Book) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(102.dp)
    ) {
        Image(
            painter = painterResource(book.coverResId),
            contentDescription = "Book Cover",
            modifier = Modifier
                .height(150.dp)
                .width(102.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Text(
            text = book.progress,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp),
            color = Color.Black
        )
    }
}

@Composable
fun NavPanel(onNavigateToLibrary: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp).width(80.dp),
        shape = RoundedCornerShape(35.dp),
        colors = CardColors(
            contentColor = Color.White,
            containerColor = Color(0xff725B5B),
            disabledContentColor = Color.White,
            disabledContainerColor = Color(0xff725B5B)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 26.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(R.drawable.ic_home),
                contentDescription = "Домой",
                modifier = Modifier.size(24.dp)
            )

            Image(
                painter = painterResource(R.drawable.ic_library),
                contentDescription = "Библиотека",
                modifier = Modifier.size(24.dp).clickable { onNavigateToLibrary() }
            )

            Image(
                painter = painterResource(R.drawable.ic_settings),
                contentDescription = "Настройки",
                modifier = Modifier.size(24.dp)
            )
        }
    }

}


@Composable
@Preview
fun HomePreview() {
    ReadBe20Theme {
        HomeScreen(
            onNavigateToLibrary = {},
            onNavigateToPage = {})
    }
}
