package ps.getit.app.models;

/**
 * Created by yasser on 26/6/2016.
 */
public class DrawerItem {

    private String ItemName;
    private int imgResID,IdItem;

    public int getIdItem() {
        return IdItem;
    }

    public void setIdItem(int idItem) {
        IdItem = idItem;
    }

    public DrawerItem(String itemName, int imgResID, int IdItem) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
        this.IdItem=IdItem;

    }

    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    public int getImgResID() {
        return imgResID;
    }
    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

}