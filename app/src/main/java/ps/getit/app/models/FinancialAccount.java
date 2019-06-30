package ps.getit.app.models;

public class FinancialAccount {

    private String order_id, paid,payment_type,name,on_you;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOn_you() {
        return on_you;
    }

    public void setOn_you(String on_you) {
        this.on_you = on_you;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getDelivery_name() {
        return name;
    }

    public void setDelivery_name(String delivery_name) {
        this.name = delivery_name;
    }
}
