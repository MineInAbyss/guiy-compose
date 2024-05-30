package com.mineinabyss.guiy.components

import androidx.compose.runtime.*

// TODO implement a component where inventory events are NOT cancelled and state is propagated both ways
//  The current system makes it hard to do so safely (without allowing for dupes)
/**
 * A grid of items that can be interacted with in an inventory.
 *
 * @param grid Positions of items in the grid.
 * @param onItemsChanged Executes when an item is changed (added, removed, or moved.)
 * @param modifier The modifier for the grid.
 */
//@Composable
//fun InteractableItemGrid(
//    grid: ItemPositions,
//    onItemsChanged: (pos: ItemPositions) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    var size by remember { mutableStateOf(Size(0, 0)) }
//    Grid(modifier.onSizeChanged { size = it }.draggable {
//        //TODO
////        grid.items + updatedItems.map { (i, item) -> IntCoordinates(i) to item }
////        updatedItems.forEach { (i, item) ->
////            cursor!!.amount -= abs(item.amount - (state.items[i]?.amount ?: 0))
////            state.items[i] = item
////        }
//    }) {
//        for (x in 0 until size.width) {
//            for (y in 0 until size.height) {
//                val item = grid[x, y]
//                Item(item, Modifier.clickable(cancelClickEvent = true) {
//                    onItemsChanged(grid.with(x, y, resultItem))
//                    val newItem = when (clickType) {
//                        ClickType.LEFT -> {
//                            if (item != null && cursor?.isSimilar(item) == true) {
//                                val total = (item.amount + (cursor?.amount ?: 0))
//                                item.amount = total.coerceAtMost(item.maxStackSize)
//                                cursor?.amount = total - item.amount
//                                item
//                            } else {
//                                val temp = cursor
//                                cursor = item
//                                temp
//                            }
//                        }
//
//                        ClickType.RIGHT -> {
//                            when {
//                                cursor != null && item != null && cursor!!.type != item.type -> {
//                                    val temp = cursor
//                                    cursor = item
//                                    temp
//                                }
//
//                                cursor == null && item != null -> {
//                                    val c = item.clone()
//                                    item.amount /= 2
//                                    cursor = c.apply {
//                                        amount -= item.amount
//                                    }
//                                    item
//                                }
//
//                                cursor != null -> {
//                                    val newItem = cursor!!.clone().apply { amount = (item?.amount ?: 0) + 1 }
//                                    cursor!!.amount -= 1
//                                    newItem
//                                }
//
//                                else -> return@clickable
//                            }
//                        }
//
//                        else -> return@clickable
//                    }
//                    onItemsChanged(grid.with(x, y, newItem))
//                })
//            }
//        }
//    }
//}
