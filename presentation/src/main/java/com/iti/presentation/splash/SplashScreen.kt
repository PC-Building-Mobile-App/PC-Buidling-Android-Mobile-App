package com.iti.presentation.splash
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.presentation.R
import com.iti.presentation.ui.theme.DeepBlack
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.Inter
import com.iti.presentation.ui.theme.RoyalPurple
import com.iti.presentation.ui.theme.TextSecondary

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .blur(24.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    RoyalPurple.copy(alpha = 0.5f),
                                    ElectricBlue.copy(alpha = 0.2f),
                                    Color.Transparent
                                )
                            ),
                            shape = MaterialTheme.shapes.large
                        )
                )

                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(MaterialTheme.shapes.large),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = Inter,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.slogan),
                color = ElectricBlue,
                fontFamily = Inter,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(56.dp))

            LinearProgressIndicator(
                modifier = Modifier
                    .width(64.dp)
                    .height(3.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                color = RoyalPurple,
                trackColor = TextSecondary.copy(alpha = 0.15f)
            )
        }
    }
}