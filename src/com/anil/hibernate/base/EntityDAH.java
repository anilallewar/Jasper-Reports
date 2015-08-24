/**
 * Module			: Operator fuctionality
 * File Name		: DomOperatorDAH
 * Created By		: Vinayak Shinde
 * Date Created		: 06 July 2009 
 * Last Modified by	: 
 * Date Last Modified: 
 * Purpose			: Class Provide Common Data Handler for Domestic Operator
 * Revision History	:
 * Remarks			:
 */

package com.anil.hibernate.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anil.hibernate.entity.BankAccount;
import com.anil.hibernate.entity.BillingDetails;
import com.anil.hibernate.entity.CreditCard;
import com.anil.hibernate.entity.DomOperatorResetReason;
import com.anil.hibernate.entity.NetworkRequest;
import com.anil.hibernate.entity.ReqDomOperator;
import com.anil.hibernate.jpa.ThreadEntityManager;
import com.anil.hibernate.vo.DomOperatorVO;

public class EntityDAH {

	// Class level logger
	private static Logger logger = LoggerFactory.getLogger(EntityDAH.class);
	// default locale code to be used if none is passed
	private static final String DEFAULT_LOCALE = "en_US";

	// default request type that needs to be appended to the type of request
	// Add/Reset/Delete
	private static final String DEFAULT_REQUEST_TYPE = "Operator";

	// constants for adding restrictions on the NetworkRequest objects. These
	// correspond to the properties on the NetworkRequest object
	private static final String NETWORK_REQUEST_ID_PROPERTY = "networkRequestId";

	private static final String NETWORK_REQUEST_AGENTID_PROPERTY = "networkAgtId";

	private static final String NETWORK_REQUEST_ACTION_PROPERTY = "networkRequestAction";

	private static final String NETWORK_REQUEST_DOM_OPERATOR_ASSOCIATION = "reqDomOperators";

	private static final String NETWORK_REQUEST_MODIFIED_DATE_PROPERTY = "modifiedDate";

	private static final String NETWORK_REQUEST_ACTION = "Add";

	private static final String DEFAULT_DATE_FORMAT = "MM/dd/yy";

	private static final String DOMESTIC_REQUEST_REASON_ID = "reasonId";
	private static final String DOMESTIC_REQUEST_REASON_CONSTANT_ID = "T0135";

	private static SimpleDateFormat moDateFormat;

	/**
	 * Static block to populate the request reasons so that they can be mapped
	 * and sent to presentation layer
	 */
	static {

		if (moDateFormat == null) {
			moDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		}
	}

	/**
	 * publlic no-args constructor
	 */
	public EntityDAH() {
		super();
	}

	/**
	 * This is method for Inserting domestic operator details in to agentlink
	 * database tables Network_requesy and Req_dom_operator
	 * 
	 * @param Session
	 *            : The Hibernate session object passed from the Manager class
	 * @param DomOperatorVO
	 *            : Domestic operator reset details from reset operator page
	 * @param requestActionType
	 *            : The request action corresponding to whether it is an "Add",
	 *            "Reset" or "Delete" operator request. The request type will be
	 *            mapped through the WUDomesticConstants.java file
	 * 
	 * @return NetworkRequest : The NetworkRequest object encapsulating the data
	 *         stored in database
	 * @exception ALinkException
	 * @author Vinayak Shinde
	 */

	public NetworkRequest InsertDomesticOperatorDetails(
			DomOperatorVO oDomOperatorVO, String requestActionType) {

		NetworkRequest nwRequest = null;
		logger.debug("Entry - InsertDomesticOperatorDetails() method");

		EntityManager em = null;
		EntityTransaction tx = null;

		try {

			em = ThreadEntityManager.getEntityManager();

			logger
					.debug("InsertDomesticOperatorDetails() - The current Entity Manager is: "
							+ em.hashCode());
			nwRequest = new NetworkRequest();

			int nRequestType = 24; // hard-coded
			/*
			 * The created date is not inserted and hence should NOT be set in
			 * the object
			 */
			nwRequest.setCreatedDate(new Date());

			nwRequest.setCreatedUser(oDomOperatorVO.getUserIDName());// from
			// session
			/*
			 * The Isagentlink2Updated flag values are used in the following
			 * manner by the application 1. 'I' - The value while data is
			 * inserted. The Network Request with Isagentlink2Updated=I is
			 * picked up by the AS400 real time SP 2. NULL - If the AS400 real
			 * time SP fails, then the transaction is rolled back and the
			 * Isagentlink2Updated value is set to NULL. This ensures that this
			 * request will be picked up by the AgentLink AS400 batch job 3. 'Y'
			 * - Value set by batch job for Isagentlink2Updated if the request
			 * is COMPLETED on AS400 4. 'N' - Value set by batch job for
			 * Isagentlink2Updated if the request is REJECTED on AS400
			 */

			nwRequest.setIsagentlink2Updated(new Character('I'));

			nwRequest.setIsemailsent(null);
			nwRequest.setModifiedDate(null);

			// For insert request the modified user is same as the created user
			nwRequest.setNetworkAgtId(oDomOperatorVO.getAgentID());
			nwRequest.setNetworkCreatedUserId(oDomOperatorVO.getUserID());
			nwRequest.setNetworkId(oDomOperatorVO.getNetworkId());
			nwRequest.setNetworkOwnerRole(oDomOperatorVO.getRoleDescription());
			// The action is hard-coded as per AS400 interface document and
			// mapped through the constant file
			// The type of request will be passed through the calling manager
			// class
			nwRequest.setNetworkRequestAction(requestActionType);
			nwRequest.setNetworkSubId(oDomOperatorVO.getSubNetworkId());
			nwRequest.setNetworkUserEmailid(oDomOperatorVO.getUserEmailID());

			nwRequest.setReqtypeId(nRequestType);
			nwRequest.setRequstatId('A');

			// Set the Domestic operator object associated with the request
			ReqDomOperator domOperatorDetails = new ReqDomOperator();
			domOperatorDetails.setCreatedDate(new Date());
			domOperatorDetails.setCreatedUser(oDomOperatorVO.getUserIDName());

			// Set the DomOperatorResetReason if there is reset reason passed in
			// the VO
			if (oDomOperatorVO.getResonForReset() != null
					&& !"".equalsIgnoreCase(oDomOperatorVO.getResonForReset()
							.trim())) {
				domOperatorDetails
						.setDomOperatorResetReason((DomOperatorResetReason) em
								.find(DomOperatorResetReason.class,
										oDomOperatorVO.getResonForReset()));
			}

			domOperatorDetails.setModifiedDate(null);
			domOperatorDetails.setModifiedUser(null);
			domOperatorDetails.setNetworkAgentFirstName(oDomOperatorVO
					.getOperatorFirstName());
			domOperatorDetails.setNetworkAgentLastName(oDomOperatorVO
					.getOperatorLastName());

			// defalult N: for Reset operator we are not sending to AS400 from
			// Batch JOB
			domOperatorDetails.setNetworkIsAdmin(oDomOperatorVO.getIsAdmin());

			domOperatorDetails.setNetworkLocationaccountNumber(oDomOperatorVO
					.getAgentID());
			domOperatorDetails.setNetworkLocationName(oDomOperatorVO
					.getLocationName());
			domOperatorDetails.setNetworkOperatorId(oDomOperatorVO
					.getOperatorID());

			// defalult null: for Reset operator we are not sending to AS400
			// from Batch JOB and not stored into databse also
			domOperatorDetails.setNetworkPassword(null);

			// Set the 2 way mapping for association
			domOperatorDetails.setNetworkRequest(nwRequest);
			nwRequest.getReqDomOperators().add(domOperatorDetails);

			tx = em.getTransaction();
			tx.begin();
			em.persist(nwRequest);
			tx.commit();
			em.clear();

			logger.debug("The saved N/W request id is: "
					+ nwRequest.getNetworkRequestId());
			logger
					.debug("The saved DOMESTIC request id within N/W request is: "
							+ ((ReqDomOperator) nwRequest.getReqDomOperators()
									.iterator().next()).getDomOperatorId());

		} catch (Throwable exception) {
			logger.error("Generic exception while inserting Network Request: "
					+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger.debug("Exit - InsertDomesticOperatorDetails() method");
		return nwRequest;
	}

	/**
	 * This method is used to update the Isagentlink2Updated to N or null as
	 * required if the real time udpate to AS400 fails. This is required so that
	 * the PIR 3739 batch job can pick up the requests which failed while being
	 * sent to AS400
	 * 
	 * @param anwRequest
	 *            - The NetworkRequest entity whose status flag is updated
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * 
	 * @return - The updated NetworkRequest entity
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 * @author anil allewar
	 */

	public NetworkRequest updateAS400DataSentFlag(NetworkRequest anwRequest) {

		EntityManager em = null;
		EntityTransaction tx = null;
		logger.debug("Entry - updateAS400DataSentFlag() method");

		try {

			em = ThreadEntityManager.getEntityManager();

			logger
					.debug("updateAS400DataSentFlag() - The current Entity Manager is: "
							+ em.hashCode());

			anwRequest.setIsagentlink2Updated(null);

			tx = em.getTransaction();
			tx.begin();
			em.merge(anwRequest);
			tx.commit();
			em.clear();

		} catch (Exception exception) {
			logger
					.error("Generic exception while updating the AS400 sent flag on AgentLink");
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger.debug("Exit - updateAS400DataSentFlag() method");
		return anwRequest;
	}

	/**
	 * This method is used to return the NetworkRequest object repesentation for
	 * the passed NetworkRequestId primary key value. The data is fetched
	 * eagarly using joins as it would be required for showing it on the
	 * presentation layer
	 * 
	 * @param networkRequestId
	 *            - the primary key for the NetworkRequest entity to be
	 *            retrieved
	 * @param mstrLocaleCode
	 *            - The locale code of passed in user. If none passed defaults
	 *            to "en_US"
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * @return NetworkRequest - the data encapsulating the NetworkRequest object
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 * @author anil allewar
	 */
	public NetworkRequest getNetworkRequestDataById(long networkRequestId,
			String mstrLocaleCode) {

		NetworkRequest nwRequest = null;
		EntityManager em = null;

		logger.debug("Entry - getNetworkRequestDataById() method");
		if (mstrLocaleCode == null
				|| "".equalsIgnoreCase(mstrLocaleCode.trim())) {
			mstrLocaleCode = DEFAULT_LOCALE;
		}

		try {
			em = ThreadEntityManager.getEntityManager();

			if (networkRequestId <= 0) {
				logger.debug("The Network Request Id passed was 0");
			} else {
				/*
				 * The getDelegate() method return the underlying provider
				 * object for the EntityManager, if available. The result of
				 * this method is implementation specific.
				 */
				Session session = (Session) em.getDelegate();

				/*
				 * Create criteria with inner join to fetch the data eagarly.
				 * Only one select statement gets fired in this case
				 */
				Criteria criteria = session
						.createCriteria(NetworkRequest.class).add(
								Restrictions.eq(NETWORK_REQUEST_ID_PROPERTY,
										new Long(networkRequestId)))
						.setFetchMode(NETWORK_REQUEST_DOM_OPERATOR_ASSOCIATION,
								FetchMode.JOIN);
				// Get the list of Network requests satisfying the said criteria
				List networkList = criteria.list();

				// Iterate through the collection and ser the Network request
				// details
				ListIterator iterator = networkList.listIterator();
				while (iterator.hasNext()) {
					nwRequest = (NetworkRequest) iterator.next();
					// Set the date as per the format specified
					if (nwRequest.getModifiedDate() != null) {
						nwRequest.setFormattedModifiedDate(moDateFormat
								.format(nwRequest.getModifiedDate()));
					}

					if (nwRequest.getCreatedDate() != null) {
						nwRequest.setFormattedCreatedDate(moDateFormat
								.format(nwRequest.getCreatedDate()));
					}

					// Set the base request action without adding opeartor at
					// the end
					nwRequest.setBaseRequestAction(nwRequest
							.getNetworkRequestAction());

					// Add the Operator word to the request action
					nwRequest.setNetworkRequestAction(nwRequest
							.getNetworkRequestAction()
							+ " " + DEFAULT_REQUEST_TYPE);

					Set opeartorSet = nwRequest.getReqDomOperators();
					Iterator setIterator = opeartorSet.iterator();
					while (setIterator.hasNext()) {
						ReqDomOperator domOperator = (ReqDomOperator) setIterator
								.next();
						if (domOperator.getDomOperatorResetReason() != null) {
							// Call the method on DomOperatorResetReason to fire
							// the query and get the data
							String resetReason = domOperator
									.getDomOperatorResetReason()
									.getReasonReset();
							System.out
									.println("The reset reason for the request is:"
											+ resetReason);
						}
					}
				}
			}
			em.clear();

		} catch (HibernateException hibernateException) {
			logger
					.error("Hibernate Error while getting the NetworkRequest data by id");
		} catch (Exception exception) {
			logger
					.error("Generic exception while getting the NetworkRequest data by id");
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger.debug("Exit - getNetworkRequestDataById() method");
		// Return the populated Network request object
		return nwRequest;
	}

	/**
	 * This method is used to return an ArrayList of NetworkRequest objects
	 * available for the passed account number. The data is fetched using the
	 * default lazy behaviour as we do not require the operator details to be
	 * shown on the my requests page
	 * 
	 * Remember that if you want to get data about these requests you would have
	 * to either use the getNetworkRequestDataById() or iterate through the List
	 * in the same session.
	 * 
	 * @param accountIdArray
	 *            - the List containing the collection of account numbers for
	 *            which the Network request data is required
	 * @param mstrLocaleCode
	 *            - The locale code of passed in user. If none passed defaults
	 *            to "en_US"
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * @param numberofdays
	 *            - The number of days for which the requests have to be shown.
	 *            The default is 7 days while the filter to be applied is 7, 30
	 *            and 60 days<b> Please note that the number of days to be
	 *            passed should be a negative value indicating that the data to
	 *            be retrieved is for -7,-30 or -60 days from the current
	 *            date</b>
	 * @param isWUDOMLDE
	 *            - Flag denoting whether the user is a DOM LDE or not. In this
	 *            case, we need to select all the Network Requests from the
	 *            table
	 * @param isWUDOMContact
	 *            - Flag denoting whether the user is WU DOM Contact or not
	 * @param isRequestForHomePage
	 *            - Flage denoting whether the request is for home page. if "Y",
	 *            then show only the top requests as specified
	 *            ascconfiguration.properties
	 * 
	 * @return ArrayList - the list encapsulating the NetworkRequest objects for
	 *         the passed criteria
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 * @author anil allewar
	 */
	public ArrayList<NetworkRequest> getNetworkRequestDataByAccountId(
			List<String> accountIdArray, String mstrLocaleCode,
			int numberofdays, String isWUDOMLDE, String isWUDOMContact,
			String isRequestForHomePage) {

		ArrayList<NetworkRequest> nwRequestList = null;
		Criteria criteria = null;
		StringBuffer accountBuffer = null;

		logger.debug("Entry - getNetworkRequestDataByAccountId() method");
		if (mstrLocaleCode == null
				|| "".equalsIgnoreCase(mstrLocaleCode.trim())) {
			mstrLocaleCode = DEFAULT_LOCALE;
		}

		try {

			Session session = HibernateSessionFactory
					.getCurrentHibernateSessionInstance();

			if (accountIdArray == null || accountIdArray.size() == 0) {
				logger
						.debug("The Account Id array passed is either null or contains 0 elements");
				nwRequestList = new ArrayList<NetworkRequest>();
			} else {

				nwRequestList = new ArrayList<NetworkRequest>();

				if (isWUDOMLDE != null && "Y".equalsIgnoreCase(isWUDOMLDE)) {
					// If WU DOM LDE then need to show all the requests
					criteria = session
							.createCriteria(NetworkRequest.class)
							.addOrder(
									Order
											.desc(NETWORK_REQUEST_MODIFIED_DATE_PROPERTY));
				} else {
					// If not WU-LDE then need to show all requests based on the
					// AccountID that the user has access to

					accountBuffer = new StringBuffer();

					ListIterator<String> accountListIterator = accountIdArray
							.listIterator();
					while (accountListIterator.hasNext()) {
						accountBuffer.append("'").append(
								accountListIterator.next()).append("',");
					}

					accountBuffer.deleteCharAt(accountBuffer.length() - 1);

					criteria = session
							.createCriteria(NetworkRequest.class)
							.add(
									Restrictions
											.sqlRestriction("NETWORK_AGT_ID IN ("
													+ accountBuffer.toString()
													+ ")"))
							.addOrder(
									Order
											.desc(NETWORK_REQUEST_MODIFIED_DATE_PROPERTY));
				}

				// If request is for showing details on home page, then add the
				// maxorder clause
				if (isRequestForHomePage != null
						&& "Y".equalsIgnoreCase(isRequestForHomePage)) {
					int maxRequestSize = 4;
					criteria.setMaxResults(maxRequestSize);
				}

				// Get the list of Network requests satisfying the said criteria
				List<NetworkRequest> networkList = criteria.list();

				// Iterate through the collection and ser the Network request
				// details
				ListIterator<NetworkRequest> iterator = networkList
						.listIterator();
				// use single variable to loop
				NetworkRequest nwRequest = null;

				while (iterator.hasNext()) {
					nwRequest = iterator.next();

					// Set the date as per the format specified
					if (nwRequest.getModifiedDate() != null) {
						nwRequest.setFormattedModifiedDate(moDateFormat
								.format(nwRequest.getModifiedDate()));
					}

					if (nwRequest.getCreatedDate() != null) {
						nwRequest.setFormattedCreatedDate(moDateFormat
								.format(nwRequest.getCreatedDate()));
					}

					// Set the base request action without adding opeartor at
					// the end
					nwRequest.setBaseRequestAction(nwRequest
							.getNetworkRequestAction());

					// Add the Operator word to the request action
					nwRequest.setNetworkRequestAction(nwRequest
							.getNetworkRequestAction()
							+ " " + DEFAULT_REQUEST_TYPE);

					// Add the NetworkRequest object to the ArrayList
					nwRequestList.add(nwRequest);
				}
			}

		} catch (HibernateException hibernateException) {
			logger
					.error("Hibernate Error while getting the NetworkRequest data by Account");
		} catch (Exception exception) {
			logger
					.error("Generic exception while getting the NetworkRequest data by Account");
		}

		logger.debug("Exit - getNetworkRequestDataByAccountId() method");
		// Return the populated Network request object
		return nwRequestList;
	}

	/**
	 * This method is used to get reasons to reset any operator
	 * 
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * 
	 * @return Collection - containing DomOperatorResetReason objects
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 */
	public List<DomOperatorResetReason> getResetReasons() {

		logger.debug("DOMOperatorDAH.getResetReasons() Starts");
		List<DomOperatorResetReason> reasonList = new ArrayList<DomOperatorResetReason>();

		try {

			Session session = HibernateSessionFactory
					.getCurrentHibernateSessionInstance();

			Criteria criteria = session.createCriteria(
					DomOperatorResetReason.class).add(
					Restrictions.eq(DOMESTIC_REQUEST_REASON_ID, new String(
							DOMESTIC_REQUEST_REASON_CONSTANT_ID)));
			reasonList = criteria.list();

		} catch (HibernateException hibernateException) {
			logger.debug("Hibernate Error while getting the Reson data ");
		} catch (Exception exception) {
			logger.error("Generic exception while getting the Reson data");
		}

		logger.debug("DOMOperatorDAH.getResetReasons() Exit");
		return reasonList;
	}

	/**
	 * This method is used to return an ArrayList of NetworkRequest objects
	 * available for the passed account number. The data is fetched using the
	 * default lazy behaviour as we do not require the operator details to be
	 * shown on the my requests page
	 * 
	 * 
	 * @param accountId
	 *            - account id whose operator details are required.
	 * @param mstrLocaleCode
	 *            - The locale code of passed in user. If none passed defaults
	 *            to "en_US"
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * 
	 * 
	 * @return ArrayList - the list encapsulating the NetworkRequest objects for
	 *         the passed criteria
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 * @author mansi gupta
	 */
	public ArrayList<NetworkRequest> getNetworkRequestOperatorDataByAccountId(
			String accountId, String mstrLocaleCode) {

		ArrayList<NetworkRequest> nwRequestList = null;
		EntityManager em = null;

		logger
				.debug("Entry - getNetworkRequestOperatorDataByAccountId() method");
		if (mstrLocaleCode == null
				|| "".equalsIgnoreCase(mstrLocaleCode.trim())) {
			mstrLocaleCode = DEFAULT_LOCALE;
		}

		// For ThreadLocalSessionContext to work all the database access must be
		// within transaction boundaries

		try {

			em = ThreadEntityManager.getEntityManager();

			logger
					.debug("getNetworkRequestOperatorDataByAccountId() - The current Entity Manager is: "
							+ em.hashCode());

			if (accountId == null || accountId.equalsIgnoreCase("")) {
				System.out
						.println("The Account Id passed is either null or empty");
			} else {

				nwRequestList = new ArrayList();
				/*
				 * The getDelegate() method return the underlying provider
				 * object for the EntityManager, if available. The result of
				 * this method is implementation specific.
				 */
				Session session = (Session) em.getDelegate();

				Criteria criteria = session
						.createCriteria(NetworkRequest.class).add(
								Restrictions.eq(
										NETWORK_REQUEST_AGENTID_PROPERTY,
										accountId)).add(
								Restrictions.eq(
										NETWORK_REQUEST_ACTION_PROPERTY,
										NETWORK_REQUEST_ACTION)).setFetchMode(
								NETWORK_REQUEST_DOM_OPERATOR_ASSOCIATION,
								FetchMode.JOIN);

				// Get the list of Network requests satisfying the said criteria
				List<NetworkRequest> networkList = criteria.list();

				/*
				 * Clear the EntityManager so that the objects are detached from
				 * the persistence context.
				 * 
				 * Otherwise when you try to do any persistence
				 * operations(persist(), remove(), merge(), refresh()) with the
				 * same EntityManager, then it will try to persist the
				 * NetworkRequest objects retrieved with this call and might
				 * lead to unintended operations.
				 */

				em.clear();

				// Iterate through the collection and ser the Network request
				// details
				ListIterator<NetworkRequest> iterator = networkList
						.listIterator();
				// use single variable to loop
				NetworkRequest nwRequest = null;

				while (iterator.hasNext()) {
					nwRequest = iterator.next();

					// Add the Operator word to the request action
					nwRequest.setNetworkRequestAction(nwRequest
							.getNetworkRequestAction()
							+ " " + DEFAULT_REQUEST_TYPE);

					// Add the NetworkRequest object to the ArrayList
					nwRequestList.add(nwRequest);
				}
			}

		} catch (HibernateException hibernateException) {
			logger
					.error("Hibernate Error while getting the getNetworkRequestOperatorDataByAccountId");
		} catch (Exception exception) {
			logger
					.error("Generic exception while getting the getNetworkRequestOperatorDataByAccountId");
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger
				.debug("Exit - getNetworkRequestOperatorDataByAccountId() method");
		// Return the populated Network request object
		return nwRequestList;
	}

	/**
	 * This method is used to return an ArrayList of NetworkRequest objects
	 * created by the passed user id. The data is fetched using the default lazy
	 * behaviour as we do not require the operator details to be shown on the my
	 * requests page
	 * 
	 * @param mstrLocaleCode
	 *            - The locale code of passed in user. If none passed defaults
	 *            to "en_US"
	 * @param userId
	 *            - user id of the logged in user.
	 * @param session
	 *            - The Hibernate session object passed from the Manager class
	 * @param numberofdays
	 *            - The number of days for which the requests have to be shown.
	 *            The default is 7 days while the filter to be applied is 7, 30
	 *            and 60 days<b> Please note that the number of days to be
	 *            passed should be a negative value indicating that the data to
	 *            be retrieved is for -7,-30 or -60 days from the current
	 *            date</b>
	 * @param isRequestForHomePage
	 *            - Flage denoting whether the request is for home page. if "Y",
	 *            then show only the top requests as specified
	 *            ascconfiguration.properties
	 * 
	 * @return ArrayList - the list encapsulating the NetworkRequest objects for
	 *         the passed criteria
	 * @throws ALinkException
	 *             - The java exception is wrapped in AlinkException and thrown
	 *             to the calling class
	 * @author mansi gupta
	 */
	public ArrayList<NetworkRequest> getNetworkRequestDataByCreatedUser(
			String mstrLocaleCode, long userId, int numberofdays,
			String isRequestForHomePage) {

		ArrayList<NetworkRequest> nwRequestList = null;
		EntityManager em = null;
		EntityTransaction tx = null;
		Query query = null;

		logger.debug("Entry - getNetworkRequestDataByCreatedUser() method");

		if (mstrLocaleCode == null
				|| "".equalsIgnoreCase(mstrLocaleCode.trim())) {
			mstrLocaleCode = DEFAULT_LOCALE;
		}

		try {

			em = ThreadEntityManager.getEntityManager();
			tx = em.getTransaction();

			nwRequestList = new ArrayList<NetworkRequest>();

			query = em
					.createQuery("SELECT nwRequest FROM NetworkRequest as nwRequest "
							+ "WHERE nwRequest.createdDate BETWEEN :lowerLimit AND :upperLimit AND nwRequest.networkCreatedUserId = :userId "
							+ "ORDER BY nwRequest."
							+ NETWORK_REQUEST_MODIFIED_DATE_PROPERTY + " DESC");

			Calendar lowerLimit = Calendar.getInstance();
			lowerLimit.add(Calendar.DATE, new Integer(numberofdays));

			query.setParameter("lowerLimit", lowerLimit.getTime());
			query.setParameter("upperLimit", Calendar.getInstance().getTime());
			query.setParameter("userId", new Long(userId));

			// If request is for showing details on home page, then add the
			// maxorder clause
			if (isRequestForHomePage != null
					&& "Y".equalsIgnoreCase(isRequestForHomePage)) {
				int maxRequestSize = 4;
				query.setMaxResults(maxRequestSize);
			}

			// It is desirable to have clear trasaction boundaries even for
			// read-only transactions
			tx.begin();

			// Get the list of Network requests satisfying the said criteria
			List<NetworkRequest> networkList = query.getResultList();

			tx.commit();
			// Iterate through the collection and ser the Network request
			// details
			ListIterator<NetworkRequest> iterator = networkList.listIterator();
			// use single variable to loop
			NetworkRequest nwRequest = null;

			while (iterator.hasNext()) {
				nwRequest = iterator.next();

				/*
				 * JPA does not provide any way to change the fetch mode (EAGER
				 * vs LAZY) during runtime. So hou would have refer to the
				 * collection so that it is loaded in this persistence context.
				 */
				Set reqDomOp = nwRequest.getReqDomOperators();

				logger
						.debug("The first Network Request id has REQ_DOM_OPERATOR size: "
								+ reqDomOp.size());

				// Set the date as per the format specified
				if (nwRequest.getModifiedDate() != null) {
					nwRequest.setFormattedModifiedDate(moDateFormat
							.format(nwRequest.getModifiedDate()));
				}

				if (nwRequest.getCreatedDate() != null) {
					nwRequest.setFormattedCreatedDate(moDateFormat
							.format(nwRequest.getCreatedDate()));
				}

				// Set the base request action without adding opeartor at
				// the end
				nwRequest.setBaseRequestAction(nwRequest
						.getNetworkRequestAction());

				// Add the Operator word to the request action
				nwRequest.setNetworkRequestAction(nwRequest
						.getNetworkRequestAction()
						+ " " + DEFAULT_REQUEST_TYPE);

				// Add the NetworkRequest object to the ArrayList
				nwRequestList.add(nwRequest);
			}

			em.clear();

		} catch (Exception exception) {
			logger
					.error("Generic exception while getting the NetworkRequest data by created user: "
							+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger.debug("Exit - getNetworkRequestDataByCreatedUser() method");
		// Return the populated Network request object
		return nwRequestList;
	}

	/**
	 * This method is used to test the inheritance relation ship for objects
	 * that are mapped through Hibernate.
	 * 
	 * @param typeOfBilling
	 *            - What type of billing you want - either Credit Card(CC) or
	 *            Bank Account(BA)
	 * @param session
	 *            - The hibernate session against which to execute the data
	 * @return BillingDetails - The persisted class
	 */
	public BillingDetails insertBillingData(String typeOfBilling) {

		BillingDetails billing;
		logger.debug("Entry - insertBillingData() method");

		if ("CC".equalsIgnoreCase(typeOfBilling)) {
			billing = new CreditCard();
			((CreditCard) billing).setExpMonth("Jun");
			((CreditCard) billing).setExpYear("2012");
			((CreditCard) billing).setType(1);
		} else {
			billing = new BankAccount();
			((BankAccount) billing).setBankName("Bank Of America");
			((BankAccount) billing).setBankSwift("68690");
		}

		billing.setCreated(new Date());
		billing.setNumber(new Long(new Random().nextLong()).toString());
		billing.setOwner("City Bank");

		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			em = ThreadEntityManager.getEntityManager();

			logger.debug("insertBillingData() - entity manager is: "
					+ em.hashCode());

			tx = em.getTransaction();
			tx.begin();
			em.persist(billing);
			tx.commit();

			/*
			 * Clear the persistence context, causing all managed entities to
			 * become detached. Changes made to entities that have not been
			 * flushed to the database will not be persisted.
			 * 
			 * The commit operation flushes the entity changes in the
			 * transaction to the database.
			 */
			em.clear();

		} catch (Exception exception) {
			logger.error("Generic exception while inserting billing Request: "
					+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */
		logger.debug("Exit - insertBillingData() method");
		return billing;
	}

	/**
	 * This method will return the different billing details objects based on
	 * the type of billing passed to the method.
	 * 
	 * @param typeOfBilling
	 *            - What type of billing objects you want - either Credit
	 *            Card(CC) or Bank Account(BA). If you want both then pass value
	 *            other than these 2
	 * @param session
	 *            - The hibernate session against which to execute the data
	 * @return - List of BillingDetails objects
	 */
	@SuppressWarnings("unchecked")
	public List<BillingDetails> getBillingDetails(String typeOfBilling) {
		List<BillingDetails> results = null;
		Query criteria = null;

		EntityManager em = ThreadEntityManager.getEntityManager();
		EntityTransaction tx = em.getTransaction();

		logger.debug("getBillingDetails() - The current entity manager is: "
				+ em.hashCode());

		try {
			if ("CC".equalsIgnoreCase(typeOfBilling)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				Calendar calObj = new GregorianCalendar(new GregorianCalendar().get(Calendar.YEAR), Calendar.JANUARY, 1);
				
				logger.debug("The calender corresponding to first of the year is: " + format.format(calObj.getTime()));
				criteria = em
						.createQuery("SELECT billingDetails FROM CreditCard as billingDetails where billingDetails.created > '" + format.format(calObj.getTime()) + "'");
			} else if ("BA".equalsIgnoreCase(typeOfBilling)) {
				criteria = em
						.createQuery("SELECT billingDetails FROM BankAccount as billingDetails");
			} else {
				criteria = em
						.createQuery("SELECT billingDetails FROM BillingDetails as billingDetails");
			}
			tx.begin();
			results = criteria.getResultList();
			tx.commit();
			em.clear();
		} catch (Exception exception) {
			logger.error("Generic exception while getting billing details: "
					+ exception.getMessage());
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		}
		/*
		 * Remember it is the responsibilty of the last operation in the thread
		 * to close the EntityManager.
		 */

		return results;
	}
}
