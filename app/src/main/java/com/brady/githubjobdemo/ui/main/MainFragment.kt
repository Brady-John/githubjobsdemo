package com.brady.githubjobdemo.ui.main

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brady.githubjobdemo.JobItemBinding
import com.brady.githubjobdemo.R
import com.brady.githubjobdemo.data.api.github.model.Job
import com.brady.githubjobdemo.ui.BaseFragment
import com.brady.githubjobdemo.util.recyclerview.ArrayAdapter

class MainFragment : BaseFragment() {
    interface MainFragmentHost

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private var host: MainFragmentHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        host = context as MainFragmentHost
    }

    override fun onDetach() {
        host = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = getViewModel(MainViewModel::class)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.vm = viewModel

        binding.jobs.layoutManager = LinearLayoutManager(activity)
        binding.jobs.adapter = JobsAdapter()

        return binding.root
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private class JobsAdapter : ArrayAdapter<Job, JobViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: JobItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_job_summary, parent, false)
            return JobViewHolder(binding)
        }

        override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
            val job = getItemAtPosition(position)
            holder.bind(job)
        }
    }

    private class JobViewHolder(
            private val binding: JobItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Job) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}
