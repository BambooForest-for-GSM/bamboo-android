package com.study.bamboo.view.fragment.admin.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.study.bamboo.adapter.admin.AdminAcceptAdapter.Companion.ACCEPTED
import com.study.bamboo.databinding.AcceptDialogBinding
import com.study.bamboo.utils.Util.Companion.DIALOG_RESULT_KEY
import com.study.bamboo.view.activity.splash.SplashActivity.Companion.deviceSizeX
import com.study.bamboo.view.fragment.admin.AdminViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.view.Gravity
import android.view.inputmethod.InputMethodManager


@AndroidEntryPoint
class AcceptDialog : DialogFragment() {

    private var _binding: AcceptDialogBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AcceptDialogArgs>()
    private val viewModel: AdminViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.updateTitle?.setText(args.title)
        _binding?.updateContent?.setText(args.content)
        Log.d("TAG", "onViewCreated: ${args.tag}")
    }

    override fun onResume() {
        super.onResume()
        dialogCorner()
        initDialog()

    }

    private var token = ""

    fun dialogCorner() {
        //다이얼로그 백그라운드 삭제 -> 모서리 둥글게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AcceptDialogBinding.inflate(inflater, container, false)

        spinnerText()

        viewModel.readToken.asLiveData().observe(viewLifecycleOwner, {
            token = it.token

        })

        binding.acceptBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            updatePost()

        }






        return binding.root
    }

    private fun initDialog() {
        //        //디바이스 크기 확인후 커스텀 다이어로그 팝업 크기 조정
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = deviceSizeX
        Log.d("로그", "acceptDialog : $deviceWidth")
        params?.width = (deviceWidth * 0.9).toInt()


        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    fun spinnerText() {
        binding.updateTag.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                _binding?.updateTagText?.text = args.tag
                when (position) {
                    0 -> {
                        binding.updateTagText.text = args.tag
                    }
                    1 -> {
                        binding.updateTagText.text = "유머"

                    }
                    2 -> {
                        binding.updateTagText.text = "공부"

                    }
                    3 -> {
                        binding.updateTagText.text = "일상"

                    }
                    4 -> {
                        binding.updateTagText.text = "학교"

                    }
                    5 -> {
                        binding.updateTagText.text = "취업"

                    }
                    6 -> {
                        binding.updateTagText.text = "관계"

                    }
                    7 -> {
                        binding.updateTagText.text = "기타"

                    }
                    else -> {
                        binding.updateTagText.text = "태그선택"
                    }
                }

            }
        }


    }


    private fun updatePost() {
        view?.let { it1 -> hideKeyboardFrom(requireContext(), it1) }
        lifecycleScope.launch {

            if (binding.updateTagText.text.toString() == "태그선택") {
                Toast.makeText(requireContext(), "잘못된 태그입니다.", Toast.LENGTH_SHORT).show()

            } else {

                viewModel.acceptPatchPost(
                    token,
                    args.auth,
                    bodySend()
                )

                viewModel.successAcceptData.observe(viewLifecycleOwner) {
                    val denied = it?.isEmpty() == true
                    binding.progressBar.isVisible = denied
                    if (denied) return@observe

                    val toast = Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    toast.show()


                    setNavResult(data = ACCEPTED)
                    findNavController().popBackStack()
                }
            }

        }


    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }
    private fun <T> Fragment.setNavResult(key: String = DIALOG_RESULT_KEY, data: T) {
        findNavController().previousBackStackEntry?.also { stack ->
            stack.savedStateHandle.set(key, data)
        }
    }

    private fun bodySend(): HashMap<String, String> {
        val accepted: HashMap<String, String> = HashMap()
        accepted["title"] = binding.updateTitle.text.toString()
        accepted["content"] = binding.updateContent.text.toString()
        accepted["tag"] = binding.updateTagText.text.toString()

        return accepted
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}
