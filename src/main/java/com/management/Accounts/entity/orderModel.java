    package com.management.Accounts.entity;

    import org.springframework.data.annotation.Id;

    import java.time.LocalDate;
    import java.util.Date;
    import java.util.List;

    public class orderModel {

        @Id
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public LocalDate getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(LocalDate orderDate) {
            this.orderDate = orderDate;
        }


        private LocalDate orderDate;


        private String orderCompanyName;

        public long getBillAmount() {
            return billAmount;
        }

        public void setBillAmount(long billAmount) {
            this.billAmount = billAmount;
        }

        private long billAmount;

        public String getOrderCompanyName() {
            return orderCompanyName;
        }

        public void setOrderCompanyName(String orderCompanyName) {
            this.orderCompanyName = orderCompanyName;
        }

        public String getPaidAmount() {
            return paidAmount;
        }

        public void setPaidAmount(String paidAmount) {
            this.paidAmount = paidAmount;
        }

        public String getBalanceAmount() {
            return balanceAmount;
        }

        public void setBalanceAmount(String balanceAmount) {
            this.balanceAmount = balanceAmount;
        }

        private String paidAmount;
        private String balanceAmount;
    }
