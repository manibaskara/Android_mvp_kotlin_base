package com.magnum.app.view.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.magnum.app.R

class RecyclerViewSwipeHelper(
    var context: Context,
    private var recyclerView: RecyclerView,
    var listener: SwipeListener,
    private var swipeDirection: Direction
) {

    enum class Direction {
        LEFT, RIGHT, BIDIRECTIONAL
    }

    interface SwipeListener {

        fun onLeftSwipe(position: Int)

        fun onRightSwipe(position: Int)
    }

    private var directions = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT


    fun attach() {

        directions = when (swipeDirection) {
            Direction.LEFT -> ItemTouchHelper.LEFT
            Direction.RIGHT -> ItemTouchHelper.RIGHT
            else -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        }

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            0, directions
        ) {

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20

                val icon: Drawable? = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_delete_white_gradient
                )
                val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
                val iconTop = itemView.top + (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
                val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)

                val background = ColorDrawable(ContextCompat.getColor(context, R.color.orange_dark))

                when {
                    dX > 0 -> { // Swiping to the right
                        val iconLeft = itemView.left + iconMargin + (icon?.intrinsicWidth ?: 0)
                        val iconRight = itemView.left + iconMargin
                        icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset,
                            itemView.bottom
                        )
                    }
                    dX < 0 -> { // Swiping to the left
                        val iconLeft = itemView.right - iconMargin - (icon?.intrinsicWidth?:0)
                        val iconRight = itemView.right - iconMargin
                        icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                        background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                        )
                    }
                    else -> // view is unSwiped
                        background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon?.draw(c)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
/*
            override fun getSwipeDirs(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {

                if (viewHolder is FundViewHolder && recyclerView.adapter is FundAdapter) {
                    if (((((recyclerView.adapter as FundAdapter).data?.size)
                            ?: 1) - 1) == viewHolder.adapterPosition
                    ) {
                        return 0
                    }
                }
                return super.getSwipeDirs(recyclerView, viewHolder)

            }*/

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    listener.onLeftSwipe(position)
                } else if (direction == ItemTouchHelper.RIGHT) {
                    listener.onRightSwipe(position)
                }

            }
        }


        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}