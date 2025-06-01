 package ru.startandroid.develop.readbe20.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.startandroid.develop.readbe20.ui.theme.ReadBe20Theme

 @Composable
 fun PageScreen(bookUri: String)
 {
     Column(
         modifier = Modifier.fillMaxSize().background(Color(0xffE5D9D9)),
     ) {
         Column(
             modifier = Modifier
                 .fillMaxSize()
                 .padding( end = 16.dp, bottom = 20.dp, top = 32.dp)
         ) {
             InfoTop()

             Spacer(Modifier.height(16.dp))

             MainText()

         }
     }
 }

 @Composable
 fun InfoTop(){
     Column (
         modifier = Modifier.fillMaxWidth()
     ) {
         Row(
             modifier = Modifier.fillMaxWidth()
         ){
             Text(modifier =Modifier.padding(start = 16.dp),
                 text = "О дивный новый мир",
                 fontSize = 13.sp,
                 color = Color(0xffADADAD),
                 fontWeight = FontWeight.Bold,
                 textAlign = TextAlign.Justify
             )

             Spacer(Modifier.weight(0.1f))

             Text(
                 text = "5 из 200",
                 fontSize = 13.sp,
                 color = Color(0xffADADAD),
                 fontWeight = FontWeight.Bold,
                 textAlign = TextAlign.Justify
             )
         }
     }
 }

 @Composable
 fun MainText() {
     Column(
         modifier = Modifier.fillMaxWidth()
     ) {
         Text(modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
             text = "Действие разворачивается в 2540 году (632 год по эре Форда), когда человечество живет в условиях тотального технологического контроля. Люди рождаются не естественным путем, а в специальных инкубаторах, где их генетически программируют на принадлежность к определенному социальному классу: от привилегированных Альф до рабочих Эпсилонов. Индивидуальность подавляется с детства, а счастье обеспечивается через потребление, развлечения и наркотик сому, который помогает избежать любых негативных эмоций.В центре повествования — Бернард Маркс, неудовлетворенный своей жизнью в этом \"идеальном\" обществе, и Джон, \"дикарь\", выросший за пределами цивилизации. Их встреча становится отправной точкой для столкновения двух миров — высокотехнологичного, лишенного эмоций и свободы, и примитивного, но полного страстей, боли и истинно человеческих переживаний.Хаксли мастерски исследует вопросы морали, свободы выбора и ценности человеческого опыта. Его роман — это предостережение о том, как стремление к комфорту и безопасности может привести к потере того, что делает нас людьми. \"О дивный новый мир\" остается актуальным и сегодня, заставляя задуматься о последствиях технологического прогресса и роли человека в мире будущего.Книга адресована всем, кто интересуется философией, социальной критикой и вопросами будущего человечества. Это произведение учит видеть тонкую грань между прогрессом и регрессом, между свободой и контролем, между счастьем и иллюзией счастья.",
             fontSize = 16.sp,
             color = Color.Black,
             textAlign = TextAlign.Justify
         )
     }
 }

@Composable
@Preview
fun PagePreview() {
    ReadBe20Theme {
        PageScreen(
            ""
        )
    }
}