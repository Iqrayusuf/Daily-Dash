package com.example.myapplicationlabexam3
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationlabexam3.adapter.ToDoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator



class RecyclerViewTouchHelper (private val adapter: ToDoAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            val builder = AlertDialog.Builder(adapter.getContext())
            builder.setTitle("Delete Task")
            builder.setMessage("Are You Sure?")
            builder.setPositiveButton("Yes") { dialog, _ ->
                adapter.deleteTask(position)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                adapter.notifyItemChanged(position)
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.colourPrimary))
            .addSwipeLeftActionIcon(R.drawable.baseline_edit)
            .addSwipeRightBackgroundColor(Color.RED)
            .addSwipeRightActionIcon(R.drawable.baseline_delete)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


}