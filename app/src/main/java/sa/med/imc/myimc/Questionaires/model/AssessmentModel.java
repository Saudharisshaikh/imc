package sa.med.imc.myimc.Questionaires.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssessmentModel implements Serializable{

    @SerializedName("DateFinished")
    String DateFinished;

    @SerializedName("Engine")
    String Engine;

    @SerializedName("Items")
    List<Items> Items;

    public String getDateFinished() {
        return DateFinished;
    }

    public void setDateFinished(String dateFinished) {
        DateFinished = dateFinished;
    }

    public String getEngine() {
        return Engine;
    }

    public void setEngine(String engine) {
        Engine = engine;
    }

    public List<AssessmentModel.Items> getItems() {
        return Items;
    }

    public void setItems(List<AssessmentModel.Items> items) {
        Items = items;
    }

    public class Items {

        @SerializedName("FormItemOID")
        String FormItemOID;

        @SerializedName("ItemResponseOID")
        String ItemResponseOID;

        @SerializedName("Response")
        String Response;

        @SerializedName("ID")
        String ID;

        @SerializedName("Order")
        String Order;

        @SerializedName("ItemType")
        String ItemType;

        @SerializedName("Elements")
        List<Elements> Elements;


        public void setFormItemOID(String FormItemOID) {
            this.FormItemOID = FormItemOID;
        }

        public String getFormItemOID() {
            return FormItemOID;
        }

        public void setItemResponseOID(String ItemResponseOID) {
            this.ItemResponseOID = ItemResponseOID;
        }

        public String getItemResponseOID() {
            return ItemResponseOID;
        }

        public void setResponse(String Response) {
            this.Response = Response;
        }

        public String getResponse() {
            return Response;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getID() {
            return ID;
        }

        public void setOrder(String Order) {
            this.Order = Order;
        }

        public String getOrder() {
            return Order;
        }

        public void setItemType(String ItemType) {
            this.ItemType = ItemType;
        }

        public String getItemType() {
            return ItemType;
        }

        public void setElements(List<Elements> Elements) {
            this.Elements = Elements;
        }

        public List<Elements> getElements() {
            return Elements;
        }

    }

    public class Elements {

        @SerializedName("ElementOID")
        String ElementOID;

        @SerializedName("Description")
        String Description;

        @SerializedName("ElementOrder")
        String ElementOrder;

        @SerializedName("Map")
        List<Map> maps;


        public void setElementOID(String ElementOID) {
            this.ElementOID = ElementOID;
        }

        public String getElementOID() {
            return ElementOID;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getDescription() {
            return Description;
        }

        public void setElementOrder(String ElementOrder) {
            this.ElementOrder = ElementOrder;
        }

        public String getElementOrder() {
            return ElementOrder;
        }

        public List<Map> getMaps() {
            return maps;
        }

        public void setMaps(List<Map> maps) {
            this.maps = maps;
        }
    }


    public class Map {


        @SerializedName("ElementOID")
        String ElementOID;

        @SerializedName("Description")
        String Description;

        @SerializedName("FormItemOID")
        String FormItemOID;

        @SerializedName("ItemResponseOID")
        String ItemResponseOID;

        @SerializedName("Value")
        String Value;

        @SerializedName("Position")
        String Position;


        public void setElementOID(String ElementOID) {
            this.ElementOID = ElementOID;
        }
        public String getElementOID() {
            return ElementOID;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
        public String getDescription() {
            return Description;
        }

        public void setFormItemOID(String FormItemOID) {
            this.FormItemOID = FormItemOID;
        }
        public String getFormItemOID() {
            return FormItemOID;
        }

        public void setItemResponseOID(String ItemResponseOID) {
            this.ItemResponseOID = ItemResponseOID;
        }
        public String getItemResponseOID() {
            return ItemResponseOID;
        }

        public void setValue(String Value) {
            this.Value = Value;
        }
        public String getValue() {
            return Value;
        }

        public void setPosition(String Position) {
            this.Position = Position;
        }
        public String getPosition() {
            return Position;
        }

    }
}
