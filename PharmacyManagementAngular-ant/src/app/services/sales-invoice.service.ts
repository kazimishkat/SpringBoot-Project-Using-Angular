import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SalesInvoiceRequest, SalesInvoiceResponse, InvoiceStatus, PaymentResponse } from '../models/sales-invoice.model';

@Injectable({
  providedIn: 'root',
})
export class SalesInvoiceService {

  // Spring Boot Controller Endpoint Path
  private apiUrl = environment.apiUrl + 'sales-invoices';

  constructor(private http: HttpClient) {}

  /**
   * Fetch all sales invoices from database
   */
  getAllInvoices(): Observable<SalesInvoiceResponse[]> {
    return this.http.get<SalesInvoiceResponse[]>(this.apiUrl);
  }

  /**
   * Fetch a single invoice by its unique ID
   */
  getInvoiceById(id: number): Observable<SalesInvoiceResponse> {
    return this.http.get<SalesInvoiceResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * 🟢 [FIXED]: Fetch a single invoice by its unique Invoice Number
   */
  getInvoiceByNumber(invoiceNumber: string): Observable<SalesInvoiceResponse> {
    return this.http.get<SalesInvoiceResponse>(`${this.apiUrl}/number/${invoiceNumber}`);
  }

  /**
   * Create a new Sales Invoice (Pos Checkout)
   */
  createInvoice(request: SalesInvoiceRequest): Observable<SalesInvoiceResponse> {
    return this.http.post<SalesInvoiceResponse>(this.apiUrl, request);
  }

  /**
   * Search invoices by query string (invoice number or customer name)
   */
  searchInvoices(keyword: string): Observable<SalesInvoiceResponse[]> {
    const params = new HttpParams().set('query', keyword);
    return this.http.get<SalesInvoiceResponse[]>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Filter invoices by date range or customer & status
   */
  filterInvoices(fromDate?: string, toDate?: string, customerId?: number, status?: InvoiceStatus): Observable<SalesInvoiceResponse[]> {
    let params = new HttpParams();
    if (fromDate) params = params.set('startDate', fromDate);
    if (toDate) params = params.set('endDate', toDate);
    if (customerId) params = params.set('customerId', customerId.toString());
    if (status) params = params.set('status', status);

    return this.http.get<SalesInvoiceResponse[]>(`${this.apiUrl}/filter`, { params });
  }

  /**
   * Filter invoices by specific Customer ID
   */
  filterInvoicesByCustomer(customerId: number): Observable<SalesInvoiceResponse[]> {
    return this.http.get<SalesInvoiceResponse[]>(`${this.apiUrl}/customer/${customerId}`);
  }

  /**
   * Filter invoices by Invoice Status (PAID, DRAFT, CANCELLED, etc.)
   */
  filterInvoicesByStatus(status: InvoiceStatus): Observable<SalesInvoiceResponse[]> {
    return this.http.get<SalesInvoiceResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  /**
   * Trigger print for invoice
   */
  printInvoice(id: number): void {
    window.print();
  }

  /**
   * Download Invoice PDF blob from backend
   */
  downloadInvoicePdf(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  /**
   * Get payment transactions history linked to an invoice
   */
  getInvoicePayment(id: number): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(`${this.apiUrl}/${id}/payments`);
  }

  /**
   * Cancel an existing invoice and restore debited stock
   */
  cancelInvoice(id: number): Observable<SalesInvoiceResponse> {
    return this.http.patch<SalesInvoiceResponse>(`${this.apiUrl}/${id}/cancel`, {});
  }
}