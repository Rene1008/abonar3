package com.reneascanta.abonar;

import java.util.List;

/**
 * Created by Rene on 02/11/2017.
 */

public class InvoiceObject {
    public int id;
    public String companyName;
    public String companyAddress;
    public String companyCountry;
    public String clientName;
    public String clientAddress;
    public String clientTelephone;
    public String date;
    public double total;

    public List<InvoiceDetails> invoiceDetailsList;

    public float clientCountry;
}
