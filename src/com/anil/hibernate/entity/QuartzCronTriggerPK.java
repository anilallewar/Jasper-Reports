package com.anil.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class QuartzCronTriggerPK implements Serializable {

	@Basic(optional = false)
	@Column(name = "TRIGGER_NAME", length = 80)
	private String triggerName;

	@Basic(optional = false)
	@Column(name = "TRIGGER_GROUP", length = 80)
	private String triggerGroup;

	/**
	 * @return the triggerName
	 */
	public String getTriggerName() {
		return triggerName;
	}

	/**
	 * @param triggerName
	 *            the triggerName to set
	 */
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	/**
	 * @return the triggerGroup
	 */
	public String getTriggerGroup() {
		return triggerGroup;
	}

	/**
	 * @param triggerGroup
	 *            the triggerGroup to set
	 */
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}

	/**
	 * The JPA specifications state that a PK id class should override the
	 * equals and hashcode method to enable the EntityManager to check for
	 * equalitity of 2 entities using the primary key.
	 * 
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (!(other instanceof QuartzCronTriggerPK)) {
			return false;
		}

		QuartzCronTriggerPK primaryKey = (QuartzCronTriggerPK) other;

		/*
		 * Return 1. True - if both point to the same object 2. False - if other
		 * is not an instance of this class 3. True - 1. If both triggerGroup &
		 * triggerName of both objects are null 2. If both triggerGroup &
		 * triggerName of both objects are the same String value
		 */
		return (this.triggerGroup == null && this.triggerName == null ? primaryKey
				.getTriggerGroup() == null
				&& primaryKey.getTriggerName() == null
				: primaryKey.getTriggerGroup().equalsIgnoreCase(
						this.triggerGroup)
						&& primaryKey.getTriggerName().equalsIgnoreCase(
								this.getTriggerName()));
	}

	@Override
	public int hashCode() {
		return (this.triggerGroup == null && this.triggerName == null ? 0
				: this.triggerGroup.hashCode() + this.triggerName.hashCode());
	}
	
	@Override
	public String toString(){
		return this.triggerGroup + "-" + this.triggerName;
	}
}
