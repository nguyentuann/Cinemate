package vn.tutorial.cinemate.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import vn.tutorial.cinemate.R

// Font family
val NetflixSans = FontFamily(
    Font(R.font.netflix_sans_regular, FontWeight.Normal),
    Font(R.font.netflix_sans_light, FontWeight.Light),
    Font(R.font.netflix_sans_medium, FontWeight.Medium),
    Font(R.font.netflix_sans_bold, FontWeight.Bold),
)

// Typography system
val Typography = Typography(
    // Regular styles
    bodySmall = TextStyle( // SmallBody 14sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyMedium = TextStyle( // Body 16sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle( // Title2 24sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),

    // Headline styles
    headlineSmall = TextStyle( // Title3 21sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 21.sp
    ),
    headlineMedium = TextStyle( // Title1 27sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 27.sp
    ),
    headlineLarge = TextStyle( // LargeTitle 50sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Normal,
        fontSize = 50.sp
    ),

    // Medium styles
    titleSmall = TextStyle( // Title4 22sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp
    ),
    titleMedium = TextStyle( // Title1 30sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    titleLarge = TextStyle( // LargeTitle 33sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Medium,
        fontSize = 33.sp
    ),

    // Bold styles
    displaySmall = TextStyle( // Title1 Bold 48sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    displayLarge = TextStyle( // LargeTitle Bold 55sp
        fontFamily = NetflixSans,
        fontWeight = FontWeight.Bold,
        fontSize = 55.sp
    )
)
