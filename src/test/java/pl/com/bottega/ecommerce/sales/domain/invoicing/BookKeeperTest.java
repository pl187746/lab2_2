package pl.com.bottega.ecommerce.sales.domain.invoicing;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

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
	
	@Test
	public void dlaZadaniaZJednymProduktemPodatekJestLiczonyRaz() {
		BookKeeper bookKeeper = new BookKeeper(new InvoiceFactory());
		InvoiceRequest invoiceRequest = new InvoiceRequest(null);
		invoiceRequest.add(new RequestItem(productData, 1, new Money(BigDecimal.ONE)));
		TestTaxPolicy taxPolicy = new TestTaxPolicy();
		bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(taxPolicy.callCounter, is(1));
	}

}
