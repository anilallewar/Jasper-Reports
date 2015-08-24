/**
* Module			: Domestic Operator VO
* File Name			: Domestic Operator
* Created By		: Vinayak Shinde
* Date Created		: 06 july 2009
* Last Modified by	:
* Date Last Modified:
* Purpose			: Value Object for Domestic Operator
* Revision History	:
* Remarks			:
*/

package com.anil.hibernate.vo;
import java.io.*;

@SuppressWarnings("serial")
public class DomOperatorVO implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5187298151910198991L;
	private String mstrNetworkId =null;
    private String mstrSubNetworkId =null;
    private String mstrOperatorFirstName =null;
    private String mstrOperatorLastName =null;
    private String mstrOperatorID =null;
    private String mstrAgentID =null;
    private String mstrLocationName =null;
    private char mchrIsAdmin='N';
    private String mstrPassword =null;
	private String mstrResonForReset =null;
	private String mstrResonForResetDetails =null;
	private String mstrUserName =null;
	private long mstrUserID=0L;
	private String mstrRoleDescription =null;
	private String mstrUserIDName =null;
	private String mstrUserEmailID =null;
	
	
	
	public String getNetworkId() {
		return mstrNetworkId;
	}
	
	public void setNetworkId(String NetworkId) {
		
		mstrNetworkId = NetworkId.trim();
		
	}
	
	public String getSubNetworkId() {
		return mstrSubNetworkId;
	}
	
	public void setSubNetworkId(String SubNetworkId) {
		mstrSubNetworkId = SubNetworkId.trim();
		
	}
	
	public String getResonForReset() {
		return mstrResonForReset;
	}
	
	public void setResonForReset(String ResonForReset) {
		mstrResonForReset = ResonForReset.trim();
		
	}
	
	public String getResonForResetDetails() {
		return mstrResonForResetDetails;
	}
	
	public void setResonForResetDetails(String ResonForResetDetails) {
		mstrResonForResetDetails = ResonForResetDetails.trim();
		
	}
	
	
	public String getAgentID() {
		return mstrAgentID;
	}
	
	public void setAgentID(String agentID) {
		mstrAgentID = agentID.trim();
		
	}
	
	public String getLocationName() {
		return mstrLocationName;
	}
	public void setLocationName(String locationName) {
		mstrLocationName = locationName.trim();
				
	}
		
	public String getOperatorFirstName() {
		return mstrOperatorFirstName;
	}
	public void setOperatorFirstName(String operatorFirstName) {
		mstrOperatorFirstName = operatorFirstName.trim();
		

	}
	public String getOperatorID() {
		return mstrOperatorID;
	}
	public void setOperatorID(String operatorID) {
		mstrOperatorID = operatorID.trim();
		
	}
	
	public String getOperatorLastName() {
		return mstrOperatorLastName;
	}
	
	public void setOperatorLastName(String operatorLastName) {
		mstrOperatorLastName = operatorLastName;
	}
	
	public String getPassword() {
		return mstrPassword;
	}
	
	public void setPassword(String password) {
		mstrPassword = password;
		
	}
	
	public String getUserName() {
		return mstrUserName;
	}
	
	public void setUserName(String UserName) {
		mstrUserName = UserName;
		
	}
	
	public long getUserID() {
		return mstrUserID;
	}
	
	public void setUserID(long UserID) {
			mstrUserID = UserID;
		
	}
	
	public String getUserIDName() {
		return mstrUserIDName;
	}
	
	public void setUserIDName(String UserIDName) {
			mstrUserIDName = UserIDName;
		
	}
	
	public String getUserEmailID() {
		return mstrUserEmailID;
	}
	
	public void setUserEmailID(String UserEmailID) {
			mstrUserEmailID = UserEmailID;
	}
	
	public char getIsAdmin() {
		return mchrIsAdmin;
	}
	
	public void setIsAdmin(char RoleDescription) {
		mchrIsAdmin = RoleDescription;
	}
	
	public String getRoleDescription() {
		return mstrRoleDescription;
	}
	
	public void setRoleDescription(String RoleDescription) {
			mstrRoleDescription = RoleDescription;
		
	}
	
	public DomOperatorVO returnDefaultDomOpVO(){
		DomOperatorVO opVO = new DomOperatorVO();
		
		opVO.setAgentID("AAT896008");
		opVO.setIsAdmin('N');
		opVO.setLocationName("Berlin");
		opVO.setNetworkId("1100");
		opVO.setOperatorFirstName("Stephen");
		opVO.setOperatorID("011");
		opVO.setOperatorLastName("Long");
		opVO.setPassword("abcd@123");
		opVO.setResonForReset("TB000");
		opVO.setResonForResetDetails("Detailed reason");
		opVO.setRoleDescription("Boss");
		opVO.setSubNetworkId("000");
		opVO.setUserEmailID("anil.user@sample.com");
		opVO.setUserIDName("Anil");
		opVO.setUserName("AnilAllewar");
		opVO.setUserID(578318267L);
		
		return opVO;
	}
}
