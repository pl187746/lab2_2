package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;

class TestTaxPolicy implements TaxPolicy {
	
	public int callCounter = 0;

	public Tax calculateTax(ProductType productType, Money net) {
		++callCounter;
		return new Tax(new Money(BigDecimal.ONE), "TestTax");
	}
	
}

public class BookKeeperTest {

	ProductData productData = new ProductData(null, null, null, ProductType.STANDARD, null);
	
	BookKeeper bookKeeper;
	InvoiceRequest invoiceRequest;
	TestTaxPolicy taxPolicy;
	
	@Before
	public void setUp() {
		bookKeeper = new BookKeeper(new InvoiceFactory());
		invoiceRequest = new InvoiceRequest(null);
		invoiceRequest.add(new RequestItem(productData, 1, new Money(BigDecimal.ONE)));
		taxPolicy = new TestTaxPolicy();
	}
	
	@Test
	public void dlaZadaniaZJednymProduktemPodatekJestLiczonyRaz() {
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(taxPolicy.callCounter, is(1));
	}
	
	@Test
	public void dlaZadaniaZJednymProduktemFakturaZawieraJednaPozycje() {
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), is(1));
	}

}
