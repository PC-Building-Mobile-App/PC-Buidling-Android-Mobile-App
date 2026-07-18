package com.iti.presentation.core.navigation

fun <T : Any> MutableList<T>.navigateSingleTop(route: T) {
    if (lastOrNull() != route) {
        add(route)
    }
}

fun <T : Any> MutableList<T>.navigateToRoot(root: T, route: T) {
    clear()
    add(root)
    if (root != route) {
        add(route)
    }
}
