package com.brady.githubjobdemo.ui.main

import android.app.AlertDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brady.githubjobdemo.JobDetailBinding
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
        private var jobView : CardView? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding: JobItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_job_summary, parent, false)
            binding.companyUrl.visibility = View.GONE
            binding.jobType.visibility = View.GONE
            binding.created.visibility = View.GONE
            binding.description.visibility = View.GONE
            binding.url.visibility = View.GONE
            jobView = binding.jobView
            jobView?.setOnClickListener {
                showJobDetailsDialog(parent, binding)
            }
            return JobViewHolder(binding)
        }

        override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
            val job = getItemAtPosition(position)
            holder.bind(job)
        }

        private fun showJobDetailsDialog(parent : ViewGroup, jobItemBinding: JobItemBinding) {
            val dialogBuilder = AlertDialog.Builder(parent.context)
            val inflater = LayoutInflater.from(parent.context)
            val binding: JobDetailBinding = DataBindingUtil.inflate(inflater, R.layout.job_details, parent, false)
            binding.company.text = jobItemBinding.company.text
            binding.jobTitle.text = jobItemBinding.jobTitle.text
            binding.location.text = jobItemBinding.location.text
            if (jobItemBinding.companyUrl.text == "URL: null")
                binding.companyUrl.text = "URL: N/A"
            else {
                binding.companyUrl.text = jobItemBinding.companyUrl.text
                Linkify.addLinks(binding.companyUrl, Linkify.WEB_URLS)
            }
            binding.jobType.text = jobItemBinding.jobType.text
            binding.created.text = jobItemBinding.created.text
            binding.description.text = Html.fromHtml(jobItemBinding.description.text.toString())
            binding.url.text = jobItemBinding.url.text
            Linkify.addLinks(binding.url, Linkify.WEB_URLS)
            dialogBuilder.setView(binding.root)
            val b = dialogBuilder.create()
            b.show()
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
