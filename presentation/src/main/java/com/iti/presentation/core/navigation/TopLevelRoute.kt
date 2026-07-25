package com.iti.presentation.core.navigation

import androidx.annotation.DrawableRes
import com.iti.presentation.R

enum class TopLevelRoute(
    val route: Route,
    val label: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
) {
    HOME(
        route = HomeRoute,
        label = "Home",
        selectedIcon = R.drawable.ic_nav_home_filled,
        unselectedIcon = R.drawable.ic_nav_home,
    ),
    PARTS(
        route = PartsRoute(),
        label = "Parts",
        selectedIcon = R.drawable.ic_nav_parts_filled,
        unselectedIcon = R.drawable.ic_nav_parts,
    ),
    AI(
        route = AiAssistantRoute,
        label = "AI",
        selectedIcon = R.drawable.ic_nav_ai,
        unselectedIcon = R.drawable.ic_nav_ai,
    ),
    MY_PCS(
        route = MyPcsRoute(),
        label = "My PCs",
        selectedIcon = R.drawable.ic_nav_my_pcs_filled,
        unselectedIcon = R.drawable.ic_nav_my_pcs,
    ),
    PROFILE(
        route = ProfileRoute,
        label = "Profile",
        selectedIcon = R.drawable.ic_nav_profile_filled,
        unselectedIcon = R.drawable.ic_nav_profile,
    );
}