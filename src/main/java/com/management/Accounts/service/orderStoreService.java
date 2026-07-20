package com.management.Accounts.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import com.management.Accounts.DTO.OrderItemRequest;

import com.management.Accounts.entity.companyProfileModel;
import com.management.Accounts.entity.customerModel;
import com.management.Accounts.entity.orderStoreModel;
import com.management.Accounts.entity.productModel;
import com.management.Accounts.repository.companyProfileRepository;
import com.management.Accounts.repository.orderStoreRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lowagie.text.factories.ElementFactory.getCell;

@Service
public class orderStoreService {
    @Autowired
    orderStoreRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    companyProfileRepository companyRepository;
    @Autowired
    NotificationService notificationService;
    public orderStoreModel saveStoreOrder(orderStoreModel order) {

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (OrderItemRequest item : order.getItems()) {

            BigDecimal itemTotal = BigDecimal.valueOf(item.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            // Set item total
            item.setTotal(itemTotal);

            // Add to grand total
            grandTotal = grandTotal.add(itemTotal);
        }

        order.setTotalAmount(grandTotal);
        order.setBillNumber(generateBillNumber());
        orderStoreModel savedOrder = repository.save(order);
        notificationService.saveNotification(
                "New Order",
                savedOrder.getBillNumber() + " order added successfully.",
                "ORDER",
                "CREATE",
                savedOrder.getId()
        );
        return savedOrder;

    }
    public List<orderStoreModel> getAllStoreOrder()
    {
        return repository.findAllByOrderByOrderDateDescCreatedAtAsc();
    }
    public orderStoreModel getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public orderStoreModel updateStoreOrder(String id, orderStoreModel newData) {
        orderStoreModel existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (OrderItemRequest item : newData.getItems()) {
            BigDecimal itemTotal = BigDecimal.valueOf(item.getPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            // Set item total
            item.setTotal(itemTotal);

            // Add to grand total
            grandTotal = grandTotal.add(itemTotal);
        }
        if (existing != null) {
            existing.setId(id);
            existing.setOrderDate(newData.getOrderDate());
            existing.setCompanyName(newData.getCompanyName());
            existing.setItems(newData.getItems());
            existing.setTotalAmount(grandTotal);
            existing.setUpdatedAt(LocalDateTime.now());
            orderStoreModel updatedOrder = repository.save(existing);
            notificationService.saveNotification(
                    "Order Updated",
                    updatedOrder.getBillNumber() + " order updated.",
                    "ORDER",
                    "UPDATE",
                    updatedOrder.getId()
            );
            return updatedOrder;
        }
        return null;
    }
    public void deleteStoreOrder(String id)
    {
        orderStoreModel order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        repository.deleteById(id);
        String billNo = order.getBillNumber() != null
                ? order.getBillNumber()
                : "Unknown Bill";
        notificationService.saveNotification(
                "Order Deleted",
                 "order" +billNo +"deleted.",
                "ORDER",
                "DELETE",
                order.getId()
        );
    }
    public orderStoreModel updateStatus(String id, String status) {

        orderStoreModel order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(status);
        notificationService.saveNotification(
                "Order Straus Updated",
                order.getBillNumber() + "status has been changed to" +order.getOrderStatus(),
                "ORDER",
                "STATUS",
                order.getId()
        );
        return repository.save(order);
    }
    public List<orderStoreModel> searchOrders(String companyName, LocalDate orderDate,String orderStatus) {


        Query query = new Query();

        if (companyName != null && !companyName.isBlank()) {
            query.addCriteria(
                    Criteria.where("companyName")
                            .regex(companyName, "i"));
        }

        if (orderDate != null) {
            query.addCriteria(
                    Criteria.where("orderDate")
                            .is(orderDate));
        }


        if (orderStatus != null && !orderStatus.isBlank()) {
            query.addCriteria(
                    Criteria.where("orderStatus")
                            .is(orderStatus));
        }

        query.with(Sort.by(Sort.Direction.DESC, "orderDate"));

        return mongoTemplate.find(query, orderStoreModel.class);
    }
    public List<orderStoreModel> searchOrdersRange(
            String companyName,
            LocalDate orderStartDate,
            LocalDate orderEndDate,
            String orderStatus) {

        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        // Company Name
        if (companyName != null && !companyName.trim().isEmpty()) {
            criteriaList.add(Criteria.where("companyName")
                    .regex(companyName, "i")); // Ignore case
        }

        // Date Range
        if (orderStartDate != null && orderEndDate != null) {
            criteriaList.add(Criteria.where("orderDate")
                    .gte(orderStartDate)
                    .lte(orderEndDate));
        }
        // Start Date Only
        else if (orderStartDate != null) {
            criteriaList.add(Criteria.where("orderDate")
                    .is(orderStartDate));
        }
        // End Date Only
        else if (orderEndDate != null) {
            criteriaList.add(Criteria.where("orderDate")
                    .is(orderEndDate));
        }

        // Order Status
        if (orderStatus != null && !orderStatus.trim().isEmpty()) {
            criteriaList.add(Criteria.where("orderStatus")
                    .is(orderStatus));
        }

        // Combine all criteria
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(
                    criteriaList.toArray(new Criteria[0])));
        }

        // Sort
        query.with(Sort.by(Sort.Direction.DESC, "orderDate"));

        return mongoTemplate.find(query, orderStoreModel.class);
    }
    private String generateBillNumber() {

        Optional<orderStoreModel> lastOrder =
                repository.findTopByOrderByBillNumberDesc();

        if (lastOrder.isEmpty()) {
            return "BILL000001";
        }

        String lastBill = lastOrder.get().getBillNumber(); // BILL000123

        int number = Integer.parseInt(lastBill.replace("BILL", ""));

        return String.format("BILL%06d", number + 1);
    }
    public byte[] generatePdf(String id) {

        orderStoreModel order = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));


        companyProfileModel company =
                companyRepository.findAll()
                        .stream()
                        .findFirst()
                        .orElse(null);


        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            Document document = new Document(PageSize.A4, 40, 40, 40, 40);

            PdfWriter.getInstance(document, out);

            document.open();


            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.HELVETICA, 11);


            // ==========================
            // COMPANY HEADER
            // ==========================

            PdfPTable companyTable = new PdfPTable(2);
            companyTable.setWidthPercentage(100);
            companyTable.setWidths(new float[]{3,2});


            // LEFT SIDE LOGO + DETAILS

            PdfPCell left = new PdfPCell();
            left.setBorder(Rectangle.NO_BORDER);


            if(company != null && company.getLogoPath()!=null){

                String logoPath = System.getProperty("user.dir")
                        + company.getLogoPath();


                Image logo = Image.getInstance(logoPath);

                logo.scaleToFit(80,80);

                left.addElement(logo);
            }


            if(company != null){

                Paragraph companyDetails = new Paragraph(

                        company.getCompanyName()
                                +"\n"
                                +company.getTagline()
                                +"\n\n"
                                +company.getAddress()
                                +"\n"
                                +company.getCity()
                                +" - "
                                +company.getPincode()
                                +"\nEmail : "
                                +company.getEmail(),

                        new Font(Font.HELVETICA,10,Font.BOLD)

                );

                left.addElement(companyDetails);

            }


            companyTable.addCell(left);



            // RIGHT SIDE GST

            PdfPCell right = new PdfPCell();

            right.setBorder(Rectangle.NO_BORDER);


            Paragraph gstInvoice = new Paragraph(

                    "GST No : "
                            +(company != null ? company.getGstNumber():""),

                    new Font(Font.HELVETICA,14,Font.BOLD)

            );


            gstInvoice.setAlignment(Element.ALIGN_RIGHT);

            right.addElement(gstInvoice);


            companyTable.addCell(right);


            document.add(companyTable);


            document.add(new Paragraph(" "));



            // ==========================
            // TITLE
            // ==========================

            Paragraph title =
                    new Paragraph("ORDER REPORT", titleFont);

            title.setAlignment(Element.ALIGN_CENTER);

            title.setSpacingAfter(20);

            document.add(title);



            // ==========================
            // ORDER DETAILS
            // ==========================

            PdfPTable details = new PdfPTable(2);

            details.setWidthPercentage(100);


            details.addCell(getCell("Bill No", headerFont));
            details.addCell(getCell(
                    String.valueOf(order.getBillNumber()),
                    bodyFont));


            details.addCell(getCell("Customer", headerFont));
            details.addCell(getCell(
                    order.getCompanyName(),
                    bodyFont));


            details.addCell(getCell("Order Date", headerFont));
            details.addCell(getCell(
                    order.getOrderDate().toString(),
                    bodyFont));


            details.addCell(getCell("Order Time", headerFont));
            details.addCell(getCell(
                    order.getUpdatedAt().toString(),
                    bodyFont));


            details.addCell(getCell("Status", headerFont));
            details.addCell(getCell(
                    order.getOrderStatus(),
                    bodyFont));


            details.setSpacingAfter(20);


            document.add(details);



            // ==========================
            // ITEMS TABLE
            // ==========================


            PdfPTable table = new PdfPTable(6);

            table.setWidthPercentage(100);

            table.setWidths(new float[]{
                    1.2f,4.5f,2f,2f,2.5f,2.5f
            });



            addHeader(table,"S.No");
            addHeader(table,"Product");
            addHeader(table,"Qty");
            addHeader(table,"Scale");
            addHeader(table,"Price");
            addHeader(table,"Total");



            BigDecimal grandTotal = BigDecimal.ZERO;


            int i=1;


            for(OrderItemRequest item : order.getItems()){


                addCell(table,String.valueOf(i++));

                addCell(table,item.getProduct());

                addCell(table,String.valueOf(item.getQuantity()));

                addCell(table,String.valueOf(item.getScale()));

                addCell(table,item.getPrice().toString());


                BigDecimal total =
                        BigDecimal.valueOf(item.getPrice())
                                .multiply(
                                        BigDecimal.valueOf(item.getQuantity())
                                );


                addCell(table,total.toString());


                grandTotal =
                        grandTotal.add(total);

            }


            document.add(table);



            document.add(new Paragraph(" "));


            Paragraph total = new Paragraph(

                    "Grand Total : ₹ " + grandTotal,

                    new Font(Font.HELVETICA,14,Font.BOLD)

            );


            total.setAlignment(Element.ALIGN_RIGHT);


            document.add(total);



            // ==========================
            // SIGNATURE
            // ==========================

            document.add(new Paragraph("\n\n"));


            PdfPTable sign = new PdfPTable(2);

            sign.setWidthPercentage(100);


            PdfPCell customer =
                    new PdfPCell(
                            new Phrase(
                                    "Customer Signature\n\n\n______________",
                                    bodyFont));


            customer.setBorder(Rectangle.NO_BORDER);



            PdfPCell owner =
                    new PdfPCell(
                            new Phrase(
                                    "For "+company.getCompanyName()
                                            +"\n\n\nAuthorized Signature",
                                    bodyFont));


            owner.setBorder(Rectangle.NO_BORDER);

            owner.setHorizontalAlignment(
                    Element.ALIGN_RIGHT);



            sign.addCell(customer);

            sign.addCell(owner);


            document.add(sign);



            document.add(new Paragraph(" "));


            Paragraph thanks =
                    new Paragraph(
                            "Thank you for your business!",
                            new Font(Font.HELVETICA,12));


            thanks.setAlignment(Element.ALIGN_CENTER);


            document.add(thanks);



            document.close();


        } catch(Exception e){

            e.printStackTrace();

        }


        return out.toByteArray();
    }
    private PdfPCell getCell(String text, Font font){

        PdfPCell cell = new PdfPCell(new Phrase(text,font));
        cell.setPadding(8);

        return cell;
    }

    private void addHeader(PdfPTable table,String text){

        PdfPCell cell = new PdfPCell(
                new Phrase(text,
                        new Font(Font.HELVETICA,12,Font.BOLD))
        );

        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        cell.setPadding(12);
        cell.setMinimumHeight(35);

        table.addCell(cell);
    }
    private void addCell(PdfPTable table, String text) {

        PdfPCell cell = new PdfPCell(
                new Phrase(text, new Font(Font.HELVETICA, 11))
        );

        cell.setPadding(10); // height increase
        cell.setMinimumHeight(30); // row height

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell);
    }
}
