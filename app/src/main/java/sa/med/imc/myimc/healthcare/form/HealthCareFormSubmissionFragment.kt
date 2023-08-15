package sa.med.imc.myimc.healthcare.form

import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sa.med.imc.myimc.Base.BaseBottomSheet
import sa.med.imc.myimc.MainActivity
import sa.med.imc.myimc.Network.Constants
import sa.med.imc.myimc.Network.SharedPreferencesUtils
import sa.med.imc.myimc.R
import sa.med.imc.myimc.databinding.FragmentHealthCareSubmissionFormBinding
import java.util.regex.Pattern

class HealthCareFormSubmissionFragment constructor(private val onClick : (name:String,email:String,number:String)->Unit) : BaseBottomSheet() {
    private lateinit var binding:FragmentHealthCareSubmissionFormBinding

    companion object{
        val DATA = "values"
        @JvmStatic
        fun newInstance(onClick : (name:String,email:String,number:String)->Unit) = HealthCareFormSubmissionFragment(onClick)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHealthCareSubmissionFormBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireActivity() is MainActivity ){
            binding.name = SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_USER).firstName + " "+SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_USER).lastName
            binding.email =SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_USER).getEmail(context)
            binding.phone =SharedPreferencesUtils.getInstance(requireActivity()).getValue(Constants.KEY_USER).mobileNumber
        }
        binding.setSubmitClick {
            val isValidName = binding.etSubmittedFor.text.toString().isNotEmpty()
            val isValidEmail = binding.etEmail.text.toString().isNotEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString().trim()).matches()
            val isValidPhone =  binding.etPhoneNumber.text.toString().isNotEmpty() &&
                    Pattern.compile("(\\+[0-9]{3})?+[0-9]{10}").matcher(binding.etPhoneNumber.text.toString().trim()).matches()
           val error =  if (!isValidName){
                resources.getString(R.string.enter_name)

            }
            else if (!isValidEmail){
                resources.getString(R.string.enter_valid_email)
            }
           else if (!isValidPhone){
               resources.getString(R.string.enter_valid_phone)
           }
            else{
                ""
            }
            if (error.isNotEmpty()){
                onFail(error)
                return@setSubmitClick
            }
            dismiss()
            onClick(binding.etSubmittedFor.text.toString().trim(),binding.etEmail.text.toString().trim(),binding.etPhoneNumber.text.toString().trim())
        }
    }

}