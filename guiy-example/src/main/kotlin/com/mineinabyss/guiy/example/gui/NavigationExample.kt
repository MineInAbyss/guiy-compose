package com.mineinabyss.guiy.example.gui

import com.mineinabyss.guiy.viewmodel.GuiyViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.bukkit.OfflinePlayer
import java.util.*

interface Routes {
    object Home : Routes

    object Search : Routes

    data class Profile(val player: UUID) : Routes
}

class ProfileViewModel: GuiyViewModel() {
    val profile = MutableStateFlow<OfflinePlayer?>(null)
}

//@Composable
//fun NavigationExample() {
//    val navController = rememberNavController<Routes>()
//    val viewModel = viewModel { ProfileViewModel() }
//    NavHost(navController, startDestination = Home) {
//        when (it) {
//            Home -> HomeScreen(
//                search = { navController.navigate(Search) },
//                openProfile = { navController.navigate(Profile(it))}
//            )
//            Search -> TextInput(selectPlayer = { player ->
//                viewModel.profile.update { player }
//                navController.popBackStack()
//            })
//
//            is Profile -> ProfileScreen()
//        }
//    }
//}
//
//@Composable
//fun HomeScreen(
//    profileViewModel: ProfileViewModel = viewModel(),
//    search: () -> Unit,
//    openProfile: (UUID) -> Unit,
//) = Chest("Home Screen") {
//    val current = profileViewModel.profile.collectAsState().value
//    if(current == null) Button(search) {
//        Text("Find a player")
//    }
//    else Button(onClick = { openProfile(current) }) { Text("Current player: $current") }
//}
//
//@Composable
//fun TextInput(selectPlayer: (OfflinePlayer) -> Unit) {
//    var input by remember { mutableStateOf("") }
//    Anvil(
//        "Search by player name",
//        onTextChanged = { input = it },
//        inputLeft = { Text("") },
//        output = { Text("Search for $input") },
//        onSubmit = {
//            val player = Bukkit.getOfflinePlayerIfCached(input) ?: return@Anvil
//            selectPlayer(player)
//        }
//    )
//}
//
//@Composable
//fun ProfileScreen() = Chest("Profile Screen") {
//
//}
