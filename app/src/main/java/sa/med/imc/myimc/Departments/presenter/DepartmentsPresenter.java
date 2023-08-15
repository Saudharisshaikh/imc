package sa.med.imc.myimc.Departments.presenter;


/*
 * Department Presenter interface to handle UI in Department Activity.
 */
public interface DepartmentsPresenter {

    void callAllDepartments(int page,String name);
    void callDepartmentDoctors(String id);

}
