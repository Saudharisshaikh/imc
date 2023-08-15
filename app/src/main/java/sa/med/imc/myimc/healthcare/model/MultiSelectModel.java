package sa.med.imc.myimc.healthcare.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiSelectModel {
    private String title;
    private boolean isChecked;
    private List<MultiSelectModel> data =new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MultiSelectModel> getData() {
        return data;
    }

    public void setData(List<MultiSelectModel> data) {
        this.data = data;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static List<MultiSelectModel> mocks(int size, int hierarchy){
        List<MultiSelectModel> list = new ArrayList<>();
        for (int i = 0 ;i<size ; i++){
            MultiSelectModel model = new MultiSelectModel();
            model.setTitle(randomStringGenerator(i+1));
            model.setData(randomModels(i+1));
            for (int j = 0;j<model.getData().size();j++){
                MultiSelectModel innerModel = model.getData().get(j);
                innerModel.setData(randomModels(j+1));
            }
            list.add(model);
        }
        return list;
    }

    private static String randomStringGenerator(int size){
        Character[] chars = new Character[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','M','N','O','P','Q','R','S','T','U','V','W','W','X','Y','Z'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ;i<size;i++){
            builder.append(chars[new Random().nextInt(chars.length-1)]);
        }
        return builder.toString();
    }

    private static List<MultiSelectModel> randomModels(int size){
        List<MultiSelectModel> models = new ArrayList<>();
        for (int i = 0 ;i<size ;i++){
            MultiSelectModel model = new MultiSelectModel();
            model.setTitle(randomStringGenerator(i+1));
            models.add(model);
        }
        return models;
    }


}
