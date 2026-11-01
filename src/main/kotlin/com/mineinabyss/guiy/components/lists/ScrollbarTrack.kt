package com.mineinabyss.guiy.components.lists

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mineinabyss.guiy.components.Item
import com.mineinabyss.guiy.modifiers.click.clickable
import com.mineinabyss.guiy.modifiers.scroll.onScroll
import com.mineinabyss.idofront.textcomponents.miniMsg
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.dvyy.compose.mini.layout.jetpack.Column
import me.dvyy.compose.mini.layout.jetpack.Row
import me.dvyy.compose.mini.modifier.Modifier
import net.kyori.adventure.key.Key
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

// Distance between slot tops in a chest GUI
private const val SLOT_PITCH_PX = 18

// The thumb overlaps the frame's border by a pixel at both ends, which takes 3 px off its travel
private const val THUMB_INSET_PX = 3

// Lowercase names are the custom model data strings the scrollbar item definition in the resource pack selects on
private enum class TrackOrientation { VERTICAL, HORIZONTAL }

private enum class TrackPiece { START, MIDDLE, END, SINGLE }

/**
 * A scrollbar filling [height] slots of a column, with a thumb showing the scroll position and a tooltip naming the visible rows.
 * Clicking a slot jumps to that part of the list and the wheel scrolls it, so it can sit outside the [Scrollable] it belongs to.
 * Nothing is drawn while the list fits without scrolling.
 *
 * Every slot shows [model] with custom model data string 0 set to `vertical` and string 1 to `start`, `middle`, `end` or `single` for its piece of the frame.
 * The bottom slot also draws the thumb, float 0 is how many GUI pixels it sits above its lowest position
 */
@Composable
fun VerticalScrollbarTrack(
    state: ScrollableState,
    height: Int,
    model: Key,
    modifier: Modifier = Modifier,
    tooltip: (firstRow: Int, lastRow: Int, rows: Int) -> String = { first, last, rows -> "<white>Rows $first-$last of $rows" },
) = ScrollbarTrack(state, height, model, modifier, tooltip, TrackOrientation.VERTICAL)

/**
 * A scrollbar filling [width] slots of a row, with a thumb showing the scroll position and a tooltip naming the visible columns.
 * Clicking a slot jumps to that part of the list and the wheel scrolls it, so it can sit outside the [Scrollable] it belongs to.
 * Nothing is drawn while the list fits without scrolling.
 *
 * Every slot shows [model] with custom model data string 0 set to `horizontal` and string 1 to `start`, `middle`, `end` or `single` for its piece of the frame.
 * The rightmost slot also draws the thumb, float 0 is how many GUI pixels it sits left of its rightmost position
 */
@Composable
fun HorizontalScrollbarTrack(
    state: ScrollableState,
    width: Int,
    model: Key,
    modifier: Modifier = Modifier,
    tooltip: (firstColumn: Int, lastColumn: Int, columns: Int) -> String = { first, last, columns -> "<white>Columns $first-$last of $columns" },
) = ScrollbarTrack(state, width, model, modifier, tooltip, TrackOrientation.HORIZONTAL)

@Composable
private fun ScrollbarTrack(
    state: ScrollableState,
    length: Int,
    model: Key,
    modifier: Modifier,
    tooltip: (first: Int, last: Int, total: Int) -> String,
    orientation: TrackOrientation,
) {
    // A page is one row or column here and pageMax counts them, so the last page still on screen is pageMax - pagesOnScreen
    val lastPage = state.pageMax - state.pagesOnScreen
    if (lastPage <= 0) return
    val travel = ((length - 1) * SLOT_PITCH_PX - THUMB_INSET_PX).coerceAtLeast(0)
    val fromStart = (state.page.coerceAtMost(lastPage) * travel / lastPage.toFloat()).roundToInt()
    val name = tooltip(state.page + 1, (state.page + state.pagesOnScreen).coerceAtMost(state.pageMax), state.pageMax)

    val slots: @Composable () -> Unit = {
        repeat(length) { index ->
            val piece = when {
                length == 1 -> TrackPiece.SINGLE
                index == 0 -> TrackPiece.START
                index == length - 1 -> TrackPiece.END
                else -> TrackPiece.MIDDLE
            }
            // Drawn by the last slot since later slots render over earlier ones
            val thumbOffset = if (index == length - 1) travel - fromStart else null
            val item = remember(model, orientation, piece, thumbOffset, name) {
                ItemStack.of(Material.PAPER).apply {
                    setData(DataComponentTypes.ITEM_MODEL, model)
                    setData(
                        DataComponentTypes.CUSTOM_MODEL_DATA,
                        CustomModelData.customModelData()
                            .addString(orientation.name.lowercase())
                            .addString(piece.name.lowercase())
                            .apply { if (thumbOffset != null) addFloat(thumbOffset.toFloat()) }
                    )
                    setData(DataComponentTypes.ITEM_NAME, name.miniMsg())
                }
            }
            Item(item, Modifier.clickable {
                if (length > 1) state.setPage((index * lastPage / (length - 1).toFloat()).roundToInt())
            })
        }
    }

    val trackModifier = modifier.onScroll { state.scroll(delta) }
    when (orientation) {
        TrackOrientation.VERTICAL -> Column(trackModifier) { slots() }
        TrackOrientation.HORIZONTAL -> Row(trackModifier) { slots() }
    }
}
