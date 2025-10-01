package vn.tutorial.cinemate.presentation.splash.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import vn.tutorial.cinemate.R
import vn.tutorial.cinemate.common.components.CommonButton
import vn.tutorial.cinemate.common.components.SignInText
import vn.tutorial.cinemate.common.data.listSplashImage
import vn.tutorial.cinemate.navigation.Route
import vn.tutorial.cinemate.presentation.splash.components.ImagePager

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SplashScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = { listSplashImage.size })

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Pager
            ImagePager(
                images = listSplashImage,
                pagerState = pagerState
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                SignInText(navController = navController, isSplash = true)
            }

            // Indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            ) {
                listSplashImage.forEachIndexed { index, _ ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color.White else Color.Gray)
                    )
                }
            }

            // Get Started button
            CommonButton(
                title = stringResource(R.string.get_started),
                onClick = { navController.navigate(Route.SignUp.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 60.dp)
            )
        }
    }
}
