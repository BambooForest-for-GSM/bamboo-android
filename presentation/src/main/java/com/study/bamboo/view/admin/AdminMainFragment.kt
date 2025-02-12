package com.study.bamboo.view.admin

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.study.bamboo.adapter.AlgorithmAdapter
import com.study.bamboo.adapter.STATUS
import com.study.bamboo.databinding.FragmentAdminMainBinding
import com.study.bamboo.view.MainActivity
import com.study.bamboo.view.user.viewmodel.EmojiViewModel
import com.study.base.base.adapter.PostLoadingAdapter
import com.study.base.base.adapter.RecyclerViewItemClickListener
import com.study.base.base.base.BaseFragment
import com.study.base.base.utils.LinearLayoutManagerWrapper
import com.study.domain.model.common.algorithm.ResultEntity
import com.study.domain.model.user.request.EmojiEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.study.bamboo.R
import com.study.bamboo.utils.getDialogNavResult

// TODO: 2021-08-26


@AndroidEntryPoint
class AdminMainFragment : BaseFragment<FragmentAdminMainBinding>(R.layout.fragment_admin_main),
    RecyclerViewItemClickListener<ResultEntity> {


    private val viewModel: AdminViewModel by viewModels()

    private val authViewModel: AuthViewModel by viewModels()

    private val emojiViewModel : EmojiViewModel by viewModels()

    private lateinit var adminAdapter: AlgorithmAdapter


    override fun FragmentAdminMainBinding.onCreateView() {
        setItemAdapter(STATUS.ACCEPTED)
        binding.fragment = this@AdminMainFragment

        binding.activitySpinner.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.AdminItemList,
            R.layout.admin_spinner_item
        )
        spinnerContact()


    }


    override fun FragmentAdminMainBinding.onViewCreated() {
        val id = findNavController().currentDestination?.id
        id?.let {
            getDialogNavResult<STATUS>(navId = it) { result ->
                adminAdapter.refresh()

            }
        }
        refreshButtonAndLayout()

    }


    private fun refreshButtonAndLayout() {
        binding.refreshLayout.setOnRefreshListener {

            this.adminAdapter.refresh()
            binding.refreshLayout.isRefreshing = false

        }


    }


    private fun spinnerContact() {

        binding.activitySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    when (position) {
                        0 -> {

                            setItemAdapter(STATUS.ACCEPTED)
                            stateAdapter()
                            lifecycleScope.launch {
                                observePagingData(
                                    STATUS.ACCEPTED
                                )
                            }

                        }
                        1 -> {
                            setItemAdapter(STATUS.PENDING)
                            stateAdapter()

                            lifecycleScope.launch {
                                observePagingData(
                                    STATUS.PENDING

                                )
                            }

                        }
                        2 -> {
                            setItemAdapter(STATUS.REJECTED)
                            stateAdapter()

                            lifecycleScope.launch {
                                observePagingData(
                                    STATUS.REJECTED

                                )

                            }

                        }
                        3 -> {
                            setItemAdapter(STATUS.DELETED)
                            stateAdapter()

                            lifecycleScope.launch {
                                observePagingData(
                                    STATUS.DELETED

                                )

                            }

                        }
                        else -> {
                            setItemAdapter(STATUS.ACCEPTED)
                            stateAdapter()
                            lifecycleScope.launch {
                                observePagingData(
                                    STATUS.ACCEPTED

                                )
                            }
                        }
                    }
                }
            }

    }


    fun setIntentUser() {
        startActivity(Intent(requireContext(), MainActivity::class.java))

    }

    private fun observePagingData(status: STATUS) {


        when (status) {
            STATUS.ACCEPTED -> {
                lifecycleScope.launch {
                    viewModel.getAlgorithm(authViewModel.getToken(), STATUS.ACCEPTED.toString())
                        .collectLatest {
                            adminAdapter.submitData(it)
                        }


                }

            }
            STATUS.DELETED -> {
                lifecycleScope.launch {
                    viewModel.getAlgorithm(authViewModel.getToken(), STATUS.DELETED.toString())
                        .collectLatest {
                            adminAdapter.submitData(it)
                        }

                }

            }
            STATUS.PENDING -> {

                lifecycleScope.launch {
                    viewModel.getAlgorithm(authViewModel.getToken(), STATUS.PENDING.toString())
                        .collectLatest {
                            adminAdapter.submitData(it)
                        }


                }

            }
            STATUS.REJECTED -> {

                lifecycleScope.launch {

                    viewModel.getAlgorithm(authViewModel.getToken(), STATUS.REJECTED.toString())
                        .collectLatest {
                            adminAdapter.submitData(it)
                        }

                }


            }

            else -> throw Exception("잘못된 접근입니다")
        }

    }


    private fun stateAdapter() {

        adminAdapter.addLoadStateListener { combinedLoadStates ->

            Log.d("TAG", "stateAdapter: ")
            binding.errorRetryBtn.isVisible =
                combinedLoadStates.source.refresh is LoadState.Error
            binding.errorText.isVisible =
                combinedLoadStates.source.refresh is LoadState.Error
            binding.errorImg.isVisible =
                combinedLoadStates.source.refresh is LoadState.Error
            binding.postRecyclerView.isVisible =
                combinedLoadStates.source.refresh is LoadState.Error

            binding.errorNText.isVisible =
                combinedLoadStates.source.refresh is LoadState.Error


            if (combinedLoadStates.source.refresh is LoadState.NotLoading && combinedLoadStates.append.endOfPaginationReached && adminAdapter.itemCount < 1) {
                Log.d("TAG", "stateAdapter: 데이터없음")

                binding.postRecyclerView.isVisible = false
                binding.notFoundImg.isVisible = true
                binding.notFoundText.isVisible = true

            } else {
                Log.d("TAG", "stateAdapter: 데이터있음")
                binding.postRecyclerView.isVisible = true
                binding.notFoundImg.isVisible = false
                binding.notFoundText.isVisible = false
            }

        }

    }

    fun retrySetButton() {
        adminAdapter.retry()
    }

    private fun setItemAdapter(status: STATUS) {

        val linearLayoutManagerWrapper =
            context?.let { LinearLayoutManagerWrapper(it, LinearLayoutManager.VERTICAL, false) }



        when (status) {
            STATUS.ACCEPTED -> {
                binding.postRecyclerView.apply {
                    adminAdapter = AlgorithmAdapter(this@AdminMainFragment, STATUS.ACCEPTED)
                    this.adapter = adminAdapter
                    this.adapter = adminAdapter.withLoadStateHeaderAndFooter(
                        header = PostLoadingAdapter { adminAdapter.retry() },
                        footer = PostLoadingAdapter { adminAdapter.retry() },
                    )
                    this.layoutManager = linearLayoutManagerWrapper
                }
            }
            STATUS.PENDING -> {
                binding.postRecyclerView.apply {
                    adminAdapter = AlgorithmAdapter(this@AdminMainFragment, STATUS.PENDING)
                    this.adapter = adminAdapter
                    this.adapter = adminAdapter.withLoadStateHeaderAndFooter(
                        header = PostLoadingAdapter { adminAdapter.retry() },
                        footer = PostLoadingAdapter { adminAdapter.retry() },
                    )
                    this.layoutManager = linearLayoutManagerWrapper
                }
            }
            STATUS.DELETED -> {
                binding.postRecyclerView.apply {
                    adminAdapter = AlgorithmAdapter(this@AdminMainFragment, STATUS.DELETED)

                    this.adapter = adminAdapter
                    this.adapter = adminAdapter.withLoadStateHeaderAndFooter(
                        header = PostLoadingAdapter { adminAdapter.retry() },
                        footer = PostLoadingAdapter { adminAdapter.retry() },
                    )
                    this.layoutManager = linearLayoutManagerWrapper
                }
            }
            STATUS.REJECTED -> {
                binding.postRecyclerView.apply {
                    adminAdapter = AlgorithmAdapter(this@AdminMainFragment, STATUS.REJECTED)
                    this.adapter = adminAdapter
                    this.adapter = adminAdapter.withLoadStateHeaderAndFooter(
                        header = PostLoadingAdapter { adminAdapter.retry() },
                        footer = PostLoadingAdapter { adminAdapter.retry() },
                    )
                    this.layoutManager = linearLayoutManagerWrapper
                }
            }
            else -> throw Exception("잘못된 접근입니다.")
        }
    }





    override fun onStateClick(data: ResultEntity, state: String) {
        when (state) {
            STATUS.ACCEPTED.toString() -> {
                val action = AdminMainFragmentDirections.actionAdminMainFragmentToAcceptDialog(
                    data
                )
                findNavController().navigate(action)

            }
            STATUS.DELETED.toString() -> {
                val action = AdminMainFragmentDirections.actionAdminMainFragmentToDeleteDialog(
                    data
                )
                findNavController().navigate(action)


            }
            STATUS.REJECTED.toString() -> {
                val action =
                    AdminMainFragmentDirections.actionAdminMainFragmentToRejectCancelDialog(
                        data
                    )
                findNavController().navigate(action)


            }
            STATUS.PENDING.toString() -> {
                val action = AdminMainFragmentDirections.actionAdminMainFragmentToPendingDialog(
                    data
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onLeafClick(data: ResultEntity) {
        if (data.isClicked) {
            lifecycleScope.launch {
                emojiViewModel.deleteEmoji(authViewModel.getToken(), EmojiEntity(data.idx))
            }

        } else {
            lifecycleScope.launch {
                emojiViewModel.postEmoji(authViewModel.getToken(), EmojiEntity(data.idx))
            }
        }
    }


}
