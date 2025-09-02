package com.example.myapplication.task

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddEditTaskBinding
import com.example.myapplication.model.Task
import com.example.myapplication.viewmodel.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by activityViewModels()
    private var task: Task? = null
    private var mode: String? = null

    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    private var dueDateCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mode = arguments?.getString("MODE")
        task = arguments?.getParcelable("TASK_OBJECT")

        binding.tvHeader.text = if (mode == "EDIT") "✏️ Edit Task" else "➕ Add New Task"

        val priorities = resources.getStringArray(R.array.priorities)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = adapter

        task?.let {
            binding.etTitle.setText(it.title)
            binding.etDescription.setText(it.description)
            binding.etDueDate.setText(it.dueDate ?: "")
            val index = priorities.indexOfFirst { p -> p.equals(it.priority, true) }
            if (index >= 0) binding.prioritySpinner.setSelection(index)
        }

        binding.etDueDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    dueDateCalendar.set(y, m, d)
                    binding.etDueDate.setText(dateFormat.format(dueDateCalendar.time))
                },
                dueDateCalendar.get(Calendar.YEAR),
                dueDateCalendar.get(Calendar.MONTH),
                dueDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.ivBack.setOnClickListener {
            dismiss()
        }


        binding.btnSave.setOnClickListener {
            val newTask = task ?: Task()
            newTask.title = binding.etTitle.text.toString()
            newTask.description = binding.etDescription.text.toString()
            newTask.priority = binding.prioritySpinner.selectedItem.toString()
            newTask.dueDate = binding.etDueDate.text.toString()
            viewModel.addOrUpdateTask(newTask)
            dismiss()
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    companion object {
        fun newInstance(task: Task?, mode: String): AddEditTaskFragment {
            val fragment = AddEditTaskFragment()
            val args = Bundle()
            if (task != null) args.putParcelable("TASK_OBJECT", task)
            args.putString("MODE", mode)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
