package com.mrntlu.PassVault.ui.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mrntlu.PassVault.models.BottomNavItem
import com.mrntlu.PassVault.ui.theme.BlueLogo

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    BottomNavigation(
        modifier = modifier,
        backgroundColor = BlueLogo,
        elevation = 8.dp,
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item)
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.White,
                label = {
                    Text(text = item.name, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.name, modifier = Modifier.size(if(selected) 28.dp else 24.dp))
                },
                alwaysShowLabel = true,
            )
        }
    }
}