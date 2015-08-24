package com.anil.hibernate.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * This is an entity class to demonstrate object inheritance ORM mapping. The inheritance is mapped
 * by using ONE-TABLE-PER-CLASS-HIERARCHY strategy.
 *  
 * All properties of all super- and subclasses are mapped into the same table, instances are
 * distinguished by a special discriminator column. 
 * 
 * There is one major problem: Columns for properties declared by subclasses must be declared to be 
 * nullable.

 * @author anila
 *
 */

@Entity
@Table(name="BILLING_DETAILS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="BILLING_DETAILS_TYPE",
		discriminatorType=DiscriminatorType.STRING)
public abstract class BillingDetails {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="BILLING_DETAILS_ID")
	private long billingDetailsId;
	
	@Column(name="OWNER")
	private String owner;
	
	@Column(name="NUMBER")
	private String number;
	
	@Column(name="CREATED")
	private Date created;
	
	/**
	 * @return the billingDetailsId
	 */
	public long getBillingDetailsId() {
		return billingDetailsId;
	}
	
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}
}
