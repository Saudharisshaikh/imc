package sa.med.imc.myimc.SignUp.view;


import sa.med.imc.myimc.Login.DataModel.LoginResponse;
import sa.med.imc.myimc.Network.SimpleResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianCompleteDetailCMSResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianDetailResponse;
import sa.med.imc.myimc.Physicians.model.PhysicianResponse;
import sa.med.imc.myimc.SignUp.model.CityResponse;
import sa.med.imc.myimc.SignUp.model.CompaniesResponse;
import sa.med.imc.myimc.SignUp.model.DistrictResponse;
import sa.med.imc.myimc.SignUp.model.NationalityResponse;
import sa.med.imc.myimc.SignUp.model.UserDetailModel;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Sign up views interface to handle UI in Sign up.
 *
 */
public interface SignUpViews extends Loading {
    void onGetInfo(UserDetailModel response);

    void onSignUp(SimpleResponse response);

    void onSuccessOTP(LoginResponse response);

    void onGetComapanies(CompaniesResponse response);

    void onGetNationality(NationalityResponse response);

    void onGetCity(CityResponse response);

    void onGetDistrict(DistrictResponse response);

}
