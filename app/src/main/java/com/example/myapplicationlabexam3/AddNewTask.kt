package com.example.myapplicationlabexam3
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.myapplicationlabexam3.model.ToDoModel
import com.example.myapplicationlabexam3.utils.ToDoPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask: BottomSheetDialogFragment() {
    // Widgets
    private lateinit var mEditText: EditText
    private lateinit var mSaveButton: Button

    private lateinit var toDoPreferenceManager: ToDoPreferenceManager

    private var isUpdate = false
    private var taskId: Int = 0

    private var listener: OnTaskAddedListener? = null

    companion object {
        const val TAG = "AddNewTask"

        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_newtask, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEditText = view.findViewById(R.id.edittext)
        mSaveButton = view.findViewById(R.id.button_save)

        toDoPreferenceManager = ToDoPreferenceManager(requireActivity())

        //var isUpdate = false

        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            taskId = bundle.getInt("id", 0)
            mEditText.setText(task)

            if (!task.isNullOrEmpty()) {
                mSaveButton.isEnabled = false
            }
        }

        mEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    mSaveButton.isEnabled = false
                    mSaveButton.setBackgroundColor(Color.GRAY)
                } else {
                    mSaveButton.isEnabled = true
                    mSaveButton.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.colourPrimary)
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })



        mSaveButton.setOnClickListener {
            val text = mEditText.text.toString()

            if (isUpdate) {
                toDoPreferenceManager.updateTask(bundle!!.getInt("id"), text)
            } else {
                //val item = ToDoModel(task = text, id = 0, status = 0)
                val currentTaskList = toDoPreferenceManager.getAllTasks()
                // Assign a new ID based on the size of the current list
                val newId = currentTaskList.size + 1
                val item = ToDoModel(task = text, id = newId, status = 0)

                toDoPreferenceManager.insertTask(item)
                listener?.onTaskAdded(text)
            }
            dismiss()
        }
    }

    fun setOnTaskAddedListener(listener: OnTaskAddedListener) {
        this.listener = listener
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is OnDialogCloseListener) {
            activity.onDialogClose(dialog)
        }
    }





}

