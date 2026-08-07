package com.iti.presentation.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.iti.presentation.R

enum class TopLevelRoute(
    val route: Route,
    @StringRes val labelRes: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
) {
    HOME(
        route = HomeRoute,
        labelRes = R.string.nav_home,
        selectedIcon = R.drawable.ic_nav_home_filled,
        unselectedIcon = R.drawable.ic_nav_home,
    ),
    PARTS(
        route = PartsRoute(),
        labelRes = R.string.nav_parts,
        selectedIcon = R.drawable.ic_nav_parts_filled,
        unselectedIcon = R.drawable.ic_nav_parts,
    ),
    AI(
        route = AiAssistantRoute,
        labelRes = R.string.nav_ai,
        selectedIcon = R.drawable.ic_nav_ai,
        unselectedIcon = R.drawable.ic_nav_ai,
    ),
    MY_PCS(
        route = MyPcsRoute(),
        labelRes = R.string.nav_my_pcs,
        selectedIcon = R.drawable.ic_nav_my_pcs_filled,
        unselectedIcon = R.drawable.ic_nav_my_pcs,
    ),
    PROFILE(
        route = ProfileRoute,
        labelRes = R.string.nav_profile,
        selectedIcon = R.drawable.ic_nav_profile_filled,
        unselectedIcon = R.drawable.ic_nav_profile,
    );
}