package com.anil.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BA")
public class BankAccount extends BillingDetails {
	
	@Column(name="BANK_ACCOUNT_BANK_NAME")
	private String bankName;
	
	@Column(name="BANK_ACCOUNT_BANK_SWIFT")
	private String bankSwift;
	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the bankSwift
	 */
	public String getBankSwift() {
		return bankSwift;
	}
	/**
	 * @param bankSwift the bankSwift to set
	 */
	public void setBankSwift(String bankSwift) {
		this.bankSwift = bankSwift;
	}
}
