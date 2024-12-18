package me.dungngminh.lets_blog_kmp.presentation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import me.dungngminh.lets_blog_kmp.navigation.FavoriteRoute
import me.dungngminh.lets_blog_kmp.navigation.HomeRoute
import me.dungngminh.lets_blog_kmp.navigation.MainScreenDestination
import me.dungngminh.lets_blog_kmp.navigation.ProfileRoute
import me.dungngminh.lets_blog_kmp.navigation.SearchRoute
import me.dungngminh.lets_blog_kmp.presentation.main.favorite.FavoriteScreen
import me.dungngminh.lets_blog_kmp.presentation.main.home.HomeScreen
import me.dungngminh.lets_blog_kmp.presentation.main.profile.ProfileScreen
import me.dungngminh.lets_blog_kmp.presentation.main.search.SearchScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            MainNavigationBar(navController = navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                ExitTransition.None
            },
            popExitTransition = {
                ExitTransition.None
            },
            popEnterTransition = {
                EnterTransition.None
            },
        ) {
            composable<HomeRoute> {
                HomeScreen()
            }
            composable<SearchRoute> {
                SearchScreen()
            }
            composable<FavoriteRoute> {
                FavoriteScreen()
            }
            composable<ProfileRoute> {
                ProfileScreen(onLoginClick = onLoginClick)
            }
        }
    }
}

@Composable
fun MainNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavigationBar(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MainScreenDestination.entries.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any {
                    it.route == destination.route::class.qualifiedName
                } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        painter =
                            if (selected) {
                                painterResource(
                                    destination.selectedIcon,
                                )
                            } else {
                                painterResource(destination.icon)
                            },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    Text(stringResource(destination.title))
                },
                selected = selected,
                onClick = {
                    if (selected) return@NavigationBarItem
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}
