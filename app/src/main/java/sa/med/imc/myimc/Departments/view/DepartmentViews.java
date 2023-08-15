package sa.med.imc.myimc.Departments.view;


import sa.med.imc.myimc.Departments.model.DepartmentDoctorResponse;
import sa.med.imc.myimc.Departments.model.DepartmentResponse;
import sa.med.imc.myimc.Utils.Loading;

/*
 * Department views interface to handle UI in Department Activity.
 */
public interface DepartmentViews extends Loading {

    void onSuccess(DepartmentResponse response);

    void onDoctorSuccess(DepartmentDoctorResponse response);

}
