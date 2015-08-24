package com.anil.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "qrtz_cron_triggers")
public class QuartzCronTrigger implements Serializable {

	@Id
	@Basic(optional = false)
	private QuartzCronTriggerPK primaryKey;

	@Basic(optional = false)
	@Column(name = "CRON_EXPRESSION", length = 80)
	private String cronExpression;

	@Basic(optional = false)
	@Column(name = "TIME_ZONE_ID", length = 80)
	private String timeZoneId;

	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * @param cronExpression
	 *            the cronExpression to set
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * @return the timeZoneId
	 */
	public String getTimeZoneId() {
		return timeZoneId;
	}

	/**
	 * @param timeZoneId
	 *            the timeZoneId to set
	 */
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}

	/**
	 * @return the primaryKey
	 */
	public QuartzCronTriggerPK getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey
	 *            the primaryKey to set
	 */
	public void setPrimaryKey(QuartzCronTriggerPK primaryKey) {
		this.primaryKey = primaryKey;
	}
}
