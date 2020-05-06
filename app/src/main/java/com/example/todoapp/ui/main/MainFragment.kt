package com.example.todoapp.ui.main

import android.animation.Animator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.todoapp.MyApplication
import com.example.todoapp.R
import com.example.todoapp.model.TaskModel
import com.example.todoapp.other.TaskListAdapter
import com.example.todoapp.other.ViewUtilities
import com.example.todoapp.other.ViewUtilities.LAST_PERCENTAGE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.warkiz.widget.IndicatorSeekBar
import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var progressSeekBar: IndicatorSeekBar
    private lateinit var addFab: FloatingActionButton
    private lateinit var btnSaveTask: Button
    private lateinit var btnCancel: Button
    private lateinit var checkAnimation: LottieAnimationView
    private lateinit var emptyConstraintLayout: ConstraintLayout
    private lateinit var populatedConstraintLayout: ConstraintLayout
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var edtTitle: TextInputEditText
    private lateinit var edtDescription: TextInputEditText
    private var deleteFromDB = false

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.let {
            (it.application as MyApplication).get(it).getApplicationComponent()
                ?.injectFragment(this)
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        activity?.let { fragmentActivity ->
            viewModel.tasks.observe(fragmentActivity, Observer {
                it?.let {
                    renderView(it)
                }
            })
        }

        val root = inflater.inflate(R.layout.main_fragment, container, false)
        initialiseViews(root)
        setUpListeners()
        return root
    }

    private fun initialiseViews(view: View) {

        //Main
        progressSeekBar = view.findViewById(R.id.progressSeekBar)
        progressSeekBar.setIndicatorTextFormat("\${PROGRESS} %")
        addFab = view.findViewById(R.id.addFab)
        emptyConstraintLayout = view.findViewById(R.id.clEmpty)
        populatedConstraintLayout = view.findViewById(R.id.mainConstraintLayout)
        taskRecyclerView = view.findViewById(R.id.taskRecycleView)
        taskListAdapter = TaskListAdapter()
        this.context?.let { context ->
            taskRecyclerView.layoutManager = LinearLayoutManager(context)
            taskRecyclerView.setHasFixedSize(true)
            taskRecyclerView.adapter = taskListAdapter
        }

        //Add Task
        btnSaveTask = view.findViewById(R.id.btnAdd)
        btnCancel = view.findViewById(R.id.btnCancel)
        checkAnimation = view.findViewById(R.id.lottieCheck)
        edtTitle = view.findViewById(R.id.edtTaskTitle)
        edtDescription = view.findViewById(R.id.edtTaskDescription)
    }

    private fun animate(startValue: Float, endValue: Float) {
        progressSeekBar.max = 100f

        ViewUtilities.animateCustomProgress(
            progressSeekBar,
            startValue,
            endValue,
            sharedPreferences
        )
    }

    private fun setUpListeners() {
        addFab.setOnClickListener {
            addFab.isExpanded = !addFab.isExpanded
        }
        btnSaveTask.setOnClickListener {
            addTask()
        }
        btnCancel.setOnClickListener {
            taskPageClosed()
        }

        checkAnimation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                taskPageClosed()
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
    }

    private fun renderView(tasks: List<TaskModel>) {
        taskListAdapter.setData(tasks)
        if (tasks.isNotEmpty()) {
            emptyConstraintLayout.visibility = View.GONE
            populatedConstraintLayout.visibility = View.VISIBLE
            taskListAdapter.setOnItemClickListener(object : TaskListAdapter.OnItemClickListener {
                override fun onCheckBoxClick(taskModel: TaskModel, view: View) {

                    if (view is CheckBox) {
                        if (view.isChecked) {
                            val updatedTask = TaskModel(
                                id = taskModel.id,
                                name = taskModel.name,
                                description = taskModel.description,
                                status = 1
                            )
                            viewModel.insertUpdateTask(updatedTask)
                            showMessage("Task successfully completed")
                            view.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        } else {
                            val updatedTask = TaskModel(
                                id = taskModel.id,
                                name = taskModel.name,
                                description = taskModel.description,
                                status = 0
                            )
                            viewModel.insertUpdateTask(updatedTask)
                            showMessage("Task moved to in progress")
                            view.paintFlags = 0
                        }

                    }
                }

                override fun onDeleteIconClick(position: Int) {
                    val task = taskListAdapter.getTask(position)
                    taskListAdapter.removeTask(position)
                    activity?.let {
                        val snackBar = Snackbar.make(
                            addFab,
                            "Task has been removed",
                            Snackbar.LENGTH_SHORT
                        )
                        snackBar.setAction("UNDO") {
                            deleteFromDB = false
                            taskListAdapter.restoreTask(task, position)
                        }
                        snackBar.show()

                        snackBar.addCallback(object : Snackbar.Callback() {
                            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                                if (event == DISMISS_EVENT_TIMEOUT) {
                                    viewModel.deleteTask(task)
                                }
                                if (event == DISMISS_EVENT_ACTION) {
                                    Toast.makeText(it, "Task restored", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onShown(sb: Snackbar?) {
                                super.onShown(sb)
                                deleteFromDB = true
                            }
                        })
                    }
                }

            })
            activity?.let { fragmentActivity ->
                viewModel.completedTask.observe(fragmentActivity, Observer { completedTasks ->
                    completedTasks?.let { completedList ->
                        val percentage = viewModel.getPercentage(tasks.size, completedList.size)
                        val lastPercentage = sharedPreferences.getFloat(LAST_PERCENTAGE, 0f)
                        animate(lastPercentage, percentage.toFloat())
                    }
                })
            }
        } else {
            emptyConstraintLayout.visibility = View.VISIBLE
            populatedConstraintLayout.visibility = View.GONE
        }
    }

    private fun addTask() {
        val title = edtTitle.text.toString()
        val description = edtDescription.text.toString()
        if (ViewUtilities.validateInput(title, description)) {
            val task = TaskModel(id = null, name = title, description = description, status = 0)
            dismissKeyBoard()
            checkAnimation.visibility = View.VISIBLE
            viewModel.insertUpdateTask(task)
            checkAnimation.playAnimation()
        } else {
            showMessage("Please add both title and description")
        }
    }

    private fun dismissKeyBoard() {
        val inputManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(btnSaveTask.windowToken, 0)
    }

    private fun taskPageClosed() {
        addFab.isExpanded = !addFab.isExpanded
        edtDescription.setText("")
        edtTitle.setText("")
        checkAnimation.clearAnimation()
        checkAnimation.visibility = View.INVISIBLE
    }

    private fun showMessage(message: String) {
        this.context?.let {
            Toast.makeText(
                it,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
